(ns myra.controllers.messaging
  (:require [keechma.controller :as controller]
            [cljs.core.async :refer [<!]]
            [oops.core :refer [oget ocall]]
            [myra.settings :refer [websocket-endpoint]]
            [cljsjs.phoenix]
            [myra.edb :as edb]
            [myra.util.api :refer [gql-req]]
            [promesa.core :as p]
            [myra.gql :as gql])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))



(def Socket (oget js/Phoenix "Socket"))

(defrecord Controller [])

(defmethod controller/params Controller [_ {:keys [data]}]
  (let [{:keys [page subpage detail]} data]
    (when (and (= "messages" page)
               (= "thread" subpage)
               detail)
      true)))

(defn connect-socket [controller {:keys [thread-id jwt]}]
  (let [socket (Socket. websocket-endpoint (clj->js {:params {:token jwt}}))
        _ (ocall socket "connect")
        channel (ocall socket "channel" (str "message_thread:" thread-id))]
    (ocall channel "join")
    (ocall channel "on" "new_msg", #(controller/execute controller :get-message (oget % "message.message_id")))
    
    (fn []
      (ocall socket "disconnect"))))

(defn load-message [app-db-atom message-id]
  (let [app-db @app-db-atom]
    (->> (gql-req gql/message-by-id-q {:id message-id} (get-in app-db [:kv :jwt]))
         (p/map (fn [data]
                  (let [msg (:messageById data)
                        msg-by-id (edb/get-item-by-id @app-db-atom :messages (:id msg))]
                    (when-not msg-by-id
                      (reset! app-db-atom
                              (edb/append-collection @app-db-atom :messages :list [msg])))))))))

(defmethod controller/handler Controller [controller app-db-atom in-chan out-chan]
  (go-loop [disconnect-fn identity
            inited false]
    (let [[cmd payload] (<! in-chan)]
      (case cmd
        :keechma.toolbox.dataloader.controller/status-change
        (when (and (= :loaded payload)
                   (not inited))
          (recur (do
                   (disconnect-fn)
                   (connect-socket controller {:jwt (get-in @app-db-atom [:kv :jwt])
                                               :thread-id (:activeThreadId (edb/get-named-item @app-db-atom :gig :current))}))
                 true))
        :get-message (load-message app-db-atom payload)
        :stop (disconnect-fn)
        nil)
      (when cmd
        (recur disconnect-fn inited)))))

(defmethod controller/stop Controller [this params app-db]
  (controller/execute this :stop nil)
  app-db)

(ns myra.forms.message
  (:require [keechma.toolbox.forms.core :as forms-core]
            [myra.forms.validators :as v]
            [myra.util.api :refer [gql-req]]
            [myra.gql :as gql]
            [myra.edb :as edb]
            [keechma.toolbox.pipeline.core :as pp :refer-macros [pipeline!]]
            [keechma.toolbox.dataloader.controller :refer [wait-dataloader-pipeline!]]
            [hodgepodge.core :refer [set-item local-storage]]
            [myra.settings :refer [settings]]
            [clojure.string :as str]))

(defrecord MessageForm [validator])

(defn prepare-data [data]
  {:credentials (select-keys data [:email :password])})

(defmethod forms-core/process-attr-with MessageForm [this path]
  (when (= [:body] path)
    (fn [app-db form-props form-state path value]
      (if (= "" (str/trim (or value "")))
        (assoc-in form-state (concat [:data] path) "")
        (assoc-in form-state (concat [:data] path) value)))))

(defmethod forms-core/get-data MessageForm [this app-db form-props]
  (pipeline! [value app-db]
    (wait-dataloader-pipeline!)
    {:message-thread-id (:activeThreadId (edb/get-named-item app-db :gig :current))}))

(defmethod forms-core/submit-data MessageForm [_ app-db _ data]
  (gql-req gql/create-message-m data (get-in app-db [:kv :jwt])))

(defmethod forms-core/on-submit-success MessageForm [this app-db form-props data]
  (let [message (get-in data [:createMessage :message])]
    (pipeline! [value app-db]
      (pp/commit! (edb/append-collection app-db :messages :list [message]))
      (pp/send-command! [forms-core/id-key :mount-form] form-props))))

(defn constructor []
  (->MessageForm (v/to-validator {:body [:not-empty]})))


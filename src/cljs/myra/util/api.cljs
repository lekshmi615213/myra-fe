(ns myra.util.api
  (:require [keechma.toolbox.ajax :refer [POST]]
            [promesa.core :as p]
            [myra.settings :refer [settings]]))

(def gql-endpoint
  (get-in settings [:gql-endpoint (if js/goog.DEBUG :dev :prod)]))

(defn add-authentication-header [headers token]
  (if token
    (assoc headers :Authorization (str "Bearer " token))
    headers))

(defn extract-gql-error [error]
  (.-data (get-in error [:payload])))

(defn gql-results-handler [unpack]
  (fn [{:keys [data errors]}]
    (if errors
      (throw (ex-info "GraphQLError" errors))
      (unpack data))))

(defn gql-req
  ([query-fn] (gql-req query-fn {} nil))
  ([query-fn variables] (gql-req query-fn variables nil))
  ([query-fn variables token]
   (let [{:keys [graphql unpack]} (query-fn variables)]
     (->> (POST gql-endpoint
                {:format :json
                 :params graphql
                 :response-format :json
                 :keywords? true
                 :headers (-> {}
                              (add-authentication-header token))})
          (p/map (gql-results-handler unpack))))))


(defn gql-loader [reqs]
  (map (fn [req]
         (when-let [params (:params req)]
           (gql-req (:query-fn params)
                    (or (:variables params) {})
                    (:token params))))
       reqs))

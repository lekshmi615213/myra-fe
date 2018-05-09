(ns myra.forms.login
  (:require [keechma.toolbox.forms.core :as forms-core]
            [myra.forms.validators :as v]
            [myra.util.api :refer [gql-req]]
            [myra.gql :as gql]
            [myra.edb :as edb]
            [keechma.toolbox.pipeline.core :as pp :refer-macros [pipeline!]]
            [hodgepodge.core :refer [set-item local-storage]]
            [myra.settings :refer [settings]]))

(defrecord LoginForm [validator])

(defn prepare-data [data]
  {:credentials (select-keys data [:email :password])})

(defmethod forms-core/submit-data LoginForm [_ app-db _ data]
  (gql-req gql/login-m (prepare-data data)))

(defmethod forms-core/on-submit-success LoginForm [this app-db form-props data]
  (let [{:keys [token account]} (:login data)]
    (pipeline! [value app-db]
      (set-item local-storage (:jwt-name settings) token)
      (pp/commit! (-> app-db
                      (assoc-in [:kv :jwt] token)
                      (edb/insert-named-item :account :current account)))
      (pp/redirect! {}))))

(defn constructor []
  (->LoginForm (v/to-validator {:email    [:not-empty :email]
                                :password [:not-empty]})))


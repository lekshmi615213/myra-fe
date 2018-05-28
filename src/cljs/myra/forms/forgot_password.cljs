(ns myra.forms.forgot-password
  (:require [keechma.toolbox.forms.core :as forms-core]
            [myra.forms.validators :as v]
            [myra.util.api :refer [gql-req]]
            [myra.gql :as gql]
            [myra.edb :as edb]
            [keechma.toolbox.pipeline.core :as pp :refer-macros [pipeline!]]
            [hodgepodge.core :refer [set-item local-storage]]
            [myra.settings :refer [settings]]))

(defrecord ForgotPasswordForm [validator])

(defn prepare-data [data]
  (select-keys data [:email]))

(defmethod forms-core/submit-data ForgotPasswordForm [_ app-db _ data]
(println data)
  (pipeline! [value app-db]
    (gql-req gql/forgot-password-m (prepare-data data))
    (rescue! [error])))

(defmethod forms-core/on-submit-success ForgotPasswordForm [this app-db form-props data]
  (let [changed? (get-in data [:forgotPassword])]
    (pipeline! [value app-db]
      )))

(defn constructor []
  (->ForgotPasswordForm (v/to-validator {:email [:not-empty :email]})))


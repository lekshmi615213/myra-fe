(ns myra.forms.new-password
  (:require [keechma.toolbox.forms.core :as forms-core]
            [myra.forms.validators :as v]
            [myra.util.api :refer [gql-req]]
            [myra.gql :as gql]
            [myra.edb :as edb]
            [keechma.toolbox.pipeline.core :as pp :refer-macros [pipeline!]]
            [hodgepodge.core :refer [set-item local-storage]]
            [myra.settings :refer [settings]]
            [keechma.toolbox.dataloader.controller :refer [wait-dataloader-pipeline!]]
            [clojure.string :as str]))

(defrecord NewPasswordForm [validator])

(defn prepare-data [data app-db]
  (select-keys data [:password :newPassword])
  (let [subpage (get-in app-db [:route :data :detail])]
    (assoc data :passwordResetKey subpage))  
)

(defmethod forms-core/submit-data NewPasswordForm [_ app-db _ data]
  (gql-req gql/reset-password-m (prepare-data data app-db) (get-in app-db [:kv :jwt])))

(defmethod forms-core/on-submit-success NewPasswordForm [this app-db form-props data]
  (let [changed? (get-in data [:resetPassword])]
    (pipeline! [value app-db]
      )))

(defn constructor [] 
  (->NewPasswordForm (v/to-validator {:password [:not-empty :ok-password :password-confirmation]
                                         :password2 [:not-empty :ok-password :password-confirmation]})))

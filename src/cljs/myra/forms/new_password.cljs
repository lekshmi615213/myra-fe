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

(defn prepare-data [data]
  (select-keys data [:password :newPassword]))

(defmethod forms-core/submit-data NewPasswordForm [_ app-db _ data]
  (gql-req gql/new-password-m (prepare-data data) (get-in app-db [:kv :jwt])))

(defmethod forms-core/on-submit-success NewPasswordForm [this app-db form-props data]
  (let [changed? (get-in data [:newPassword])]
    (pipeline! [value app-db]
      (if changed?
        (pp/redirect! {:page "profile"})
        (js/alert "There was a problem with the password update. Please try again!")
        ))))

(defn constructor [] 
  (->NewPasswordForm (v/to-validator {:password [:not-empty :ok-password :password-confirmation]
                                         :newPassword [:not-empty :ok-password :password-confirmation]})))

(ns myra.forms.change-password
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

(defrecord ChangePasswordForm [validator])

(defn prepare-data [data]
  (select-keys data [:password :newPassword]))

(defmethod forms-core/submit-data ChangePasswordForm [_ app-db _ data]
  (gql-req gql/change-password-m (prepare-data data) (get-in app-db [:kv :jwt])))

(defmethod forms-core/on-submit-success ChangePasswordForm [this app-db form-props data]
  (let [changed? (get-in data [:changePassword])]
    (pipeline! [value app-db]
      (if changed?
        (pp/redirect! {:page "profile"})
        (js/alert "The old password you have entered is incorrect. Please try again!")
        ))))

(defn constructor [] 
  (->ChangePasswordForm (v/to-validator {:password [:not-empty]
                                         :newPassword [:not-empty :ok-password]})))

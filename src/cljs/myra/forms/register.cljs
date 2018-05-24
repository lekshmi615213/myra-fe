(ns myra.forms.register
  (:require [keechma.toolbox.forms.core :as forms-core]
            [myra.forms.validators :as v]
            [myra.util.api :refer [gql-req]]
            [myra.gql :as gql]
            [myra.edb :as edb]
            [keechma.toolbox.pipeline.core :as pp :refer-macros [pipeline!]]
            [hodgepodge.core :refer [set-item local-storage]]
            [myra.settings :refer [settings]]
            [keechma.toolbox.dataloader.controller :refer [wait-dataloader-pipeline!]]))

(defrecord RegisterForm [validator])

(defn prepare-data [data]
  {:credentials (select-keys data [:email :password])
   :profile (select-keys data [:fullName :department :phoneNumber :type])})

(defn determine-profile-type [data app-db]
  (let [subpage (get-in app-db [:route :data :subpage])]
    (assoc data :type (if (= subpage "handler") "HANDLER" "EMPLOYEE"))))

(defmethod forms-core/get-data RegisterForm [_ _ _ _]
  (pipeline! [value app-db]
    (wait-dataloader-pipeline!)
    nil))

(defmethod forms-core/submit-data RegisterForm [_ app-db _ data]
  (gql-req gql/register-m (-> data
                              (determine-profile-type app-db)
                              (prepare-data))))

(defmethod forms-core/on-submit-success RegisterForm [this app-db form-props data]
  (let [{:keys [token account]} (:register data)]
    (pipeline! [value app-db]
      (set-item local-storage (:jwt-name settings) token)
      (pp/commit! (-> app-db
                      (assoc-in [:kv :jwt] token)
                      (edb/insert-named-item :account :current account)))
      (pp/redirect! {}))))

(defn constructor []
  (->RegisterForm (v/to-validator {
                                   :email       [:not-empty :email]
                                   :password    [:not-empty :ok-password :password-confirmation]
                                   :password2   [:not-empty :ok-password :password-confirmation]
                                   :fullName    [:not-empty :valid-fullname]
                                   :department  [:not-empty]
                                   :phoneNumber [:not-empty :phone]
                                   })))

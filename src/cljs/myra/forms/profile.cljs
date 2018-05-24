(ns myra.forms.profile
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

(defrecord ProfileForm [validator])

(defn prepare-data [data]
  {;;:credentials (select-keys data [:email])
   :profile (-> (select-keys data [:fullName :department :phoneNumber])
                (update :department str/upper-case))})

(defn add-avatar [data app-db]
  (if-let [avatar (get-in app-db [:kv :image-upload-preview])]
    (assoc-in data [:profile :encodedImage] avatar)
    data))

(defn get-form-data [app-db]
  (let [current-account (edb/get-named-item app-db :account :current)
        profile ((:profile current-account))
        department (or (:department profile) "")]
    (merge (select-keys current-account [:email])
           (select-keys profile [:fullName :phoneNumber])
           {:department department})))

(defmethod forms-core/get-data ProfileForm [_ _ _]
  (pipeline! [value app-db]
    (wait-dataloader-pipeline!)
    (get-form-data app-db)))

(defmethod forms-core/submit-data ProfileForm [_ app-db _ data]
  (gql-req gql/update-profile-m (-> data
                                    (prepare-data)
                                    (add-avatar app-db)) (get-in app-db [:kv :jwt])))

(defmethod forms-core/on-submit-success ProfileForm [this app-db form-props data]
  (let [profile (get-in data [:updateProfile :profile])]
    (pipeline! [value app-db]
      (pp/commit! (edb/insert-item app-db :profile profile))
      (pp/redirect! {:page "profile"}))))

(defn constructor []
  (->ProfileForm (v/to-validator {:email       [:not-empty :email]
                                  :fullName    [:not-empty :valid-fullname]
                                  :department  [:not-empty]
                                  :phoneNumber [:not-empty :phone]})))

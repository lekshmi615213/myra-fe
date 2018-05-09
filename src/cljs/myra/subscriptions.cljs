(ns myra.subscriptions
  (:require [keechma.toolbox.dataloader.subscriptions :as dataloader]
            [myra.edb :as edb :refer [edb-schema]]
            [myra.datasources  :refer [datasources]]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [myra.domain.profile :as profile])
  (:require-macros [reagent.ratom :refer [reaction]]))

(defn get-kv [key]
  (fn [app-db-atom]
    (reaction
     (get-in @app-db-atom (flatten [:kv key])))))

(defn current-profile [app-db-atom]
  (reaction
   (let [current-account (edb/get-named-item @app-db-atom :account :current)
         profile-fn      (:profile current-account)]
     (when (fn? profile-fn)
       (profile-fn)))))

(defn current-employee? [app-db-atom]
  (reaction
   (profile/employee? (deref (current-profile app-db-atom)))))

(defn current-handler? [app-db-atom]
  (reaction
   (profile/handler? (deref (current-profile app-db-atom)))))

(defn profile-page-profile [app-db-atom]
  (reaction
   (let [app-db @app-db-atom
         {:keys [page subpage detail]} (get-in app-db [:route :data])]
     (when (= page "profile")
       (if detail
         (edb/get-item-by-id app-db :profile detail)
         @(current-profile app-db-atom))))))

(defn profile-page-profile-current? [app-db-atom]
  (reaction 
   (= (:id @(profile-page-profile app-db-atom))
      (:id @(current-profile app-db-atom)))))
    

(def subscriptions
  (merge (dataloader/make-subscriptions datasources edb-schema)
         {:initialized?                  (get-kv :initialized?)
          :form-state                    forms-helpers/form-state-sub
          :current-employee?             current-employee?
          :current-handler?              current-handler?
          :profile-page-profile          profile-page-profile
          :profile-page-profile-current? profile-page-profile-current?
          :current-modal                 (get-kv :current-modal)
          :image-upload-preview          (get-kv :image-upload-preview)}))

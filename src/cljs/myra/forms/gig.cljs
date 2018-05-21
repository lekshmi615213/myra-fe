(ns myra.forms.gig
  (:require [keechma.toolbox.forms.core :as forms-core]
            [myra.forms.validators :as v]
            [myra.util.api :refer [gql-req]]
            [myra.gql :as gql]
            [myra.edb :as edb]
            [keechma.toolbox.pipeline.core :as pp :refer-macros [pipeline!]]
            [hodgepodge.core :refer [set-item local-storage]]
            [myra.settings :refer [settings]]
            [keechma.toolbox.dataloader.controller :refer [wait-dataloader-pipeline!]]
            [cljsjs.moment]
            [oops.core :refer [ocall]]))

(defn prepend-gig [app-db gig]
  (let [gigs (edb/get-collection app-db :gig :list)
        gig-ids (set (map :id gigs))]
    (if (contains? gig-ids (:id gig))
      app-db
      (edb/prepend-collection app-db :gig :list [gig]))))

(defn date-to-iso [value]
  (ocall (js/moment value) "toISOString"))

(defrecord GigForm [validator])

(defn prepare-data [data]
  {:gig (-> (select-keys data [:startDatetime :endDatetime :notes :department])
            (update :startDatetime date-to-iso)
            (update :endDatetime date-to-iso))})

(defmethod forms-core/get-data GigForm [this app-db form-props]
  (let [[_ id] form-props]
    (if (= "new" id)
      {}
      (pipeline! [value app-db]
        (wait-dataloader-pipeline!)
        (edb/get-item-by-id app-db :gig id)))))

(defmethod forms-core/submit-data GigForm [_ app-db _ data]
  (let [id (:id data)]
    (if id
      (gql-req gql/update-gig-m (assoc (prepare-data data) :id id) (get-in app-db [:kv :jwt]))
      (gql-req gql/create-gig-m (prepare-data data) (get-in app-db [:kv :jwt])))))

(defmethod forms-core/on-submit-success GigForm [this app-db form-props data]
  (let [gig (get-in data [:gig :gig])]
    (pipeline! [value app-db]
      (pp/commit! (prepend-gig app-db gig))
      (pp/redirect! {:page "gigs" :subpage "future"}))))

(defn constructor []
  (->GigForm (v/to-validator {:startDatetime [:not-empty :future-date]
                              :endDatetime   [:not-empty :future-date :future-date-from-start]
                              :department    [:not-empty]})))


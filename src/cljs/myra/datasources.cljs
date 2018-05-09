(ns myra.datasources
  (:require [myra.gql :as gql]
            [myra.util.api :refer [gql-loader]]
            [hodgepodge.core :refer [get-item local-storage]]
            [myra.settings :refer [settings]]
            [myra.util :as util]
            [clojure.string :as str]
            [oops.core :refer [ocall]]
            [cljsjs.moment]))

(defn gql-processor [path]
  (fn [data]
    (get-in data (flatten [path]))))

(defn map-loader [loader]
  (fn [reqs]
    (map loader reqs)))

(def ignore-datasource!
  :keechma.toolbox.dataloader.core/ignore)

(def default-pagination {:object-count 100})

(def jwt
  {:target [:kv :jwt]
   :loader (map-loader #(get-item local-storage (:jwt-name settings)))
   :params (fn [prev _ _]
             (when (:data prev)
               ignore-datasource!))})

(def current-account
  {:target [:edb/named-item :account/current]
   :loader gql-loader
   :deps [:jwt]
   :processor (gql-processor :currentAccount)
   :params (fn [prev _ {:keys [jwt]}]
             (when jwt
               (if (:data prev)
                 ignore-datasource!
                 {:query-fn gql/current-account-q
                  :token jwt})))})

(def departments
  {:target [:edb/collection :department/list]
   :loader (fn [reqs]
             (map (fn [_]
                    (map (fn [[id label]] {:id (str/upper-case id) :label label})
                         [["emergency_department" "Emergency Department"]
                          ["intensive_care_unit" "ICU"]
                          ["telemetry" "Telemetry"]
                          ["medical_surgical" "Medical Surgical"]
                          ["operating_room" "Operating Room"]
                          ["post_anaesthesia_care_unit" "PACU"]
                          ["labor_and_delivery" "Labor and Delivery"]
                          ["mother_baby" "Mother and Baby"]])) reqs))
   :params (fn [_ _ _])})

(defn build-future-gigs-params [jwt]
  {:query-fn  gql/future-gigs-q
   :variables {:pagination default-pagination}
   :token     jwt})

(defn build-history-gigs-params [jwt route]
  (let [{:keys [year month]} route]
    {:query-fn  gql/gigs-by-month-q
     :variables {:month      (js/parseInt (or month (util/current-month)))
                 :year       (js/parseInt (or year (util/current-year)))
                 :timezone   (ocall (js/moment) "format" "Z")
                 :pagination default-pagination}
     :token     jwt}))

(def gigs
  {:target [:edb/collection :gig/list]
   :loader gql-loader
   :processor (gql-processor :gigs)
   :deps [:jwt]
   :params (fn [_ route {:keys [jwt]}]
             (let [page (:page route)
                   subpage (:subpage route)]
               (when (and (= page "gigs") jwt)
                 (if (= subpage "future")
                   (build-future-gigs-params jwt)
                   (build-history-gigs-params jwt route)))))})

(defn thread-page? [page subpage detail]
  (and (= "messages" page)
       (= "thread" subpage)
       (not= "new" detail)
       detail))

(def current-gig
  {:target [:edb/named-item :gig/current]
   :loader gql-loader
   :processor (gql-processor :gigById)
   :deps [:jwt]
   :params (fn [_ {:keys [page subpage detail]} {:keys [jwt]}]
             (when (and jwt
                        (or (thread-page? page subpage detail)
                            (and (= "gigs" page)
                                 (not= "new" detail)
                                 detail)))
               {:query-fn gql/gig-by-id-q
                :token jwt
                :variables {:id detail}}))})

(def thread-messages
  {:target [:edb/collection :messages/list]
   :loader gql-loader
   :processor (gql-processor :getThreadMessages)
   :deps [:jwt :current-gig]
   :params (fn [_ {:keys [page subpage detail]} {:keys [jwt current-gig]}]
             (when (and jwt
                        (thread-page? page subpage detail))
               {:query-fn gql/get-thread-messages-q
                :token jwt
                :variables {:message-thread-id (:activeThreadId current-gig)
                            :pagination {:object-count 1000}}}))})

(def datasources
  {:jwt             jwt
   :current-account current-account
   :gigs            gigs
   :current-gig     current-gig
   :departments     departments
   :thread-messages thread-messages})

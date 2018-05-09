(ns myra.edb
  (:require [entitydb.core]
            [keechma.toolbox.edb :refer-macros [defentitydb]]
            [cljsjs.moment]
            [oops.core :refer [ocall]]))

(defn parse-date [date]
  (js/moment date))

(def edb-schema
  {:account    {:id        :id
                :relations {:profile [:one :profile]}}
   :department {:id :id}
   :profile    {:id :id}
   :gig        {:id         :id
                :relations  {:employee [:one :profile]
                             :handler  [:one :profile]}
                :middleware {:set [(fn [gig]
                                     (-> gig
                                         (update :startDatetime parse-date)
                                         (update :endDatetime parse-date)))]}}})

(defentitydb edb-schema)

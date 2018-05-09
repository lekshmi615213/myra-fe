(ns myra.domain.gig
  (:require [cljsjs.moment]
            [oops.core :refer [ocall]]))

(defn done? [gig]
  (= "DONE" (:state gig)))

(defn pending? [gig]
  (= "PENDING" (:state gig)))

(defn claimed? [gig]
  (= "CLAIMED" (:state gig)))

(defn title [gig]
  (let [start-datetime (js/moment (:startDatetime gig))
        end-datetime (js/moment (:endDatetime gig))
        same-day? (= (ocall start-datetime "format" "YYYY-MM-DD")
                     (ocall end-datetime "format" "YYYY-MM-DD"))]
    
    (if same-day?
      (str
       (ocall start-datetime "format" "dddd, MMMM D @ HH:mm")
       " - "
       (ocall end-datetime "format" "hh:mma"))
      (str
       (ocall start-datetime "format" "ddd, MMM D @ HH:mm")
       " - "
       (ocall end-datetime "format" "ddd, MMM D @ HH:mm")))))

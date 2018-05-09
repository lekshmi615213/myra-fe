(ns myra.util
  (:require [cljsjs.moment]
            [oops.core :refer [ocall]]))

(def months
  {1  "January"
   2  "February"
   3  "March"
   4  "April"
   5  "May"
   6  "June"
   7  "July"
   8  "August"
   9  "September"
   10 "October"
   11 "November"
   12 "December"})

(defn current-year []
  (ocall (js/moment) "format" "YYYY"))

(defn current-month []
  (ocall (js/moment) "format" "M"))

(defn month-name [month]
  (get months month))

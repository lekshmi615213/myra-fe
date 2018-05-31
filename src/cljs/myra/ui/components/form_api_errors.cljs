(ns myra.ui.components.form-api-errors
  (:require [clojure.string :as str]  
            [cljs.reader :as r :refer [read-string]]
  ))

(defn render [form-state]
  (let [cause (get-in form-state [:state :cause])] 
  (def causeStr (pr-str cause))
  (def causeStr (str/replace causeStr #"#error " ""))
  (def causeObj (read-string causeStr))
  (def message (:message causeObj))
  (if message
	  [:span.api-error-message.status-message message] 
)))

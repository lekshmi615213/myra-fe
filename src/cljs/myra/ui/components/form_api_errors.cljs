(ns myra.ui.components.form-api-errors
  (:require [clojure.string :as str]  
            [cljs.reader :as r :refer [read-string]]
  ))

(defn render [form-state]
  (let [state (:state form-state)]  
  (def cause (:cause state))
  (def causeStr (pr-str cause))
  (def causeStr (str/replace causeStr #"#error " ""))
  (def causeObj (read-string causeStr))
  (def message (:message causeObj))
  (if message
	[:span.api-error-message.status-message message] 
)))

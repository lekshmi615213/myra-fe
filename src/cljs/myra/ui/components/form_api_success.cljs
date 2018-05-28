(ns myra.ui.components.form-api-success
  (:require [clojure.string :as str]  
            [cljs.reader :as r :refer [read-string]]
  ))

(defn render [form-state message]
  (let [state (:state form-state)] 
    (def type (:type state))
    (def typeStr (pr-str type))
    (when (= ":submitted" typeStr)
      [:span.api-success-message.status-message message]
    )
  )
  
)

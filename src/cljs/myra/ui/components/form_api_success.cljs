(ns myra.ui.components.form-api-success
  (:require [clojure.string :as str]  
            [cljs.reader :as r :refer [read-string]]
  ))

(defn render [form-state message]
  (let [type (get-in form-state [:state :type])] 
    (def typeStr (pr-str type))
    (when (= ":submitted" typeStr)
      [:span.api-success-message.status-message message]
    )
  )
  
)

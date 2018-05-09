(ns myra.controllers.modal
 (:require [keechma.toolbox.pipeline.core :as pp :refer-macros [pipeline!]]
           [keechma.toolbox.pipeline.controller :as pp-controller]
           [myra.edb :as edb]))

(defn toggle-modal [modal app-db]
    (let [current-modal (get-in app-db [:kv :current-modal])]
     (assoc-in app-db [:kv :current-modal]
        (if (= modal current-modal) nil modal))))

(def controller
  (pp-controller/constructor
   (fn [route] route)
   {:toggle (pipeline! [value app-db]
              (pp/commit! (toggle-modal value app-db)))}))

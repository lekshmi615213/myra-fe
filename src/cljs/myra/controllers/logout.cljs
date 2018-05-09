(ns myra.controllers.logout
  (:require [keechma.toolbox.pipeline.core :as pp :refer-macros [pipeline!]]
            [keechma.toolbox.pipeline.controller :as pp-controller]
            [keechma.toolbox.dataloader.controller :as dataloader-controller]
            [myra.edb :as edb]
            [hodgepodge.core :refer [remove-item local-storage]]
            [medley.core :refer [dissoc-in]]
            [myra.settings :refer [settings]]))



(def controller
  (pp-controller/constructor
   (fn [route] 
     (when (= "logout" (get-in route [:data :page]))
       true))
   {:on-start (pipeline! [value app-db]
                (remove-item local-storage (:jwt-name settings))
                (pp/commit! (-> app-db
                                (edb/remove-named-item :account :current)
                                (dissoc-in [:kv :jwt])))
                (pp/redirect! {:page "login"}))}))

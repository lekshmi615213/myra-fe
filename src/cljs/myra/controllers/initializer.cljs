(ns myra.controllers.initializer
  (:require [keechma.toolbox.pipeline.core :as pp :refer-macros [pipeline!]]
            [keechma.toolbox.pipeline.controller :as pp-controller]
            [keechma.toolbox.dataloader.controller :as dataloader-controller]
            [myra.edb :as edb]))

(def logged-out-pages #{"register" "login" "logout" "forgot-password" "reset-password"})

(defn redirect-or-initialize! [app-db]
  (let [current-account (edb/get-named-item app-db :account :current)
        current-page (get-in app-db [:route :data :page])]
    (if (and (not current-account)
             (not (contains? logged-out-pages current-page)))
      (pp/redirect! {:page "login"})
      (pipeline! [value app-db]
        (pp/commit! (assoc-in app-db [:kv :initialized?] true))
        (when (and (contains? logged-out-pages (get-in app-db [:route :data :page]))
               (not (nil? (edb/get-named-item app-db :account :current)))  
               (pp/redirect! {:page "gigs" :subpage "future"})))))))

(def controller
  (pp-controller/constructor
   (fn [route] route)
   {:start (pipeline! [value app-db]
             (dataloader-controller/wait-dataloader-pipeline!)
             (redirect-or-initialize! app-db))}))

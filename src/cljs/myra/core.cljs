(ns myra.core 
  (:require [reagent.core :as reagent]
            [keechma.app-state :as app-state]
            [myra.controllers :refer [controllers]]
            [myra.ui :refer [ui]]
            [myra.subscriptions :refer [subscriptions]]
            [myra.stylesheets.core :refer [stylesheet]]
            [keechma.toolbox.css.core :refer [update-page-css]]))

(def app-definition
  {:components    ui
   :controllers   controllers
   :subscriptions subscriptions
   :routes [["" {:page "gigs" :subpage "future"}]
            ":page"
            ":page/:subpage"
            ":page/:subpage/:detail"]
   :html-element  (.getElementById js/document "app")})

(defonce running-app (clojure.core/atom nil))

(defn start-app! []
  (reset! running-app (app-state/start! app-definition))
  (update-page-css (stylesheet)))

(defn dev-setup []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)
    (println "dev mode")))

(defn reload []
  (let [current @running-app]
    (if current
      (app-state/stop! current start-app!)
      (start-app!))))

(defn ^:export main []
  (dev-setup)
  (start-app!))

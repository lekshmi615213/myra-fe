(ns myra.ui.main
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub> <cmd route>]]
            [keechma.toolbox.css.core :refer-macros [defelement]]
            [garden.stylesheet :refer [at-media]]
            [garden-basscss.vars :refer [vars]]))

(def pages-with-header #{"gigs" "messages" "profile"})

(def breakpoints (:breakpoints @vars))

(defelement -wrapper
  :tag :div
  :style [{:background-image "url('img/img-profile-desktop.jpg')"
           :background-repeat "no-repeat"
           :background-size "cover"
           :position "absolute"
           :z-index -1
           :width "100%"
           :height "200px"}])

(defn render [ctx]
  (let [current-route (route> ctx)
        page (:page current-route)
        initialized? (sub> ctx :initialized?)
        current-account (sub> ctx :current-account)]
    (if initialized?
      (if current-account
        [:div
         [(ui/component ctx :modal)]
         (when (= "profile" page)
           [-wrapper])
         [:div.max-width-4.mx-auto
          (when (contains? pages-with-header page)
            [(ui/component ctx :header)])
          (case page
            "gigs"     [(ui/component ctx :page-gigs)]
            "messages" [(ui/component ctx :page-messages)]
            "profile"  [(ui/component ctx :page-profile)]
            nil)]]
         
        [(ui/component ctx :page-session)])
      [:div])))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:current-account
                                       :initialized?]
                   :component-deps [:header
                                    :page-gigs
                                    :page-profile
                                    :page-messages
                                    :page-session
                                    :modal]}))

(ns myra.ui.modals.late-gig
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub>]]
            [myra.ui.components.inputs :refer [-btn-alt -btn-default]]))

(defn render [ctx {:keys [close-modal]}]
  [:div.parent
   [:img {:src "/img/icn-sorry.png"}]
   [:h1.c-black.semibold "We're sorry!"]
   [:p.h3.bold.c-black "You need to have canceled at least 4 hours in advance. Please call the hospital if you need further assistance."]
   [:div.flex.flex-column.justify-center.items-center.w-100
    [-btn-default {:on-click close-modal} "I UNDERSTAND"]
    [:a.bold.c-cyan.mt1 {:href "#"} "CONTACT HOSPITAL"]]])

(def component
  (ui/constructor {:renderer render}))

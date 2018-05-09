(ns myra.ui.modals.added-gig
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub>]]
            [myra.ui.components.inputs :refer [-btn-alt -btn-default]]))

(defn render [ctx {:keys [close-modal]}]
  [:div.parent
   [:img {:src "/img/icn-remove.png"}]
   [:h1.c-black.semibold "Your Gig has been successfully added!"]
   [:div.w-100.justify-around.flex.flex-wrap
    [-btn-default {:class [:mb0-5] :on-click close-modal} "OK"]]])

(def component
  (ui/constructor {:renderer render}))


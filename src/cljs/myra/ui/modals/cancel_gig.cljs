(ns myra.ui.modals.cancel-gig
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub>]]
            [myra.ui.components.inputs :refer [-btn-alt -btn-default]]))

(defn render [ctx {:keys [close-modal]}]
  [:div.parent
   [:img {:src "/img/icn-remove.png"}]
   [:h1.c-black.semibold "Are you sure you want to remove this shift?"]
   [:div.w-100.justify-around.flex.flex-wrap
    [-btn-alt {:class [:mb0-5] :on-click close-modal} "CANCEL"]
    [-btn-default {:class [:mb0-5]} "REMOVE"]]])

(def component
  (ui/constructor {:renderer render}))

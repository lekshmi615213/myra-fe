(ns myra.ui.modals.late-gig
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub>]]
            [myra.ui.components.inputs :refer [-btn-alt -btn-default]]))

(defn contact-phone-number [gig]
  (when gig
    (let [handler ((:handler gig))]
      (:phoneNumber handler))))

(defn render [ctx {:keys [close-modal]}]
  (let [current-gig (sub> ctx :current-gig)]
    
    [:div.parent
     [:img {:src "/img/icn-sorry.png"}]
     [:h1.c-black.semibold "We're sorry!"]
     [:p.h3.bold.c-black "You need to have canceled at least 2 hours in advance. Please call the hospital if you need further assistance."]
     [:div.flex.flex-column.justify-center.items-center.w-100
      [-btn-default {:on-click close-modal} "I UNDERSTAND"]
      (when-let [p (contact-phone-number current-gig)]
        [:a.bold.c-cyan.mt1 {:href (str "tel:" p)} "CONTACT HOSPITAL"])]]))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:current-gig]}))

(ns myra.ui.components.gig-details
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub> route> <cmd]]
            [myra.ui.components.typography :refer [-title-xl]]
            [myra.ui.components.inputs :refer [-btn-default -btn-alt]]
            [myra.domain.gig :as gig]
            [myra.ui.components.modal :refer [toggle-modal]]))

(defn render [ctx]
  (let [gig (sub> ctx :current-gig)
        id (:id gig)
        current-route (route> ctx)]
    (when gig
      [:div.py2.px3.bg-white.c-black.mb1
       [-title-xl {:class "m0"} (gig/title gig)]
       [:p.center (:notes gig)]
       [:div.flex.justify-center
        (when (gig/claimed? gig)
          [-btn-alt {:on-click #(<cmd ctx [:employee-gig-actions :cancel] nil)} "CANCEL"])
        (when (gig/pending? gig)
          [-btn-default {:on-click #(<cmd ctx :claim id)} "ADD GIG"])]
       (when (gig/claimed? gig)
         [:div.center.mt2
          [:a.c-cyan.condensed {:href (ui/url ctx {:page "messages" :subpage "thread" :detail id})} "MESSAGE HANDLER"]])])))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:current-gig]
                   :topic :employee-gig-actions}))

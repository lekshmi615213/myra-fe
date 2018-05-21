(ns myra.ui.forms.gig
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [<cmd sub> route>]]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [keechma.toolbox.forms.core :as forms-core]
            [myra.ui.components.form-inputs :refer [controlled-input controlled-textarea render-errors controlled-select]]
            [myra.ui.components.inputs :refer [-btn-default -btn-alt-link]]
            [cljsjs.react-datetime]
            [keechma.toolbox.css.core :refer-macros [defelement]]
            [reagent.core :as r]))

(def datetime (r/adapt-react-class js/Datetime))

(def datetime-classes "inline-block mx-auto bg-white border bd-grey-l c-black h4 flex items-center datetime-input")

(defelement -datetime-input-wrap
  :tag :div
  :class [:mx0-5]
  :style [[:.datetime-input {:width "100%"
                             :max-width "300px"
                             :border-radius "50px"
                             :margin "0 5px 5px 5px"
                             :padding "5px 1rem"
                             :height "40px"
                             :font-family "Source Sans Pro"}]])


(defn render [ctx]
  (let [current-route (route> ctx)
        detail (:detail current-route)
        form-id [:gig detail]
        form-state @(forms-helpers/form-state ctx form-id)
        helpers (forms-helpers/make-component-helpers ctx form-id)
        {:keys [set-value submit]} helpers
        department-options (map (fn [d] [(:id d) (:label d)]) (sub> ctx :departments))]


    [:form {:on-submit submit}
     [:div.p2.bg-white.c-black.mb1.flex.flex-column.center
      [:div.mb1
       [:div.flex.justify-center
        [-datetime-input-wrap
         [datetime {:value (forms-helpers/attr-get-in form-state :startDatetime)
                    :input-props {:placeholder "Gig Start"
                                  :className datetime-classes}
                    :on-change #(set-value :startDatetime %)
                    :date-format "MM-DD-YYYY"
                    :time-format "HH:mm"}]
         (render-errors (forms-helpers/attr-errors form-state :startDatetime))]
        [-datetime-input-wrap
         [datetime {:value (forms-helpers/attr-get-in form-state :endDatetime)
                    :input-props {:placeholder "Gig End"
                                  :className datetime-classes}
                    :on-change #(set-value :endDatetime %)
                    :date-format "MM-DD-YYYY"
                    :time-format "HH:mm"}]
         (render-errors (forms-helpers/attr-errors form-state :endDatetime))]
        [:div.ml0-5
         [controlled-select
          {:form-state form-state :helpers helpers :label "Select Department" :attr :department :options department-options}]]]]
      [:div.fit.mx-auto.mb1
       [controlled-textarea {:form-state form-state :helpers helpers :placeholder "Notes" :attr :notes}]]
      [:div.inline-block
       [-btn-alt-link {:class "mr0-5" :href (ui/url ctx (dissoc current-route :detail))} "CANCEL"]
       [-btn-default {:class "ml0-5"}
        (if (= "new" detail) "ADD GIG" "UPDATE GIG")]]
      (when (not= "new" detail)
        [:div.center.mt2
         [:a.c-cyan.condensed {:href "javascript://" :on-click #(<cmd ctx [:handler-gig-actions :delete] detail)} "DELETE"]])]]))

(def component
  (ui/constructor {:renderer render
                   :topic forms-core/id-key
                   :subscription-deps [:form-state :departments]}))

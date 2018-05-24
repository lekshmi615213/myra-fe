(ns myra.ui.forms.new-password
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub> route> ]]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [keechma.toolbox.forms.core :as forms-core]
            [myra.ui.components.form-inputs :refer [controlled-input]]
            [myra.ui.components.form-api-errors :as form-api-errors]
            [myra.ui.components.inputs :refer [-btn-default -btn-alt-small -new-password-modal]]
           ))

(defn render [ctx]
  (let [form-id [:new-password :form]
        form-state @(forms-helpers/form-state ctx form-id)
        helpers (forms-helpers/make-component-helpers ctx form-id)
        current-route (route> ctx)
        subpage (:subpage current-route)]
  [-new-password-modal
    [:div.mt2
    [form-api-errors/render form-state]
     [:form.flex.flex-column.justify-center.items-center {:on-submit (:submit helpers)}
      [:div {:style {:min-width "320px"}}

       [controlled-input
        {:form-state form-state :helpers helpers :placeholder "New Password" :attr :password :input-type :password}]
       [controlled-input
        {:form-state form-state :helpers helpers :placeholder "Confirm Password" :attr :newPassword :input-type :password}]]
      [:div.mt2.flex.flex-1.items-center.justify-center.w-100
       [-btn-default "CHANGE PASSWORD"]]]]]))

(def component
  (ui/constructor {:renderer render
                   :topic forms-core/id-key
                   :subscription-deps [:form-state]}))
(ns myra.ui.forms.change-password
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub> route> <cmd]]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [keechma.toolbox.forms.core :as forms-core]
            [myra.ui.components.form-inputs :refer [controlled-input]]
            [myra.ui.components.inputs :refer [-btn-default -btn-alt-small]]
            [keechma.toolbox.css.core :refer-macros [defelement]]
            [oops.core :refer [oget]]))

(defn render [ctx]
  (let [form-id [:change-password :form]
        form-state @(forms-helpers/form-state ctx form-id)
        helpers (forms-helpers/make-component-helpers ctx form-id)]
    [:div.mt2
     [:form.flex.flex-column.justify-center.items-center {:on-submit (:submit helpers)}
      [:div {:style {:min-width "320px"}}
      
       [controlled-input
        {:form-state form-state :helpers helpers :placeholder "Old Password" :attr :password :input-type :password}] 
       [controlled-input
        {:form-state form-state :helpers helpers :placeholder "New Password" :attr :newPassword :input-type :password}]]
      [:div.mt2.flex.flex-1.items-center.justify-center.w-100
       [-btn-default "CHANGE PASSWORD"]]]]))

(def component
  (ui/constructor {:renderer render
                   :topic forms-core/id-key
                   :subscription-deps [:form-state]}))

(ns myra.ui.forms.forgot-password
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub> route>]]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [keechma.toolbox.forms.core :as forms-core]
            [myra.ui.components.form-inputs :refer [controlled-input]]
            [myra.ui.components.form-api-errors :as form-api-errors]
            [myra.ui.components.form-api-success :as form-api-success]
            [myra.ui.components.inputs :refer [-btn-default -btn-alt -forgot-password-modal]]))

(defn render [ctx]
  (let [form-id [:forgot-password :form]
        form-state @(forms-helpers/form-state ctx form-id)
        helpers (forms-helpers/make-component-helpers ctx form-id)
        current-route (route> ctx)
        subpage (:subpage current-route)]
    [-forgot-password-modal
     [:h1.c-black.h2 "Enter your email address."]
     [form-api-errors/render form-state]
     [form-api-success/render form-state "Sent an email with reset link"] 
     [:form.flex.justify-center.items-center.flex-column {:on-submit (:submit helpers)}
      [:div.flex.flex-wrap.center.justify-center
       [:div.mx0-5
        [controlled-input
         {:form-state form-state :helpers helpers :placeholder "Email" :attr :email}]]]

      [:div.w-100.mt1
      [:div.mb1 
      [:a {:href (ui/url ctx {:page "reset-password" :subpage ""})}[-btn-default "SUBMIT"]]]]]
     ]))

(def component
  (ui/constructor {:renderer render
                   :topic forms-core/id-key
                   :subscription-deps [:form-state]}))

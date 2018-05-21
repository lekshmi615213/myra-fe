(ns myra.ui.forms.register
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub> route>]]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [keechma.toolbox.forms.core :as forms-core]
            [myra.ui.components.form-inputs :refer [controlled-input controlled-select]]
            [myra.ui.components.form-api-errors :as form-api-errors]
            [myra.ui.components.inputs :refer [-btn-default -btn-alt -login-modal]]))


(defn render [ctx]
  (let [form-id [:register :form]
        form-state @(forms-helpers/form-state ctx form-id)
        helpers (forms-helpers/make-component-helpers ctx form-id)
        department-options (map (fn [d] [(:id d) (:label d)]) (sub> ctx :departments))
        current-route (route> ctx)
        subpage (:subpage current-route)]
    [-login-modal
     [:h1.c-black.h2 "Sign in below or create an account."]
     [:a {:href (ui/url ctx {:page "login" :subpage subpage})} [-btn-default "SIGN IN"]]
     [:h2.c-black.pt1.border-top.bd-grey-l "To create an account, fill in the content below."]
     [form-api-errors/render form-state]
     [:form {:on-submit (:submit helpers)}
      [:div.flex.flex-wrap.justify-center.mt1

        [:div.flex.flex-row.justify-center.items-center.mx0-5.form-wrapper-item
          [controlled-input
            {:form-state form-state :helpers helpers :class "mx0-5" :placeholder "Full Name" :attr :fullName}]
          [controlled-input
            {:form-state form-state :helpers helpers :class "mx0-5" :placeholder "Email" :attr :email}]]

        [:div.flex.flex-row.justify-center.items-center.mx0-5.form-wrapper-item
          [controlled-input
           {:form-state form-state :helpers helpers :class "mx0-5" :placeholder "Password" :attr :password :input-type :password}]
          [controlled-input
            {:form-state form-state :helpers helpers :class "mx0-5" :placeholder "Confirm Password" :attr :password2 :input-type :password}]]

        [:div.flex.flex-row.justify-center.items-center.mx0-5.form-wrapper-item
          [controlled-input
            {:form-state form-state :helpers helpers :class "mx0-5" :placeholder "Phone Number" :attr :phoneNumber}]
          [controlled-select
            {:form-state form-state :helpers helpers :class "select-department mx0-5" :label "Select Department" :attr :department :options department-options}]]]


      [:div.mt2 [-btn-alt "CREATE ACCOUNT"]]]]))

(def component
  (ui/constructor {:renderer render
                   :topic forms-core/id-key
                   :subscription-deps [:form-state :departments]}))

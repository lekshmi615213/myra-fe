(ns myra.ui.forms.login
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub> route>]]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [keechma.toolbox.forms.core :as forms-core]
            [myra.ui.components.form-inputs :refer [controlled-input]]
            [myra.ui.components.form-api-errors :as form-api-errors]
            [myra.ui.components.inputs :refer [-btn-default -btn-alt -login-modal]]))

(defn render [ctx]
  (let [form-id [:login :form]
        form-state @(forms-helpers/form-state ctx form-id)
        helpers (forms-helpers/make-component-helpers ctx form-id)
        current-route (route> ctx)
        subpage (:subpage current-route)]
    [-login-modal
     [:h1.c-black.h2 "Sign in below or create an account."]
     [form-api-errors/render form-state]
     [:span#login-error.api-error-message.status-message]
     [:form.flex.justify-center.items-center.flex-column {:on-submit (:submit helpers)}
      [:div.flex.flex-wrap.center.justify-center
       [:div.mx0-5
        [controlled-input
         {:form-state form-state :helpers helpers :placeholder "Email" :attr :email}]]
       [:div.mx0-5
        [controlled-input
         {:form-state form-state :helpers helpers :placeholder "Password" :attr :password :input-type :password}]
        [:div.left-align.pl1 [:a.c-cyan.h4.bold.mb2 {:href (ui/url ctx {:page "forgot-password" :subpage subpage})} "Forgot password?"]]]]
      [:div.w-100.mt1
       [:div.mb1 [-btn-default "SIGN IN"]]]]
     [:a {:href (ui/url ctx {:page "register" :subpage subpage})} [-btn-alt "CREATE ACCOUNT"]]]))

(def component
  (ui/constructor {:renderer render
                   :topic forms-core/id-key
                   :subscription-deps [:form-state]}))

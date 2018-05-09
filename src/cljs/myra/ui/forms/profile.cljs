(ns myra.ui.forms.profile
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub> route> <cmd]]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [keechma.toolbox.forms.core :as forms-core]
            [myra.ui.components.form-inputs :refer [controlled-input controlled-select]]
            [myra.ui.components.inputs :refer [-btn-default -btn-alt-small]]
            [keechma.toolbox.css.core :refer-macros [defelement]]
            [oops.core :refer [oget]]))


(defelement -dropzone-wrap
  :tag :label
  :class [:block :p0-5 :center :c-cyan :c-h-white :condensed :bold :bg-h-cyan :border :bd-cyan :relative :overflow-hidden]
  :style [{
           :box-sizing "border-box"
           :background-color "rgba(255,255,255,0)"
           :font-size "1rem"
           :max-width "300px"
           :border-radius "50px"
           :cursor "pointer"}])

(defelement -dropzone-input
  :tag :input
  :class [:block :absolute :top-0 :right-0 :left-0 :bottom-0 ]
  :style [{:min-width "100%"
           :opacity 0
           :text-align "right"
           :min-height "100%"
           :font-size "999px"}])

(defn render [ctx]
  (let [form-id [:profile :form]
        form-state @(forms-helpers/form-state ctx form-id)
        helpers (forms-helpers/make-component-helpers ctx form-id)
        department-options (map (fn [d] [(:id d) (:label d)]) (sub> ctx :departments))]
    [:div.mt2
     [:form.flex.flex-column.justify-center.items-center {:on-submit (:submit helpers)}
      [:div {:style {:min-width "320px"}}
       [-dropzone-wrap
        {:style {:margin "0 5px 5px"}} "SELECT NEW AVATAR"
        [-dropzone-input {:type :file
                          :accept "image/jpeg"
                          :on-change #(<cmd ctx [:image-upload :upload] (oget % "target.?files.?0"))}]]
       [controlled-input
        {:form-state form-state :helpers helpers :placeholder "Email" :attr :email}] 
       [controlled-input
        {:form-state form-state :helpers helpers :placeholder "Full Name" :attr :fullName}]
       [controlled-input
        {:form-state form-state :helpers helpers :placeholder "Phone Number" :attr :phoneNumber}]
       [controlled-select
        {:form-state form-state :helpers helpers :label "Select Department" :attr :department :options department-options}]
       #_[:div.mb2
          [:a.h6.c-cyan.pr4.mr4 {:href "#"} "Change password"]]]
      [:div.mt2.flex.flex-1.items-center.justify-center.w-100
       [-btn-default "SAVE"]]]]))

(def component
  (ui/constructor {:renderer render
                   :topic forms-core/id-key
                   :subscription-deps [:form-state :departments]}))

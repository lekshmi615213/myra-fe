(ns myra.ui.pages.profile
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub> route>]]
            [keechma.toolbox.css.core :refer-macros [defelement]]
            [myra.ui.components.inputs :refer [-btn-default -input]]
            [myra.stylesheets.colors :refer [colors-with-variations]]
            [garden.stylesheet :refer [at-media]]
            [garden-basscss.vars :refer [vars]]))

(def breakpoints (:breakpoints @vars))

(defelement -card
  :tag :div
  :class [:bg-white :pb4]
  :style [{:position "relative"
           :box-shadow "0 3px 5px 2px rgba(0,0,0,0.1)"
           :margin-top "96px"}
          [:img {:margin-top "-100px"
                 :width "200px"
                 :height "200px"
                 :border (str "5px solid " (:white colors-with-variations))
                 :box-shadow "0 3px 5px 2px rgba(0,0,0,0.1)"}]])

(defelement -edit
  :tag :div
  :style [{:position "absolute"
           :top "10px"
           :right "10px"}
          [:a {:width "2rem"
               :height "2rem"
               :background-image "url('img/icn-edit.png')"
               :background-size "contain"}]])
  
        
(defn render-details [ctx profile]
  (let [departments (sub> ctx :departments)
        department (:department profile)
        department-label (:label (first (filter #(= department (:id %)) departments)))]
    [:div.center
     [:h1 (:fullName profile)]
     [:div (str "Contact Number: " (:phoneNumber profile))]
     [:div (str "Department: " department-label)]]))


(defn render [ctx]
  (let [profile (sub> ctx :profile-page-profile)
        profile-current? (sub> ctx :profile-page-profile-current?)
        image-upload-preview (sub> ctx :image-upload-preview)
        current-route (route> ctx)
        {:keys [subpage detail]} current-route]

    (when profile
      [-card
       [:div.center
        [:img.circle {:src (or image-upload-preview (:avatarUrl profile) "/img/icn_md.png")}]]
       (when (and (not= "edit" subpage)
                  profile-current?))
       [-edit
        [:a.inline-block {:href (ui/url ctx {:page "profile" :subpage "edit"})}]]
       (if (= "edit" subpage)
         [:div [(ui/component ctx :form-profile)]
          [:hr.my2.border.border-top.bd-gray-l]]
         [render-details ctx profile])
       
       (when (= "edit" subpage)
         [(ui/component ctx :form-change-password)])])))
       
   

(def component
  (ui/constructor {:renderer render
                   :component-deps [:form-profile
                                    :form-change-password]
                   :subscription-deps [:profile-page-profile
                                       :profile-page-profile-current?
                                       :image-upload-preview
                                       :departments]}))

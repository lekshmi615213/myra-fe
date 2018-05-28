(ns myra.ui.pages.session
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub> route>]]
            [keechma.toolbox.css.core :refer-macros [defelement]]
            [myra.ui.components.inputs :refer [-btn-default -btn-arrow]]))

(defelement -splash
  :tag :div
  :class [:flex :justify-center :items-center :flex-column]
  :style [{:position "relative"
           :min-height "100vh"}
          [:&:after {:content "''"
                      :position "fixed"
                      :background-image "url('img/img-header-desktop.png')"
                      :background-repeat "no-repeat"
                      :background-size "cover"
                      :height "100vh"
                      :width "100vw"
                      :top 0
                      :z-index "-1"
                      :box-sizing "border-box"
                      :background-attachment "fixed"}]
          [:.h1-xl {:font-size "3rem"
                    :margin-bottom "1rem"}]])
                    


(defelement -btn-wrapper
  :tag :div
  :class [:flex :items-center :xs-hide :sm-hide :justify-around :flex-wrap :mb2]
  :style [{:width "100%"
           :max-width "700px"}
          [:a {:width "100%"
               :max-width "300px"
               :box-sizing "border-box"}]])

(defelement -btn-wrapper-mobile
  :tag :div
  :class [:flex :items-center :lg-hide :md-hide :justify-around :flex-wrap :mb2]
  :style [{:width "100%"
           :max-width "700px"}
          [:a {:width "100%"
               :max-width "300px"
               :box-sizing "border-box"}]])

(defn login-register-mobile [{:keys [ctx page subpage]}]
  [-btn-wrapper-mobile
    [:a.mt1 {:href (ui/url ctx {:page "login" :subpage "employee"})} 
      (if (= subpage "employee") [-btn-arrow "EMPLOYEE"] [-btn-default "EMPLOYEE"])]
    (if (= subpage "employee")
      (case page
        "login" [(ui/component ctx :form-login)]
        "register" [(ui/component ctx :form-register)]
        "forgot-password" [(ui/component ctx :form-forgot-password)]
        "reset-password" [(ui/component ctx :form-new-password)]
        nil)
      nil)
    [:a.mt1 {:href (ui/url ctx {:page "login" :subpage "handler"})} 
      (if (= subpage "handler") [-btn-arrow "HOSPITAL"] [-btn-default "HOSPITAL"])]
    (if (= subpage "handler")
      (case page
        "login" [(ui/component ctx :form-login)]
        "register" [(ui/component ctx :form-register)]
        "forgot-password" [(ui/component ctx :form-forgot-password)]
        "reset-password" [(ui/component ctx :form-new-password)]
        nil)
      nil)])

(defn login-register [{:keys [ctx page subpage]}]
  [-btn-wrapper
    [:a.mt1 {:href (ui/url ctx {:page "login" :subpage "employee"})} 
      (if (= subpage "employee") [-btn-arrow "EMPLOYEE"] [-btn-default "EMPLOYEE"])]
    [:a.mt1 {:href (ui/url ctx {:page "login" :subpage "handler"})} 
      (if (= subpage "handler") [-btn-arrow "HOSPITAL"] [-btn-default "HOSPITAL"])]
    (if subpage
      (case page
        "login" [(ui/component ctx :form-login)]
        "register" [(ui/component ctx :form-register)]
        "forgot-password" [(ui/component ctx :form-forgot-password)]
        "reset-password" [(ui/component ctx :form-new-password)]
        nil)
      nil)])            

(defn render [ctx]
  (let [page (:page (route> ctx))
        subpage (:subpage (route> ctx))]
    [-splash
     [:img.mt1.xs-hide.sm-hide {:height 150 :src "img/logo-white.svg"}]
     [:img.mt1.md-hide.lg-hide {:height 100 :src "img/logo-white.svg"}]
     [:h1.md-hide.lg-hide.c-white.regular.center "Scheduling made easy"]
     [:h1.h1-xl.sm-hide.xs-hide.c-white.regular.center "Scheduling made easy"]
     [login-register {:ctx ctx :page page :subpage subpage}]
     [login-register-mobile {:ctx ctx :page page :subpage subpage}]]))
      
(def component
  (ui/constructor {:renderer render
                   :component-deps [:form-login
                                    :form-register
                                    :form-forgot-password
                                    :form-new-password]}))

(ns myra.ui.header
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub> route>]]
            [keechma.toolbox.css.core :refer-macros [defelement]]
            [keechma.toolbox.util :refer [class-names]]
            [reagent.core :as r]
            [myra.stylesheets.colors :refer [colors-with-variations]]))


(defelement -nav-li
  :tag :li
  :class [:inline-block]
  :style [[:&.active {:border-bottom "1px solid"}]])

(defelement -mob-li
  :tag :li
  :class [:inline-block :py1]
  :style [[:a.mobile {:color (:black colors-with-variations)}]
          [:&.current
           [:a {:color (:cyan colors-with-variations)
                :font-weight "bold"}]]])

(defelement -navbar-wrapper
  :tag :div
  :class [:flex :justify-between]
  :style [{}
          [:img {:max-width "140px"}]
          [:div.w-450 {:width "100%"
                       :max-width "700px"}]
          [:a.desktop {:color (:black colors-with-variations)}]
          [:li.active {:border-bottom (str "1px solid " (:black colors-with-variations))}]])

(defelement -navbar-wrapper-alt
  :tag :div
  :class [:flex :justify-between]
  :style [[:img {:max-width "140px"}]
          [:div.w-450 {:width "100%"
                       :max-width "700px"}]
          [:a.desktop {:color (:white colors-with-variations)}]
          [:li.active {:border-bottom (str "1px solid " (:white colors-with-variations))}]])


(defn active-future-gigs? [current-route]
   (or (= {:page "gigs" :subpage "future"} (select-keys current-route [:page :subpage]))
       (= "messages" (:page current-route))))
(defn active-history-gigs? [current-route]
  (= {:page "gigs" :subpage "history"} (select-keys current-route [:page :subpage])))

(defn active-profile? [current-route]
  (= "profile" (:page current-route)))

(defelement -dropdown-wrapper
  :tag :div
  :class [:md-hide :lg-hide]
  :style [{:position "absolute"
           :top 0
           :left 0
           :right 0
           :z-index 5}])
  
(defelement -dropdown-content
  :tag :div
  :class [:bg-white-d :left-align]
  :style [{:position "absolute"
            :top "-200px"
            :left 0
            :right 0
            :box-shadow "0px 1px 3px 5px rgba(0,0,0,0.1)"
            :transition "all .25s ease-in-out"}
          [:&.active {:top "0"}]])
          

(defelement -dropdown-button
  :tag :span
  :class [:flex :justify-center :items-center]
  :style [{:position "absolute"
           :width "40px"
           :height "30px"
           :right "40px"
           :z-index "10"
           :cursor "pointer"
           :text-align "left"
           :top "20px"}
          [:span {:position "relative"
                  :display "block"
                  :width "28px"
                  :height "4px"
                  :background (:black colors-with-variations)
                  :transition "all .2s ease-in-out"}
           [:&:before {:position "absolute"
                       :background (:black colors-with-variations)
                       :content "''"
                       :width "28px"
                       :height "4px"
                       :transition "all .2s ease-in-out"
                       :top "-8px"}]
           [:&:after {:position "absolute"
                       :background (:black colors-with-variations)
                       :content "''"
                       :width "28px"
                       :height "4px"
                       :transition "all .2s ease-in-out"
                       :top "8px"}]
           [:&.active {:background "transparent"}
             [:&:before {:transform "rotate(45deg) translate(5px, 6px);"}]
             [:&:after {:transform "rotate(-45deg) translate(5px, -6px);"}]]
           [:&.invert {:background (:white colors-with-variations)}
             [:&:before {:background (:white colors-with-variations)}]
             [:&:after {:background (:white colors-with-variations)}]]
           [:&.invert.active {:background "transparent"}
            [:&:before {:background (:black colors-with-variations)}]
            [:&:after {:background (:black colors-with-variations)}]]]])

(defn mobile-menu [ctx]
  (let [dropdown-open? (r/atom false)]
    (fn []
      (let [current-route (route> ctx)]
        [-dropdown-wrapper
          [-dropdown-button { :on-click #(swap! dropdown-open? not)}
           [:span {:class (class-names {:active @dropdown-open? :invert (active-profile? current-route)})}]]
          [-dropdown-content {:class (class-names {:active @dropdown-open?})}
            [:ul.flex.flex-column.justify-between.ml-auto.pt1.mb0.pl0.center
              [-mob-li {:class (class-names {:current (active-future-gigs? current-route)})}
                [:a.text-decoration-none.h3.mobile
                  {:href (ui/url ctx {:page "gigs" :subpage "future"})} "View gigs"]]
              [-mob-li {:class (class-names {:current (active-history-gigs? current-route)})}
                [:a.text-decoration-none.h3.mobile
                  {:href (ui/url ctx {:page "gigs" :subpage "history"})} "View gig history"]]
              [-mob-li {:class (class-names {:current (active-profile? current-route)})}
                [:a.text-decoration-none.h3.mobile
                  {:href (ui/url ctx {:page "profile"})} "Account settings"]]]]]))))


(defn render [ctx]
  (let [current-route (route> ctx)
        current-account (sub> ctx :current-account)]
    [(if (active-profile? current-route) -navbar-wrapper-alt -navbar-wrapper)
     [:img.mt1.ml1 {:height 88 :src ( if (active-profile? current-route) "img/logo-white.svg" "img/logo-black.svg")}]
     [mobile-menu ctx]
     [:div.xs-hide.sm-hide.w-450
      [:ul.flex.justify-between.ml-auto.py1
       [-nav-li {:class (class-names {:active (active-future-gigs? current-route)})}
        [:a.text-decoration-none.h3.desktop
         {:href (ui/url ctx {:page "gigs" :subpage "future"})} "View gigs"]]
       [-nav-li {:class (class-names {:active (active-history-gigs? current-route)})}
        [:a.text-decoration-none.h3.desktop
         {:href (ui/url ctx {:page "gigs" :subpage "history"})} "View gig history"]]
       [-nav-li  {:class (class-names {:active (active-profile? current-route)})}
        [:a.text-decoration-none.h3.desktop
         {:href (ui/url ctx {:page "profile"})} "Account settings"]]
       [-nav-li  {:class (class-names {:active (active-profile? current-route)})}
        [:a.text-decoration-none.h3.desktop
         {:href (ui/url ctx {:page "logout"})}"Logout " 
         [:span {:style {:font-size "70%"}} (str "(" (:email current-account) ) ")"]]]]]]))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:current-account]}))

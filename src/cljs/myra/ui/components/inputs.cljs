(ns myra.ui.components.inputs
    (:require [garden.stylesheet :refer [at-media]]
              [garden-basscss.vars :refer [vars]]
              [keechma.toolbox.css.core :refer-macros [defelement]]
              [myra.stylesheets.colors :refer [colors-with-variations]]))

(defelement -btn-default
    :tag :button
    :class [:inline-block :py1 :center :c-white :c-h-cyan :condensed :bold :bg-cyan :border :bd-cyan]
    :style [{:width "100%"
             :max-width "300px"
             :border-radius "50px"
             :cursor "pointer"
             :font-size "1.2rem"
             :box-sizing "border-box"}
            [:&:hover {:background-color "rgba(255,255,255,0)"}]])

(defelement -btn-default-link
    :tag :a
    :class [:inline-block :py1 :center :c-white :c-h-cyan :condensed :bold :bg-cyan :border :bd-cyan :text-decoration-none :uppercase]
    :style [{:width "100%"
             :max-width "300px"
             :border-radius "50px"
             :cursor "pointer"
             :font-size "1.2rem"
             :box-sizing "border-box"}
            [:&:hover {:background-color "rgba(255,255,255,0)"}]])

(defelement -btn-arrow
    :tag :button
    :class [:inline-block :py1 :center :c-white :c-h-cyan :condensed :bold :bg-cyan :border :bd-cyan]
    :style [{:width "100%"
             :max-width "300px"
             :border-radius "50px"
             :cursor "pointer"
             :font-size "1.2rem"
             :margin "25px 0"
             :position "relative"
             :box-sizing "border-box"}
            [:&:hover {:background-color "rgba(255,255,255,0)"}]
            [:&:after {:content "''"
                       :width 0
                       :height 0
                       :border-left "15px solid transparent"
                       :border-right "15px solid transparent"
                       :border-bottom "15px solid rgba(255,255,255,0.8)"
                       :position "absolute"
                       :bottom "-26px"
                       :left "calc(50% - 15px)"}]])
            
            
(defelement -btn-alt
    :tag :button
    :class [:inline-block :py1 :center :c-cyan :c-h-white :condensed :bold :bg-h-cyan :border :bd-cyan]
    :style [{:width "100%"
                :box-sizing "border-box"
                :background-color "rgba(255,255,255,0)"
                :font-size "1.2rem"
                :max-width "300px"
                :border-radius "50px"
                :cursor "pointer"}])

(defelement -btn-alt-small
    :tag :button
    :class [:inline-block :p0-5 :center :c-cyan :c-h-white :condensed :bold :bg-h-cyan :border :bd-cyan]
    :style [{:width "100%"
             :box-sizing "border-box"
             :background-color "rgba(255,255,255,0)"
             :font-size "1rem"
             :max-width "300px"
             :border-radius "50px"
             :cursor "pointer"}])

(defelement -btn-alt-link
    :tag :a
    :class [:inline-block :py1 :center :c-cyan :c-h-white :condensed :bold :bg-h-cyan :border :bd-cyan]
    :style [{:width "100%"
             :box-sizing "border-box"
             :background-color "rgba(255,255,255,0)"
             :font-size "1.2rem"
             :max-width "300px"
             :border-radius "50px"
             :cursor "pointer"}])

(defelement -input
    :tag :input
    :class [:inline-block :mx-auto :bg-white :border :bd-grey-l :c-black :h4 :flex :items-center]
    :style [{:width "100%"
                :max-width "300px"
                :border-radius "50px"
                :margin "0 5px 5px 5px"
                :padding "5px 1rem"
                :height "40px"
                :font-family "Source Sans Pro"}])
                

(defelement -input-fullwidth
    :tag :input
    :class [:inline-block :mx-auto :bg-white :border :bd-grey-l :c-black :h4 :flex :items-center]
    :style [{:width "100%"
                :border-radius "50px"
                :margin "0 5px 5px 5px"
                :padding "5px 1rem"
                :height "40px"
                :font-family "Source Sans Pro"}])
                

(defelement -textarea
    :tag :textarea
    :class [:mx-auto :bg-white :border :bd-grey-l :c-black :h4 :block :px2 :py1]
    :style [{:width "750px"
             :max-width "100%"
             :height "200px"
             :border-radius "40px"
             :margin "0 5px"
             :font-family "Source Sans Pro"}])            

(defelement -login-modal
    :tag :div
    :class [:max-width-4 :center :p2]
    :style [{:width "100%"
             :margin "0 5px"
             :font-family "Source Sans Pro"
             :border-radius "5px"
             :background-color "rgba(255,255,255,0.8)"}])

(defelement -forgot-password-modal
    :tag :div
    :class [:max-width-4 :center :p2]
    :style [{:width "100%"
             :margin "0 5px"
             :font-family "Source Sans Pro"
             :border-radius "5px"
             :background-color "rgba(255,255,255,0.8)"}])

(defelement -new-password-modal
    :tag :div
    :class [:max-width-4 :center :p2]
    :style [{:width "100%"
             :margin "0 5px"
             :font-family "Source Sans Pro"
             :border-radius "5px"
             :background-color "rgba(255,255,255,0.8)"}])
             

(defelement -select
    :tag :select
    :class [:inline-block :mx-auto :bg-white :border :bd-grey-l :c-black :h4 :flex :items-center]
    :style [{:width "100%"
                :max-width "300px"
                :border-radius "50px"
                :margin "0 0px 5px 4px"
                :padding "5px 1rem"
                :height "40px"
                :font-family "Source Sans Pro"}])

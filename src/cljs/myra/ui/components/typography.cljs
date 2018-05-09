(ns myra.ui.components.typography
  (:require [myra.stylesheets.colors :refer [colors-with-variations]]
            [keechma.toolbox.css.core :refer-macros [defelement]]))

(defelement -title-xl
  :tag :h1
  :class [:c-black :center :regular]
  :style [{:font-size "3rem"}])

(defelement -message
  :tag :div
  :class [:align-self-end :border :bd-grey :bg-white :c-black :ml-auto :inline-block :max-width-2 :mb1]
  :style [{:border-radius "15px"
           :padding "0.5rem 1.5rem"}])

(defelement -message-alt
  :tag :div
  :class [:align-self-end :border :bd-white-d :bg-white-d :c-black :inline-block :max-width-2 :mb1]
  :style [{:border-radius "15px"
           :padding "0.5rem 1.5rem"}])

;; sender is bool to determine which message will appear on screen
(defn message [{:keys [text sender]}]
  (if sender [:div.right-align [-message text]] [:div [-message-alt text]]))

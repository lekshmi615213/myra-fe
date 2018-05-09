(ns myra.stylesheets.typography
  (:require [myra.stylesheets.colors :refer [colors]]
            [garden-basscss.vars :refer [vars]]
            [garden.stylesheet :refer [at-media]]))

(def breakpoints (:breakpoints @vars))

(defn stylesheet []
  [[:.a-text {:font-size "1.125rem"}]
   [:.condensed {:font-family "Roboto Condensed"
                 :font-weight "bold"}]
   [:.semibold {:font-weight 600}]])

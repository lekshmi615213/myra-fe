(ns myra.ui.components.modal
  (:require
   [keechma.ui-component :as ui]
   [keechma.toolbox.ui :refer [sub> <cmd]]
   [garden.stylesheet :refer [at-media]]
   [garden-basscss.vars :refer [vars]]
   [keechma.toolbox.css.core :refer-macros [defelement]]
   [myra.stylesheets.colors :refer [colors-with-variations]]
   [myra.ui.components.inputs :refer [-btn-alt -btn-default]]))

(defelement -modal-wrapper
  :tag :div
  :class [:flex :justify-center :items-center]
  :style [{:position "fixed"
           :top 0
           :left 0
           :bottom 0
           :right 0
           :z-index 15
           :background-color "rgba(0,0,0,0.4)"}])

(defelement -modal-box
  :tag :div
  :class [:max-width-3 :flex :flex-column :items-center :justify-center :center :p2]
  :style [{:border-radius "5px"
           :margin "1rem"
           :background-color "rgba(255,255,255,0.9)"
           :min-width "60vw"}
          [:img {:width "128px"}]
          [:.parent {:width "100%"}]])

(defn toggle-modal [ctx modal]
  (<cmd ctx [:modal :toggle] modal))

(defn render [ctx {:keys [id]}]
  (when-let [current-modal (sub> ctx :current-modal)]
    (let [current-gig (sub> ctx :current-gig)]
      [-modal-wrapper
       {:on-click (fn [e]
                    (let [target (.-target e)
                          current-target (.-currentTarget e)]
                      (when (= target current-target)
                        (<cmd ctx [:modal :toggle] current-modal))))}

       [-modal-box
        [(ui/component ctx current-modal) {:close-modal (fn [] (<cmd ctx [:modal :toggle] current-modal))}]]])))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:current-modal :current-gig]
                   :component-deps [:modal-cancel-gig
                                    :modal-remove-gig
                                    :modal-late-gig
                                    :modal-added-gig]}))

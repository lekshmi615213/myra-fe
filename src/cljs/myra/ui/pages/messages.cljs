(ns myra.ui.pages.messages
  (:require [keechma.ui-component :as ui]
            [myra.ui.components.inputs :refer [-btn-default -input-fullwidth]]
            [keechma.toolbox.css.core :refer-macros [defelement]]
            [myra.ui.components.typography :refer [message -message -message-alt]]
            [reagent.core :as r]
            [keechma.toolbox.ui :refer [sub> <cmd]]))

(defelement -inbox
  :tag :div
  :class [:bg-white]
  :style [{:max-height "600px" 
           :overflow "auto"
           :margin-top "0.5rem"}])

(defelement -inbox-inner
  :tag :div
  :class [:p2])

(defn scroll-to-bottom [inbox-inner-atom inbox-atom]
  (let [inbox-inner @inbox-inner-atom
        inbox @inbox-atom
        inbox-inner-height (.-clientHeight inbox-inner)
        inbox-height (.-clientHeight inbox)
        inbox-scroll-top (.-scrollTop inbox)]
      (when (> inbox-inner-height inbox-height)
        (set! (.-scrollTop inbox) (- inbox-inner-height inbox-height)))))

(defn inbox-render [ctx inbox-atom inbox-inner-atom]
  (let [thread-messages (sub> ctx :thread-messages)
        current-account (sub> ctx :current-account)
        current-profile ((:profile current-account))]
    [-inbox {:ref #(reset! inbox-atom %)}
     [-inbox-inner {:ref #(reset! inbox-inner-atom %)}
      (map (fn [m]
             ^{:key (:id m)} [message {:sender (= (get-in m [:sender :id]) (:id current-profile)) :text (:body m)}])
           thread-messages)]]))

(defn inbox [ctx]
  (let [inbox-inner-atom (atom nil)
        inbox-atom (atom nil)]
    (r/create-class
      {:reagent-render (partial inbox-render ctx inbox-atom inbox-inner-atom)
       :component-did-mount (partial scroll-to-bottom inbox-inner-atom inbox-atom)
       :component-did-update (partial scroll-to-bottom inbox-inner-atom inbox-atom)})))


(defn render [ctx]
  (let [current-account (sub> ctx :current-account)
        current-gig (sub> ctx :current-gig)]
    (when (and current-gig current-account)
      (let [handler ((:handler current-gig))
            employee ((:employee current-gig))
            current-profile ((:profile current-account))
            interlocutor (first (filter #(not= (:id %) (:id current-profile)) [handler employee]))]
        
        [:div.mt2
         [:div.bg-cyan.flex.justify-between.items-center.px1 
          [:a.c-white.h3.text-decoration-none {:href (ui/url ctx {})} "< Back"]
          [:p.c-white.h3 (:fullName interlocutor)]]
         
         [inbox ctx]
         [(ui/component ctx :form-message)]]))))



(def component
  (ui/constructor {:renderer render
                   :component-deps [:form-message]
                   :subscription-deps [:thread-messages
                                       :current-account
                                       :current-gig]}))

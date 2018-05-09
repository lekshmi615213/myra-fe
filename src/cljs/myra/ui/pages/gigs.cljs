(ns myra.ui.pages.gigs
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub> route>]]
            [keechma.toolbox.css.core :refer-macros [defelement]]
            [keechma.toolbox.util :refer [class-names]]
            [myra.stylesheets.colors :refer [colors-with-variations]]
            [myra.ui.components.typography :refer [-title-xl]]
            [myra.ui.components.inputs :refer [-btn-default -btn-default-link -btn-alt -input -textarea]]
            [myra.domain.gig :as gig]
            [myra.util :as util]))

(defelement -gig
  :tag :li
  :class [:c-white]
  :style [{:margin-bottom "5px"}
          [:&.claimed {:background (:teal colors-with-variations)}]
          [:&.pending {:background (:cyan colors-with-variations)}]
          [:&.done {:background (:grey colors-with-variations)}]])

(defelement -gig-menu
  :tag :div
  :class [:bold :flex])

(defelement -gig-menu-link
  :tag :a
  :class [:c-white :condensed :mx1])

(defelement -gig-menu-fake-link
  :tag :span
  :class [:c-white :condensed :mx1])


(defn future-header [ctx current-employee?]
  [-title-xl
   (if current-employee?
     "Add or cancel gigs"
     "Create or delete gigs")])

(defn route-or-current-month [{:keys [month]}]
  (js/parseInt (if month month (util/current-month))))

(defn route-or-current-year [{:keys [year]}]
  (js/parseInt (if year year (util/current-year))))

(defn prev-month-url [current-route]
  (let [month (route-or-current-month current-route)
        year (route-or-current-year current-route)
        prev-month (dec month)
        params (if (zero? prev-month)
                 {:month 12 :year (dec year)}
                 {:month prev-month :year year})]
    (merge current-route params)))

(defn next-month-url [current-route]
  (let [month (route-or-current-month current-route)
        year (route-or-current-year current-route)
        next-month (inc month)
        params (if (< 12 next-month)
                 {:month 1 :year (inc year)}
                 {:month next-month :year year})]
    (merge current-route params)))

(defn current-month-title [current-route]
  (str (util/month-name (route-or-current-month current-route))
       ", "
       (route-or-current-year current-route)))

(defn history-header [ctx current-employee?]
  (let [current-route (route> ctx)]
    [:div
     [-title-xl "Your gig history"]
     [:div.flex.justify-around.items-center.h2.max-width-2.mx-auto
      [:a.c-black.text-decoration-none {:href (ui/url ctx (next-month-url current-route))} "<"]
      [:span.c-black (current-month-title current-route)]
      [:a.c-black.text-decoration-none {:href (ui/url ctx (prev-month-url current-route))} ">"]]]))


(defn render-gig-create [ctx gig]
  (let [current-route (route> ctx)]
    [:div.p2.bg-white.c-black.mb1.flex.flex-column.center
     [:div.mb1
      [-input {:placeholder "Gig Date"}]
      [-input {:placeholder "Gig Start Time"}]
      [-input {:placeholder "Gig End Time"}]]
     [:div.fit.mx-auto.mb1
      [-textarea]]
     [:div.inline-block
      [-btn-alt {:on-click #(ui/redirect ctx (dissoc current-route :detail))} "CANCEL"]
      [-btn-default "ADD GIG"]]
     [:div.center.mt2
      [:a.c-cyan.condensed {:href "#"} "DELETE"]]]))

(defn gig-handler-menu [ctx gig]
  (let [current-route (route> ctx)
        detail (:detail current-route)
        id (:id gig)
        employee ((:employee gig))]
    [-gig-menu
     (when employee
       [-gig-menu-link {:href (ui/url ctx {:page "messages" :subpage "thread" :detail id})}
        "MESSAGE EMPLOYEE"])
     (if (= detail id)
       [-gig-menu-link {:href (ui/url ctx (dissoc current-route :detail))} "CLOSE"]
       [-gig-menu-link {:href (ui/url ctx (merge current-route {:detail id}))} "EDIT"])]))


(defn gig-employee-menu [ctx gig]
  (let [current-route (route> ctx)
        detail (:detail current-route)
        id (:id gig)]
    (into [-gig-menu]
          (cond
            (gig/claimed? gig)
            [[-gig-menu-link
              {:href (ui/url ctx {:page "messages" :subpage "thread" :detail id})}
              "MESSAGE HANDLER"]
             (if (= id detail)
               [-gig-menu-link
                {:href (ui/url ctx (dissoc current-route :detail))}
                "CLOSE"]
               [-gig-menu-link
                {:href (ui/url ctx (merge current-route {:detail id}))}
                "EDIT"])]
            
            (gig/pending? gig)
            [[-gig-menu-link
              {:href (ui/url ctx (merge current-route {:detail id}))}
              "SELECT"]]

            (gig/done? gig)
            [[-gig-menu-link
              {:href (ui/url ctx {:page "messages" :subpage "thread" :detail id})}
              "MESSAGES"]
             [-gig-menu-fake-link "DONE"]]

            :else nil))))

(defn gig-menu [ctx current-employee? gig]
  (if current-employee?
    [gig-employee-menu ctx gig]
    [gig-handler-menu ctx gig]))

(defn render-gig [ctx gig current-gig current-employee?]
  (let [{:keys [id state title]} gig
        current-route (route> ctx)
        current-gig? (= (:id gig) (:id current-gig))
        gig-employee ((:employee gig))]
 
    [-gig {:class (class-names {:claimed (gig/claimed? gig) 
                                :pending (gig/pending? gig)
                                :done    (gig/done? gig)})}
     [:div.flex.justify-between.items-center
      [:div.h3.p1
       (if current-employee?
         (gig/title gig)
         (if gig-employee
           (str (gig/title gig) " - Booked by: " (:fullName gig-employee))
           (gig/title gig)))]
      [gig-menu ctx current-employee? gig]] 
     (when current-gig?
       (if current-employee?
         [(ui/component ctx :gig-details)]
         [(ui/component ctx :form-gig)]))]))

(defn gigs-with-current [gigs current-gig]
  (let [gig-ids (map :id gigs)
        current-gig-id (:id current-gig)]
    (if current-gig
      (if (contains? (set gig-ids) current-gig-id)
        gigs
        (into [current-gig] gigs))
      gigs)))

(defn handler-buttons [ctx current-route current-employee?]
  (let [current-route (route> ctx)
        subpage (:subpage current-route)]
    (when (and (= "future" subpage)
               (not current-employee?))
      [:div.center
       [-btn-default-link {:href (ui/url ctx {:page "gigs" :subpage "future" :detail "new"})} "Create New Gig"]])))

(defn render [ctx]
  (let [current-route     (route> ctx)
        subpage           (:subpage current-route)
        gigs              (sub> ctx :gigs)
        current-gig       (sub> ctx :current-gig)
        current-employee? (sub> ctx :current-employee?)
        current-detail    (:detail current-route)]
    [:div
     (case subpage
       "future"  [future-header ctx current-employee?]
       "history" [history-header ctx current-employee?]
       nil)
     [handler-buttons ctx current-route current-employee?]
     (if (or (seq gigs)
               (= "new" current-detail))
       (into [:ul.list-reset
              (when (= "new" current-detail)
                [-gig {:class "pending"}
                 [:div.flex.justify-between.items-center [:div.h3.p1 "New Gig"]]
                 [(ui/component ctx :form-gig)]])]
             (map (fn [gig]
                    ^{:key (:id gig)} [render-gig ctx gig current-gig current-employee?])
                  (gigs-with-current gigs current-gig)))
       [:div.bd-cyan.border.rounded.mt2.p1.bold.c-cyan "There are no gigs that match the selection"])]))

(def component
  (ui/constructor {:renderer render
                   :component-deps [:gig-details :form-gig]
                   :subscription-deps [:gigs
                                       :current-gig
                                       :current-employee?
                                       :current-handler?]}))

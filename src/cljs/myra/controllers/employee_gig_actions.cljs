(ns myra.controllers.employee-gig-actions
  (:require [keechma.toolbox.pipeline.core :as pp :refer-macros [pipeline!]]
            [keechma.toolbox.pipeline.controller :as pp-controller]
            [myra.util.api :refer [gql-req]]
            [myra.gql :as gql]
            [myra.edb :as edb]))

(def controller
  (pp-controller/constructor
   (fn [route] true)
   {:claim (pipeline! [value app-db]
             (gql-req gql/claim-gig-m {:id value} (get-in app-db [:kv :jwt]))
             (pp/commit! (edb/insert-item app-db :gig (get-in value [:gig :gig]))))
    :cancel (pipeline! [value app-db]
              (gql-req gql/cancel-claimed-gig-m {:id (get-in app-db [:route :data :detail])} (get-in app-db [:kv :jwt]))
              (pp/commit! (edb/insert-item app-db :gig (get-in value [:gig :gig])))
              (rescue! [error]
                (pp/send-command! [:modal :toggle] :modal-late-gig)))}))

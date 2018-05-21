(ns myra.controllers.handler-gig-actions
  (:require [keechma.toolbox.pipeline.core :as pp :refer-macros [pipeline!]]
            [keechma.toolbox.pipeline.controller :as pp-controller]
            [myra.util.api :refer [gql-req]]
            [myra.gql :as gql]
            [myra.edb :as edb]))

(def controller
  (pp-controller/constructor
   (fn [route] true)
   {:delete (pipeline! [value app-db]
              (when (js/confirm "Are you sure you want to delete this gig?")
                (pipeline! [value app-db]
                  (gql-req gql/delete-gig-m {:id value} (get-in app-db [:kv :jwt]))
                  (pp/commit! (edb/remove-item app-db :gig (get-in value [:gig :gig :id]))))))}))

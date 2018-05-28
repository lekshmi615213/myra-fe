(ns myra.gql
  (:require [graphql-builder.parser :refer-macros [defgraphql]]
            [graphql-builder.core :as core]))

(defn get-mutation [query-map name]
  (get-in query-map [:mutation name]))

(defn get-query [query-map name]
  (get-in query-map [:query name]))

(defgraphql queries "resources/graphql/queries.graphql")

(def q-map (try (core/query-map queries {:inline-fragments true}) (catch js/Object e (throw queries))))

(def current-account-q (get-query q-map :current-account))
(def future-gigs-q (get-query q-map :future-gigs))
(def gig-by-id-q (get-query q-map :gig-by-id))
(def gigs-by-month-q (get-query q-map :gigs-by-month))
(def profile-by-id-q (get-query q-map :profile-by-id))
(def get-thread-messages-q (get-query q-map :get-thread-messages))
(def message-by-id-q (get-query q-map :message-by-id))

(def login-m (get-mutation q-map :login))
(def register-m (get-mutation q-map :register))
(def create-gig-m (get-mutation q-map :create-gig))
(def update-gig-m (get-mutation q-map :update-gig))
(def claim-gig-m (get-mutation q-map :claim-gig))
(def cancel-claimed-gig-m (get-mutation q-map :cancel-claimed-gig))
(def create-message-m (get-mutation q-map :create-message))
(def update-profile-m (get-mutation q-map :update-profile))
(def delete-gig-m (get-mutation q-map :delete-gig))
(def forgot-password-m (get-mutation q-map :forgot-password))
(def reset-password-m (get-mutation q-map :reset-password))

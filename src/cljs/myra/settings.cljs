(ns myra.settings)

(def settings
  {:jwt-name     "myra-jwt"
   :gql-endpoint {:dev  "http://localhost:4000/api/graphql"
                  :prod "https://myra-backend-staging.herokuapp.com/api/graphql"}})

(def websocket-endpoint
  (if js/goog.DEBUG
    "ws://localhost:4000/socket"
    "wss://myra-backend-staging.herokuapp.com/socket"))

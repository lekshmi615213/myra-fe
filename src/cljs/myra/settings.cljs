(ns myra.settings)

(def settings
  {:jwt-name     "myra-jwt"
   :gql-endpoint {:dev  "http://localhost:4000/api/graphql"
                  :prod "https://tranquil-falls-15561.herokuapp.com/api/graphql"}})

(def websocket-endpoint
  (if js/goog.DEBUG
    "ws://localhost:4000/socket"
    "wss://tranquil-falls-15561.herokuapp.com/socket"))

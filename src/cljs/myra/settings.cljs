(ns myra.settings)

(def settings
  {:jwt-name     "myra-jwt"
   :gql-endpoint {:dev  "https://tranquil-falls-15561.herokuapp.com/api/graphql" ;;"https://tranquil-falls-15561.herokuapp.com/api/graphql"
                  :prod "https://tranquil-falls-15561.herokuapp.com/api/graphql"}})

(def websocket-endpoint
  (if js/goog.DEBUG
    ;"ws://tranquil-falls-15561.herokuapp.com/socket"
    "wss://tranquil-falls-15561.herokuapp.com/socket"
    "wss://tranquil-falls-15561.herokuapp.com/socket"))

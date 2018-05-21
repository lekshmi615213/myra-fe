(ns myra.settings)

(def settings
  {:jwt-name     "myra-jwt"
   :gql-endpoint {:dev  "https://dry-spire-96871.herokuapp.com/api/graphql" ;;"https://dry-spire-96871.herokuapp.com/api/graphql"
                  :prod "https://dry-spire-96871.herokuapp.com/api/graphql"}})

(def websocket-endpoint
  (if js/goog.DEBUG
    ;"ws://dry-spire-96871.herokuapp.com/socket"
    "wss://dry-spire-96871.herokuapp.com/socket"
    "wss://dry-spire-96871.herokuapp.com/socket"))

(ns myra.forms.login
  (:require [keechma.toolbox.forms.core :as forms-core]
            [myra.forms.validators :as v]
            [myra.util.api :refer [gql-req]]
            [myra.gql :as gql]
            [myra.edb :as edb]
            [keechma.toolbox.pipeline.core :as pp :refer-macros [pipeline!]]
            [hodgepodge.core :refer [set-item local-storage]]
            [myra.settings :refer [settings]]))

(defrecord LoginForm [validator])

(defn prepare-data [data app-db]
  {:credentials (select-keys data [:email :password])}
)

(defn showError []
  (let [span (. js/document getElementById "login-error")]  
    (set! (. span -innerHTML) "Not authenticated or authorized.")))

(defn redirectPage [token account app-db data] 
  (pipeline! [value app-db]
        (set-item local-storage (:jwt-name settings) token)
        (pp/commit! (-> app-db
                        (assoc-in [:kv :jwt] token)
                        (edb/insert-named-item :account :current account)))
        (pp/redirect! {:page "gigs" :subpage "future"})        
        (rescue! [error])))

(defmethod forms-core/submit-data LoginForm [_ app-db _ data] 
  (pipeline! [value app-db]
    (let [span (. js/document getElementById "login-error")]
    (set! (. span -innerHTML) ""))
    (gql-req gql/login-m (prepare-data data app-db))
    (rescue! [error])))


(defmethod forms-core/on-submit-success LoginForm [this app-db form-props data]
  (let [{:keys [token account]} (:login data)]
    (let [subpage (get-in app-db [:route :data :subpage])
      subpageStr (clojure.string/lower-case subpage)   
      userType (get-in account [:profile :type])
      userTypeStr (clojure.string/lower-case userType)
    ]
    (if (= subpageStr userTypeStr)
      (redirectPage token account  app-db data)
      (showError)
    ))))

(defn constructor []
  (->LoginForm (v/to-validator {:email    [:not-empty :email]
                                :password [:not-empty]})))


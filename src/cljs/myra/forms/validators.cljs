(ns myra.forms.validators
  (:require [clojure.string :as str]
            [forms.validator :as v]
            [oops.core :refer [ocall]]))

(def email-regex  #"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")

(def url-regex #"https?://(www\.)?[-a-zA-Z0-9@:%._\+~#=]{2,256}\.[a-z]{2,6}\b([-a-zA-Z0-9@:%_\+.~#?&//=]*)")

(def states
  #{"AL", "AK", "AS", "AZ", "AR", "CA", "CO", "CT", "DE", "DC", "FL", "GA", "GU", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MH", "MA", "MI", "FM", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "MP", "OH", "OK", "OR", "PW", "PA", "PR", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "VI", "WA", "WV", "WI", "WY"})


(defn zero-count? [v]
  (if (satisfies? ICounted v)
    (zero? (count v))
    false))

(defn not-empty? [v _ _]
  (cond
    (nil? v) false
    (= "" v) false
    (zero-count? v) false
    :else true))

(defn url? [v _ _]
  (not (nil? (re-matches url-regex (str v)))))

(defn email? [v _ _]
   (if (not (not-empty? v nil nil))
    true
    (re-matches email-regex (str v))
  ))

(defn edu-email? [v _ _]
  (if (nil? v)
    true
    (str/ends-with? (str/trim v) ".edu")))

(defn number0>100? [v _ _]
  (if (not (not-empty? v nil nil))
    true
    (let [n (js/parseFloat v 10)]
      (and (< 0 n) (>= 100 n)))))

(defn bool? [v _ _]
  (if (nil? v)
    true
    (or (= true v) (= false v))))

(defn numeric? [v _ _]
  (if (not (not-empty? v nil nil))
    true
    (re-matches #"^\d+$" v)))

(defn valid-phone? [v _ _]
  (if (seq v)
    (
    and (re-matches #"^\d+$" v)
        (< 7 (count v))
        (> 11 (count v))
    )
    true))

(defn ok-password? [v _ _]
  (if (seq v)
    (< 7 (count v))
    true))

(defn valid-state? [v _ _]
  (if (seq v)
    (contains? states (str/upper-case v))
    true))

(defn valid-cvv? [v _ _]
  (if (seq v)
    (or (= 3 (count v))
        (= 4 (count v)))
    true))

(defn valid-fullname? [v _ _]
  (if (seq v)
    (< 2 (count v))
    true))



(defn valid-zipcode? [v _ _]
  (if (seq v)
    (not (nil? (re-matches #"(^\d{5}$)|(^\d{5}-\d{4}$)" (str v))))
    true))

(defn password-confirmation [_ data _]
  (let [pass (:password data)
        pass-confirmation (:password2 data)]
    (if (some nil? [pass pass-confirmation])
      true
      (= pass pass-confirmation))))

(def types
  {:not-empty          {:message   "Value can't be empty"
                        :validator not-empty?}
   :wrong-access-token {:message   "Wrong access-token"
                        :validator (fn [_ _ _] true)}
   :bool               {:message   "Value must be true or false"
                        :validator bool?}
   :url                {:message   "Value is not a valid URL"
                        :validator url?}
   :email              {:message   "Value is not a valid email"
                        :validator email?}
   :edu-email          {:message   "Not a valid .edu email"
                        :validator edu-email?}
   :email-confirmation {:message   "Email doesn't match email confirmation"
                        :validator (fn [_ data _]
                                     (let [email              (:email data)
                                           email-confirmation (:email-confirmation data)]
                                       (if (some nil? [email email-confirmation])
                                         true
                                         (= email email-confirmation))))}

   :password-confirmation {:message   "Passwords don't match"
                           :validator password-confirmation}
   :ok-password           {:message   "Password must have at least 8 characters"
                           :validator ok-password?}
   :numeric               {:message   "Value is not a number"
                           :validator numeric?}
   :phone                 {:message   "Value is not a valid phone number"
                           :validator valid-phone?}
   :valid-state           {:message   "Not a valid US state"
                           :validator valid-state?}
   :valid-zipcode         {:message   "Not a valid Zipcode"
                           :validator valid-zipcode?}
   :valid-fullname         {:message   "Are you sure you entered your name correctly?"
                           :validator valid-fullname?}

   :0>                     {:message   "Must be bigger than zero"
                            :validator (fn [v _ _]
                                         (if (not (not-empty? v nil nil))
                                           true
                                           (< 0 (js/parseFloat v))))}
   :0>100                  {:message   "Must be between 0 and 100"
                            :validator number0>100?}
   :future-date            {:message   "Date must be in the future"
                            :validator (fn [v _ _]
                                         (let [current-timestamp (ocall js/Date "now")]
                                           (if (not (not-empty? v nil nil))
                                             true
                                             (< current-timestamp (ocall v "format" "x")))))}
   :future-date-from-start {:message   "Date must after the start date"
                            :validator (fn [_ data _]
                                         (let [start (get-in data [:startDatetime])
                                               end   (get-in data [:endDatetime])]
                                           (if (or (not (not-empty? start nil nil))
                                                   (not (not-empty? end nil nil)))
                                             true
                                             (< (ocall start "format" "x") (ocall end "format" "x")))))}})

(defn get-validator-message [type]
  (or (get-in types [type :message])
      "Value failed validation."))

(defn to-validator
  "Helper function that extracts the validator definitions."
  [config]
  (v/validator
   (reduce-kv (fn [m attr v]
                (assoc m attr
                       (map (fn [k] [k (get-in types [k :validator])]) v))) {} config)))

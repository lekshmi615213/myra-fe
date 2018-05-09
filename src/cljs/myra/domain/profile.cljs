(ns myra.domain.profile)

(defn employee? [profile]
  (= "EMPLOYEE" (:type profile)))

(defn handler? [profile]
  (= "HANDLER" (:type profile)))

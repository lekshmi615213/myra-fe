(ns myra.ui.components.form-inputs
  (:require [keechma.toolbox.forms.helpers :as forms-helpers]
            [myra.forms.validators :as validators]
            [myra.ui.components.inputs :as inputs]))

(defn render-errors [attr-errors]
  (when-let [errors (get-in attr-errors [:$errors$ :failed])]
    (into [:ul.list-style-none.c-red.p0.error-message]
          (doall (map (fn [e]
                        [:li.p0 (validators/get-validator-message e)])
                      errors)))))

(defn controlled-input [{:keys [form-state helpers placeholder label attr input-type class]}]
  (let [{:keys [on-change on-blur]} helpers]
    [:div
     [inputs/-input
      {:placeholder placeholder
       :on-change (on-change attr)
       :on-blur (on-blur attr)
       :type (or input-type :text)
       :value (forms-helpers/attr-get-in form-state attr)
       :class class}]
     (render-errors (forms-helpers/attr-errors form-state attr))]))

(defn controlled-input-fullwidth [{:keys [form-state helpers placeholder label attr input-type class]}]
  (let [{:keys [on-change on-blur]} helpers]
    [inputs/-input-fullwidth
     {:placeholder placeholder
      :on-change (on-change attr)
      :on-blur (on-blur attr)
      :type (or input-type :text)
      :value (forms-helpers/attr-get-in form-state attr)
      :class class}]))

(defn controlled-textarea [{:keys [form-state helpers placeholder label attr rows class]}]
  (let [{:keys [on-change on-blur]} helpers]
    [:div
     [inputs/-textarea
      {:placeholder placeholder
       :rows (or rows 8)
       :on-change (on-change attr)
       :on-blur (on-blur attr)
       :value (forms-helpers/attr-get-in form-state attr)
       :class class}]
     (render-errors (forms-helpers/attr-errors form-state attr))]))

(defn controlled-select [{:keys [form-state helpers placeholder label attr options class]}]
  (let [{:keys [on-change select]} helpers]
    [:div
     [inputs/-select
      {:on-change (on-change attr)
       :class class
       :value (or (forms-helpers/attr-get-in form-state attr) "")}
      [:option {:value ""} (or label placeholder)]
      (doall (map (fn [[value label]]
                    [:option {:value value :key value} label]) options))]
     (render-errors (forms-helpers/attr-errors form-state attr))]))

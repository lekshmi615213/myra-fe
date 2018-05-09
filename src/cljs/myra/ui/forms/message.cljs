(ns myra.ui.forms.message
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub> route>]]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [keechma.toolbox.forms.core :as forms-core]
            [myra.ui.components.form-inputs :refer [controlled-input-fullwidth]]
            [myra.ui.components.inputs :refer [-btn-default -btn-alt -login-modal -input-fullwidth]]
            ))

(defn render [ctx]
  (let [detail (:detail (route> ctx))
        form-id [:message detail]
        form-state @(forms-helpers/form-state ctx form-id)
        helpers (forms-helpers/make-component-helpers ctx form-id)
        submit (:submit helpers)]
    [:form {:on-submit submit}
     [:div.p2.bg-white.flex.items-end
      [controlled-input-fullwidth {:placeholder "Enter Message" :attr :body :helpers helpers :form-state form-state}]
      [:div.xs-hide.sm-hide.col-6 [-btn-default "SEND"]]]]))

(def component
  (ui/constructor {:renderer render
                   :topic forms-core/id-key
                   :subscription-deps [:form-state]}))

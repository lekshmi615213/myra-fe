(ns myra.controllers
  (:require [myra.controllers.initializer :as initializer]
            [myra.controllers.employee-gig-actions :as employee-gig-actions]
            [keechma.toolbox.dataloader.controller :as dataloader-controller]
            [myra.controllers.messaging :as messaging]
            [myra.controllers.logout :as logout]
            [myra.datasources :refer [datasources]]
            [myra.edb :refer [edb-schema]]
            [keechma.toolbox.forms.controller :as forms-controller]
            [keechma.toolbox.forms.mount-controller :as forms-mount-controller]
            [myra.controllers.image-upload :as image-upload]
            [myra.forms :as forms]
            [myra.controllers.modal :as modal]
            [myra.controllers.handler-gig-actions :as handler-gig-actions]))

(def controllers
  (-> {:initializer          initializer/controller
       :logout               logout/controller
       :employee-gig-actions employee-gig-actions/controller
       :handler-gig-actions  handler-gig-actions/controller
       :modal                modal/controller
       :messaging            (messaging/->Controller)
       :image-upload         image-upload/controller}
      (forms-controller/register forms/forms)
      (forms-mount-controller/register forms/forms-ids)
      (dataloader-controller/register datasources edb-schema)))

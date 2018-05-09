(ns myra.ui
  (:require [myra.ui.main :as main]
            [myra.ui.header :as header]
            [myra.ui.pages.gigs :as page-gigs]
            [myra.ui.pages.messages :as page-messages]
            [myra.ui.pages.profile :as page-profile]
            [myra.ui.pages.session :as page-session]
            [myra.ui.forms.login :as form-login]
            [myra.ui.forms.register :as form-register]
            [myra.ui.forms.profile :as form-profile]
            [myra.ui.forms.gig :as form-gig]
            [myra.ui.forms.message :as form-message]
            [myra.ui.components.gig-details :as gig-details]
            [myra.ui.components.modal :as modal]
            [myra.ui.modals.cancel-gig :as modal-cancel-gig]
            [myra.ui.modals.remove-gig :as modal-remove-gig]
            [myra.ui.modals.late-gig :as modal-late-gig]
            [myra.ui.modals.added-gig :as modal-added-gig]))

(def ui
  {:main             main/component
   :header           header/component
   :page-gigs        page-gigs/component
   :page-profile     page-profile/component
   :page-messages    page-messages/component
   :page-session     page-session/component
   :form-register    form-register/component
   :form-login       form-login/component
   :form-profile     form-profile/component
   :form-gig         form-gig/component
   :form-message     form-message/component
   :gig-details      gig-details/component
   :modal            modal/component
   :modal-cancel-gig modal-cancel-gig/component
   :modal-remove-gig modal-remove-gig/component
   :modal-late-gig   modal-late-gig/component
   :modal-added-gig  modal-added-gig/component})

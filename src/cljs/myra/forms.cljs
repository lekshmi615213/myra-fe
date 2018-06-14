(ns myra.forms
  (:require [myra.forms.login :as login]
            [myra.forms.register :as register]
            [myra.forms.profile :as profile]
            [myra.forms.gig :as gig]
            [myra.forms.message :as message]
            [myra.forms.change-password :as change-password]
            [myra.forms.forgot-password :as forgot-password]
            [myra.forms.new-password :as new-password]))

(def forms
  {:login           (login/constructor)
   :register        (register/constructor)
   :profile         (profile/constructor)
   :gig             (gig/constructor)
   :message         (message/constructor)
   :change-password (change-password/constructor)
   :forgot-password (forgot-password/constructor)
   :new-password    (new-password/constructor)})

(def forms-ids
  {:login (fn [{:keys [page]}]
            (when (= "login" page)
              :form))
   :register (fn [{:keys [page]}]
               (when (= "register" page)
                 :form))
   :profile (fn [{:keys [page subpage]}]
              (when (and (= "profile" page)
                         (= "edit" subpage))
                :form))
   :forgot-password (fn [{:keys [page]}]
                      (when (= "forgot-password" page)
                      :form))
   :change-password (fn [{:keys [page subpage]}]
                      (when (and (= "profile" page)
                                 (= "edit" subpage))
                        :form))
   :new-password (fn [{:keys [page]}]
                      (when (= "reset-password" page)
                        :form))
   :gig (fn [{:keys [page subpage detail]}]
          (when (and (= "gigs" page)
                     detail)
            detail))
   :message (fn [{:keys [page subpage detail]}]
              (when (and (= "messages" page)
                         (= "thread" subpage)
                         detail)
                detail))})

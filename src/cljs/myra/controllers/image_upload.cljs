(ns myra.controllers.image-upload
  (:require [keechma.toolbox.pipeline.controller :as pp-controller]
            [keechma.toolbox.pipeline.core :as pp :refer-macros [pipeline!]] 
            [keechma.toolbox.forms.core :as forms-core]
            [oops.core :refer [oget ocall]]
            [medley.core :refer [dissoc-in]]
            [promesa.core :as p]
            [keechma.toolbox.forms.core :refer [id-key]]
           ))


(defn read-image-from-file [file]
  (p/promise
   (fn [resolve reject]
     (let [fr (js/FileReader.)
           handler-fn (fn handler-fn []
                        (ocall fr "removeEventListener" "load" handler-fn false)
                        (resolve (oget fr "result")))]
       (ocall fr "addEventListener" "load" handler-fn false)
       (ocall fr "readAsDataURL" file)))))


(def controller
  (pp-controller/constructor
   (fn [{:keys [data]}]
     (when (and (= "profile" (:page data))
                (= "edit" (:subpage data)))
       true))
   {:upload (pipeline! [value app-db]
              (when value
                (pipeline! [value app-db]
                  (read-image-from-file value)
                  (pp/commit! (-> app-db
                                  (assoc-in [:kv :image-upload-preview] value))))))
    :stop (pipeline! [value app-db]
            (dissoc-in app-db [:kv :image-upload-preview]))}))

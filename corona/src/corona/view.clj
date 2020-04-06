(ns corona.view
  (:require [hiccup.page :refer [html5]]
            [hiccup.form :refer [form-to text-area submit-button]]))

(defn render-result [result]
  (html5 [:head]
         [:body
          [:pre [:code result]]]))

(defn render-form []
  (html5 [:head]
         [:body
          (form-to [:post "/"]
                   (text-area {:cols 7
                               :rows 3} "content")
                   [:div]
                   (submit-button "Pretrazi"))]))

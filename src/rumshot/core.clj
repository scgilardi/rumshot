(ns rumshot.core
  (:require
   [compojure.core :refer [ANY defroutes]]
   [compojure.route :refer [resources]]
   [hiccup.page :refer [html5]]
   [liberator.core :refer [resource]]))

(defn ring-init [])
(defn ring-destroy [])

(defn rumshot-home-page
  [_]
  (html5
   [:head
    [:title "rumshot"]]
   [:body
    [:h1 "rumshot"]
    [:h3 "label"]
    [:hr]
    [:div {:id "label"}]
    [:hr]
    [:script {:src "js/rumshot.js"}]
    [:script
     "rumshot.core.start_label(document.getElementById(\"label\"));"]]))

(defroutes handler
  (ANY "/"
       []
       (resource :available-media-types ["text/html"]
                 :handle-ok rumshot-home-page))
  (resources "/"))

(def ring-handler #'handler)

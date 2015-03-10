(ns rumshot.core
  (:require
   [compojure.core :refer [ANY defroutes]]
   [compojure.route :refer [resources]]
   [hiccup.page :refer [html5]]
   [liberator.core :refer [resource]]))

(defn ring-init [])
(defn ring-destroy [])

(defn example [title & [id]]
  (list
   [:h3 {:class "example-title"} title]
   [:div {:class "example" :id (or id title)}]
   [:hr]))

(defn rumshot-home-page
  [_]
  (html5
   [:head
    [:title "rumshot"]
    [:link {:rel "stylesheet" :href "style.css"}]]
   [:body
    [:h1 "rumshot"]
    (example "label")
    (example "stateful")
    (example "stateful2" "stateful")
    (example "bmi")
    (example "binary clock")
    [:script {:src "js/rumshot.js"}]]))

(defroutes handler
  (ANY "/"
       []
       (resource :available-media-types ["text/html"]
                 :handle-ok rumshot-home-page))
  (resources "/"))

(def ring-handler #'handler)

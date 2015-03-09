(ns rumshot.core
  (:require rum))

(enable-console-print!)

(rum/defc label [n text]
  [:.label (repeat n text)])

(defn ^:export start-label [mount-el]
  (rum/mount (label 5 "abc") mount-el))

(rum/defcs stateful < (rum/local 0) [state title]
  (let [local (:rum/local state)]
    [:div
     {:on-click (fn [_] (swap! local inc))}
     title ": " @local]))

(defn start-stateful [mount-el]
  (rum/mount (stateful "Clicks count") mount-el))


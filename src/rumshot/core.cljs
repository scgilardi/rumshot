(ns rumshot.core
  (:require rum))

(enable-console-print!)

(rum/defc label [n text]
  [:.label (repeat n text)])

(defn ^:export start-label [mount-el]
  (rum/mount (label 5 "abc") mount-el))

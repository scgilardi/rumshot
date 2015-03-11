(ns rumshot.core
  (:require rum))

;; timer

(defn now []
  (.getTime (js/Date.)))

(def timer (atom (now)))
(def speed (atom 167))

(defn tick []
  (reset! timer (now))
  (js/setTimeout tick @speed))

(tick)

(def bmi-data (atom {:height 180 :weight 80}))

(def color (atom "#FA8D97"))

(enable-console-print!)

(rum/defc label [n text]
  [:.label (repeat n text)])

(defn start-label [mount-el]
  (rum/mount (label 5 "abc") mount-el))

(rum/defcs stateful < (rum/local 0) [state title]
  (let [local (:rum/local state)]
    [:div
     {:on-click (fn [_] (swap! local inc))}
     title ": " @local]))

(defn start-stateful [mount-el]
  (rum/mount (stateful "Click count") mount-el))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Reagent stype BMI calculator

(defn calc-bmi [{:keys [height weight bmi] :as data}]
  (let [h (/ height 100)]
    (if (nil? bmi)
      (assoc data :bmi (/ weight (* h h)))
      (assoc data :weight (* bmi h h)))))

(defn slider [param value min max]
  (let [reset (case param :bmi :weight :bmi)]
    [:input {:type "range" :value value :min min :max max
             :style {:width "100%"}
             :on-change #(swap! bmi-data assoc
                                param (-> % .-target .-value)
                                reset nil)}]))

(rum/defc bmi-component < rum/reactive []
  (let [{:keys [weight height bmi] :as data} (calc-bmi (rum/react bmi-data))
        [color diagnose] (cond
                           (< bmi 18.5) ["orange" "underweight"]
                           (< bmi 25) ["inherit" "normal"]
                           (< bmi 30) ["orange" "overweight"]
                           :else ["red" "obese"])]
    (reset! bmi-data data)
    [:div.bmi
     [:div
      "Height: " (int height) "cm"
      (slider :height height 100 220)
      ]
     [:div
      "Weight: " (int weight) "kg"
      (slider :weight weight 30 150)]
     [:div
      "BMI: " (int bmi) " "
      [:span {:style {:color color}} diagnose]
      (slider :bmi bmi 10 50)]]))

;; Binary clock

;; Generic render-count label

(def bclock-renders (atom 0))

(rum/defc bit < rum/static [n bit]
  (swap! bclock-renders inc)
  (case bit
    9 [:td]
    8 [:th]
    7 [:td {:style {:text-align "center"}} n]
    [:td.bclock-bit
     {:style (when (bit-test n bit)
               {:backgroundColor @color})}]))

(rum/defc render-count < rum/reactive [ref]
  [:div.stats "Renders: " (rum/react ref)])

(defn clock-table [h m s t]
  (let [hh (quot h 10) hl (mod  h 10)
        mh (quot m 10) ml (mod  m 10)
        sh (quot s 10) sl (mod  s 10)
        th (quot t 100) tm (-> t (quot 10) (mod 10)) tl (mod t 10)
        data [hh hl  0 mh ml  0 sh sl  0 th tm tl]
        form [[9  3  8  9  3  8  9  3  8  3  3  3]
              [9  2  8  2  2  8  2  2  8  2  2  2]
              [1  1  8  1  1  8  1  1  8  1  1  1]
              [0  0  8  0  0  8  0  0  8  0  0  0]
              [7  7  8  7  7  8  7  7  8  7  7  7]]]
    [:table.bclock
     (for [row (range 0 (count form))
           :let [row-form (form row)]]
       [:tr
        (for [col (range 0 (count row-form))
              :let [cell-data (data col)
                    cell-form (row-form col)]]
          (rum/with-props bit cell-data cell-form :rum/key [row col]))])
     [:tr
      [:th {:colSpan 8}
       (rum/with-props render-count bclock-renders :rum/key "renders")]]]))

(rum/defc bclock < rum/reactive []
  (let [date (js/Date. (rum/react timer))]
    (clock-table (.getHours date)
                 (.getMinutes date)
                 (.getSeconds date)
                 (.getMilliseconds date))))

(defn start []
  (doseq [el (array-seq (.getElementsByClassName js/document "example"))]
    (case (.-id el)
      "label"
      (start-label el)
      "stateful"
      (start-stateful el)
      "bmi"
      (rum/mount (bmi-component) el)
      "binary clock"
      (rum/mount (bclock) el))))

(start)

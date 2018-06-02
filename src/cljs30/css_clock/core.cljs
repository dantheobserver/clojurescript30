(ns cljs30.css-clock.core
  (:require [reagent.core :as r]
            [cljs30.css-clock.styles :as s]
            [cljs30.utils :as utils]))

;; clock-components
(def rems #(str % "rem"))
(def clock-size 30)

(def clock-container (s/styled-component :div [s/clock-container]))
(def clock-face (s/styled-component :div [s/all-centered #(s/face (rems clock-size))]))
(def hour-hand (s/styled-component :div [#(s/hand ".4rem" "30%")]))
(def minute-hand (s/styled-component :div [#(s/hand ".2rem" "40%")]))
(def second-hand (s/styled-component :div [#(s/hand ".1rem" "45%")]))

(def clock-tick-container
  (s/styled-component :div [s/all-centered #(s/tick-container (rems clock-size))]))
(def small-tick (s/styled-component :div [s/all-centered #(s/clock-tick ".1rem" "blue")]))
(def large-tick (s/styled-component :div [s/all-centered #(s/clock-tick ".5rem" "black")]))

;; Digital Clock
(def digi-clock-container (s/styled-component :div [s/all-centered s/digi-clock-container]))

(defn- part-str [part]
  (str (when (< part 10) 0)
       part))

(defn clock-part [part]
  [:span (part-str part)])

(defn digi-clock-sep [sep]
  [:span sep])

(defn digital-clock [hr min sec]
  [digi-clock-container {}
   [clock-part hr]
   [digi-clock-sep ":"]
   [clock-part min]
   [digi-clock-sep ":"]
   [clock-part sec]])

(defn- get-time-parts [date]
  (let [time-parts-fn (juxt #(.getHours %) #(.getMinutes %) #(.getSeconds %))]
    (time-parts-fn date)))

(defn- set-time! [atom]
  (let [[hours minutes seconds] (get-time-parts (js/Date.))]
    (-> atom
        (swap! assoc
               :hours (mod hours 12)
               :minutes minutes
               :seconds seconds))))

(defn- rotation-style [amt]
  {:style {:transform
           (str "rotateZ(" (mod (+ 180 amt) 720) "deg)")}})

(defn- hr-rot [hr] ;;add minute modifier
  (rotation-style (* 30 hr)))

(defn- min-rot [min]  ;;add second modifier
  (rotation-style (* 6 min)))

(defn- sec-rot [sec]
  (rotation-style (* 6 sec)))

;; TODO: Add large and small time ticks/numbers
;; TODO: Add sounds for each hand 
(defn lesson []
  (let [time (r/atom {:hours 0 :minutes 0 :seconds 0}) 
        hour-c (r/cursor time [:hours])
        minute-c (r/cursor time [:minutes])
        second-c (r/cursor time [:seconds])
        _ (set-time! time)
        _ (js/setInterval #(set-time! time) 1000)]
    (fn []
      [:div
       [clock-container {}
        [clock-face {}
         [hour-hand (hr-rot @hour-c)]
         [minute-hand (min-rot @minute-c)]
         [second-hand (sec-rot @second-c)]]
        [digital-clock @hour-c @minute-c @second-c]
        [clock-tick-container {}
         (for [i (range 1 61)
               :let [style (str "rotateZ(" (+ 180 (* i 6)) "deg) "
                                " translate(0, 17.2rem)")]] ;; tick per 6 deg
           (if (= 0 (mod i 5))
             ^{:key (str "tick" i)}[large-tick {:style {:transform style}}]
             ^{:key (str "tick" i)}[small-tick {:style {:transform style}}]))]
        ]])))

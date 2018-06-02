(ns cljs30.css-clock.core
  (:require [reagent.core :as r]
            [cljs30.css-clock.styles :as styles]
            [cljs30.utils :as utils]))

;; Components
(def clock-container (styles/styled-component :div [styles/clock-container]))
(def clock-face (styles/styled-component :div [styles/all-centered styles/face]))
(def hour-hand (styles/styled-component :div [#(styles/hand ".4rem" "30%")])) ;; call function returned by defstyles with arguments to et style.
(def minute-hand (styles/styled-component :div [#(styles/hand ".2rem" "40%")]))
(def second-hand (styles/styled-component :div [#(styles/hand ".1rem" "45%")]))

;; Digital Clock
(def digi-clock-container (styles/styled-component :div [styles/all-centered styles/digi-clock-container]))

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
           (str "rotateZ(" (mod (+ 180 amt) 360 ) "deg)")}})

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
        [digital-clock @hour-c @minute-c @second-c]]])))

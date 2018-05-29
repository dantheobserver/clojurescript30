(ns cljs30.css-clock
  (:require [reagent.core :as r]
            [cljss.core :as ss]
            [cljss.reagent :refer-macros [defstyled]]
            [cljs30.utils :as utils]))

(defstyled ClockContainer :div
  {:background-image "linear-gradient(white 10%, lightblue)"
   :height "90rem"})

(defstyled ClockFace :div
  {:background-color "white"})

(defstyled HourHand :div
  {:background-color "black"
   :width ".2rem"
   :height "2rem"})

(defstyled MinuteHand :div
  {:background-color "black"
   :width ".2rem"
   :height "4rem"})

(defstyled SecondHand :div
  {:background-color "black"
   :width ".2rem"
   :height "2rem"})

;;state will be current time in hour minute second format
;;Will update via an event
(defn lesson []
  (ClockContainer
   (ClockFace)
   (HourHand)
   (MinuteHand)
   (SecondHand)))

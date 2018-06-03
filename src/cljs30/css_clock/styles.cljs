(ns cljs30.css-clock.styles
  (:require [cljss.core :as css :refer-macros [defstyles]]
            [cljs30.utils :as utils]))

(defn- with-styles [& styles]
  (str " " (clojure.string/join " " (for [s styles] (s)))))

;; TODO: arguments should just be & content and do a type check for first arg being a map of attrs, more versatile when not passing in any arguments
;; (defn styled-component [tag styles]
;;   {:style/indent 1}
;;   (fn [attrs & content]
;;     [tag (merge-with str attrs {:class (apply with-styles styles)})
;;      (for [c content] c)]))
(defn styled-component [tag styles]
  {:style/indent 1}
  (fn [attrs & content]
    (into [tag (merge-with str attrs {:class (apply with-styles styles)})]
          content)))

;; Analog Clock
(defstyles all-centered []
  {:position "absolute"
   :top "50%"
   :left "50%"
   :transform "translate(-50%, -50%)"})

(defstyles clock-container []
  {:position "relative"
   :background-image "linear-gradient(white 10%, lightblue)"
   :height "90rem"})

(defstyles face [size]
  {:border "3rem solid rgba(0, 0, 0, 0)"
   :border-radius "50%"
   :width size
   :height size
   :font-size "2rem"
   :box-shadow "0px 0px 20px 7px black"})

(defstyles hand [width length]
  {:background-color "black"
   :width width
   :height length
   :position "absolute"
   :top "50%" :left "50%"
   :transform-origin "top"
   :transform "rotateZ(180deg)"
   :transition "all 0.05s"
   :transition-timing-function "cubic-bezier(0.1, 2.7, 0.58, 1)"})

(defstyles digi-clock-container []
  {:margin "0 auto"
   :font-family "courier new"
   :font-size "3rem"})

(defstyles tick-container [size]
  {;;:clip-path "circle(3% at 50% 50%)"
   :width size
   :height size})

(defstyles clock-tick [width, color]
  {:width width
   :height "1.1rem"
   :background-color color
   :opacity ".7"
   :transform-origin "top"
   })

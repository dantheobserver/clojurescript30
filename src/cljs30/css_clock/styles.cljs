(ns cljs30.css-clock.styles
  (:require [cljss.core :as css :refer-macros [defstyles]]))

(defn- with-styles [& styles]
  (str " " (clojure.string/join " " (for [s styles] (s)))))

;; TODO: arguments should just be & content and do a type check for first arg being a map of attrs, more versatile when not passing in any arguments
(defn styled-component [tag styles]
  {:style/indent 1}
  (fn [attrs & content]
    [tag (merge-with str attrs {:class (apply with-styles styles)})
     (for [c content] c)]))

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

(defstyles face []
  {:border "3rem solid rgba(0, 0, 0, 0)"
   :border-radius "50%"
   :width "30rem"
   :height "30rem"
   :font-size "2rem"
   :box-shadow "0px 0px 20px 7px black"
   })

(defstyles hand [width length]
  {:background-color "black"
   :width width
   :height length
   :line-height (str "calc(" length " * 30)")
   :position "absolute"
   :top "50%" :left "50%"
   :transform-origin "top"
   :transform "rotateZ(180deg)"
   })

(defstyles digi-clock-container []
  {:margin "0 auto"
   :font-family "courier new"
   :font-size "3rem"})

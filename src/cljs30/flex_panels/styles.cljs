(ns cljs30.flex-panels.styles
  (:require [stylefy.core :as stylefy]
            [clojure.string :refer [join]]))

(def panel-container
  {:height "100%"
   :display "flex"
   :flex-flow "row nowrap"})

(defn css-fn* [name]
  (fn [& args]
    (str name "(" (join "," args) ")")))

(def url (css-fn* "url"))
(def rgba (css-fn* "rgba"))
(def box-shadow (css-fn* "box-shadow"))
(defn border [size color] (str size "rem solid " color))

(defn panel
  ([] (panel nil))
  ([background-url]
   (merge
    {:font-size "2rem"
     :flex "1 1 auto"
     :border-radius ".4rem"}
    (when background-url {:background-image (url background-url)
                          :background-position "center"
                          :background-origin "border-box"
                          :background-size "cover"
                          :border-top (border 0.2 (rgba 255, 255, 255, 0.5))
                          :border-right (border 0.2 (rgba 255, 255, 255, 0.5))
                          :border-left (border 0.2 (rgba 255, 255, 255, 0.5))
                          :border-bottom (border 0.2 (rgba 255, 255, 255, 0.5))}))))

(def panel-active {;;:flex-basis "20rem"
                   :flex "3 1 auto";
                   :z-index 1
                   :box-shadow "-32px 0 37px -26px, 32px 0 37px -26px" })

(def bounce-transition
  {:transition "all 500ms"
   :transition-timing-function "cubic-bezier(0.1, 2.7, .58, 1)"
   })

(ns starter.utils
  (:require [garden.core :refer [css]]
            [reagent.core :as r]))

(defn styled-component
  [component style]
  (if style
    (with-meta component {:style style})
    component))

(defn mount-css
  "sets the current component style to the one passed in"
  [style]
  (let [style-elem [:style {:type "text/css"} (css style)]
        head (.querySelector js/document "#componentStyle")]
    (set! (.-innerHTML head) (css style))))


(ns cljs30.utils
  (:require [garden.core :refer [css]]
            [reagent.core :as r]))

;; CSS
(defn styled-component
  [component style]
  (if style
    (with-meta component {:style style})
    component))

(def style-header-elem #(.querySelector js/document "#componentStyle"))

(defn- mount-css!
  "sets the current component style to the one passed in"
  [style]
  (let [style-elem [:style {:type "text/css"} (css style)]
        head (style-header-elem)]
    (set! (.-innerHTML head) (css style))))

(defn- unmount-css!
  "clears the css in the header container"
  []
  (let [head (style-header-elem)]
    (set! (.-innerHTML head) "")))

(defn- mount-style!
  [component]
  (unmount-css!)
  (let [style (-> component meta :style)]
    (when style (mount-css! style))))

(defn set-lesson!
  "Places `lesson-component` into the lesson container"
  [lesson-component]
  (r/render [lesson-component]
            (js/document.getElementById "lesson")
            #(mount-style! lesson-component)))

(defn current-path
  "Gets the has-value from window-location"
  []
  (-> js/window .-location .-hash (subs 1)))

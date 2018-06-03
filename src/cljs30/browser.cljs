(ns cljs30.browser
  (:require [reagent.core :as r]
            [cljs30.utils :as utils]
            [react :as react]
            [goog.object :as obj]
            [cljs30.drum-kit :as drum-kit]
            [cljs30.css-clock.core :as css-clock]
            [cljss.core :as css]))

(def lessons
  (sorted-map :01-drum-kit ["Drum Kit" drum-kit/lesson]
              :02-css-clock ["Css Clock" css-clock/lesson]))

(def window-key (-> (utils/location-hash)
                    keyword))

(defn lesson-links []
  [:ul
   (for [[lesson-key [title lesson]] lessons]
     ^{:key lesson-key}
     [:li [:a {:href (str "#/" (name lesson-key))
               :on-click #(utils/set-lesson! lesson)}
            lesson-key]])])

(defn app-component []
  [:div
   [:span "-Choose A lesson to go to-"]
   [:br]
   [lesson-links]])


;; start is called by init and after code reloading finishes
(defn ^:dev/after-load start []
  (css/remove-styles!)
  (obj/set js/window "React" react)
  (r/render [app-component] (js/document.querySelector "#app"))
  (let [[_ lesson] (get lessons window-key)] 
    (utils/set-lesson! lesson)))

(defn ^:export init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  ;; (devtools.core/install! [:formatters :hints])
  (js/console.log "init")
  (start))

;; this is called before any code is reloaded
(defn ^:dev/before-load stop []
  (js/console.log "stop"))

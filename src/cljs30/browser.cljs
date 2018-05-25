(ns cljs30.browser
  (:require [reagent.core :as r]
            [cljs30.utils :as utils]
            [cljs30.drum-kit :as drum-kit]))

(def lesson-map {:drum-kit drum-kit/lesson})
(def lessons [["drum-kit" :drum-kit]])

(defn window-hash
  "Gets the has-value from window-location"
  []
  (-> js/window .-location .-hash
      (.split "/")
      (get 1)))

(def window-key (comp keyword window-hash))

(defn mount-style!
  [component]
  (let [style (-> component meta :style)]
    (when style (utils/mount-css style))))

(defn set-lesson!
  "Places `lesson-component` into the lesson container"
  [lesson-component]
  (r/render [lesson-component]
            (js/document.getElementById "lesson")
            #(mount-style! lesson-component)))

(defn lesson-links []
  [:ul
   (for [[name lesson-key] lessons]
     ^{:key name}
     [:li [:a {:href (str "#/" name)
                :on-click #(set-lesson! (lesson-key lesson-map))}
            name]])])

(defn app-component []
  [:div
   [:span "-Choose A lesson to go to-"]
   [:br]
   [lesson-links]])

;; start is called by init and after code reloading finishes
(defn ^:dev/after-load start []
  (r/render [app-component]
            (js/document.getElementById "app"))
  (if-let [k (window-key)]
    (set-lesson! (k lesson-map))))

(defn ^:export init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (js/console.log "init")
  (start))

;; this is called before any code is reloaded
(defn ^:dev/before-load stop []
  (js/console.log "stop"))

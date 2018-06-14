(ns cljs30.browser
  (:require [cljs30.utils :as utils]
            [cljs30.routes :as routes]
            [reagent.core :as r]
            [cljss.core :as css]
            [react :as react]
            [goog.object :as obj]))

(def lessons (map (fn [[route {:keys [name component]}]]
                    [route name component])
                  (.-routes routes/router)))

(defn lesson-links []
  [:ul
   (for [[route name lesson] lessons]
     ^{:key name}
     [:li [:a {:href (str "#" route)
               :on-click #(utils/set-lesson! lesson)}
            name]])])

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
  (when-let [{:keys [component]} (-> (utils/current-path) routes/route-data)] 
    (utils/set-lesson! component)))

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

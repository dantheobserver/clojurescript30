(ns cljs30.routes
  (:require [reitit.core :as r]
            ;; [clojure.spec.alpha :as s]
            [cljs30.drum-kit :as drum-kit]
            [cljs30.css-clock.core :as css-clock]
            [cljs30.flex-panels.core :as flex-panels]))

(defn route-cmp [name cmp]
  {:name name :component cmp})

(def router
  (r/router
   [["/drum-kit" (route-cmp "Drum Kit" drum-kit/lesson)]
    ["/css-clock" (route-cmp "CSS Clock" css-clock/lesson)]
    ["/flex-panels" (route-cmp "Flex Panels" flex-panels/lesson)]
    ]))

;; Routing
(defn route-data [path]
  (:data (r/match-by-path router path)))


(ns cljs30.flex-panels.core
  (:require [reagent.core :as r]
            [cljs30.flex-panels.styles :as styles]
            [stylefy.core :refer [use-style sub-style init prepare-styles]]))

(def key-gen (memoize hash))

(def panels
  [{:url "https://tinyurl.com/y74u9brr" :content "A"}
   {:url "https://tinyurl.com/ya356auj" :content "B"}
   {:url "https://tinyurl.com/yccc3f8l" :content "C"}
   {:url "https://tinyurl.com/jk9q7nt" :content "D"}
   {:url "https://tinyurl.com/yad6azce" :content "E"}])


(defn panel [content url]
  (let [active? (r/atom false)]
    (fn [content url]
      [:div
       (use-style (merge (styles/panel url)
                         styles/bounce-transition
                         (when @active? styles/panel-active))
                  {:on-click #(swap! active? not)
                   :on-mouse-leave #(reset! active? false)
                   })
       content])))

(defn lesson []
  (init)
  (fn []
    [:div (use-style styles/panel-container)
     (for [{:keys [url content] :as panel-item} panels]
       ^{:key (key-gen panel-item)}[panel content url])]))

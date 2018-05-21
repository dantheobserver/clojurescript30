(ns starter.drum-kit
  (:require [starter.utils :refer [styled-component]]
            [reagent.core :as r]))

(def style
  [:#lesson
   {:width "100%" :height "900px";
    :background-image "url(http://edmchicago.com/wp-content/uploads/2016/04/concert.jpg)"
    :background-size "cover"
    :display "flex"
    :flex-flow "row nowrap"
    :align-items "center"
    :justify-content "center"}
   [:.letter-container
    {:display "flex"
     :flex-flow "row wrap"
     :justify-content "space-around"
     :align-items "center"
     :width "100%"
     }]
   [:.letter
    {:font-size "34px"
     :font-weight "400"
     :color "#f9d275"
     :width "100px"
     :height "100px"
     :margin "10px 15px;"
     :background-color "rgba(0, 0, 0, .2)"
     :text-align "center"
     :line-height "100px"
     :border "3px solid black"
     :box-shadow "inset black 0 0 10px"
     :border-radius "10px"
     :transition "0.4s all"
     }
    [:&.letter:hover :&.playing
     {
      :font-weight "800"
      :background-color "rgba(157, 197, 95, .4)"
      :transform "scale(1.1)"
      :border "3px solid #f9d275"}]
    ]
   ])

(def char->str (comp str char))
;; (def key-codes [65 83 68 70 71 72 74 75 76])
(def key-codes [{:key 65
                 :clip "/audio/dnb_loop.mp3"}
                {:key 83}
                {:key 68}
                {:key 70}
                {:key 71}
                {:key 72}
                {:key 74}
                {:key 75}
                {:key 76}])


;; Helpers
(defn- play [audio] (.play audio))
(defn- pause [audio]
  (.pause audio)
  (set! (.-currentTime audio) 0.0))

(defn- play-pause [audio]
  (if (.-paused audio)
    (play audio)
    (pause audio)))

;; Components
(defn letter-button
  "Renders a button displaying the key
  and playing an audio clip if clicked"
  [{:keys [key clip]} key-code]
  (r/with-let [audio (when clip (js/Audio. clip)) ;; Keep reference to object;
               play-handler #(play-pause audio)
               key-handler #(when (or (= (.-charCode %) key)
                                  (= (.-charCode %) (+ 32 key))) ;;TODO: also check for audio existence
                          (play-handler))
               _ (.addEventListener js/document "keypress" key-handler)]
  [:div {:class 'letter
         :on-click (when clip play-handler)} ;;TODO: set style when playing
   (char->str key)]
  (finally
    (.removeEventListener js/document "keypress" key-handler))))

(defn letters []
  [:div {:class "letter-container"}
   (for [{:keys [key] :as kc} key-codes]
     ^{:key key} [letter-button kc])])

(def lesson (styled-component letters style))

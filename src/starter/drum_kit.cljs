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
    [:&:hover :&.playing
     {:font-weight "800"
      :background-color "rgba(157, 197, 95, .4)"
      :transform "scale(1.1)"
      :border "3px solid #f9d275"}]
    ]
   ])

;;This probably would be better of using strings as keys.
(def mp3 #(str "/audio/" % ".mp3"))
(def key-codes [{:key "A" :clip (mp3 "dnb_loop") :name "backdrop"}
                {:key "B"}
                {:key "C"}
                {:key "D"}
                {:key "E"}
                {:key "F"}
                {:key "G"}
                {:key "H"}
                {:key "I"}])

;; State
(def state (r/atom {:playing #{}})) 
(def add-playing! #(swap! state update :playing conj %))
(def rem-playing! #(swap! state update :playing disj %))

;; Pointers
(def playing #(:playing @state))
(defn am-i-playing? [clip]
  (-> @(r/track playing)
      (get clip)
      nil?
      not))

;; Audio Helpers
(defn audio-cache
  "Builds a cache of audio files from `CODES`
  by their string key."
  [codes]
  (into {} (map
            (fn [{:keys [key clip]}]
              [key (when clip (js/Audio. clip))])
            codes)))

(defn- play-pause
  [clip]
  (let [playing? (not (.-paused clip))]
    (if playing?
      (do
        (.pause clip)
        (set! (.-currentTime clip) 0.0)
        (rem-playing! clip))
      (do
        (.play clip)
        (add-playing! clip)))))

(defn play-pause-from-cache
  [key cache]
  (play-pause (cache key)))

;; Components
(defn letter-button
  "Renders a button displaying the key
  and playing an audio clip if clicked"
  [{:keys [key clip]} audio]
  (let [play-handler #(play-pause audio)
        _ (when audio (set! (.-onended audio) #(rem-playing! audio)))]
    (fn [_ _]
      [:div {:class ["letter" (when @(r/track am-i-playing? audio) "playing")]
             :on-click (when clip play-handler)} ;;TODO: set style when playing
       key])))

(defn letters []
  (r/with-let [audio-clips (audio-cache key-codes)
               key-handler #(play-pause-from-cache (.toUpperCase (.-key %)) audio-clips)
               _ (.addEventListener js/document "keypress" key-handler)]
    [:div {:class "letter-container"}
     (for [{:keys [key] :as key-code} key-codes]
       ^{:key key} [letter-button key-code (audio-clips key)])]
    (finally
      (.removeEventListener js/document "keypress" key-handler))))

(def lesson (styled-component letters style))

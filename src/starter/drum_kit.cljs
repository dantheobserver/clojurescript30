(ns starter.drum-kit
  (:require [starter.utils :refer [styled-component]]
            [reagent.core :as r]))

(defn random-color [] (-> (js/Math.random) (* 255) int))
(defn random-rgb [] (vec (repeatedly 3 random-color)))

(defn random-rgb-style
  [a]
  (let [rgba (into-array (conj (random-rgb) a))]
    (str "rgba(" (.join rgba) ")")))

(js/console.log "test: " (random-rgb-style 1))
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
    {:display "flex"
     :flex-flow "column nowrap"
     :justify-content "center"
     :width "100px"
     :height "100px"
     :margin "10px 15px;"
     :background-color "rgba(0, 0, 0, .2)"
     :text-align "center"
     :border "3px solid black"
     :box-shadow "inset black 0 0 10px"
     :border-radius "10px"
     :transition "0.4s all"
     }
    [:.key
     {:font-size "4rem"
      :font-weight "400"
      :color "#f9d275"}]
    [:.name
     {:font-size "1.5rem"
      :color "orange"
      }]
    [:&:Hover :&.playing
     {:font-weight "800"
      :background-color "rgba(157, 197, 95, .4)"
      :transform "scale(1.1)"
      :border "3px solid #f9d275"
      :box-shadow (str "1rem 1rem 2rem " (random-rgb-style 1))
      }]
    ]
   ])

;;This probably would be better of using strings as keys.
(def audio-dir #(str "/audio/" %1 %2))
(def mp3 #(audio-dir % ".mp3"))
(def wav #(audio-dir % ".wav"))
(def key-codes [{:key "a" :clip (wav "clap-analog") :name "analog clap" }
                {:key "s" :clip (wav "cowbell-808") :name "cowbell" }
                {:key "d" :clip (wav "hihat-acoustic02") :name "hi acoustic" }
                {:key "f" :clip (wav "hihat-digital") :name "hi digital" }
                {:key "g" :clip (wav "hihat-ring") :name "hi ring" }
                {:key "h" :clip (wav "kick-big") :name "kick big" }
                {:key "j" :clip (wav "perc-chirpy") :name "perc chirpy" }
                {:key "k" :clip (wav "perc-nasty") :name "perc nasty" }
                {:key "l" :clip (wav "snare-block") :name "snare vinyl" }])

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

(defn- play-clip
  [clip]
  (doto clip
    (.pause)
    (-> .-currentTime (set! 0.0))
    (.play)
    (add-playing!)))

(defn play-clip-from-cache
  [key cache]
  (if-let [clip (cache key)]
    (play-clip clip)))

;; Components
(defn letter-button
  "Renders a button displaying the key
  and playing an audio clip if clicked"
  [{:keys [key clip name]} audio]
  (let [play-handler #(play-clip audio)
        _ (when audio (set! (.-onended audio) #(rem-playing! audio)))]
    (fn []
      [:div {:class ["letter"
                     (when @(r/track am-i-playing? audio) "playing")]
             :on-click (when clip play-handler)} ;;TODO: set style when playing
       [:span {:class "key"}(.toUpperCase key)]
       [:span {:class "name"} name]])))

(defn letters []
  (r/with-let [audio-clips (audio-cache key-codes)
               key-handler #(play-clip-from-cache (.-key %) audio-clips)
               _ (.addEventListener js/document "keypress" key-handler)]
    [:div {:class "letter-container"}
     (for [{:keys [key] :as key-code} key-codes]
       ^{:key key} [letter-button key-code (audio-clips key)])]
    (finally
      (.removeEventListener js/document "keypress" key-handler))))

(def lesson (styled-component letters style))

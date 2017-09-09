(ns jam.db
  (:require [re-frame.core :as re-frame]))

(defn gen-notes []
  (let [length 40
        note (fn [] [(rand 100) (+ 50 (rand-int 20))])]
    (repeatedly length note)))

(def default-tracks
  {:guit (gen-notes)
   :test (gen-notes)})


(def default-db
  {:name "re-frame"
   :active-panel :jam-panel
   :key-held-frames 0
   :audio-context nil
   :pitch-shift nil
   :sounds {}
   :selected-sound nil

   :tick-handler-ids #{}
   :state :playing
   :play-time 0
   :tracks default-tracks
   :played-notes {:guit 0}
   })

(def drumkit
  {:name "Drumkit"
   :type :drum
   :sounds [:test]})

(def guit
  {:name "Guitar"
   :type :sampler
   :sounds [:guit]})

(def sounds [drumkit guit])

(defn sounds-to-load [sounds]
  (->> sounds
       (map :sounds)
       (apply concat)
       dedupe))

(defn all-sounds [] (sounds-to-load sounds))

(defn sound-requests []
  (map #(vec [:try-load-sound %]) (all-sounds)))

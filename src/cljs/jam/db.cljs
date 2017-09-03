(ns jam.db
  (:require [re-frame.core :as re-frame]))

(def default-db
  {:name "re-frame"
   :key-held-frames 0
   :audio-context nil
   :pitch-shift nil
   :sounds {}
   :selected-sound nil})

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

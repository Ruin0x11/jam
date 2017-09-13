(ns jam.db
  (:require [re-frame.core :as re-frame]
            [jam.audio :as audio]
            [jam.song :as song]))


(defn synth-sample [instrument note]
  (let [instrument (instrument audio/instruments)
        sample-names (:sounds instrument)
        get-midi (comp (partial apply (fnil audio/note-name->midi :c 4)) audio/sounds)
        pitches (map get-midi sample-names)
        names-to-pitches (zipmap sample-names pitches)
        closest-sample (apply min-key #(Math/abs (- note (val %))) names-to-pitches)]
    (key closest-sample)))

(defn sampler-sample [instrument note]
  (let [instrument (instrument audio/instruments)
        sample-names (:sounds instrument)]
    (rand-nth sample-names)))

(defn note->sample [instrument note]
  (let [data (instrument audio/instruments)
        type (:type data)]
    (if (= type :synth)
      (synth-sample instrument note)
      (sampler-sample instrument note))))

(defn sounds-to-load [sounds]
  (->> sounds
       vals
       (map :sounds)
       (apply concat)
       dedupe))

(defn all-sounds [] (sounds-to-load audio/instruments))

(defn sound-requests []
  (map #(vec [:try-load-sound %]) (all-sounds)))


(defn gen-notes []
  (let [length 50
        note (fn [] [(rand 200) (- 2 (rand-int 4))])]
    (repeatedly length note)))

(defn default-tracks []
  (let [keys (keys audio/instruments)]
    (zipmap keys (repeatedly (count keys) gen-notes))))


(defn test-tracks []
  {:guit []})

(def default-db
  {:name "re-frame"
   :active-panel :jam-panel
   :key-held-frames 0
   :audio-context nil
   :pitch-shift nil
   :sounds {}
   :instruments audio/instruments
   :selected-instrument :guit

   :tick-handler-ids #{}
   :state :paused
   :play-time 0
   :tracks (test-tracks)
   :played-notes {}

   :song {:guit song/test-song}
   })

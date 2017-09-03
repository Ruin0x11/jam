(ns jam.audio
  (:require [re-frame.core :as re-frame]
            [hum.core :as hum]
            [jam.logic :as logic]
            [leipzig.scale :refer [C major]]
            ;; [jam.pitch-shifter :as ps]
            ))

(defn create-context []
  (let [ctx (hum/create-context)
        ;; pitch-shift (ps/pitch-shift ctx)

        ]
    ;; (.connect pitch-shift (.-destination ctx))
    ;; (set! (-> pitch-shift .-transpose) 12)
    ;; (set! (-> pitch-shift .-wet .-value) 1)
    ;; (set! (-> pitch-shift .-dry .-value) 0.5)
    [ctx nil]
    )
  )

;; var pitchShift = PitchShift(audioContext)
;; pitchShift.connect(audioContext.destination)

(defn init [db]
  (let [[ctx pitch-shift] (create-context)]
    (-> db
        (assoc :pitch-shift pitch-shift)
        (assoc :audio-context ctx))
    ))

;; pitchShift.transpose = 12
;; pitchShift.wet.value = 1
;; pitchShift.dry.value = 0.5


(defn load-sound! [name buffer]
  (re-frame/dispatch [:update-sound buffer name]))

(defn make-buffer! [context raw-response name]
  (.decodeAudioData context raw-response (partial load-sound! name)))

(defn set-playback-rate [source rate]
  (set! (-> source .-playbackRate .-value) rate))

(defn start-source [source]
  (.start source 0))

(def note->offset
  {:c 0
   :c# 1
   :d 2
   :d# 3
   :e 4
   :f 5
   :f# 6
   :g 7
   :g# 8
   :a 9
   :a# 10
   :b 11})

(defn note-name->midi [note octave]
  (let [offset (note->offset note)]
    (+ offset (* 12 (+ 1 octave)))))

(defn midi->note-name [midi]
  (let [offset (mod midi 12)
        octave (logic/midi->octave midi)
        note ((clojure.set/map-invert note->offset) offset)]
    {note octave}))

(defn midi->freq [midi]
  (* 440 (Math.pow 2 (/ (- (Math.floor midi) 69) 12))))

(defn midi->playback-rate [midi sample-freq]
  (/ (midi->freq midi) sample-freq))

(defn play-note [ctx pitch-shift buf midi]
  (let [source (hum/create-buffer-source ctx buf)
        gain (hum/create-gain ctx 0.5)

        true-midi (if (< (rand 1) 0.60)
          (logic/similar-note midi C major)
          midi)

        rate (midi->playback-rate true-midi (-> (note-name->midi :g 4) (midi->freq)))]

    (set-playback-rate source rate)

    (hum/connect source gain (.-destination ctx))
    (start-source source)
    (.stop source (+ 0.5 (.-currentTime ctx)))
    ))

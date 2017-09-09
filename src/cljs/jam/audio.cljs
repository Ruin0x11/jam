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

(def note-names [:C :Db :D :Eb :E :F :Gb :G :Ab :A :Bb :B])

(defn note->offset [note]
  (.indexOf note-names note))

(defn note-name->midi [note octave]
  (let [offset (note->offset note)]
    (+ offset (* 12 (+ 1 octave)))))

(defn midi->note-name [midi]
  (let [offset (mod midi 12)
        octave (logic/midi->octave midi)
        note (nth note-names offset)]
    {note octave}))

(defn midi->hz [note-num]
  (let [expt-numerator (- note-num 69)
        expt-denominator 12
        expt (/ expt-numerator expt-denominator)
        multiplier (.pow js/Math 2 expt)
        a 440]
  (* multiplier a)))


(defn midi->playback-rate [midi sample-freq]
  (/ (midi->hz midi) sample-freq))

(defn play-note [ctx pitch-shift buf midi]
  (let [source (hum/create-buffer-source ctx buf)
        gain (hum/create-gain ctx 0.5)

        true-midi (if (< (rand 1) 0.40)
          (logic/similar-note midi C major)
          midi)

        rate (midi->playback-rate true-midi (-> (note-name->midi :g 4) (midi->hz)))]

    (set-playback-rate source rate)

    (hum/connect source gain (.-destination ctx))
    (start-source source)
    (.stop source (+ 0.5 (.-currentTime ctx)))
    ))

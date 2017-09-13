(ns jam.audio
  (:require [re-frame.core :as re-frame]
            [hum.core :as hum]
            [jam.logic :as logic]
            [leipzig.scale :refer [C major]]
            ;; [jam.pitch-shifter :as ps]
            ))

(def drumkit
  {:name "Drumkit"
   :type :sampler
   :sounds [:bassdrum :hihat :snare1 :snare2 :crash1 :crash2]})

(def guit
  {:name "Guitar"
   :type :synth
   :sounds [:guit1 :guit2 :guit3 :guit4]})

(def acoustic
  {:name "Acoustic Guitar"
   :type :synth
   :sounds [:acoustic]})

(def sounds
  {:guit1 [:a 2]
   :guit2 [:d 3]
   :guit3 [:b 3]
   :guit4 [:g 4]
   :acoustic [:b 4]})

(def instruments {:drumkit drumkit
                  :guit guit
                  :acoustic acoustic})

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

(def note-names [:c :c# :d :d# :e :f :f# :g :g# :a :a# :b])

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

(defn play-note [ctx pitch-shift buf midi sample]
  (let [source (hum/create-buffer-source ctx buf)
        gain (hum/create-gain ctx 0.5)
        base-note-name (sounds sample)
        is-synth (not (nil? base-note-name))
        playback-rate (if is-synth
                        (let [sound-rate (->> base-note-name
                                              (apply note-name->midi)
                                              (midi->hz))]
                          (midi->playback-rate midi sound-rate))
                        1)]

    (set-playback-rate source playback-rate)

    (hum/connect source gain (.-destination ctx))
    (start-source source)

    (when is-synth
      (.stop source (+ 0.5 (.-currentTime ctx))))))

(ns jam.audio
  (:require [re-frame.core :as re-frame]
            [hum.core :as hum]
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
  (.start source 0)
  )

(defn pitch->freq [pitch]
  (* 440 (Math.pow 2 (/ (- (Math.floor pitch) 69) 12))))

(defn pitch->playback-rate [pitch]
  (Math.pow 2 (/ pitch 12)))

(defn play-note [ctx pitch-shift buf note]
  (let [source (hum/create-buffer-source ctx buf)
        gain (hum/create-gain ctx 0.5)]

    (set-playback-rate source note)

    (hum/connect source gain (.-destination ctx))
    (start-source source)))

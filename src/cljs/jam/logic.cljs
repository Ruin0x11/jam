(ns jam.logic
  (:require [re-frame.core :as re-frame]
            [jam.utils :as u]
            [leipzig.melody :refer [bpm all phrase then times where with tempo]]
            [leipzig.scale :as scale]
            [leipzig.canon :as canon]
            [leipzig.chord :as chord]
            ))

(def special-key->code
  "map from a character code (read from events with event.which)
  to a string representation of it.
  Only need to add 'special' things here."
  {"/" 16
   "," 188
   "." 190
   ";" 186
   "[" 219})

(def keyboard-keys
  "The keys that can be used by the computer keyboard.
  The first starts at middle C and each char increases the tone by a half step."
  "ZSXDCVGBHNJM,L.;/Q2W3ER5T6Y7UI9O0P[")

(defn get-char-code [chars i]
  (or (special-key->code (nth chars i))
      (.charCodeAt chars i)))

(defn keys->codes [chars]
  (map (partial get-char-code chars) (range (count chars))))

(defn codes->pitches [codes]
  (zipmap codes (map #(- % 12) (range (count codes)))))

(defn pitches []
(->> keyboard-keys
     keys->codes
     codes->pitches))

(defn keycode->note [keycode]
  (get (pitches) keycode))

(defn note->midi [note]
  (leipzig.scale/C note))

(defn note->midi-locked [note key mode]
  ((comp key mode) note))


(defn midi->octave [midi]
  (- (quot midi 12) 1))

(defn octave-for-note [midi key mode]
  (let [octave (midi->octave midi)
        start (* (- octave 4) 7)
        notes (map (comp key mode) (range start (+ start 7)))]
    notes))

(defn position-in-scale [midi key mode]
  (let [octave (midi->octave midi)
        notes (octave-for-note midi key mode)
        closest (u/closest-num-in notes midi)
        local-pos (.indexOf notes closest)
        offset-pos (- (+ local-pos (* (+ 1 octave) 7)) 1)]
    offset-pos))

(defn full-scale [key mode]
  (map (comp key mode) (range -35 35)))

(defn notes-surrounding [midi key mode]
  (let [pos (position-in-scale midi key mode)
        scale (full-scale key mode)
        num 4]
    (u/surrounding scale pos num)))

(defn similar-note [note key mode]
  (rand-nth (notes-surrounding note key mode)))


(defn key-code [e]
  (if (number? (.-which e))
    (.-which e)
    (.-keyCode e)))

(defn keydown [e]
  ()
  (when (u/in? (keys (pitches)) (key-code e))
    (.preventDefault e)
    (re-frame/dispatch [:keypressed e])))

(defn init-key-handler! []
  (set! (.-onkeydown js/document) keydown))

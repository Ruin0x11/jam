(ns jam.logic
  (:require [re-frame.core :as re-frame]
            [jam.utils :as u]
            [leipzig.melody :refer [bpm all phrase then times where with tempo]]
            [leipzig.scale :as scale]
            [leipzig.canon :as canon]
            [leipzig.chord :as chord]
            ))

(def keyboard-keys
  "The keys that can be used by the computer keyboard.
  The first starts at middle C and each char increases the tone by a half step."
  "ZSXDCVGBHNJM,L.;/Q2W3ER5T6Y7UI9O0P[")

(defn keys->codes [chars]
  (map #(.charCodeAt chars %) (range (count chars))))

(defn codes->pitches [codes]
  (zipmap codes (map #(- % 12) (range (count codes)))))

(defn pitches []
(->> keyboard-keys
       keys->codes
       codes->pitches))

(defn keycode->note [keycode]
  (get (pitches) keycode))

(defn note->pitch [note key mode]
  ((comp key mode) note))

(defn keydown [e]
  (when (u/in? (keys (pitches)) (.-keyCode e))
    (.preventDefault e)
    (re-frame/dispatch [:keypressed e])))

(defn init-key-handler! []
  (set! (.-onkeydown js/document) keydown))

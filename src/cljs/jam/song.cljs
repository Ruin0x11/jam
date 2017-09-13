(ns jam.song
  (:require [re-frame.core :as re-frame]
            [leipzig.scale :as scale]
            [leipzig.melody :as melody]))



(def row-row-row-your-boat
  (melody/phrase [3/3   3/3   (/ 2 3) (/ 1 3)  3/3 (/ 2 3) (/ 1 3) (/ 2 3) (/ 1 3) 3/3]
                 [  0     0     0     1     2  2 1 2 3 4]))

(def test-song
  (->> row-row-row-your-boat
       (melody/tempo (melody/bpm 10))
       ;; (melody/where :pitch (comp scale/C scale/major))
       ))


(defn note-missed? [time note]
  (let [note-time (:time note)]
    (> time (+ note-time 2))))

(defn note-playable? [time note]
  (if (nil? note) false
      (let [note-time (:time note)]
        (and (> time (- note-time 0.5))
             (not (note-missed? note time))))))

(defn update-track [time song-track]
  (let [next-note (first song-track)]
    (if (and (not (nil? next-note))
             (note-missed? time next-note))
      (rest song-track)
      song-track)))

(defn update-song [db]
  (let [time (:play-time db)
        song-keys (keys (:song db))
        song-notes (vals (:song db))
        new-vals (map (partial update-track time) song-notes)]
    (assoc db :song (zipmap song-keys new-vals))))


(defn play-note [db track]
  (let [song-track (get-in db [:song track])
        next-note (first song-track)
        time (:play-time db)
        note (or (and (note-playable? time next-note) (:pitch next-note))
                 0)
        track-updated-db (if (note-playable? time next-note)
                           (update-in db [:song track] rest)
                           db)
        new-db (update-in track-updated-db [:tracks track] conj [time note])]
    [new-db note]))

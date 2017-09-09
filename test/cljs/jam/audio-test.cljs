(ns jam.audio-test
  (:require [jam.audio :as audio]))

;; (fact
;;  (merge (map (fn [[a b]] (note-name->midi a b))
;;              []
;;              )))

;; (fact
;;  (map midi->note-name [70 71 60 0 12 69]) => [{:a 4} {:b 4} {:c 4} {:c -1} {:c 0} {:a 4}])

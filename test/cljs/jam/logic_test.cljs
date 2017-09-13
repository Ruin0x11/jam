(ns jam.logic-test
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]
            [jam.logic :as logic]))

(deftest logic-test-cljs
  (testing "position of notes in a scale"
    (is (= (position-in-scale 73  leipzig.scale/C leipzig.scale/major) 36)))

  (testing "octaves of midi notes"
    (is (= (logic/midi->octave 60) 4))
    (is (= (logic/midi->octave 4) -1))
    (is (= (logic/midi->octave 31) 1))
    (is (= (logic/midi->octave 127) 9))))

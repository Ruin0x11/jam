(ns jam.song-test
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]
            [jam.song :as song]))

(deftest song-test-cljs
  (testing "song updating"
    (is (= (song/update-song {:song [row-row-row-your-boat]
                              :play-time 100})
           {:song
            (({:pitch 0, :time 1, :duration 1}
              {:pitch 0, :time 2, :duration 0.6666666666666666}
              {:pitch 1, :time 2.6666666666666665, :duration 0.3333333333333333}
              {:pitch 2, :time 3, :duration 1}
              {:pitch 2, :time 4, :duration 0.6666666666666666}
              {:pitch 1, :time 4.666666666666667, :duration 0.3333333333333333}
              {:pitch 2, :time 5, :duration 0.6666666666666666}
              {:pitch 3, :time 5.666666666666667, :duration 0.3333333333333333}
              {:pitch 4, :time 6, :duration 1})),
            :play-time 100}
           ))))

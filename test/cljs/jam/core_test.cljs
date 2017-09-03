(ns jam.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [purnam.test :refer fact]
            [jam.core :as core]))

(deftest fake-test
  (is (= 1 2)))

; (fact
;  (jam.db/sounds-to load
;                    [{:name "Drumkit", :type :drum, :sounds [:drum]}
;                     {:name "Guitar", :type :sampler, :sounds [:guit]}])
;  => #{:guit :drum})

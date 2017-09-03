(ns jam.core-test
  (:require [purnam.test :refer fact]
            [jam.db :as db]))

; (deftest sounds-to-load test
;   (fact
;    (jam.db/sounds-to-load
;     [{:name "Drumkit", :type :drum, :sounds [:drum]}
;      {:name "Guitar", :type :sampler, :sounds [:guit]}])
;    => #{:guit :drum}))

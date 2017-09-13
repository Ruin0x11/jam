(ns jam.db-test
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]
            [jam.db :as db]))

(deftest db-test-cljs
  (testing "that sounds are detected in instruments"
    (is (= (db/sounds-to-load
            {:drum {:name "Drumkit", :type :drum, :sounds [:drum]}
             :guit {:name "Guitar", :type :sampler, :sounds [:guit]}})
           '(:drum :guit)))))

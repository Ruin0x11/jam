(ns jam.core-test
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]
            [jam.db :as db]))


(deftest core-test-cljs
  (testing "something"
    (is (= 1 1))))

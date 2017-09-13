(ns jam.runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [jam.core-test]
            [jam.db-test]
            [jam.logic-test]
            [jam.song-test]))

;; (set! (.-error js/console) (fn [x] (.log js/console x)))

 (doo-tests 'jam.core-test
            'jam.db-test
            'jam.logic-test
            'jam.song-test)


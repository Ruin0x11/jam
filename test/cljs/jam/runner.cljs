(ns jam.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [jam.core-test]
              [jam.db-test]))

(doo-tests 'jam.core-test)
(doo-tests 'jam.db-test)


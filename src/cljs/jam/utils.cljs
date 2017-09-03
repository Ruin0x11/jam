(ns jam.utils)

(defn in?
  "true if coll contains elm"
  [coll elm]
  (some #(= elm %) coll))

(defn closest-num-in
  "Return the number in coll with the smallest difference from elm"
  [coll elm]
  (apply min-key #(Math.abs(- elm %)) coll))

(defn surrounding
  "Returns the elements in coll up to n indices before and after index."
  [coll index num]
  (->> coll
         (drop (- index num))
         (take (+ 1 (* 2 num)))))

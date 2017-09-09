(ns jam.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 :key-held-frames
 (fn [db]
   (:key-held-frames db)))

(re-frame/reg-sub
 :loaded-sounds
 (fn [db]
   (map name (keys (:sounds db)))))

(re-frame/reg-sub
 :seeker-pos
 (fn [db]
   (:play-time db)))

(re-frame/reg-sub
 :selected-sound
 (fn [db]
   (or (:selected-sound db)
       "")))

(re-frame/reg-sub
 :tracks
 (fn [db _]
   (:tracks db)))


(re-frame/reg-sub
 :active-panel
 (fn [db _]
   (:active-panel db)))

(ns jam.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [re-frisk.core :refer [enable-re-frisk!]]
            [jam.events]
            [jam.subs]
            [jam.routes :as routes]
            [jam.views :as views]
            [jam.config :as config]))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (enable-re-frisk!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn on-js-reload []
  (re-frame/dispatch-sync [:initialize-db])
  (re-frame/dispatch-sync [:add-tick-handler-ids [:tick-child1]])
  (mount-root))

(defn ^:export init []
  (routes/app-routes)
  (dev-setup)
  (on-js-reload)
  (re-frame/dispatch [:next-tick]))

(ns jam.events
  (:require [re-frame.core :as re-frame]
            [jam.db :as db]
            [jam.logic :as logic]
            [jam.audio :as audio]
            [ajax.core :as ajax]
            [jam.ajax]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   (set! (.-onkeydown js/document) logic/init-key-handler!) ;;impure
   (doseq [req (db/sound-requests)]
     (re-frame/dispatch req))                       ;; impure
   (-> db/default-db
       audio/init)))

(re-frame/reg-event-db
 :hold-key
 (fn [db _]
   (update db :key-held-frames inc)))

(re-frame/reg-event-db
 :release-key
 (fn [db _]
   (assoc db :key-held-frames 0)))

(re-frame/reg-event-db
 :set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(defn parse-sound-name [uri]
  (or (keyword (second (re-find #"sounds/(.+).wav" uri)))
      :test))

(re-frame/reg-event-db
 :load-sound-success
 (fn [db [_ result]]
   (let [ctx (:audio-context db)
         response (:response result)
         sound-name ((comp parse-sound-name :uri) result)]
     (audio/make-buffer! ctx response sound-name) ;;impure
     (-> db
         (assoc :show-twirly false)))))

(re-frame/reg-event-db
 :update-sound
 (fn [db [_ result sound]]
   (println (str "Loaded sound " sound" "))
   (-> db
       (assoc :selected-sound (name sound))
       (assoc-in [:sounds sound] result))))

(re-frame/reg-event-db
 :select-sound
 (fn [db [_ sound]]
   (assoc db :selected-sound sound)))

(re-frame/reg-event-db
 :load-sound-failure
 (fn [db [_ result]]
   (-> db
       (assoc :show-twirly false))))

(defn arraybuffer-response-format []
  {:content-type "audio/wav" :description "WAV file" :read ajax.protocols/-body :type :arraybuffer})

(defn load-sound-request [sound-key]
  {:http-xhrio-uri {:method :get
                    :uri (str "sounds/" (name sound-key) ".wav")
                    :timeout 8000
                    :response-format (arraybuffer-response-format)
                    :on-success [:load-sound-success]
                    :on-failure [:load-sound-failure]}})

(re-frame/reg-event-fx
 :try-load-sound
 (fn [{:keys [db]} [_ sound]]
   (merge
    {:db (assoc db :show-twirly true)}
    (load-sound-request sound))))

(re-frame/reg-event-db
 :keypressed
 ;;after?
 (fn [db [_ e]]
   (let [note (-> e .-keyCode
                  logic/keycode->note
                  audio/pitch->playback-rate)]
     (re-frame/dispatch [:play-sound (:selected-sound db) note])
     db)))

(re-frame/reg-event-db
 :play-sound
 (fn [db [_ sound-key note]]
   (let [{:keys [audio-context pitch-shift sounds]} db
         sound-buffer ((keyword sound-key) sounds)]
     (audio/play-note audio-context pitch-shift sound-buffer note)
     db)))

;; (re-frame/reg-event-db
;;  :keypressed
;;  ;;after?
;;  (fn [db [_ e]]
;;    (let [{:keys [selected-sound audio-context pitch-shift sounds]} db
;;          sound (selected-sound sounds)
;;          note (logic/keycode->note (.-keyCode e))]
     
;;      (audio/play-note audio-context pitch-shift sound note)
;;      db))
 ;; )

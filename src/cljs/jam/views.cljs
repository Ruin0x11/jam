(ns jam.views
  (:require [re-frame.core :as re-frame]))


;; home

(defn home-panel []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [:div (str "Hello from " @name ". This is the jamming Home Page.")
       [:div [:a {:href "#/jam"} "Start"]]
       [:div [:a {:href "#/about"} "go to About Page"]]])))


;; about

(defn about-panel []
  (fn []
    [:div "This is the About Page."
     [:div [:a {:href "#/"} "go to Home Page"]]]))


;; jam

(defn sound-option [s]
  ^{:key s} [:option {:value s}
             s])

(defn sound-options [sounds]
  (map sound-option sounds))

(defn sound-selector []
  (let [value (re-frame/subscribe [:selected-sound])
        sounds (re-frame/subscribe [:loaded-sounds])]
    [:select {:value @value
              :on-change #(re-frame/dispatch [:select-sound (keyword (-> % .-target .-value))])
             }
     (sound-options @sounds)]))

(defn jam-panel []
  (fn []
    [:div
     [:section#jam-main
      [sound-selector]]
     [:footer#jam-footer
      [:div.footer-left
       [:a {:href "#/"} "return"]]]]))


;; main

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    :about-panel [about-panel]
    :jam-panel [jam-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [show-panel @active-panel])))

(ns jam.views
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            ;; [reanimated.core :as anim]
            ))


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

;; (defn spring-test []
;;   (let [size (reagent/atom 24)
;;         size-spring (anim/spring size)]
;;     (fn []
;;       [:p
;;        {:on-click (fn click [e] (swap! size + 10))}
;;        [:svg {:width "500" :height "500"}
;;         [:circle {:cx "250" :cy "250" :r @size-spring}]
;;         ]]))
;;   )



(defn jam-panel []
  (fn []
    [:div
     [:section#jam-main
      [sound-selector]
      ;; [spring-test]
      ]
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
      [show-panel @active-panel]))
  )

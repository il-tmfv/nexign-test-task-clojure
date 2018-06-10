(ns nexign-test-task-clojure.views
  (:require [reagent.core :as r]))


(defn loading []
  [:div.Loading "Loading"])

(defn error [{:keys [text]}]
  (if text [:div.Error text] nil))

(defn player [{:keys [steamid username on-delete-click]}]
  [:div.Player
   [:div.Player__texts
    [:div.Player__username username]
    [:div.Player__steamid (str "id:" steamid)]]
   [:button.Player__delete-btn {:on-click on-delete-click}]])

(defn players-list [{:keys [list on-delete-click]}]
  [:div.PlayersList
   (map
     (fn [{:keys [username steamid]}]
       (player {:username username :steamid steamid :on-delete-click on-delete-click}))
     list)])

(defn game [{:keys [appid name userscore genre]}]
  [:div.Game
   [:div.Game__data-col
    [:div.Game__name name]
    [:div.Game__genre genre]]
   [:div.Game__userscore userscore]
   [:a.Game__launch-link {:href (str "steam://run/" appid)}]])

(defn games-list [{:keys [list empty]}]
  [:div.PlayersList
   (if empty [:div.GamesList__empty-indicator "No games found :("]
             (map
               (fn [{:keys [appid name userscore genre]}]
                 (game {:appid appid :name name :userscore userscore :genre genre}))
               list))])

(defn add-new-player-form [{:keys [new-player-input-value
                                   disabled-submit
                                   disabled-input
                                   disabled-find-games
                                   loading
                                   on-submit
                                   on-input-ref-ready
                                   on-input-change
                                   on-find-games-click]}]
  [:form.AddNewPlayerForm {:on-submit #(-> % .-preventDefault on-submit)}
   [:input {:ref       on-input-ref-ready
            :disabled  disabled-input
            :on-change on-input-change
            :value     new-player-input-value
            :type      "text"
            :name      "new-player-username"
            :autoFocus true}]
   [:button {:disabled disabled-submit
             :type     "submit"} "Add"]
   [:button {:disabled disabled-find-games
             :on-click on-find-games-click
             :type     "button"} "Find games"]
   loading
   ])

(defn app []
  [:div.App
   [error {:text "subscribe for error here"}]
   [add-new-player-form {}]])

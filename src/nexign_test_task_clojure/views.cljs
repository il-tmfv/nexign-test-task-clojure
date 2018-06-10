(ns nexign-test-task-clojure.views
  (:require [reagent.core :as r]
            [re-frame.core :as re]))


(defn loading []
  [:div.Loading "Loading"])

(defn error []
  (let [error-text @(re/subscribe [:error])]
    (if (-> error-text count (> 0))
      [:div.Error error-text]
      nil)))

(defn player [{:keys [steamid username on-delete-click]}]
  [:div.Player {:key steamid}
   [:div.Player__texts
    [:div.Player__username username]
    [:div.Player__steamid (str "id:" steamid)]]
   [:button.Player__delete-btn {:on-click on-delete-click} "X"]])

(defn players-list []
  [:div.PlayersList
   (map
     (fn [{:keys [username steamid]}]
       (player {:username username :steamid steamid :on-delete-click #(re/dispatch [:remove-player steamid])}))
     @(re/subscribe [:players]))])

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

(defn add-new-player-form []
  [:form.AddNewPlayerForm
   {:on-submit #(do
                  (.preventDefault %)
                  (re/dispatch [:try-add-new-player]))}
   [:input {:ref       #(re/dispatch [:set-input-ref %])
            :disabled  @(re/subscribe [:loading?])
            :on-change #(re/dispatch [:on-form-input-change (-> % .-target .-value)])
            :value     @(re/subscribe [:new-player-input-value])
            :type      "text"
            :name      "new-player-username"
            :autoFocus true}]
   [:button {:disabled @(re/subscribe [:add-new-player-disabled])
             :type     "submit"} "Add"]
   [:button {:disabled @(re/subscribe [:get-games-disabled])
             :on-click #()
             :type     "button"} "Find games"]
   (when @(re/subscribe [:loading?]) [loading])
   ])

(defn app []
  [:div.App
   [error]
   [add-new-player-form]
   [players-list]])


;deletePlayer = steamid => {
;                           this.players = this.players.filter(x => x.steamid !== steamid);
;                           };

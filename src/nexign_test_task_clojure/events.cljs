(ns nexign-test-task-clojure.events
  (:require
    [clojure.string :as s]
    [ajax.core :as ajax]
    [re-frame.core :as re]
    [day8.re-frame.http-fx]))

(def base-url "http://localhost:3333")

(def initial-db-state {
                       :inputRef               nil
                       :players                []
                       :games                  []
                       :no-games-found         false
                       :error                  ""
                       :loading?               false
                       :new-player-input-value ""
                       })

(re/reg-event-db :init-db
                 (fn [_]
                   initial-db-state))

(re/reg-event-db :on-form-input-change
                 (fn [db event]
                   (let [value (second event)]
                     (assoc db :new-player-input-value value))))

(re/reg-event-db :set-input-ref
                 (fn [db event]
                   (let [ref (second event)]
                     (assoc db :inputRef ref))))

(re/reg-event-db :remove-player
                 (fn [db event]
                   (let [players (:players db)
                         steamid (second event)
                         filtered-players (filter #(not (= (:steamid %) steamid)) players)]
                     (assoc db :players filtered-players))))

(re/reg-event-fx :try-add-new-player
                 (fn [{:keys [db]} _]
                   (let [username (:new-player-input-value db)
                         uri (str base-url "/steamid?username=" username)]
                     {
                      :http-xhrio {:method          :get
                                   :uri             uri
                                   :format          (ajax/json-request-format)
                                   :response-format (ajax/json-response-format {:keywords? true})
                                   :on-success      [:on-player-added]
                                   :on-failure      [:on-add-player-error]}
                      :db         (assoc db :loading? true)
                      })))

(re/reg-event-db :on-player-added
                 (fn [db event]
                   (let [success (-> event second :response :success)
                         username (:new-player-input-value db)]
                     (if (= success 1)
                       (-> db
                           (assoc :new-player-input-value "")
                           (assoc :error "")
                           (assoc :loading? false)
                           (update :players conj {:steamid (-> event second :response :steamid) :username username}))
                       (-> db
                           (assoc :error "Error getting steamid")
                           (assoc :loading? false))
                       ))))

(re/reg-event-db :on-add-player-error
                 (fn [db _]
                   (-> db
                       (assoc :loading? false)
                       (assoc :error "Error getting steamid"))))

(defn steamids [db]
  (let [players (get db :players [])]
    (map :steamid players)))

(re/reg-event-fx :try-get-games
                 (fn [{:keys [db]}]
                   (let [steamids-string (->> db steamids (map #(str "steamids[]=" %)) (s/join "&"))
                         uri (str base-url "/common-games?" steamids-string)]
                     {
                      :http-xhrio {:method          :get
                                   :uri             uri
                                   :format          (ajax/json-request-format)
                                   :response-format (ajax/json-response-format {:keywords? true})
                                   :on-success      [:on-games-fetched]
                                   :on-failure      [:on-games-fetch-error]}
                      :db         (assoc db :loading? true)
                      })))

(re/reg-event-db :on-games-fetched
                 (fn [db event]
                   (let [games (second event)]
                     (-> db
                         (assoc :loading? false)
                         (assoc :error "")
                         (assoc :no-games-found (-> games count (= 0)))
                         (assoc :games games)))))

(re/reg-event-db :on-games-fetch-error
                 (fn [db _]
                   (-> db
                       (assoc :loading? false)
                       (assoc :error "Error getting games"))))

(ns nexign-test-task-clojure.events
  (:require [re-frame.core :as re]))

(def initial-db-state {
                       :inputRef               nil
                       :players                []
                       :games                  []
                       :no-games-found         false
                       :error                  ""
                       :loading                false
                       :new-player-input-value ""
                       })

(re/reg-event-db :init-db
                 (fn [_]
                   initial-db-state))

(re/reg-event-db :on-form-input-change
                 (fn [db event]
                   (let [value (-> event second .target .value)]
                     (assoc db :new-player-input-value value))))

(re/reg-event-db :set-input-ref
                 (fn [db event]
                   (let [ref (second event)]
                     (assoc db :inputRef ref))))


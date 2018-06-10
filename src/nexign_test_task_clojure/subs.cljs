(ns nexign-test-task-clojure.subs
  (:require [re-frame.core :as re]))

(re/reg-sub :add-new-player-disabled
            (fn [db _]
              (let [new-player-input-value (:new-player-input-value db)
                    loading (:loading db)
                    players (:players db)
                    already-in-list (some #(= (:username %) new-player-input-value) players)]
                (or (= new-player-input-value "") loading already-in-list))))

(re/reg-sub :get-games-disabled
            (fn [db _]
              (let [loading (:loading db)
                    players (:players db)
                    players-count (count players)]
                (or (< players-count 2) loading))))


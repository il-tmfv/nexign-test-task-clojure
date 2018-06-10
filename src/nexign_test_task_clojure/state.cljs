(ns nexign-test-task-clojure.state
  (:require [reagent.core :as r]
            [re-frame.core :as re]))

(defn steamids [db]
  (let [players (get db :players [])]
    (map :steamid players)))


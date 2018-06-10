(ns nexign-test-task-clojure.core
  (:require [reagent.core :as r]
            [re-frame.core :as re]
            [nexign-test-task-clojure.subs]
            [nexign-test-task-clojure.views :refer [app]]
            [nexign-test-task-clojure.events]
            [nexign-test-task-clojure.state]))

(enable-console-print!)

(re/dispatch-sync [:init-db])

;; define your app data so that it doesn't get over-written on reload

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )

(r/render app (.getElementById js/document "app"))

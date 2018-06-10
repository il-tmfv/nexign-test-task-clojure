(ns nexign-test-task-clojure.effects
  (:require [re-frame.core :as re]))

(re/reg-fx :focus-on-input
           (fn [{:keys [input-ref select]}]
             (println select)
             (.setTimeout js/window #(when (-> input-ref nil? not)
                                       (.focus input-ref)
                                       (when select
                                         (.select input-ref))) 0)))

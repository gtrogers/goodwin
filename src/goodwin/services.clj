(ns goodwin.services
  (:require [clojure.string :as string]))

(defn- not-service-status? [line]
  (or (= (first line) \$)
      (string/blank? line)))

(defn- service-status [line]
  (cond
    (re-find #"running" line) "running"
    (re-find #"stopped" line) "stopped"
    :else "unknown"))

(defn output->services-map [status-provider]
  (let [service-statuses (status-provider)]
    (map (fn [line]
           (let [text (string/trim line)]
             {:text text
              :status (service-status text)}))
         service-statuses)))

(defn service-matching [description services]
  (let [matcher (cond
                  (string? description)  (fn [service] (.contains (:text service) description))
                  (instance? java.util.regex.Pattern description) (fn [service] (re-find description (:text service)))
                  :else (throw (IllegalArgumentException. "Expecting string or regex pattern.")))]
  (first (filter matcher services))))


(ns goodwin.mappers.services
  (:require [clojure.string :as string]))

(defn- service-status [line]
  (cond
    (re-find #"running" line) "running"
    (re-find #"stopped" line) "stopped"
    :else "unknown"))

(defn output->services-map [status-provider]
  (let [service-statuses (:out (status-provider))]
    (map (fn [line]
           (let [text (string/trim line)]
             {:text text
              :status (service-status text)}))
         service-statuses)))

(defn- string-matcher [string]
  (fn [service] (.contains (:text service) string)))

(defn- regex-matcher [regex]
  (fn [service] (re-find regex (:text service))))

(defn- regex? [thing]
  (instance? java.util.regex.Pattern thing))

(defn service-matching [description services]
  (let [matcher (cond
                  (string? description) (string-matcher description) 
                  (regex? description)  (regex-matcher description)
                  :else (throw (IllegalArgumentException. "Expecting string or regex pattern.")))]
  (first (filter matcher services))))


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

(def string-matcher (fn [service] (.contains (:text service) description)))
(def regex-matcher  (fn [service] (re-find description (:text service))))

(defn service-matching [description services]
  (let [matcher (cond
                  (string? description)                           string-matcher 
                  (instance? java.util.regex.Pattern description) regex-matcher
                  :else (throw (IllegalArgumentException. "Expecting string or regex pattern.")))]
  (first (filter matcher services))))


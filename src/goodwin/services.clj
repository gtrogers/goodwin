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
  (let [status-str (status-provider)
        lines (remove not-service-status? (string/split status-str #"\p{Space}*\n+"))]
    (map (fn [line]
           (let [text (string/trim line)]
             {:text text
              :status (service-status text)}))
         lines)))

(defn service-matching [description services]
  (first (filter (fn [service]
                   (.contains (:text service) description))
                 services)))


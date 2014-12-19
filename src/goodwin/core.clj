(ns goodwin.core
  (:require [goodwin.mappers.services :refer [output->services-map]]
            [goodwin.ssh]))

; immigration
(def SSH> goodwin.ssh/SSH>)
; end immigration

(def ^:private mappers {:services output->services-map})

(defn map-server [collector]
  (let [collected-data (collector)]
    (reduce (fn [server-map k]
              (let [data (k collected-data)]
                (if-let [mapper (k mappers)]
                  (assoc server-map k (mapper data)) 
                  (assoc server-map k "no mapper found for " (str k)))))
            {}
            (keys collected-data))))


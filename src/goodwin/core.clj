(ns goodwin.core
  (:require [goodwin.mappers.services :refer [output->services-map]]
            [goodwin.ssh]))

; immigration
(def SSH> goodwin.ssh/SSH>)
; end immigration

(def ^:private mappers {:services output->services-map
                        :command  identity})

(defn map-server [collector]
  (let [collected-data (collector)]
    (reduce (fn [server-map k]
              (let [data (k collected-data)]
                (if-let [mapper (k mappers)]
                  (assoc server-map k (mapper data)) 
                  (assoc server-map k (str "no mapper found for " k)))))
            {}
            (keys collected-data))))


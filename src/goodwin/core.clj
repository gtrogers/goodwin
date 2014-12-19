(ns goodwin.core
  (:require [clj-ssh.ssh :refer :all]))

(defn- get-session [ip user key-path]
  (let [agent (ssh-agent {:use-system-ssh-agent false})]
    (add-identity agent  {:private-key-path key-path})
    (session agent ip {:username user
                       :strict-host-key-checking :no})))

(def ^:private services-command "/sbin/service --status-all")

(defn get-services [ip user key-path]
  (let [session (get-session ip user key-path)]
    (with-connection session 
        (:out (ssh session {:in services-command})))))


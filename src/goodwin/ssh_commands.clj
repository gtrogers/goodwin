(ns goodwin.ssh-commands
  (:require [clj-ssh.ssh :refer :all]))

(defn- get-session [ip user key-path]
  (let [agent (ssh-agent {:use-system-ssh-agent false})]
    (add-identity agent  {:private-key-path key-path})
    (session agent ip {:username user
                       :strict-host-key-checking :no})))

(def ^:private services-command
  (fn [session] (ssh session {:cmd "/sbin/service --status-all"})))

(defn services-status [ip user key-path]
  (let [session (get-session ip user key-path)]
    (with-connection session 
      (clojure.string/split (:out (services-command session)) #"\n"))))


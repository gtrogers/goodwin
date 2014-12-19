(ns goodwin.ssh-commands
  (:require [clj-ssh.ssh :refer :all]))

(defn- get-session [ip user key-path]
  (let [agent (ssh-agent {:use-system-ssh-agent false})]
    (add-identity agent  {:private-key-path key-path})
    (session agent ip {:username user
                       :strict-host-key-checking :no})))

(def ^:private services-command "/sbin/service --status-all")

(defn tidy-output
  "Returns a vector of the output of service and
  removes lines (first 3 and last) which aren't
  part of /sbin/service"
  [output]
  (let [raw-lines (clojure.string/split output #"\r+\n+")] 
    (map clojure.string/trim (pop (subvec raw-lines 3)))))

(defn services-status [ip user key-path]
  (let [session (get-session ip user key-path)]
    (with-connection session 
      (identity (:out (ssh session {:in services-command}))))))


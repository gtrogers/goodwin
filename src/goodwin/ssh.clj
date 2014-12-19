(ns goodwin.ssh
  (:require [clj-ssh.ssh :refer :all]))

(defn- get-session [ip user key-path]
  "Creates an SSH session using a standalone agent"
  (let [agent (ssh-agent {:use-system-ssh-agent false})]
    (add-identity agent  {:private-key-path key-path})
    (session agent ip {:username user
                       :strict-host-key-checking :no})))

(defn SSH>
  "Attempts to run the commands provided and returns
  a map of command-names to command results"
  [session & commands]
  (with-connection session
    (reduce (fn [result command]
              (assoc result (:name command) ((:cmd command) session))) {} commands)))


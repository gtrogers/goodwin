(ns goodwin.ssh
  (:require [clj-ssh.ssh :refer :all]))

(defn- get-session [host user key-path]
  "Creates an SSH session using a standalone agent"
  (let [agent (ssh-agent {:use-system-ssh-agent false})]
    (add-identity agent  {:private-key-path key-path})
    (session agent host {:username user
                       :strict-host-key-checking :no})))

(defn- do-SSH>
  "Attempts to run the commands provided and returns
  a map of command-names to command results"
  [session commands]
  (prn session commands)
  (with-connection session
    (reduce (fn [result command]
              (assoc result (:name command) ((:cmd command) session))) {} commands)))

(defn- parse-name-and-host [conn-string]
  (clojure.string/split conn-string #"@"))

(defn SSH> [conn-string private-key-path & commands]
  (let [[user host] (parse-name-and-host conn-string)]
    (do-SSH> (get-session host user private-key-path)
            commands)))


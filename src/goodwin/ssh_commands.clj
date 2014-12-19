(ns goodwin.ssh-commands
  (:require [clj-ssh.ssh :refer :all]))

(defn- get-session [ip user key-path]
  (let [agent (ssh-agent {:use-system-ssh-agent false})]
    (add-identity agent  {:private-key-path key-path})
    (session agent ip {:username user
                       :strict-host-key-checking :no})))

(defn- build-command-fn [cmd-string]
  (fn [session] (ssh session {:cmd cmd-string})))

(def ^:private services-command {:name :services
                                 :cmd (build-command-fn "/sbin/service --status-all")})

(defn SSH>
  "Attempts to run the commands provided and returns
  a map of command-names to command results"
  [session & commands]
  (with-connection session
    (reduce (fn [result command]
              (assoc result (:name command) ((:cmd command) session))) {} commands)))

(defn services-status [ip user key-path]
  (let [session (get-session ip user key-path)]
    (with-connection session 
      (clojure.string/split (:out (services-command session)) #"\n"))))


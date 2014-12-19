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
  [session collectors]
  (with-connection session
    (reduce (fn [result collector]
              (assoc result (:name collector) ((:cmd collector) session))) {} collectors)))

(defn- parse-name-and-host [conn-string]
  (clojure.string/split conn-string #"@"))

(defn SSH>
  "Returns a function which runs given collectors using SSH
  
  eg. (SSH> \"user@hostname\"
            \"~/.ssh/my-super-private-key\"
            services)"
  [conn-string private-key-path & collectors]
  (let [[user host] (parse-name-and-host conn-string)]
    #(do-SSH> (get-session host user private-key-path) collectors)))


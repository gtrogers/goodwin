(ns goodwin.ssh-collectors
  (:require [clj-ssh.ssh :refer :all]))

(defn- build-collector-fn [cmd-string]
  (fn [session] 
    (update-in (ssh session {:cmd cmd-string})
               [:out]
               clojure.string/split #"\n")))

(def services
  "Runs '/sbin/service --status-all' and returns the results as a vector"
  {:name :services :cmd (build-collector-fn   "/sbin/service --status-all")})

(def processes
  "Runs 'ps aux' and returns the results as a vector"
  {:name :processes :cmd (build-collector-fn "ps aux")})

(defn command 
  "Runs a custom command and returns the result as a vector"
  [cmd]
  {:name :command :cmd (build-collector-fn cmd)})


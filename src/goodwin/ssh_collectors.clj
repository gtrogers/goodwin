(ns goodwin.ssh-collectors
  (:require [clj-ssh.ssh :refer :all]))

(defn- build-command-fn [cmd-string]
  (fn [session] 
    (update-in (ssh session {:cmd cmd-string}) [:out] clojure.string/split #"\n")))

(def services {:name :services :cmd (build-command-fn "/sbin/service --status-all")})


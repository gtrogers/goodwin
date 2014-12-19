(ns goodwin.ssh-collectors
  (:require [clj-ssh.ssh :refer :all]))

(defn- build-command-fn [cmd-string]
  (fn [session] (ssh session {:cmd cmd-string})))

(def services-status {:name :services :cmd (build-command-fn "/sbin/service --status-all")})


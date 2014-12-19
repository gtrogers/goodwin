(ns goodwin.ssh-collectors
  (:require [clj-ssh.ssh :refer :all]))

(defn- build-collector-fn [cmd-string]
  (fn [session] 
    (update-in (ssh session {:cmd cmd-string}) [:out] clojure.string/split #"\n")))

(def services {:name :services :cmd (build-collector-fn "/sbin/service --status-all")})


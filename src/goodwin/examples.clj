(ns goodwin.examples (:require [goodwin.ssh-commands :as commands]
                               [goodwin.mappers.services :as services]))

(defn example []
  (let [services (services/output->services-map
                   #(commands/services-status "192.168.50.4"
                                              "vagrant" 
                                              "resources/vagrant_private_key"))
        sshd (services/service-matching "sshd" services)] 
    (= (:status sshd) "running")))

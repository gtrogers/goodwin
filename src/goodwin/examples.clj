(ns goodwin.examples (:require [goodwin.core :as core]
                               [goodwin.services :as services]))

(defn example []
  (let [services (services/output->services-map
                   #(core/get-services "192.168.50.4" "vagrant" "resources/vagrant_private_key"))]
    (services/service-matching "sshd" services)))

(ns goodwin.examples (:require [goodwin.ssh :refer :all]
                               [goodwin.ssh-collectors :as c]
                               [goodwin.mappers.services :as s]))

(defn example []
  (let [every-status (s/output->services-map #(:services (SSH> "vagrant@192.168.50.4"
                                                               "resources/vagrant_private_key"
                                                               c/services)))]))


(ns goodwin.examples (:require [goodwin.core :refer :all]
                               [goodwin.ssh-collectors :as c]))

(defn example []
  (map-server (SSH> "vagrant@192.168.50.4"
                    "resources/vagrant_private_key"
                    c/services)))


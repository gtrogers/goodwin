(ns goodwin.examples (:require [goodwin.core :refer :all]
                               [goodwin.ssh-collectors :as c]
                               [goodwin.mappers.services :as s]
                               [clojure.test :refer :all]))

(deftest the-server
  (testing "that something is running"
    (let [server-data (map-server (SSH> "vagrant@192.168.50.4"
                                        "resources/vagrant_private_key"
                                        c/services
                                        (c/command "echo hello") 
                                        c/processes
                                        ))]
      (is (= (-> server-data
                 :services
                 (s/service-matching "mongod")
                 :status)
             "running")))))


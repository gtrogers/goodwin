(ns goodwin.services-test
  (:require [clojure.test :refer :all]
            [goodwin.services :refer :all]))

(defn fake-service-status-provider []
  "
  auditd (pid  867) is running...\r\n
  crond (pid  1339) is running...\r\n
  elasticsearch is stopped\r\n
  netconsole module not loaded\r\n
  Configured devices:\r\n
  lo eth0 eth1\r\n
  Currently active devices:\r\n
  lo eth0 eth1\r\n
  master status unknown due to insufficient privileges.\r\n
  rsyslogd status unknown due to insufficient privileges.\r\n
  Checking for VBoxService ...running\r\n
  ")

(deftest maps-service-output
  (testing "Parses the output of service status"
    (let [results (output->services-map #(fake-service-status-provider))]

      (is (= (first results)
             {:text "auditd (pid  867) is running..." :status "running"})) 

      (is (= (second results)
             {:text "crond (pid  1339) is running..." :status "running"})) 

      (is (= (nth results 2) ; third
             {:text "elasticsearch is stopped" :status "stopped"}))

      (is (= (nth results 9) ; tenth
             {:text "rsyslogd status unknown due to insufficient privileges." :status "unknown"})))))

(deftest finds-service-in-map
  (testing "Finding a service in the services map"
    (is (= (service-matching "master" (output->services-map #(fake-service-status-provider)))
           {:text "master status unknown due to insufficient privileges." :status "unknown"}))))


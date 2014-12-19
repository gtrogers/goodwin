(ns goodwin.ssh-commands-test
  (:require [goodwin.ssh-commands :refer :all] 
            [clojure.test :refer :all]))

(def untidy-output
  "Last login: Fri Dec 19 14:17:18 2014 from 192.168.50.1\r\r\n
  /sbin/service --status-all;exit $?;\r\n
  [vagrant@vagrant-centos65 ~]$/sbin/service --status-all;exit $?;\r\n
  This is the stuff we care about\r\n
  and this\r\n
  and this...\r\n
  logout\r\n")

(deftest test-tidying-output
  (testing "Output is a vector with non-service related lines removed"
    (is (= (tidy-output untidy-output) ["This is the stuff we care about"
                                        "and this"
                                        "and this..."]))))


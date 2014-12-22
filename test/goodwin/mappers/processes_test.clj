(ns goodwin.mappers.processes-test
  (:require [clojure.test :refer :all]
            [goodwin.mappers.processes :refer :all]))

(def process-data {:out ["USER       PID %CPU %MEM    VSZ   RSS TTY      STAT START   TIME COMMAND"
                         "root         1  0.0  0.0  19232  1508 ?        Ss   00:31   0:00 /sbin/init"
                         "root         2  0.0  0.0      0     0 ?        S    00:31   0:00  [kthreadd]"
                         "root         7  0.0  0.0      0     0 ?        S    00:31   0:25  [events/0]"
                         "root       765  0.0  0.0   9120   956 ?        Ss   00:31   0:00 /sbin/dhclient -H vagrant-centos65.vagrantup.com -1 -q -cf /etc/dhcp/dhclient-eth0.conf -lf /var/lib/dhclient/dhclient-eth0.leases -pf /var"
                         "root       867  0.0  0.0  27640   804 ?        S<sl 00:31   0:00 auditd"
                         "root       883  0.0  0.0 249088  1604 ?        Sl   00:31   0:00 /sbin/rsyslogd -i /var/run/syslogd.pid -c 5"
                         "rpc        907  0.0  0.0  18976   888 ?        Ss   00:31   0:00 rpcbind"
                         "rpcuser    925  0.0  0.0  23348  1328 ?        Ss   00:31   0:00 rpc.statd"
                         "dbus       951  0.0  0.0  21404  1028 ?        Ss   00:31   0:05 dbus-daemon --system"]})

(deftest splitting-output
  (testing "Splitting output line into vector"
    (is (= (split-output-line "root         1  0.0  0.0  19232  1508 ?        Ss   00:31   0:00 /sbin/init") 
           ["root" "1" "0.0" "0.0"
            "19232" "1508" "?" "Ss" "00:31"
            "0:00" "/sbin/init"])))

  (testing "Splitting output line with flags into vector"
    (is (= (split-output-line "root       883  0.0  0.0 249088  1604 ?        Sl   00:31   0:00 /sbin/rsyslogd -i /var/run/syslogd.pid -c 5")
            ["root" "883" "0.0" "0.0"
             "249088" "1604" "?" "Sl" "00:31"
             "0:00" "/sbin/rsyslogd -i /var/run/syslogd.pid -c 5"]))))

(deftest maps-processes-output
  (testing "Parses the output of process aux"
    (let [results (output->processes process-data)
          first-result (first results)
          second-result (second results) ]
      (are [expected actual] (= expected actual)

           ;; result 1 assertions
           "root         1  0.0  0.0  19232  1508 ?        Ss   00:31   0:00 /sbin/init" (:text first-result) 
           "1" (:process-id first-result)

           ;; result 2 assertions
           "root         2  0.0  0.0      0     0 ?        S    00:31   0:00  [kthreadd]" (:text second-result) 
           "2" (:process-id second-result)))))


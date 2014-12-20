[![Clojars Project](http://clojars.org/goodwin/latest-version.svg)](http://clojars.org/goodwin)

# Goodwin

> "He's in a battle with the elements. He's fighting his way through a raging blizzard,
> just sitting there comfortably looking at pictures of snowflakes."
>  - Archie Goodwin (from the The League of Frightened Men by Rex Stout)

Goodwin is a Clojure library for capturing the state of servers. It runs shell commands via
SSH and turns the output into Clojure data structures which can then be used in your testing
framework of choice. It is currently a work in progress.

## Installation

This library is very new and likely to change a lot. But if you would like to try it you can add
this to your `project.clj`

`[goodwin "0.2.0-SNAPSHOT"]`


## Usage

The `SSH>` function builds up a list of collectors to run against a server
and the `map-server` function turns the raw output into a map describing the
state of the server.

```Clojure
(ns your.awesome.project
  (:require [goodwin.core :refer :all]
            [goodwin.ssh-collectors :as c])

(let [server-data (map-server (SSH> "user@hostname"
                                    "~/.ssh/super_private_key"
                                    c/services))]
  (prn server-data)))
```

## License

Copyright © 2014 Gareth Rogers

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

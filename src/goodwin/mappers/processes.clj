(ns goodwin.mappers.processes)

(def ^:private headers [:user
                        :process-id
                        :percent-cpu
                        :percent-memory
                        :virtual-memory
                        :non-swapped-physical-memory
                        :controlling-terminal
                        :stat
                        :started
                        :time
                        :command])

(defn- pack-after [v n]
  (let [[first-n remaining] (split-at n v)]
    (conj (vec first-n) (clojure.string/join " " remaining))))

(defn split-output-line [line]
  (pack-after (clojure.string/split line #"\s+") (dec (count headers))))

(defn line->map [line]
  (zipmap headers (split-output-line line)))

(defn output->processes [output]
  (->> output
       :out
       rest
       (map (fn [line] 
              (assoc (line->map line) :text line)))))


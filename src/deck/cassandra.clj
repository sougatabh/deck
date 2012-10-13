(ns deck.cassandra
  (:use [clj-hector.core])
  (:use [clj-hector.ddl] ))

(def c (cluster "Test Cluster" "localhost"))
(def ks (keyspace c "DEMO"))

(defn get-record[]
(println (get-rows ks "Users" ["1234"] :n-serializer :string)))

(defn get-keyspaces[]
  (keyspaces c))

(defn cassandra-save-keyspace
  "This is create new key space in cassandra cluster"
  [keyspace-name]
  (add-keyspace c {:name keyspace-name
                     :strategy :simple
                     :replication 1}))




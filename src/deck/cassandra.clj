(ns deck.cassandra
  (:use [clj-hector.core])
  (:use [clj-hector.ddl] ))

(def c (cluster "Test Cluster" "localhost"))
(def ks (keyspace c "DEMO"))

(defn get-record[]
(println (get-rows ks "Users" ["1234"] :n-serializer :string)))

(defn get-keyspaces[]
  (keyspaces c))

(defn cassandra-get-columnspaces
  "Get the keyspace for given keyspace"
  [keyspace]
  (column-families c keyspace))

(defn cassandra-save-keyspace
  "This is create new key space in cassandra cluster"
  [keyspace-name]
  (add-keyspace c {:name keyspace-name
                     :strategy :simple
                     :replication 1}))

(defn cassandra-save-columnfamily
  "This is to create a column family in the provided key space"
  [keyspace-name column-familyname mcomparator mtype]
  
  
  (add-column-family c keyspace-name {:name column-familyname :comparator :long :type mtype}))





(defn cassandra-get-columns
  [keyspace column-family]
  )
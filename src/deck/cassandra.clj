(ns ^{:author "Sougata Bhattacharya"
      :doc "A Cassandra Admin Tool"}
   deck.cassandra
  (:use clj-hector.core,clj-hector.ddl,clj-hector.serialize))

(def c (cluster "Test Cluster" "localhost"))
(def ks (keyspace c "DEMO"))


(defn get-keyspaces[host mycluster]
  (keyspaces (cluster mycluster host)))

(defn cassandra-get-columnspaces
  "Get the keyspace for given keyspace"
  [keyspace hostname clustername]
  (let [cluster (cluster clustername hostname)]
  (column-families cluster keyspace)))

(defn cassandra-save-keyspace
  "This is create new key space in cassandra cluster"
  [hostname clustername keyspace-name strategy replicationfactor]
  (let [rf (if (empty replicationfactor) "1" replicationfactor)
        cluster (cluster clustername hostname)]
  (add-keyspace cluster {:name keyspace-name
                     :strategy strategy
                     :replication (Integer/parseInt rf)})))

(defn cassandra-save-columnfamily
  "This is to create a column family in the provided key space"
  [ hostname clustername keyspace-name column-familyname mcomparator mtype validator k-validator]
  (let [cluster (cluster clustername hostname) ]
  (add-column-family cluster keyspace-name {:name column-familyname :comparator :long :type mtype :validator validator :k-validator k-validator})))

(defn cassandra-get-rows
  "Get the rows for the  given keyspace,column family key and serlization type"
  [keyspace-name columnfamily key serialization-type hostname clustername]
  (let [cluster (cluster clustername hostname)
        selected-ks (keyspace cluster keyspace-name)]
     (get-rows selected-ks columnfamily [key] :n-serializer serialization-type :v-serializer :string)))

(defn cassandra-drop-column-family
  "This function is to delete the column family of a keyspace"
  [host-name cluster-name keyspace-name column-family-name]
  (let [selected-cluster (cluster cluster-name host-name)]
    (drop-column-family selected-cluster keyspace-name column-family-name)))

(defn cassandra-drop-keyspace
  [hostname clustername keyspace]
  
  (let [selected-cluster (cluster clustername hostname)]
    (drop-keyspace selected-cluster keyspace)))

(defn cassandra-get-rows-cql-query
  [hostname clustername keyspacename query]
  (let[cluster (cluster clustername hostname)
       keyspace (keyspace cluster keyspacename)
       opts [:v-serializer :string
             :n-serializer :string
             :k-serializer :string]
      ]
   (println keyspace)
   (get-rows-cql-query keyspace "select * from person")))


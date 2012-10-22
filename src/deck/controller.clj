(ns deck.controller
  (:use compojure.core,ring.middleware.params,ring.middleware.session,ring.util.response,hiccup.core,hiccup.form,deck.cassandra,deck.utils)
  (:require [compojure.route :as route] 
            [compojure.handler :as handler]
            [ring.util.response :as resp]
            [basil.core :as basil-core]
	          [basil.jvm  :as basil-jvm]
            [basil.group :as basil-group]))


(def mtask-tpl (basil-jvm/make-group-from-classpath :prefix "templates/"))


(defn show-keyspaces-list[keyspace]
  (str "<li> <a href='/show-columnfamilies?keyspace="(:name keyspace)"'>"(:name keyspace)"</a></li>"))

(defn generate-keyspace-list
  "This is to generate the key spaces list in the cluster and show in left nav"
  [keyspaces]
  (map show-keyspaces-list keyspaces))        
        
(defn create-keyspace
  "This is to create New Key space"
  []
  (basil-core/render-by-name mtask-tpl "create-keyspace.basil" [{:error "" :keyspaces (generate-keyspace-list (get-keyspaces))}]))

(defn save-keyspace[request]
  (let [params (:params request)]
  (cassandra-save-keyspace (:keyspace params))
  (basil-core/render-by-name mtask-tpl "create-keyspace.basil" [{:message "Keyspace created Successfully!"}])))

(defn render-column-family [columnfamily]
  (str "<tr>" "<td>" (:id columnfamily) "</td>" "<td>" (:name columnfamily) "</td>" "<td>" (:comparator columnfamily) "</td>" "<td>" (:type columnfamily) "</td>" "<td>" (:validator columnfamily) "</td>" "</tr>"))

(defn generate-columnfamily-list
  [columnfamilies]
  (map render-column-family columnfamilies))

(defn show-columnfamilies
  "This will render the column spaces for the selected Key Space"
  [request]
  (let [params (:params request)]
    (basil-core/render-by-name mtask-tpl "show-column-families.basil" 
                               [{:error "" :keyspaces (generate-keyspace-list (get-keyspaces))
                                 :selectedKeySpace (:keyspace params) 
                                 :columnfamilies (generate-columnfamily-list (cassandra-get-columnspaces (:keyspace params)))}]
                               )))


(defn create-columnfamily[request]
  (let [params (:params request)]
  
  (basil-core/render-by-name mtask-tpl "create-columnfamily.basil" 
                             [{:keyspaces (generate-keyspace-list (get-keyspaces)) 
                               :comparators (get-comparator-dropdown) 
                               :columnfamilies (get-comlumnfamily-type-dropdown)
                               :selectedKeySpace (:keyspace params)}])))


(defn save-columnfamily[request]
  (let [params (:params request)]
    ( cassandra-save-columnfamily (:keyspace params)  (:columnfamilyname params) (:comparator params) (:columnfamilytype params))
    (html "Column Famliy Created Successfully")))



(defn show-keyspaces[]
  (basil-core/render-by-name mtask-tpl "show-keyspaces.basil" [{:keyspaces-dropdown ""}])) 

(defn show-columns[request]
  (cassandra-get-columns "DEMO" "Users"))
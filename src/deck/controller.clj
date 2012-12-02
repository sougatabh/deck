(ns ^{:author "Sougata Bhattacharya"
      :doc "A Cassandra Admin Tool"} 
  deck.controller
  (:use compojure.core,ring.middleware.params,ring.middleware.session,ring.util.response,hiccup.core,hiccup.form,deck.cassandra,deck.utils)
  (:require [compojure.route :as route] 
            [compojure.handler :as handler]
            [ring.util.response :as resp]
            [basil.core :as basil-core]
	          [basil.public  :as basil-public]
            [basil.group :as basil-group])
   (:use [
         clojure.string :only (join split)]))


(def mtask-tpl (basil-public/make-group-from-classpath :prefix "templates/"))

(def ^:dynamic *selectedkeyspace* "")
(def ^:dynamic *selectedhostname* "")
(def ^:dynamic *selectedcluster* "")


(defn render-columnfamily-leftnav
  [columnfamily]
  (str "<li><a href='/search-column-family?columnfamily="(:name columnfamily)"&keyspace=" *selectedkeyspace* "'>"(:name columnfamily)"</a></li>"))



(defn generate-columnfamilies-leftnav[columnfamiles]  
   (map render-columnfamily-leftnav columnfamiles))

(defn show-keyspaces-list[keyspace]
  (binding [*selectedkeyspace* (:name keyspace)]
  (str "<li> <a href='/show-columnfamilies?keyspace="(:name keyspace)"&hostname="*selectedhostname* "&clustername="*selectedcluster* "'>"(:name keyspace)"</a>"
       "<ul>"
       (if (cassandra-get-columnspaces (:name keyspace))
       (apply str(generate-columnfamilies-leftnav (cassandra-get-columnspaces (:name keyspace)))))
       "</ul>"
       "</li>")))

(defn generate-keyspace-list
  "This is to generate the key spaces list in the cluster and show in left nav"
  [keyspaces]
  (map show-keyspaces-list keyspaces))        

(defn generate-leftnav-items
  "This is to generate left nav items"
  [connection-name host-name cluster-name]
    (binding [*selectedhostname* host-name
              *selectedcluster* cluster-name]
    (str "<li>" connection-name "<ul><li><a href='/show-keyspaces?hostname=" host-name "&clustername="cluster-name "'>" cluster-name "</a><ul>"
         (apply str (generate-keyspace-list (get-keyspaces host-name  cluster-name))) "</ul></li></ul></li>")))


(defn generate-each-connection
  [each-connection-detail]
  (let [[connection-name host cluster] (.split each-connection-detail ",")]
  (generate-leftnav-items connection-name host cluster)))

(defn generate-main-left-nav
  "This generates the main left nav"
  []
  (map generate-each-connection (.split (read-all-settings) "\n")))

(defn render-each-keyspace
  [each-keyspace]
  (str "<tr class='table-row'>"
       "<td><input type='radio' name='deleteKeyspace' value='"(:name each-keyspace)"'></td>"
       "<td>" (:name each-keyspace) "</td>"
       "<td>" (:replication-factor each-keyspace) "</td>"
       "</tr>"))
(defn render-keyspaces-table[host-name cluster-name]
  (map render-each-keyspace (get-keyspaces host-name  cluster-name)))
  
(defn show-keyspaces-page
  [request]
  (let [params (:params request)]
  (basil-core/render-by-name mtask-tpl "show-keyspaces.html" [{:message "" :error "" 
                                                               :keyspaces (generate-main-left-nav)
                                                               :all-keyspaces (render-keyspaces-table (:hostname params) (:clustername params))
                                                               :hostname (:hostname params)
                                                               :clustername (:clustername params)}])))


(defn create-keyspace
  "This is to create New Key space"
  [request]
  (let [params (:params request)]
  (basil-core/render-by-name mtask-tpl "create-keyspace.html" [{:message "" :error "" :keyspaces (generate-main-left-nav)
                                                                :hostname (:hostname params)
                                                                :clustername (:clustername params)}])))

(defn save-keyspace[request]
  (let [params (:params request)]
  (cassandra-save-keyspace (:hostname params) (:clustername params)(:keyspace params) (:strategyclass params) (:replicationfactor params))
  (redirect (str "/show-keyspaces?hostname=" (:hostname params) "&clustername=" (:clustername params)))))

(defn render-column-family 
  [columnfamily]
  (str "<tr class='table-row'>"
       "<td><input type='radio' name='deleteColumnFamily' value='"(:name columnfamily)"'></td>"
       "<td><a href='/search-column-family?columnfamily="(:name columnfamily)"&keyspace=" *selectedkeyspace* "''>" (:id columnfamily) "</a></td>" 
       "<td>" (:name columnfamily) "</td>"
       "<td>" (validator-class-type (:comparator columnfamily)) "</td>" 
       "<td>" (types (:type columnfamily)) "</td>" 
       "<td>" (validator-class-type (:validator columnfamily)) "</td>"
       "<td>" (validator-class-type (:k-validator columnfamily))"</td>"
       "<td>"  " 0 </td>" 
       "<td>"  "0 </td>" 
       "<td>"  "0 </td>"
       "<td>"  "0 </td>" 
       
       "</tr>"))

(defn generate-columnfamily-list
  [columnfamilies]
  (map render-column-family columnfamilies))

(defn show-columnfamilies
  "This will render the column spaces for the selected Key Space"
  [request]
  (let [params (:params request)]
    (binding [*selectedkeyspace* (:keyspace params)]
    (basil-core/render-by-name mtask-tpl "show-columnfamilies.html" 
                               [{:error "" 
                                 :keyspaces (generate-main-left-nav)
                                 :message ""
                                 :keyspace (:keyspace params) 
                                 :hostname (:hostname params)
                                 :clustername (:clustername params)
                                 :columnfamilies (generate-columnfamily-list (cassandra-get-columnspaces (:keyspace params)))}]
                               ))))


(defn create-columnfamily[request]
  (generate-main-left-nav)
  (let [params (:params request)]
  (basil-core/render-by-name mtask-tpl "create-columnfamily.html" 
                             [{:keyspaces (generate-main-left-nav) 
                               :comparators (get-comparator-dropdown)
                               :subcomparators (get-subcomparator-dropdown)
                               :validator-class (get-validation-class-dropdown)
                               :columnfamilies (get-comlumnfamily-type-dropdown)
                               :selectedKeySpace (:keyspace params)
                               :hostname (:hostname params)
                               :clustername (:clustername params)}])))


(defn save-columnfamily[request]
  (let [params (:params request)]
    ( cassandra-save-columnfamily (:hostname params) (:clustername params) (:keyspace params)  
                                  (:columnfamilyname params) (:comparator params) (:columnfamilytype params)
                                  (:defaultvalidator params) (:keyvalidator params))
    (redirect (str "/show-columnfamilies?keyspace=" (:keyspace params) "&hostname="(:hostname params) "&clustername=" (:clustername params)))))



(defn show-keyspaces[]
  (basil-core/render-by-name mtask-tpl "show-keyspaces.basil" [{:keyspaces-dropdown ""}])) 

(defn search-column-family[request]
  (let [params (:params request)]
  (basil-core/render-by-name mtask-tpl "search-columnfamily.html" [{:keyspaces (generate-main-left-nav)
                                                                    :keyspace (:keyspace params) :columnfamily (:columnfamily params) 
                                                                    :search-result ""}])))
(def data-type-drop-down 
  (str "<select name='data-type'>"
         "<option value=':string'>String</option>"
         "<option value=':long'>Long</option>"
         "<option value=':integer'>Integer</option>"
         "<option value=':bytes'>Bytes</option>"
        "</select>"))


(defn printer[ks column-name-value]
  (if-not(= ":rowkey" (str ks))
  (str "<tr  class='warning'><td>" ks "</td>" "<td>" (second column-name-value)"</td><td>" data-type-drop-down "</td></tr>")))

(defn populate-data[column-name-value]
  (let [ks (keys column-name-value)
        body (map printer ks column-name-value)]
        (apply str  body)))
       
(defn pupulate-search-result[all-values]
  (map populate-data all-values))

(defn search [request]
  (let [params (:params request)
        search-result (cassandra-get-rows (:keyspace params) (:columnfamily params)(:key params) (:serializationtype params))
        re  (map (fn [m] (let [k (first (keys m)) v (get m k)] (merge v {:rowkey k}))) search-result)
        search-body (apply str (pupulate-search-result re))
        header  (str "<thead>""<tr  class='success'><td><b>Column Name </b></td>" "<td><b>Column Value</b></td> <td><b>Data Type</b></td></tr>"
                                            "</thead><tbody>")
        result (str header search-body)                                            
        ]
    (basil-core/render-by-name mtask-tpl "search-columnfamily.html" [{:keyspaces (generate-main-left-nav)
                                                                      :keyspace (:keyspace params) :columnfamily (:columnfamily params) 
                                                                      :search-result result}])))

(defn add-column
  "This is to add coulmn in the column family"
  [request]
  (let [params (:params request)]
   (basil-core/render-by-name mtask-tpl "add-column.html" [{:keyspaces (generate-main-left-nav):keyspace (:keyspace params) :columnfamily (:columnfamily params)}]) ))


(defn populate-all-connections-table[all-connections]
  (map  (fn [x]  (str "<tr class='table-row'><td>" (clojure.string/replace (str x)  #","  "</td><td>")  "</td></tr>")) (.split all-connections "\n")))

(defn show-all-settings
  [request]
  (let [all-connections (read-all-settings)
        connections (populate-all-connections-table all-connections)]
  
  (basil-core/render-by-name mtask-tpl "all-settings.html" [{:keyspaces (generate-main-left-nav) :connections connections}])))

(defn setup-new-host[request]
  (basil-core/render-by-name mtask-tpl "setup-new-connection.html" [{:keyspaces (generate-main-left-nav)}]))

(defn save-connection
  [request]
  (let [params (:params request)]
  (write-settings (str (:connectionname params) "," (:hostname params) "," (:clustername params)))
  (redirect "/show-all-settings")))


(defn delete-columnfamily[request]
  (let [
       params (:params request)
       ]
  (cassandra-drop-column-family (:hostname params) (:clustername params) (:keyspace params) (:deleteColumnFamily params))
  (redirect (str "/show-columnfamilies?keyspace=" (:keyspace params) "&hostname="(:hostname params) "&clustername=" (:clustername params)))))



(defn delete-keyspace[request]
  (let [
       params (:params request)
       ]
  (cassandra-drop-keyspace (:hostname params) (:clustername params) (:deleteKeyspace params))
  (redirect (str "/show-keyspaces?hostname="(:hostname params) "&clustername=" (:clustername params)))))


(defn index
  "This is the index page"
  [request]
  (let [params (:params request)]
  (basil-core/render-by-name mtask-tpl "index.html" [{:message "" :error "" :keyspaces (generate-main-left-nav)
                                                                }])))
(ns ^{:author "Sougata Bhattacharya"
      :doc "A Cassandra Admin Tool"} 
  deck.core
  (:use compojure.core,ring.middleware.params,ring.middleware.session,deck.controller)
	(:require [compojure.route :as route] 
            [compojure.handler :as handler]
	          [basil.core :as basil-core]
	          [basil.public  :as basil-public]
            [basil.group :as basil-group]
            [ring.util.response :as resp]))




(defroutes deck-routes
   (route/files "/" {:root "public"})
   (GET "/" {:as request} (main-page request))
   (GET "/contact" {:as request} (contact-page request))
   (GET "/index" {:as request} (index request))
   (GET "/create-keyspace" {:as request} (create-keyspace request))
   (GET "/show-keyspaces" {:as request} (show-keyspaces-page request))
   (POST "/save-keyspace" {:as request}   (save-keyspace request))
   (GET "/delete-keyspace" {:as request}   (delete-keyspace request))
   (GET "/show-columnfamilies" {:as request} (show-columnfamilies request))
   (GET "/create-columnfamily" {:as request} (create-columnfamily request))
   (POST "/save-columnfamily" {:as request}  (save-columnfamily request))
   (GET "/add-column" {:as request} (add-column request))
   (GET "/search-column-family" {:as request}  (search-column-family request))
   (POST "/search" {:as request} (search request))
   (GET "/show-all-settings" {:as request} (show-all-settings request))
   (GET "/setup-new-host" {:as request} (setup-new-host request))
   (POST "/save-connection" {:as request} (save-connection request))
   (GET "/delete-columnfamily" {:as request} (delete-columnfamily request))
   (GET "/show-cql-editor" {:as request} (show-cql-editor request))
   (GET "/action-cluster" {:as request} (action-cluster request))
   (GET "/action-keyspace" {:as request} (action-keyspace request))
   (GET "/action-columnfamily" {:as request} (action-columnfamily request))
   (POST "/execute-cql" {:as request} (execute-cql request)))


(def app (compojure.handler/site deck-routes))

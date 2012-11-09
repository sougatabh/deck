(ns ^{:author "Sougata Bhattacharya"
      :doc "A Cassandra Admin Tool"} 
  deck.core
  (:use compojure.core,ring.middleware.params,ring.middleware.session,hiccup.core,deck.controller)
	(:require [compojure.route :as route] 
            [compojure.handler :as handler]
	          [basil.core :as basil-core]
	          [basil.public  :as basil-public]
            [basil.group :as basil-group]
            [ring.util.response :as resp]))

(defn display[]
  
  (html [:h1 "Hello Welcome to the Deck"]))

(defn index
  "This is to create New Key space"
  []
  (basil-public/render-by-name mtask-tpl "index.basil" [{:error ""}]))

(defroutes deck-routes
   (route/files "/" {:root "public"})
   (GET "/index" [] (index))
   (GET "/" [] (create-keyspace))
   (POST "/save-keyspace" {:as request}   (save-keyspace request))
   (GET "/show-columnfamilies" {:as request} (show-columnfamilies request))
   (GET "/create-columnfamily" {:as request} (create-columnfamily request))
   (POST "/save-columnfamily" {:as request}  (save-columnfamily request))
   (GET "/add-column" {:as request} (add-column request))
   (GET "/search-column-family" {:as request}  (search-column-family request))
   (POST "/search" {:as request} (search request))
   (GET "/show-all-settings" {:as request} (show-all-settings request))
   (GET "/setup-new-host" {:as request} (setup-new-host request))
   (POST "/save-connection" {:as request} (save-connection request))
   (GET "/delete-columnfamily" {:as request} (delete-columnfamily request)))



;;(def app (wrap-params mtask-routes:session))
(def app (compojure.handler/site deck-routes))

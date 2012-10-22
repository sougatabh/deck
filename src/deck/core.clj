(ns deck.core
  	(:use compojure.core,ring.middleware.params,ring.middleware.session,hiccup.core,deck.controller)
	(:require [compojure.route :as route] 
            [compojure.handler :as handler]
	          [basil.core :as basil-core]
	          [basil.jvm  :as basil-jvm]
            [basil.group :as basil-group]
            [ring.util.response :as resp]))

(defn display[]
  
  (html [:h1 "Hello Welcome to the Deck"]))


(defroutes deck-routes
   (route/files "/" {:root "public"})
   (GET "/" [] (create-keyspace))
   (POST "/save-keyspace" {:as request}   (save-keyspace request))
   (GET "/show-columnfamilies" {:as request} (show-columnfamilies request))
   (GET "/create-columnfamily" {:as request} (create-columnfamily request))
   (POST "/save-columnfamily" {:as request}  (save-columnfamily request))
   (GET "/show-columns" {:as request}  (show-columns request)))


;;(def app (wrap-params mtask-routes:session))
(def app (compojure.handler/site deck-routes))

(ns deck.controller
  (:use compojure.core,ring.middleware.params,ring.middleware.session,ring.util.response,hiccup.core,hiccup.form,deck.cassandra)
  (:require [compojure.route :as route] 
            [compojure.handler :as handler]
            [ring.util.response :as resp]
            [basil.core :as basil-core]
	          [basil.jvm  :as basil-jvm]
            [basil.group :as basil-group]))

(def mtask-tpl (basil-jvm/make-group-from-classpath :prefix "templates/"))

(defn create-column-space[]
  (basil-core/render-by-name mtask-tpl "create-column-space.basil" [{:error ""}]))

(defn create-keyspace[]
  (basil-core/render-by-name mtask-tpl "create-keyspace.basil" [{:error ""}]))

(defn save-keyspace[request]
  (let [params (:params request)]
  (cassandra-save-keyspace (:keyspace params))
  (basil-core/render-by-name mtask-tpl "create-keyspace.basil" [{:message "Keyspace created Successfully!"}])))

(defn show-dropdown-options [keyspace]
  (println keyspace)
 (str "<option value='"(:name keyspace)"'>"(:name keyspace)"</option>"))

(defn keyspace-dropdown [keyspaces]
  (map show-dropdown-options keyspaces))


(defn show-keyspaces[]
  (basil-core/render-by-name mtask-tpl "show-keyspaces.basil" [{:keyspaces-dropdown (keyspace-dropdown (get-keyspaces))}])) 


(defproject deck "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
    :plugins [[lein-ring "0.7.1"]]
    :dependencies [
                [org.clojure/clojure "1.4.0"]
                [compojure "1.1.1"]
                [hiccup "1.0.0"]
		 		        [org.clojure/java.jdbc "0.0.6"]
		            [basil "0.4.0"]
		 		        [mysql/mysql-connector-java "5.1.6"]
                [org.clojars.paul/clj-hector "0.2.5"]]
    :ring {:handler deck.core/app})

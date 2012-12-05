(defproject deck "0.1.0"
  :description "This is a Cassandra admin web tool"
  :url "https://github.com/sougatabh/deck"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-ring "0.7.1"]]
  :dependencies [[org.clojure/clojure "1.4.0"]
                [compojure "1.1.1"]
                [basil "0.4.0"]
                [org.clojars.paul/clj-hector "0.2.5"]]
  :ring {:handler deck.core/app})

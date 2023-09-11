(defproject simple-server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/data.json "2.4.0"]
                 [compojure "1.7.0"]
                 [clj-http "3.12.3"]
                 [org.clj-commons/hickory "0.7.3"]
                 [ring/ring-defaults "0.3.4"]
                 [ring/ring-json "0.5.1"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler simple-server.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.4.0"]]}})

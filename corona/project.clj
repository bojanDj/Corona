(defproject corona "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-codox "0.10.7"]]
  :main corona.core
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [com.stuartsierra/component "1.0.0"]
                 [org.clojure/data.json "1.0.0"]
                 [aleph "0.4.6"]
                 [hiccup "1.0.5"]
                 [bidi "2.1.3"]
                 [ring/ring-mock "0.3.2"]
                 [org.seleniumhq.selenium/selenium-server "2.47.0"]
                 [clj-webdriver "0.7.1"]
                 [ring/ring-jetty-adapter "1.8.1"]
                 [ring/ring-core "1.8.1"]])
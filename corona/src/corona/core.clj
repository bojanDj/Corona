(ns corona.core
  (:require [com.stuartsierra.component :as component]
            [corona.server :as server]
            [corona.store :as store]
            [clojure.test :refer :all]
            [corona.config :refer :all]
            [clj-webdriver.taxi :refer :all]
            [clojure.tools.logging :refer [error]]))

(def system nil)

(defn build-system 
  "Build sistem"
  []
  (try
    (-> (component/system-map
         :server (server/make-server)
         :store (store/make-store))
        (component/system-using {:server [:store]}))
    (catch Exception e
      (error "Failed to build system" e))))

(defn init 
  "Initialize system"
  []
  (let [sys (build-system)]
    (alter-var-root #'system (constantly sys))))

(defn start
  "Start system"
  []
  (alter-var-root #'system component/start-system)
  (println "Start"))

(defn stop 
  "Stop system"
  []
  (alter-var-root #'system component/stop-system))

(defn -main
  "Main function"
  []
  (init)
  (start))

(defn test-system 
  "Test system"
  [] 
  (init)
  (start)
  system)
(ns corona.core
  (:require [com.stuartsierra.component :as component]
            [corona.server :as server]
            [corona.store :as store]
            [clojure.tools.logging :refer [error]]))

(def system nil)

(defn build-system []
  (try
    (-> (component/system-map
         :server (server/make-server)
         :store (store/make-store))
        (component/system-using {:server [:store]}))
    (catch Exception e
      (error "Failed to build system" e))))

(defn init []
  (let [sys (build-system)]
    (alter-var-root #'system (constantly sys))))

(defn start []
  (alter-var-root #'system component/start-system)
  (println "Start"))

(defn stop []
  (alter-var-root #'system component/stop-system))

(defn -main []
  (init)
  (start))

(defn test-system [] 
  (init)
  (start)
  system
)
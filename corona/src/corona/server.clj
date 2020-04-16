(ns corona.server
  (:require [com.stuartsierra.component :as component]
            [bidi.ring :refer [make-handler resources]]
            [aleph.http :as http]
            [ring.util.response :as res]
            [ring.util.request :as req]
            [ring.middleware.params :refer [wrap-params]]
            [corona.view :as view]
            [corona.store :as store]))

(defn handle-post [store request]
  (let [content (get (:form-params request) "content")
        uuid (store/insert-country-in-db store content)]
    (res/redirect (str "/" uuid) :see-other)))

(defn handle-search [request]
  (res/response (view/render-form)))

(defn search-handler [store request]
  (if (= (:request-method request) :post)
    (handle-post store request)
    (handle-search request)))

(defn news-handler [request]
  (res/response (view/news)))

(defn result-handler [store request]
  (let [paste (store/get-country-by-uuid store (:uuid (:route-params request)))]
    (res/response (view/render-result paste))))

(defn handler [store]
  (make-handler ["/" {"index" (partial news-handler)
                      "search" (partial search-handler store)
                      [:uuid] (partial result-handler store)
                      "" (resources {:prefix "public/"})}]))

(defn app
  [store]
  (-> (handler store)
      wrap-params))

(defrecord WebServer [server]

  component/Lifecycle

  (start [this]
    (assoc this :server (http/start-server (app (:store this)) {:port 8080})))

  (stop [this]
    (dissoc this :server)))

(defn make-server
  []
  (map->WebServer {}))

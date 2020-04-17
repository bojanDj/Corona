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
  (let [news1 (store/get-updates 0)
        news2 (store/get-updates 1)
        news3 (store/get-updates 2)]
    (res/response (view/news news1 news2 news3))))
;staviti u petlju!!!!!!
(defn updates-handler [request]
  (let [mapa1 (store/get-updates 0)
        mapa2 (store/get-updates 1)
        mapa3 (store/get-updates 2)
        mapa4 (store/get-updates 3)]
    (res/response (view/updates  (view/list-item (:title mapa1) (:desc mapa1) (:img mapa1) (:content mapa1))
                                 (view/list-item (:title mapa2) (:desc mapa2) (:img mapa2) (:content mapa2))
                                 (view/list-item (:title mapa3) (:desc mapa3) (:img mapa3) (:content mapa3))
                                 (view/list-item (:title mapa4) (:desc mapa4) (:img mapa4) (:content mapa4))))))

(defn result-handler [store request]
  (let [paste (store/get-country-by-uuid store (:uuid (:route-params request)))]
    (res/response (view/render-result paste))))

(defn handler [store]
  (make-handler ["/" {"index" (partial news-handler)
                      "search" (partial search-handler store)
                      "updates" (partial updates-handler)
                      [:uuid] (partial result-handler store)
                      "" (resources {:prefix "public/"})}]))

(defn app
  [store]
  (-> (handler store)
      wrap-params))

(defrecord WebServer [server]

  component/Lifecycle

  (start [this]
    (assoc this :server (http/start-server (app (:store this)) {:port 8081})))

  (stop [this]
    (dissoc this :server)))

(defn make-server
  []
  (map->WebServer {}))

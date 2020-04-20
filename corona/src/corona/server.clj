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
    (res/redirect (str "search/" uuid) :see-other)))

(defn handle-search [request]
  (res/response (view/render-form)))

(defn search-handler [store request]
  (if (= (:request-method request) :post)
    (handle-post store request)
    (handle-search request)))

(defn news-handler [request]
  (let [url "http://newsapi.org/v2/top-headlines?q=coronavirus&country=us&from=2020-03-30&apiKey=5c9694ca2b7147039cd6614c21cb361c"]
    ;(res/response (view/news (for [x (range 0 3)] (store/get-updates x url)) ))))
    (res/response (view/news (store/get-updates 0 url) (store/get-updates 1 url) (store/get-updates 2 url)))))

(defn updates-page-handler [request]
    (let [url "http://newsapi.org/v2/everything?q=coronavirus&from=2020-03-30&apiKey=5c9694ca2b7147039cd6614c21cb361c"
          page (* (- (Integer/parseInt (:page (:route-params request))) 1) 4)]
      (res/response (view/updates (for [x (range 0 4)]  (view/list-item (store/get-updates (+ page x) url)))))))

(defn result-handler [store request]
  (let [paste (store/get-country-by-uuid store (:uuid (:route-params request)))
        url (str "http://newsapi.org/v2/everything?q=coronavirus+" (:name paste) "&from=2020-03-30&apiKey=5c9694ca2b7147039cd6614c21cb361c")
        ;map1 {}
        map1 {:1 (store/get-updates 0 url) :2 (store/get-updates 1 url) :3 (store/get-updates 2 url) :4 (store/get-updates 3 url) :5 (store/get-updates 4 url) :6 (store/get-updates 5 url)}]
;    (for [x (range 0 6)]
;       (def map1 (assoc map1 (keyword (java.lang.Long/toString (+ x 1))) (store/get-updates x url))))
    (res/response (view/render-result paste map1))))

(defn handler [store]
  (make-handler ["/" {"index" (partial news-handler)
                      "search" { "" (partial search-handler store)
                                ["/" :uuid] (partial result-handler store)}
                      ["updates/" :page] (partial updates-page-handler) 
                      "" (resources {:prefix "public/"})}]))

(defn app
  [store]
  (-> (handler store)
      wrap-params))

(defrecord WebServer [server]

  component/Lifecycle

  (start [this]
    (assoc this :server (http/start-server (app (:store this)) {:port 8155})))

  (stop [this]
    (dissoc this :server)))

(defn make-server
  []
  (map->WebServer {}))

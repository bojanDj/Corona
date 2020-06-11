(ns corona.core-test
  (:require [clojure.test :refer :all]
            [ring.adapter.jetty :refer [run-jetty]]
            [clj-webdriver.taxi :refer :all]
            [corona.config :refer :all]
            [corona.core :refer :all]
            [ring.mock.request :as mock]
            [corona.server :as server]
            [corona.store :as store]            
            [corona.core :as core]
            [corona.server :refer [app]]))

(def system-for-test (core/test-system))

(deftest test-app
  (testing "index route"
    (let [store (:store system-for-test)
          response ((server/app store) (mock/request :get "/index"))]
      (is (= (:status response) 200))
      (is (string? (:body response)))))

  (testing "not-found route"
    (let [store (:store system-for-test)
          response ((server/app store) (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))

(deftest db-insert
  (testing "Find country inserted in db"
    (let [store (:store system-for-test)
          uuid (corona.store/insert-country-in-db store "serbia")
          response (corona.store/get-country-by-uuid store uuid)]
      (is (= (:name response) "Serbia")))))

(deftest open-connection
	(testing "show data 1"
	    (let [response (store/open-connection "")]
	      (is (= (:resp response) "Country name isn't well inserted.")))))

(defn stop-server [server]
  (.stop server))

(defn with-server [t]
    (t)
    (stop-server (:server system-for-test)))

(defn start-browser []
  (set-driver! {:browser :firefox}))

(defn stop-browser []
  (quit))

(defn with-browser [t]
  (start-browser)
  (t)
  (stop-browser))

(use-fixtures :once with-server with-browser)

(deftest index-page
  (to (str base-url "index"))
  (is (= (current-url) "http://localhost:8080/index"))
  (is (= (title) "CORONA"))
  (is (= (text (find-element {:id "big-title"})) "CORONA STATS")))

(deftest news
  (to (str base-url "top-news"))
  (is (= (current-url) "http://localhost:8080/top-news"))
  (is (= (title) "CORONA"))
  (is (not= (text (find-element {:class "post_title"})) ""))
  (to (str base-url "updates/1"))
  (is (not= (text (find-element {:class "post_title"})) "")))

(deftest search-page
  (to (str base-url "search"))
  (is (= (current-url) "http://localhost:8080/search")) 
  (is (= (title) "CORONA"))
  (is (= (text (find-element {:id "big-title"})) "CORONA STATS"))
  (input-text (find-element {:xpath "//input[@id='form-search']"})"serbia") 
  (click (find-element {:class "buttons"}))
  (is (= (text (find-element {:id "country-name"})) "Serbia"))
  (click (find-element {:id "home-menu"}))
  (is (= (current-url) "http://localhost:8080/index")))
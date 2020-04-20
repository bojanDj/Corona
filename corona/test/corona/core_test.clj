(ns corona.core-test
  (:require [clojure.test :refer :all]
            [corona.core :refer :all]
            [ring.mock.request :as mock]
            [corona.core :as core]
            [corona.server :as server]
            [corona.store :as store]))

(def system-for-test (core/test-system))

(deftest test-app
  (testing "index route"
    (let [store (:store system-for-test)
          response ((server/app store) (mock/request :get "/index"))]
      (is (= (:status response) 200))
      (is (string? (:body response)))
      ))

  (testing "not-found route"
    (let [store (:store system-for-test)
          response ((server/app store) (mock/request :get "/invalid"))]
      (is (= (:status response) 404))
      ))
)
(deftest db-insert
  (testing "Find country inserted in db"
    (let [store (:store system-for-test)
          uuid (corona.store/insert-country-in-db store "serbia")
          response (corona.store/get-country-by-uuid store uuid)]
      (is (= (:name response) "serbia")))))

(deftest open-connection
	(testing "show data 1"
	    (let [response (store/open-connection "")]
	      (is (= (:resp response) "Uneseno ime drzave nije validno.")))))


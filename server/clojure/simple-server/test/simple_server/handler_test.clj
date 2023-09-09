(ns simple-server.handler-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.data.json :as J]
            [ring.mock.request :as mock]
            [simple-server.handler :refer [app]]))


(defn JS
  "Helper to convert map to string"
  [s]
  (J/write-str s))


(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (println response)
      (is (= (:status response) 200))
      (is (= (:body response) (JS {:status "Ok"})))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))

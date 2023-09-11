(ns simple-server.handler
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [clj-http.client :as client]
            [hickory.core :as H]
            [hickory.select :as S]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.util.response :refer [response status]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]])
  (:gen-class))


(def status-ok "OK")


(defn build-url [page]
  (str "https://en.wikipedia.org/wiki/" page))


(defn parse-page [raw]
  (H/as-hickory (H/parse raw)))

(defn get-h1 [parsed]
  (-> (S/select (S/child (S/tag :h1))
                parsed)
      first
      :content
      first
      :content
      first))


(defn err-response [rsp]
  (status {:body {:message (:reason-phrase rsp)}}
          (:status rsp)))


(defn index []
  (response {:status status-ok}))


(defn health [req]
  (response {:status status-ok
             :datetime (str (java.time.LocalDateTime/now))}))


(defn page [page]
  (let [rsp (client/get (build-url page)
                        {:throw-exceptions false})]
    (if (= (:status rsp) 200)

      (let [parsed (parse-page (:body rsp))
            h1 (get-h1 parsed)]
        (response {:html (subs (:body rsp) 0 100)
                   :page page
                   :h1 h1
                   :status status-ok}))
      (err-response rsp))))


(defroutes app-routes
  (GET "/" [] (index))
  (GET "/health" [:as req] (health req))
  (GET "/p/:url" [url] (page url))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      wrap-json-body
      wrap-json-response
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))))

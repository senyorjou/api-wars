(ns simple-server.handler
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [clj-http.client :as client]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.util.response :refer [response status]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))


(def status-ok "Ok")


(defn build-url [page]
  (str "https://en.wikipedia.org/wiki/" page))


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
      (response {:html (subs (:body rsp) 0 100)
                 :page page
                 :status status-ok})
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

(ns simple-server.parser
  (:require [hickory.core :as H]
            [hickory.select :as S]))



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


(defn get-links [parsed]
  (->> (S/select (S/child (S/tag :a))
                 parsed)
       (map :attrs)
       (map :href)))

(defn stats [raw]
  (let [parsed (parse-page raw)]
    {:h1 (get-h1 parsed)
     :links (take 10 (get-links parsed))}))

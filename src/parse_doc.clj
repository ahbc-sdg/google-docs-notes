(ns parse-doc
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [java-time.api :as jt]))

(defn read-json-from-file [filename]
  (let [json-string (slurp (io/file filename))]
    (json/read-str json-string :key-fn keyword)))

(defn parse-date-keyword [keyword]
  (let [date-str (name keyword)
        date (jt/local-date "MMM d, yyyy" date-str)]
    (jt/format :iso-date date)))

(defn valid-date-keyword? [keyword]
  (try
    (jt/local-date "MMM d, yyyy" (name keyword))
    true
    (catch Exception _ false)))

(defn replace-date-keys [input-map]
  (reduce-kv
    (fn [acc k v]
      (if (and (instance? clojure.lang.Keyword k)
               (valid-date-keyword? k))
        (assoc acc (keyword (parse-date-keyword k)) v)
        (assoc acc k v)))
    {}
    input-map))


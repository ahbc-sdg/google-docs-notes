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


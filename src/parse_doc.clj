(ns parse-doc
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [java-time.api :as jt]))

(defn read-json-from-file [filename]
  (let [json-string (slurp (io/file filename))]
    (json/read-str json-string :key-fn keyword)))

(defn valid-date-keyword? [keyword]
  (try
    (jt/local-date "MMM d, yyyy" (name keyword))
    true
    (catch Exception _ false)))

(defn parse-date-keyword [keyword]
  (if (valid-date-keyword? keyword)
      (let [date-str (name keyword)
        date (jt/local-date "MMM d, yyyy" date-str)]
        (jt/format :iso-date date))
      keyword))

(defn process [entries index acc category date]
  "flatten the ENTRIES trees, inc index with INDEX, reduce ACC"
  (reduce
   (fn [acc entry]
     (let [parent     {:value (first entry) :index index}
           children   (rest entry)
           flat-entry {:index        (count acc)
                       :value        (:value parent)
                       :parent-index (:index parent)
                       :category     category
                       :date         date}]

       (if (empty? children)
         (conj acc flat-entry)
         (process children (count acc) (conj acc flat-entry) category date))))
   acc entries))

(defn reshape-data [input-map]
  (reduce-kv
   (fn [acc category-key category-val]
     (reduce-kv
      (fn [acc date-key entries]
         (process entries nil acc (name category-key) (parse-date-keyword date-key)))
      acc category-val))
   [] input-map))

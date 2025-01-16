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

(defn process [entries index acc]
  "flatten the ENTRIES trees, inc index with INDEX, reduce ACC"
  (reduce
   (fn [acc entry]
     (let [parent     {:value (first entry) :index index}
           children   (rest entry)
           flat-entry {:index        (count acc)
                       :value        (:value parent)
                       :parent-index (:index parent)}]

       (if (empty? children)
         (conj acc flat-entry)
         (process children (count acc) (conj acc flat-entry)))))
   acc entries))

(defn reshape-data [input-map]
  (reduce-kv
   (fn [acc category-key category-val]
     (reduce-kv
      (fn [acc date-key entries]
        (map #(assoc (assoc % :category (name category-key)) :date date-key)
             (process entries nil acc)))
      acc category-val))
   [] input-map))

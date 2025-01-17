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

(defn replace-date-keys [input-map]
  (reduce-kv
   (fn [acc k v]
     (if (and (instance? clojure.lang.Keyword k)
              (valid-date-keyword? k))
       (assoc acc (keyword (parse-date-keyword k)) v)
       (assoc acc k v)))
   {}
   input-map))
(defn update-map-entries[m e]
     (reduce-kv (fn [r k v] (assoc  r k v))  m e))

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
(defn update-map-entries [m e]
  (reduce-kv (fn [r k v] (assoc  r k v))  m e))
(defn reshape-data [input-map]
  (reduce-kv
   (fn [acc category-key category-val]
     (reduce-kv
      (fn [acc date-key entries]
        (map
         #(update-map-entries %
                              {:category (name category-key)
                               :date     (parse-date-keyword date-key)})
         (process entries nil [])))
      ) acc category-val)
   [] input-map))

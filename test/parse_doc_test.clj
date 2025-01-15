(ns parse-doc-test
  (:require [clojure.test :refer :all]
            [parse-doc :refer :all]))

(deftest test-parse-date-keyword
  (is (= (parse-date-keyword (keyword "Sep 5, 2023")) "2023-09-05"))
  (is (= (parse-date-keyword (keyword "Sep 23, 2023")) "2023-09-23"))
  (is (= (parse-date-keyword (keyword "Dec 31, 2023")) "2023-12-31"))
  (is (= (parse-date-keyword (keyword "Jan 1, 2024")) "2024-01-01")))

(run-tests)

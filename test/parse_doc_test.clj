(ns parse-doc-test
  (:require [clojure.test :refer :all]
            [parse-doc :refer :all]))

(deftest test-parse-date-keyword
  (is (= (parse-date-keyword (keyword "Sep 5, 2023")) "2023-09-05"))
  (is (= (parse-date-keyword (keyword "Sep 23, 2023")) "2023-09-23"))
  (is (= (parse-date-keyword (keyword "Dec 31, 2023")) "2023-12-31"))
  (is (= (parse-date-keyword (keyword "Jan 1, 2024")) "2024-01-01")))

(deftest test-valid-date-keyword
  (is (= (valid-date-keyword? (keyword "Sep 5, 2023")) true))
  (is (= (valid-date-keyword? :nope) false)))

(deftest test-replace-date-keys
  (let [test-map {(keyword "Sep 5, 2023") "Event A"
                  (keyword "Not a Date") "Event B"
                  (keyword "Oct 1, 2023") "Event C"}]
    (is (= (replace-date-keys test-map)
           {:2023-09-05 "Event A"
            (keyword "Not a Date") "Event B"
            :2023-10-01 "Event C"}))))

(deftest test-reshape-data
  (let [test-input-map
        {:stackoverflow
         {:20250116
          [["Today I read the article Audio Nonlinear Modeling through Hyperbolic Tangent Functionals (Adalberto Schuck Jr., Bardo Ernst Josef Bodmann) and noticed a footnote attached to each author stating, \"This work was supported by author own support\". I've never seen this in a paper before and am curious what it means. Does it imply the paper wasn't supported by the associated university, i.e. produced in the authors' spare time without university resources? – cvpines asked 2 days ago"
            ["The sentence is not written in a way someone whose principal language is English would have written it, and so there is a level of ambiguity in this statement that you can probably only resolve by asking the authors directly.\nBut your suggestion that the work was done in the authors' spare time and without university resources is certainly a reasonable reading. It is what I would have assumed as well."
             ["Thanks, and good point! Sometimes I forget that I can just reach out to authors directly.  – cvpines Commented 2 days ago"]
             ["It feels like someone filling in a form. \"This work was supported by:\" as a prompt, and the answer \"author own support\" where it would normally be \"funding body under grant xxx\" – Chris H Commented 2 days ago"]]]]}}
        test-output-map
        [{:index 0,
          :value
          "Today I read the article Audio Nonlinear Modeling through Hyperbolic Tangent Functionals (Adalberto Schuck Jr., Bardo Ernst Josef Bodmann) and noticed a footnote attached to each author stating, \"This work was supported by author own support\". I've never seen this in a paper before and am curious what it means. Does it imply the paper wasn't supported by the associated university, i.e. produced in the authors' spare time without university resources? – cvpines asked 2 days ago",
          :parent-index nil,
          :category "stackoverflow",
          :date :20250116}
         {:index 1,
          :value
          "The sentence is not written in a way someone whose principal language is English would have written it, and so there is a level of ambiguity in this statement that you can probably only resolve by asking the authors directly.\nBut your suggestion that the work was done in the authors' spare time and without university resources is certainly a reasonable reading. It is what I would have assumed as well.",
          :parent-index 0,
          :category "stackoverflow",
          :date :20250116}
         {:index 2,
          :value
          "Thanks, and good point! Sometimes I forget that I can just reach out to authors directly.  – cvpines Commented 2 days ago",
          :parent-index 1,
          :category "stackoverflow",
          :date :20250116}
         {:index 3,
          :value
          "It feels like someone filling in a form. \"This work was supported by:\" as a prompt, and the answer \"author own support\" where it would normally be \"funding body under grant xxx\" – Chris H Commented 2 days ago",
          :parent-index 1,
          :category "stackoverflow",
          :date :20250116}]]
    (is (= (reshape-data test-input-map) test-output-map))))

(run-tests)

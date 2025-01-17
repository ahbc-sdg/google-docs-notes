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


(deftest test-reshape-data
  (let [test-input-map
        {:stackoverflow
         {:20250115
          [["If I'm writing a paper to prove Theorem A and I need a specific lemma that I found out to be already proven as Theorem B in some source, should I cite it as a Theorem (as it was originally proposed) or can I cite it as a Lemma as I only need it as an intermediate step?"
            ["I suggest that you characterize the result as it was in the original paper. While I could probably finesse it otherwise, many would find it strange.\nIf you need the Fundamental Theorem of Calculus in proving some new result, I doubt that referring to it as the Fundamental Lemma of Calculus would seem fine to most (any?) readers.\nAn exception might be made for some very minor result that was labeled a theorem elsewhere, and your citation should make things clear in any case, there is nothing lost by keeping the prior terminology."]]]
          :20250116
          [["Today I read the article Audio Nonlinear Modeling through Hyperbolic Tangent Functionals (Adalberto Schuck Jr., Bardo Ernst Josef Bodmann) and noticed a footnote attached to each author stating, \"This work was supported by author own support\". I've never seen this in a paper before and am curious what it means. Does it imply the paper wasn't supported by the associated university, i.e. produced in the authors' spare time without university resources? – cvpines asked 2 days ago"
            ["The sentence is not written in a way someone whose principal language is English would have written it, and so there is a level of ambiguity in this statement that you can probably only resolve by asking the authors directly.\nBut your suggestion that the work was done in the authors' spare time and without university resources is certainly a reasonable reading. It is what I would have assumed as well."
             ["Thanks, and good point! Sometimes I forget that I can just reach out to authors directly.  – cvpines Commented 2 days ago"]
             ["It feels like someone filling in a form. \"This work was supported by:\" as a prompt, and the answer \"author own support\" where it would normally be \"funding body under grant xxx\" – Chris H Commented 2 days ago"]]]]}}
        test-output-map
        [{:index 0,
          :value
          "If I'm writing a paper to prove Theorem A and I need a specific lemma that I found out to be already proven as Theorem B in some source, should I cite it as a Theorem (as it was originally proposed) or can I cite it as a Lemma as I only need it as an intermediate step?",
          :parent-index nil,
          :category "stackoverflow",
          :date :20250115}
         {:index 1,
          :value
          "I suggest that you characterize the result as it was in the original paper. While I could probably finesse it otherwise, many would find it strange.\nIf you need the Fundamental Theorem of Calculus in proving some new result, I doubt that referring to it as the Fundamental Lemma of Calculus would seem fine to most (any?) readers.\nAn exception might be made for some very minor result that was labeled a theorem elsewhere, and your citation should make things clear in any case, there is nothing lost by keeping the prior terminology.",
          :parent-index 0,
          :category "stackoverflow",
          :date :20250115}
         {:index 2,
          :value
          "Today I read the article Audio Nonlinear Modeling through Hyperbolic Tangent Functionals (Adalberto Schuck Jr., Bardo Ernst Josef Bodmann) and noticed a footnote attached to each author stating, \"This work was supported by author own support\". I've never seen this in a paper before and am curious what it means. Does it imply the paper wasn't supported by the associated university, i.e. produced in the authors' spare time without university resources? – cvpines asked 2 days ago",
          :parent-index nil,
          :category "stackoverflow",
          :date :20250116}
         {:index 3,
          :value
          "The sentence is not written in a way someone whose principal language is English would have written it, and so there is a level of ambiguity in this statement that you can probably only resolve by asking the authors directly.\nBut your suggestion that the work was done in the authors' spare time and without university resources is certainly a reasonable reading. It is what I would have assumed as well.",
          :parent-index 2,
          :category "stackoverflow",
          :date :20250116}
         {:index 4,
          :value
          "Thanks, and good point! Sometimes I forget that I can just reach out to authors directly.  – cvpines Commented 2 days ago",
          :parent-index 3,
          :category "stackoverflow",
          :date :20250116}
         {:index 5,
          :value
          "It feels like someone filling in a form. \"This work was supported by:\" as a prompt, and the answer \"author own support\" where it would normally be \"funding body under grant xxx\" – Chris H Commented 2 days ago",
          :parent-index 3,
          :category "stackoverflow",
          :date :20250116}]]
        (is (= (reshape-data test-input-map) test-output-map))))

  (run-tests)

(ns insta-parser-practice.core
  (:require [instaparse.core :as insta]))

(def as-and-bs
  (insta/parser
    "S = NUMBER add NUMBER | NUMBER sub NUMBER
     NUMBER = #'[0-9]+'
     add = <'+'>
     sub = <'-'>"))

(def arithmetic
  (insta/parser
    "expr =  <opening-parenthesis> add-sub <closing-parenthesis>
     <add-sub> = mul-div | add | sub
     add = add-sub <'+'> mul-div
     sub = add-sub <'-'> mul-div
     <mul-div> = term | mul | div
     mul = mul-div <'*'> term
     div = mul-div <'/'> term
     <term> = number | <'('> add-sub <')'>
     opening-parenthesis = '('
     closing-parenthesis = ')'
     number = #'[0-9]+'"))


(defn choose-operator [op]
  (case op
    "+" +
    "-" -
    "*" *
    "/" /))

(def transform-options
  {:number read-string
   :vector (comp vec list)
   :operator choose-operator
   :operation apply
   :expr identity})

(def evaluate-arithmetic-expression
  {:add +, :sub -, :mul *, :div /,
   :number clojure.edn/read-string :expr identity})

(defn parse [input]
  (->> (arithmetic input)
       (evaluate-arithmetic-expression)))
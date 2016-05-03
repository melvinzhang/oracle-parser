(ns oracle-parser.core
  (:require [instaparse.core :as insta])
  (:gen-class))

(def parser (insta/parser "grammar.bnf"))

(defn -main
  "Parse lines from stdin based on grammar"
  [& args]
  (let [lines (line-seq (java.io.BufferedReader. (clojure.java.io/reader (first args))))]
    (dorun (map #(do (println "line :" %) (println "parse:"(parser %))) lines))))

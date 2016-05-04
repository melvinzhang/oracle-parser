(ns oracle-parser.core
  (:require [instaparse.core :as insta])
  (:gen-class))

(def parser (insta/parser "grammar.bnf" :string-ci true))

(defn parse-file [reader]
  (let [lines (line-seq (java.io.BufferedReader. reader))]
    (doseq [line lines]
      (let [parsed (parser line)]
        (if (insta/failure? parsed)
          (println "fail :" line)
          (do
            (println "parse:" line)
            (println "tree :" (parser line))))))))

(defn -main
  "Parse lines from stdin based on grammar"
  [& args]
  (with-open [r (clojure.java.io/reader (first args))]
    (parse-file r)))

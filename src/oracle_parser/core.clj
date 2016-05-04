(ns oracle-parser.core
  (:require [instaparse.core :as insta])
  (:gen-class))

(def parser (insta/parser "grammar.bnf" :string-ci true))

(defn parse-file [reader]
  (let [lines (line-seq (java.io.BufferedReader. reader))]
    (doseq [line lines]
      (let [parsed (parser line)]
        (if (insta/failure? parsed)
          (binding  [*out* *err*]
            (println "fail :" line)
            (println "error:" parsed))
          (do
            (println "parse:" line)
            (println "tree :" parsed)))))))

(defn -main
  "Parse lines from stdin based on grammar"
  [& args]
  (with-open [r (clojure.java.io/reader (first args))]
    (parse-file r)))

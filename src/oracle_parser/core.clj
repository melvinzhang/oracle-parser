(ns oracle-parser.core
  (:require [instaparse.core :as insta]
            [cheshire.core :as json]
            [clojure.string :as string])
  (:gen-class))

(def parser (insta/parser "grammar.bnf" :string-ci true))

(defn parse-line [line]
  (let [parsed (parser line)]
    (if (insta/failure? parsed)
      (binding  [*out* *err*]
        (println "fail :" line)
        (println "error:" parsed))
      (do
        (println "parse:" line)
        (println "tree :" parsed)))))

(defn parse-file [reader]
  (let [lines (line-seq (java.io.BufferedReader. reader))]
    (doseq [line lines] (parse-line line))))

(defn print-rules [file]
  (let [json (json/parse-string (slurp file))]
    (doseq [[name data] json]
      #_(println "==" name "==")
      (let [text (get data "text" "")
            parts (-> text
             (string/replace name "@")
             (string/replace #" \([^\)]*\)" "")
             (string/replace "—\n" "— ")
             (string/replace "\n•" " •")
             (string/split #"\n"))]
        (doseq [rule (filter #(> (.length %) 1) parts)]
          (println rule))))))

(defn -main
  "Parse lines from stdin based on grammar"
  [& args]
  (let [arg (first args)]
    (if (.endsWith arg "json")
      (print-rules arg)
      (with-open [r (clojure.java.io/reader arg)]
        (parse-file r)))))

(ns oracle-parser.core
  (:require [instaparse.core :as insta]
            [cheshire.core :as json]
            [clojure.core.async :refer (to-chan chan <!! pipeline)]
            [clojure.string :as string])
  (:gen-class))

(def parser (insta/parser "grammar.bnf" :string-ci true))

(defn output-parsed [{:keys [line parsed]}]
  (if (insta/failure? parsed)
    (binding  [*out* *err*]
      (println "fail :" line)
      #_(println "error:" parsed))
    (do
      (println "parse:" line)
      (println "tree :" parsed))))

(defn parse-file [file]
  (let [lines (string/split-lines (slurp file))
        trans (fn [l] {:line l :parsed (parser l)})]
    #_(doall (pmap (comp output-parsed trans) lines))
    (doseq [line lines] (output-parsed (trans line)))
    #_(let [out (chan 128)] 
      (pipeline 3 out (map trans) (to-chan lines))
      (loop [v (<!! out)]
        (when v
          (output-parsed v)
          (recur (<!! out)))))))

(defn print-rules [file]
  (let [json (json/parse-string (slurp file))]
    (doseq [[name data] json]
      #_(println "==" name "==")
      (let [text (get data "text" "")
            parts (-> text
             (string/replace name "@")
             (string/replace (first (string/split name #",")) "@")
             (string/replace #" \([^\)]*\)" "")
             (string/replace #"\([^\)]*\)\n" "")
             (string/replace #"\([^\)]*\)" "")
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
      (parse-file arg))))

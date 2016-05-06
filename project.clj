(defproject oracle-parser "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
    [instaparse "1.4.2"]
    [cheshire  "5.6.1"]
    [org.clojure/clojure "1.5.1"]]
  :main ^:skip-aot oracle-parser.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

(defproject collectov "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [twitter-api "0.7.5"]
                 [clojurewerkz/quartzite "1.2.0"]]
  :main ^:skip-aot collectov.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

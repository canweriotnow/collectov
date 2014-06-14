(defproject collectov "0.1.0-SNAPSHOT"
  :description "A Hive-Mind Markov chain-based Twitter bot"
  :url "http://decomplecting.org/collectov"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [twitter-api "0.7.5"]
                 [schejulure "0.1.4"]]
  :main ^:skip-aot collectov.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

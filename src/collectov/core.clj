(ns collectov.core
  (:require [collectov.markov :as m]
            [collectov.twitter :as tw]
            [schejulure.core :refer :all])
  (:gen-class))

(defn- tweet []
  (tw/send-tweet (m/tweetify)))

(defn -main
  "Just gonna tweet!"
  [& args]
  (schedule {:minute [0 20 30 45]} tweet))

(ns collectov.twitter
  (:require [clojure.string :as st]
            [clojure.edn :as edn]
            [twitter.oauth :as oauth]
            [twitter.callbacks :refer :all]
            [twitter.callbacks.handlers :refer :all]
            [twitter.api.restful :as t]))

(def config
  (edn/read-string (slurp "resources/config.edn")))



(def creds
  (oauth/make-oauth-creds
   (vals (get-in config [:config :twitter-oauth]))))

(defonce last-tweet (atom {:text nil}))

(defn get-timeline [& cnt]
  (let [timeline (t/statuses-home-timeline :oauth-creds creds :params {:exclude-replies true :count 40})]
  (map :text (:body timeline))))

(defn build-corpus []
  (st/join " "
   (filter
    #(not
      (or
       (re-matches #"@\S+" %)
       (re-matches #"http\S+" %)
       (re-matches #"RT" %)))
    (st/split (st/join " " (get-timeline)) #" "))))

(defn send-tweet [tweet]
  (if-not (identical? tweet (:text @last-tweet))
    (try
      (do
        (t/statuses-update :oauth-creds creds :params {:status tweet})
        (swap! last-tweet #(assoc % :text tweet)))
    (catch Exception e (str "caught exception: " (.getMessage e))))))



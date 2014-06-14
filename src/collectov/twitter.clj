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
  (apply oauth/make-oauth-creds
   (vals (get-in config [:config :twitter-oauth]))))

(def users
  (get-in config [:config :twitter-users]))

(defonce last-tweet (atom {:text nil}))

(defn get-timeline [& cnt]
  (let [timeline (t/statuses-home-timeline :oauth-creds creds :params {:exclude-replies true :count 40})]
  (map :text (:body timeline))))

(defn user-tweets [user & cnt]
  (let [tweets (t/statuses-user-timeline
                :oauth-creds creds
                :params {:screen-name user
                         :trim-user true
                         :exclude-replies true
                         :include-rts false
                         :count 7})]
    (map :text (:body tweets))))





(defn fetch-tweets []
  (do
    (flatten (map user-tweets users))))


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



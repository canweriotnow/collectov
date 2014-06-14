(ns collectov.twitter
  (:require [clojure.string :as st]
            [clojure.edn :as edn]
            [twitter.oauth :as oauth]
            [twitter.callbacks :refer :all]
            [twitter.callbacks.handlers :refer :all]
            [twitter.api.restful :as t]))

(defonce config
  (edn/read-string (slurp "resources/config.edn")))



(def creds
  (apply oauth/make-oauth-creds
   (vals (get-in config [:config :twitter-oauth]))))

(def users
  (get-in config [:config :twitter-users]))

(defonce last-tweet (atom {:text nil}))


(defn user-tweets
  "Get the last n tweets for the specified user.
   Default is 7. TODO: make this configurable."
  [user & cnt]
  (let [tweets (t/statuses-user-timeline
                :oauth-creds creds
                :params {:screen-name user
                         :trim-user true
                         :exclude-replies true
                         :include-rts false
                         :count 7})]
    (map :text (:body tweets))))





(defn fetch-tweets
  "Get the tweets for all the users."
  []
  (do
    (shuffle (flatten (map user-tweets users)))))


(defn build-corpus
  "Uses retrieved tweets to build a corpus for the Markov
   chain function. Filters out links, usernames, and 'RT'
   just b/c it's annoying and tends to break up otherwise
   hilarious bodies of text."
  []
  (st/join " "
   (filter
    #(not
      (or
       (re-matches #"@\S+" %)
       (re-matches #"http\S+" %)
       (re-matches #"RT" %)))
    (st/split (st/join " " (fetch-tweets)) #" "))))

(defn send-tweet [tweet]
  (if-not (identical? tweet (:text @last-tweet))
    (try
      (do
        (t/statuses-update :oauth-creds creds :params {:status tweet})
        (swap! last-tweet #(assoc % :text tweet)))
    (catch Exception e
      (println
       (str "caught exception: " (.getMessage e)))))))



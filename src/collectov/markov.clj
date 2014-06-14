(ns collectov.markov
  (:require [clojure.string]
            [collectov.twitter :as tw]))

;; Markov chain generation
;; Borrowed with thanks from https://github.com/dbasch/markov

(defn corpus []
  (str (tw/build-corpus)))


(defn markov-data [text]
  (let [maps
        (for [line (clojure.string/split text #"\.")
              m (let [l (str line ".")
                      words
                      (cons :start (clojure.string/split l #"\s+"))]
                  (for [p (partition 2 1 (remove #(= "" %) words))]
                    {(first p) [(second p)]}))]
          m)]
    (apply merge-with concat maps)))

(defn sentence [data]
  (loop [ws (data :start)
         acc []]
    (let [w (rand-nth ws)
          nws (data w)
          nacc (concat acc [w])]
      (if (= \. (last w))
        (clojure.string/join " " nacc)
        (recur nws nacc)))))

;; TODO Re-write the generate fn to create tweet-friendly phrases.
(defn generate []
  (let [d (markov-data (corpus))]
    (clojure.string/join " " (take 6 (repeatedly #(sentence d))))))

(defn tweetify []
  (apply str (take 140 (generate))))





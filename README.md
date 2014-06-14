# Collectov

A Hive-Mind Twitter Bot


## Usage

Collectov will combine recent tweets from multiple Twitter users to create
a corpus for Markov chain substitution. It then tweets the generated text
at a fixed interval.

You'll need to clone the repo, and copy `resources/config.edn.example` to
`resources/config.edn`. Fill in the requisite Twitter credentials, and populate
a vector with the usernames of Twitter uers you want to draw upon for your bot.

The Markov chain substitution functions are the "naive" examples borrowed from
[this post](http://diegobasch.com/fun-with-markov-chains-and-clojure) by Diego Basch, so I wouldn't go for anything to big until that part is refined.

Other than that, once you've built your `config.edn`, nothing should be needed
beyond running `lein run` in the project directory. Packaging a jar and deploying
as a service are on the TODO list.

## Options

Um, just the config for now.

## Examples

The current proof-of-concept bot is [AnEstuary Ebooks](https://twitter.com/ae_ebooks), a bot based off of the Twitter activity of my co-workers at [An Estuary](http://anestuary.com).

### Bugs

All of the bugs. All of them.


## License

Copyright © 2014 Jason Lewis

Distributed under the Eclipse Public License, the same as Clojure.

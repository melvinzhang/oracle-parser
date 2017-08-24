# Magic: The Gathering Oracle Parser

Requires [Leiningen](https://leiningen.org/) to build, run `lein uberjar` to build a standalone jar file.

Uses [instaparse](https://github.com/Engelberg/instaparse) to generate the parser from [grammar](https://github.com/melvinzhang/oracle-parser/blob/master/grammar.bnf) in BNF.

The goal is to generate parsed oracle text to be used by [Magarena](https://magarena.github.io/).

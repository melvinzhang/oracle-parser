AllCards.json:
	wget http://mtgjson.com/json/AllCards.json.zip && unzip AllCards.json.zip && rm AllCards.json.zip

rules.txt: target/uberjar/oracle-parser-0.1.0-SNAPSHOT-standalone.jar AllCards.json
	java -jar $^ | sort | uniq > $@

rules.out: target/uberjar/oracle-parser-0.1.0-SNAPSHOT-standalone.jar rules.txt
	java -jar $^ > rules.out 2> rules.err

parsable.txt: grammar.bnf rules.out
	cat rules.out | grep "parse:" | sed 's/parse: //' | sort > $@
	cat rules.err | grep "fail :" | sed 's/fail : //' | sort > failed.txt

parsable.new: grammar.bnf
	mv parsable.txt parsable.old
	make parsable.txt
	diff parsable.old parsable.txt

CounterType:
	grep -o "[^ ]* counter \(on\|from\)" rules.txt | cut -d' ' -f1 | sort | uniq > $@
	# remove a, each, that
	# add poison

test:
	lein run parsable.txt

target/uberjar/oracle-parser-0.1.0-SNAPSHOT-standalone.jar: src/oracle_parser/core.clj
	lein uberjar

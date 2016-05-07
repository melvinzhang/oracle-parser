test:
	lein run parsable.txt

target/uberjar/oracle-parser-0.1.0-SNAPSHOT-standalone.jar: src/oracle_parser/core.clj
	lein uberjar

test_all: target/uberjar/oracle-parser-0.1.0-SNAPSHOT-standalone.jar
	java -jar $^ rules.txt

parsable.txt: grammar.bnf
	make test_all > test_all.out 2> test_all.err
	cat test_all.out | grep "parse:" | sed 's/parse: //' | sort > $@
	cat test_all.err | grep "fail :" | sed 's/fail : //' | sort > failed.txt

parsable.new: grammar.bnf
	mv parsable.txt parsable.old
	make parsable.txt
	diff parsable.old parsable.txt

CounterType:
	grep -o "[^ ]* counter \(on\|from\)" rules.txt | cut -d' ' -f1 | sort | uniq > $@
	# remove a, each, that
	# add poison

rules.txt:
	lein run AllCards.json | sort | uniq > $@

AllCards.json:
	wget http://mtgjson.com/json/AllCards.json.zip && unzip AllCards.json.zip && rm AllCards.json.zip

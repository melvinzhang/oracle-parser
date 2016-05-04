test:
	lein run parsable.txt

test_all:
	lein run rules.txt

parsable.txt: grammar.bnf
	make test_all > test_all.out 2> test_all.err
	cat test_all.out | grep "parse:" | sed 's/parse: //' | sort | uniq > $@
	cat test_all.err | grep "fail :" | sort | uniq -c | sort -n > failed.txt

parsable.new: grammar.bnf
	mv parsable.txt parsable.old
	make parsable.txt
	diff parsable.old parsable.txt

CounterType:
	grep -o "[^ ]* counter \(on\|from\)" rules.txt | cut -d' ' -f1 | sort | uniq > $@
	# remove a, each, that
	# add poison

text:
	jq "values[].text" AllCards.json
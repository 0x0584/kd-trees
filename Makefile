CC		= javac-algs4
RUN 	= java-algs4 -Xmx1g
INPUT 	?= < input8.txt

%: %.java
	@rm -f $(addsuffix $@, .class)
	$(CC) $^
	$(RUN) $@ $(INPUT)

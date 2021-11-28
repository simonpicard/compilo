JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.PHONY: test

CLASSES = core/Main.java

default: classes

classes:
	find . -name "*.java" > sources.txt
	javac $(JFLAGS) @sources.txt
	rm sources.txt

test:
	java $(CLASSES:.java=) Main test/test.il

clean:
	find . -name "*.class" > classes.txt
	xargs rm < classes.txt
	rm classes.txt
#!/bin/sh
java -cp .:JFlex.jar JFlex.Main LexicalAnalyzer.flex
javac *.java

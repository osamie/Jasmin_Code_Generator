

all:
	./sscc --prefix CMM cmm.t cmm.g
	javac *.java

run:	all
	java CMM


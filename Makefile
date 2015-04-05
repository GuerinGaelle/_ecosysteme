JAVAC = javac -g
JAVA = java
DEBUG = jdb

SRC = $(shell ls *.java)

OBJ = $(SRC:.java=.class)

all : MyEcosystem_predprey.class

MyEcosystem_predprey.class : $(SRC)
	$(JAVAC) $(SRC)

clean :
	rm -f *.class

run :
	-$(JAVA) MyEcosystem_predprey

debug :
	-$(DEBUG) MyEcosystem_predprey

.PHONY : all clean run

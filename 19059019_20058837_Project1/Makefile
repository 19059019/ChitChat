JC = javac

.SUFFIXES: .java .class
.java.class:
	$(JC) $*.java

CLASSES = \
	chitchatapp/clientInstance.java\
	chitchatapp/Server.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) chitchatapp/*.class

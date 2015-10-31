JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java


CLASSES = \
    Client.java \
    ClientRecieve.java \
    ClientSend.java \
    Server.java \
    HandleClient.java \
    HandleMessage.java \
    UserMessage.java \
    User.java \
    TimeBlock.java \
    TimeNoActive.java \
    TimeLastHour.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
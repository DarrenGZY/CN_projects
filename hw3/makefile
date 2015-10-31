JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java


CLASSES = \
    bfclient.java \
    bfclientSend.java \
    bfclientReceive.java \
    SendTimeTask.java \
    RoutingTable.java \
    NeighborList.java \
    ReceiveTimeTask.java \
    NeighborVectors.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
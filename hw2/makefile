JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java


CLASSES = \
    Sender.java \
    Receiver.java \
    DataSegment.java \
    TimeOut.java \
    Checker.java \
    Translator.java \
    LogFile.java \
    SendCount.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
########################
Authour: Zhiyuan Guo
UNI: zg2201
DATA: 2014-11-6
Content: Readme.txt for simple realization 
of TCP_like protocal based on udp transmission
#######################

#######################
1.About the code
#######################
This project includes 7 java files, here are simple introduction to these files:

Sender.java: Sender gets data from file, then translate them into byte array. Create headers for these data segments and add 
them into segment to send.

Receiver.java: Receiver receives data segments from udp link. Examine the some informations in headers and recompute the checksum. If recieving right segment, it will send a header containing acknowledgement number to sender on tcp.

DataSegment.java: It is a class help get specific length of string. SEGMENT_SIZE denotes the length of string.

Timeout.java: It solves the problem that packets need to resend after a period of time receiving no acknowledgement in sender.

Checker.java: It is used to compute the checksum for sender and receiver.

Translator.java: It is used as a tool to translate bewteen byte array and int or short numbers.

LogFile.java: It help to write into log files in formatted pattern

SendCount.java: Count the segments sent and resent. 
#######################
2.Details on develop environment
#######################
java version "1.8.0_20"
Java(TM) SE Runtime Environment (build 1.8.0_20-b26)

#######################
3.To run the code
#######################
Type in the terminal:

First Run newudpl like this:
./newudpl -o 127.0.0.1:8000 -i 127.0.0.1:4000 -p5000:6000 -L 20 -B 50000 -O 20

For Server:
java Server filename proxy_ip proxy_port sender_port logfile
java Sender data.txt 127.0.0.1 5000 4000 sendlog.txt

For Client:
java Client filename receiver_port sender_ip sender_port logfile
java Receiver result.txt 8000 127.0.0.1 4000 receivelog.txt

#######################
4.Important notification
#######################
For the input file, we only treat standart ASCII char, which we treats a single char as one byte. We have ignored those situation that a single char equals two bytes.

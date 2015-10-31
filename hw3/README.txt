########################
Authour: Zhiyuan Guo
UNI: zg2201
DATA: 2014-12-11
Content: Readme.txt for Bellman-Ford Algorithm
#######################


######################
1.About the code
######################
The project contains 8 java files, here are simple description of them:

bfclient.java: Works as user interface for client, providing SHOWRT, LINKDOWN, LINKUP, CLOSE commands

bfclientReceive.java: A Thread for receiving UDP packets from neighbors, and update its own routing table.

ReceiveTimeTask.java: A Task for timer to count down 3*TIMEOUT

bfclientSend.java: A Thread for sending UDP packets to neighbors.

SendTimeTask.java: A Task for timer to count down TIMEOUT, and send its packets

RoutingTable.java: Store its own routing vectors.

NeighborList.java: Store its neignbors.

NeighborVectors.java: Store its neighbors’ routing vectors.


#######################
2.Details on develop environment
#######################
java version "1.8.0_20"
Java(TM) SE Runtime Environment (build 1.8.0_20-b26)


#######################
3.To run the code
#######################
Type in the terminal:

For Client:
java bfclient Port_num cost IP_number Port_number cost …

#######################
4.Implementation of bellman-ford
######################
Every client maintain its own distance vector and its received distance vectors from its neighbors.
Whenever it receives a new packets, it updates its own distance vector, if there is any change, send updates to its neighbors at once.
When sending packets to neighbors, the message is like this in a byte array:
0-3 bits : total size of its distance vector
4-11 bits : cost(double) from itself to the sending neighbor
12-27 bits : destination ip address(4) + destination port(4) + cost(8)
every 12bits store like this

##############################
5. Command realized in program
##############################
1. Command”SHOWRT”
Show the distance vector of this client

2. Command”LINKDOWN”
Break a link and all clients update their vectors.

3. Command”LINKUP”
Restore a link to its value and all clients update their vectors

4. Command”CLOSE”
Successfully close and all its links break.



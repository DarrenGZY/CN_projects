########################
Authour: Zhiyuan Guo
UNI: zg2201
DATA: 2014-10-2
Content: Readme.txt for simple chatroom 
realized by client and server structure
#######################


######################
1.About the code
######################
The project contains 11 java files, here are simple description of them:

Client.java: Create a Client service . Connect with Server and each Client handles two thread ClientRecieve and ClientSend

ClientRecieve.java: Create a class extends runnable, responsible for receiving messages from server.

ClientSend.java: Create a class extends runnable, responsible for sending messages to server.

Server.java: Create a Server service. Connect Clients and allocate each client a new thread to serve

HandleClients.java: Responsible for login and commands of clients. Each client will be allocated one thread of this

HandleMessage.java: Responsible for passing message among different clients. It listens for if there are messages for this client. 

TimeBlock.java: Count time for wrong key. BLOCK_TIME is defined here.

TimeLastHour.java: Count time for last hour login. LAST_HOUR is defined here.

TimeNoActive.java: Count time for inactivity. TIME_OUT is defined here.

User.java: Contain most of information of users.

UserMessage.java: Store messages passing among clients.


#######################
2.Details on develop environment
#######################
java version "1.8.0_20"
Java(TM) SE Runtime Environment (build 1.8.0_20-b26)



#######################
3.To run the code
#######################
Type in the terminal:

For Server:
java Server (port_number)

For Client:
java Client IP_number Port_number

#######################
4.Important variable 
######################
1. BLOCK_TIME
Defined in TimeBlock.java
Decide the time period block for user

2. TIME_OUT
Defined in TimeNoActive.java
Decide the time period for user with no action

3. LAST_HOUR
Defined in TimeLastHour.java
Decide the time period to record user login information


##############################
5. Command realized in program
##############################
1. Successfully login:
Input username and key

if username is not right, it will continue ask you to input
when username is right, user has 3 times opportunities to input key. If 3 times up, the user will be block for BLOCK_TIME period

2. Command”whoelse”
To see what other users are in chatroom

3. Command”wholasthr”
To see what users have logined in LAST_HOUR period

4. Command”broadcast”
usage: broadcast message_tosend
all other users on chat room will see

5. Command”message”
usage: message user_name message_tosend
specific user will see the message

6. Command”logout”
user will successfully log out and some relative information of this user will be removed.

7. Control + c
For both Client side and Server side, they can both use control + c to terminate gracefully.

8. Automatically logout in TIME_OUT period
If user send nothing, he will be force to log out. And some relative information will be removed. 





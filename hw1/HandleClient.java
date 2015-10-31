import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class HandleClient implements Runnable{
    private Socket socket; //A connected socket
    private int index;     //Record which user to serve
   	private User users;	   //get and modify information of User
    private TimeBlock timer;//timer is used to calculate time and excute specify action
   	private String name;   //name of user login in this thread
   	private int size; // total number of users
    //Construct a thread
    public HandleClient(Socket socket){
    	this.socket = socket;
    	index = -1;
    	users = new User();
    	size = users.getSize();
    	timer = new TimeBlock(size);
    	
    }

    @Override
    public void run(){
    	TimeLastHour timer_lasthour = new TimeLastHour();

    	boolean login = isLogin();

    	if(login == true){
  
    		users.setLastHour(index);  //once user login, record them as login last hour
    
    		checkCommand();
    	}

    	timer_lasthour.execute(index);  // when user logout, set timer to record whether their last login was in an hour
		
		//System.out.println("finish");
			
		users.resetRecord(index);  //delete their record for command "whoelse"           


   	}

    // Check if the user successfully login
    public boolean isLogin(){
		String key;        //key of user
		int times = 3;     //Times failed login for key
		boolean nameright = false; //if
		boolean login = false;

    	
		try{	

			BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			PrintWriter toClient = new PrintWriter(socket.getOutputStream(), true);

			while(login == false){
				//Read in username, test if it is correct.
				while(index == -1){
					toClient.println("Usrname:");
					name = fromClient.readLine();
					
					// Check the username
					if((index = users.isNameRight(name)) == -1){
						toClient.println("Reply: username is not correct");
					}
					else{
						if(timer.getBlock(index)) { //detect whether user is in block
							toClient.println("You are in 60 seconds block! ");
							index = -1;
						}								
					}
				}	

				//Read in Key, test if it is correct
				while(times > 0 && login == false){
					toClient.println("Key:");
					key = fromClient.readLine();
					
					//if key is true, log in
					if(users.isKeyRight(index, key)){
						if(users.getRecord(index)){ // the user has already login
							toClient.println("This user has already login");
							times = 0;  //return to input username
							index = -1; //return to input username
						}
						else{
							login = true; //login successfully
							users.setRecord(index); //record this user
							break;
						}
						
					}
					else {
						times --;
						if(times == 0){
							toClient.println("This User will be blocked for 60 seconds ");
							timer.setBlock(index);  //set block for this user
							timer.execute(index);  // begin to count down time
							index = -1; // make it able to input username again
						}
						else{
							toClient.println("Worng Key! You hava another " + times + " opportunities " );
						}
					} 			    
				}

				if(login == false) times = 3; //reset times for key
		    }

		}catch(IOException e){
		    System.err.println(e);
		}
		return login;
	}

	public void checkCommand(){
		try{
			//Create inputstream
			BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//Create outputstream
			PrintWriter toClient = new PrintWriter(socket.getOutputStream(), true);

			toClient.println("Welcome to Chatroom! ");

			String inputLine;          //String read from Client
			String outputLine;         //String send to Client
				
			HandleMessage listener = new HandleMessage(socket, users.getName(index)); //to listen if it has message sent by other users
			new Thread(listener).start();  //Start the thread

			// Create UserMessage instance, to store and get message passing among users
			UserMessage usermessage = new UserMessage();

			// Create TimeNoActive instance, to find if user did nothing for a period time
			TimeNoActive timer_noactive = new TimeNoActive(socket);
			// Begin to detect no action
			timer_noactive.execute();

			toClient.println("> ");
			//name = users.getName(index);  // name of the current user

			//Begin to serve the clients and accept commands
			while((inputLine = fromClient.readLine()) != null){
				//System.out.println(inputLine);
					
				//Command "whoelse", to see who are now in the chatroom	
				if(inputLine.equals("whoelse")){
					//int size = users.getSize();   // get the size of the current users
					for(int j = 0; j < size; ++j){
						if(users.getRecord(j) && j != index) toClient.println(users.getName(j));
					}

					timer_noactive.reset();
					//timer_noactive.execute();

				}

				//Command "wholasthr", to see who logined in last hour
				else if(inputLine.equals("wholasthr")){
					//int size = users.getSize();
					for(int j = 0; j < size; ++j){
						if(users.getLastHour(j) && j != index) toClient.println(users.getName(j));
					}

					timer_noactive.reset();
					//timer_noactive.execute();

				}

				//Command "logout"
				else if(inputLine.equals("logout")){
					users.resetRecord(index);  //clear the record for the user
					break;
				}

				//Command "broadcast"
				else if(inputLine.length() > 9 && inputLine.substring(0,9).equals("broadcast")){
					//String name = users.getName(index);       // get the user name
					String message = inputLine.substring(9);  // get the message after broadcast
					//int size = users.getSize();  // get the size of current users
					for(int i = 0; i < size; ++i){
						if(users.getRecord(i) && i != index){
							usermessage.addMessage(message, name, users.getName(i));
						}
					}

					timer_noactive.reset();
					//timer_noactive.execute();

				}

				//Command "Private"
				else if(inputLine.length() > 7 && inputLine.substring(0,7).equals("message")){
					int i = 7;
					while(inputLine.charAt(i) == ' ' && i < inputLine.length())  // Ignore the blank " "
						i ++;

					//int size = users.getSize();
					int j = 0;
					for (j = 0; j < size; ++j){
						if(users.getRecord(j) && inputLine.indexOf(users.getName(j)) == i)	// It does not detect names like "win"			
							break;		
					}																		// and "windows", just return first find

					if(j == size){
						toClient.println("This user is not online or not exist ");
					} 
					else{
						String reciever = users.getName(j);
						String message = inputLine.substring(i+reciever.length());//message will stay after "private" and "name"
						usermessage.addMessage(message, name, reciever);
					}

					timer_noactive.reset();
					//timer_noactive.execute();

				}
				// Wrong command
				else{
					toClient.println("Wrong Command ");

					timer_noactive.reset();
					//timer_noactive.execute();
				}

				toClient.println("> ");
			}

			toClient.println("logout");  // act as a flag to stop the reciever of client 
			
			
   		} catch(IOException e){
   				System.err.println(e);
   		}
   	}
}
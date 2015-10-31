import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class Server {
    private int portnum;   // Port number for Server
    //static User users = new User(); 

    public Server(String[] args){
    	try {                      
    		
    		portnum = Integer.parseInt(args[0]);  

			// Create a server socket
			ServerSocket serverSocket = new ServerSocket(portnum);

			//Create information of users
			User users = new User(); 
			// read user information for file, store them
			users.init();  // Read the file usr_file.txt

			while(true){
				// Listen for a connection request
				Socket socket = serverSocket.accept();
				//Create task and start thread
				HandleClient task = new HandleClient(socket);

			    new Thread(task).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        new Server(args);
	}

}

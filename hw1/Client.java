import java.io.*;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

/**It is a simple Client program.
 * Client
 * @author zhiyuanguo
 *
 */
public class Client {
	public Client(String[] args) {
		String addr = args[0];    // IP address
		String port = args[1];    // Port number
		int portnum = Integer.parseInt(port);

		boolean logout = false;

		try{
			//create a Client socket with destination port number 8000.
		Socket socket = new Socket(addr, portnum);
		
		//Create thread to recieve messages for client
		ClientRecieve r = new ClientRecieve(socket);
		Thread rr = new Thread(r);
		rr.start();
		//Create thread to send messages for client
		ClientSend s = new ClientSend(socket);
		Thread ss = new Thread(s);
		ss.start();

		try{
			
			while(!r.getLogout()){
				Thread.sleep(1*1000);
				//System.out.println("in while");
			}
		
		}catch(InterruptedException e){
			System.out.println(e);
		}

		//if ClientRecieve is closed, try to close ClientSend
		ss.interrupt();

	 	} catch (IOException e){
	 		System.err.println(e);
	 	}		
	}

		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Client(args);
		//System.out.println("over");
	}

}

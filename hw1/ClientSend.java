import java.io.*;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ClientSend implements Runnable{
	private Socket socket;
	private int TIME_OUT = 1;  //TIME_OUT for a period of no action, unit is minute

	public ClientSend(Socket socket){
		this.socket = socket;
	}

	@Override
	public void run(){
		try{
			//Create output stream
			PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);

			String outputLine;
	    	//Scanner s = new Scanner(System.in);

			BufferedReader s = new BufferedReader(new InputStreamReader(System.in));

	    	//socket.setSoTimeout(TIME_OUT*60*1000);

			//while((outputLine = s.nextLine()) != null){
	    	while(true){
	    		while(!s.ready()){
	    			Thread.sleep(20);   // if there is nothing written, sleep in while loop. It also acts as recieving interrupt
	    		}
				outputLine = s.readLine();
				//outputLine = s.nextLine();
		    	toServer.println(outputLine);

		    	if(outputLine.equals("logout")){
		    		System.out.println("Closed BYE!");
		    		break;
		    	}
		    		
	
			}
		}catch(IOException e){
			System.err.println(e);
		}catch(InterruptedException ex){
			System.out.println("Closed BYE!");
		}
	}
}
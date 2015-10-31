import java.io.*;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;
import java.util.NoSuchElementException;

public class ClientRecieve implements Runnable{
	private Socket socket;
	private boolean logout;

	public ClientRecieve(Socket socket){
		this.socket = socket;
		logout = false;
	}

	public boolean getLogout(){
		return logout;
	}



	@Override
	public void run(){
		try{
			//Create Inputstream
			BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String inputLine = null;
			//Ctreate Inputstream
	    	Scanner recieve = new Scanner(socket.getInputStream());

		
			//while(recieve.hasNextLine()){
	    	while((inputLine = recieve.nextLine()) != null ){
		    	//inputLine = fromServer.readLine();
		    	//inputLine = recieve.nextLine();
		    	if(inputLine.equals("logout")) { 
		    		logout = true; 
		    		break;
		    	}
		    	
		    	if(inputLine.equals("> ")){
		    		System.out.print(inputLine);
		    	}
		    	else{
		    		System.out.println(inputLine);
		    	}	    	

		    }
		
	 	} catch(NoSuchElementException s){ // if reach TIME_OUT , recieve.nextLine() will throw this exception
	 		
	 		logout = true; 
		    
	 	}
	 	catch (IOException e){
	 		System.err.println(e);
	 	} 
	}
}
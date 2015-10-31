import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;

public class HandleMessage implements Runnable{
	private Socket socket;  //Client socket 
	private UserMessage message;  //UserMessage, to get and store message here
	private String user; //user name in accepting client

	public HandleMessage(Socket socket, String user){
		this.socket = socket;
		this.user = user;
		message = new UserMessage();
	}

	@Override
	public void run(){
		try{
			PrintWriter toClient = new PrintWriter(socket.getOutputStream(), true);

			while(true){
				if(message.isRecieved(user)){  //listen if a message to recieve
					toClient.println(message.getMessage(user));  // send the message to the client
					message.removeMessage(user);   // remove the massage that have sent
				}
			}
		}catch(IOException e){
			System.err.println(e);
		}
		
	}
}
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class bfclientUserInterface implements Runnable{
	
	@Override
	public void run(){
		try{
			BufferedReader scaner = new BufferedReader(new InputStreamReader(System.in));
			RoutingTable routingTable = new RoutingTable();
			while(true){
				String command = scaner.readLine();
				if (command.equals("SHOWRT")){
					routingTable.showTable();
				}
				else if (command.equals("CLOSE")){
					break;
				}
			}
		}catch(IOException e){
			System.err.println(e);
		}
	}
}
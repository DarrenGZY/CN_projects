import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Timer;

public class bfclient{
	public static boolean closeFlag = false;
	public static void main(String []args){
		int localport = Integer.parseInt(args[0]);
		int time_out = Integer.parseInt(args[1]);
		//System.out.println(args.length);
		RoutingTable routeTable = new RoutingTable();
		NeighborList neighborList = new NeighborList();
		Timer timer = new Timer();
		//initial routing table
		for (int i = 2; i < args.length; i = i + 3){
			routeTable.initialEntry(args[i], args[i + 1], 
				Double.parseDouble(args[i + 2]));
			neighborList.addNeighbor(args[i], args[i + 1], 
				Double.parseDouble(args[i + 2]));
			timer.schedule(new ReceiveTimeTask(args[i], args[i+1], time_out), time_out*3*1000);
		}

		//int port = Integer.parseInt(args[2]);

		try{
			DatagramSocket socket = new DatagramSocket(localport, InetAddress.getLocalHost());

			String ipadd = InetAddress.getLocalHost().getHostAddress();
			routeTable.addLocal(ipadd, args[0]);
			//System.out.println(ipadd + ":" + args[0]);

			Thread sender = new Thread(new bfclientSend(socket, time_out));
			sender.start();
			Thread receiver = new Thread(new bfclientReceive(socket, time_out));
			receiver.start();

			BufferedReader scaner = new BufferedReader(new InputStreamReader(System.in));
			while(true){
				String command = scaner.readLine();
				String[] commands = command.split(" ");
				if (commands[0].equals("SHOWRT")){
					routeTable.showTable();
					
				}
				else if (commands[0].equals("LINKDOWN")){
					neighborList.setLinkDown(commands[1], commands[2]);
					boolean flag = routeTable.updateTable();

				}
				else if (commands[0].equals("LINKUP")){
					neighborList.setLinkUp(commands[1], commands[2]);
					boolean flag = routeTable.updateTable();
				}
				else if (commands[0].equals("CLOSE")){
					closeFlag = true;
					socket.close();
					break;
				}
			}
			timer.cancel();
			//Thread userInterface = new Thread(new bfclientUserInterface());
			//userInterface.start();
		}catch(IOException e){
			System.out.println(e);
		}		
	}
}
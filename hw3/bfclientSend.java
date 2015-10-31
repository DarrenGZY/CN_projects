import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;
import java.util.Timer;
import java.net.SocketException;


public class bfclientSend implements Runnable{
	private DatagramSocket socket;
	private int timeout;
	//private int listenPort;
	private RoutingTable routeTable = new RoutingTable();
	private NeighborList neighborList = new NeighborList();

	public bfclientSend(DatagramSocket socket, int timeout){
		this.socket = socket;
		this.timeout = timeout;
	}

	@Override
	public void run(){
		
		byte[] buf = new byte[1024];
		String destination;
		String [] dest_port;
		for(int i = 0; i < neighborList.sizeofNeighbors(); ++ i){	
			if(neighborList.getFlag(i)){
				try{
				
					destination = neighborList.getNeighbor(i);
					dest_port = destination.split(":");

					InetAddress addr = InetAddress.getByName(dest_port[0]);			
					int port = Integer.parseInt(dest_port[1]);

					routeTable.tableToBytes(buf, i);

					DatagramPacket packet = new DatagramPacket(buf, buf.length, addr, port);
					socket.send(packet);
				}catch(SocketException e){
					System.out.println("close");
				}catch(UnknownHostException e){
					System.out.println("This is an unkown ip address.");
				}catch(IOException e){
					System.err.println(e);
				}
			}
		}

		neighborList.resetSendFlag();
		Timer timer = new Timer();
		timer.schedule(new SendTimeTask(), timeout * 1000);

		while(true){
			if(bfclient.closeFlag == true){
				timer.cancel();
				break;
			}

			try{
				Thread.sleep(10);
			}catch(InterruptedException e){
				System.out.println("interrupt");
			}

			if(neighborList.getSendFlag()){
				if(neighborList.sizeofNeighbors() != 0){
				
					buf = new byte[256];
					//System.out.print("neighbor size :");
					//System.out.println(neighborList.sizeofNeighbors());
					for(int i = 0; i < neighborList.sizeofNeighbors(); ++ i){	
						if(neighborList.getFlag(i)){
							try{
				
								destination = neighborList.getNeighbor(i);
								dest_port = destination.split(":");

								InetAddress addr = InetAddress.getByName(dest_port[0]);			
								int port = Integer.parseInt(dest_port[1]);

								String iii = addr.getHostAddress();
								String ppp = Integer.toString(port);
								//System.out.println("Send To: " + iii + ":" + ppp);

								routeTable.tableToBytes(buf, i);

								DatagramPacket packet = new DatagramPacket(buf, buf.length, addr, port);
								socket.send(packet);
							}catch(SocketException e){
								timer.cancel();
								break;
							}catch(UnknownHostException e){
								System.out.println("This is an unkown ip address.");
							}catch(IOException e){
								System.err.println(e);
							}
						}
					}
					timer.cancel();
					neighborList.resetSendFlag();
					timer = new Timer();
					timer.schedule(new SendTimeTask(), timeout * 1000);
				}
				else{
					timer.cancel();
					neighborList.resetSendFlag();
					timer = new Timer();
					timer.schedule(new SendTimeTask(), timeout * 1000);
				}
			}
		}
	}	

}




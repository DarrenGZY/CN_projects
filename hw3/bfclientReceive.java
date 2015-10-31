import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.net.SocketException;

public class bfclientReceive implements Runnable{
	private DatagramSocket socket;
	private int timeout;
	private RoutingTable routeTable = new RoutingTable();
	private NeighborList neighborList = new NeighborList();

	public double INFINITY = 1000;

	public bfclientReceive(DatagramSocket socket, int timeout){
		this.socket = socket;
		this.timeout = timeout;
	}

	@Override
	public void run(){
		Timer timer = new Timer();

		while(true){
			try{
				byte[] buf = new byte[1024];
				boolean flag = false;
				DatagramPacket packet = new DatagramPacket(buf, buf.length);

				socket.receive(packet);

				InetAddress ip = packet.getAddress();
				String ipaddr = ip.getHostAddress();
				String port = Integer.toString(packet.getPort());
				

				int size = toInt(buf, 0);
				double cost = toDouble(buf, 4); 

				// if it is not in neighbor list, add it
				if(neighborList.isNeighbor(ipaddr, port) == false){
					neighborList.addNeighbor(ipaddr, port, cost);
					timer.schedule(new ReceiveTimeTask(ipaddr, port, timeout), 3*timeout*1000);
					//System.out.println("Not in neighbor");
				}
				else{
					neighborList.modifyTime(ipaddr, port);
					//timer.cancel();
					//timer = new Timer();
					timer.schedule(new ReceiveTimeTask(ipaddr, port, timeout), 3*timeout*1000);
					//System.out.println("In neighbor");
				}

				//if it is not in routing table, add it
				if(!routeTable.isInTable(ipaddr, port)){
					routeTable.initialEntry(ipaddr, port, cost);
					flag = true;
					//System.out.println(1);
				}
				else{  // if it is in, update it
					int index = routeTable.getIndex(ipaddr, port);

					if(cost < routeTable.getCost(index)){
						routeTable.modifyEntry(index, ipaddr, port, cost);
						flag = true;
						//System.out.println(2);
					}
				}

				String ip_tmp;
				String port_tmp;
				double cost_tmp;

				int index = neighborList.getIndex(ipaddr, port);
				neighborList.removeEntryInVector(index);

				for(int i = 0; i < size; ++i){
					ip_tmp = getIp(buf, 12 + i*16);
					port_tmp = getPort(buf, 16 + i*16);
					cost_tmp = toDouble(buf, 20 + i*16);
					
					neighborList.addEntryInVector(index, ip_tmp, port_tmp, cost_tmp);

					if(!routeTable.isLocal(ip_tmp, port_tmp)){
						if(!routeTable.isInTable(ip_tmp, port_tmp)){
							routeTable.addEntry(ip_tmp, port_tmp, neighborList.getCost(index) + cost_tmp, ipaddr, port);
							flag = true;
							//System.out.println(6);
						}
					}
				}

				//Bigin to update route table
				if(routeTable.updateTable() == true)
				{
					flag = true;
					//System.out.println(4);
				}
					

				if(flag == true){
					neighborList.setSendFlag();
					//System.out.println(5);
				} 
					
			}catch(SocketException e){
				timer.cancel();
				break;
			}catch(IOException e){
				System.err.println(e);
			}
			
		}
	}

	public int toInt(byte[] buf, int offset){
		int tmp = (buf[offset] & 0xFF) | ((buf[offset + 1] << 8) & 0xFF00) |
					((buf[offset + 2] << 16) & 0xFF0000) | ((buf[offset + 3] << 24) & 0xFF000000);
		return tmp;
	}

	public double toDouble(byte[] buf, int offset){
		long tmp = 0;
		for (int i = 0; i < 8; i++)
		{
    		tmp += ((long) buf[i + offset] & 0xffL) << (8 * i);
		}
		double tmp2 = Double.longBitsToDouble(tmp);
		return tmp2;
	}

	public String getIp(byte[] buf, int offset) throws UnknownHostException{
		byte[] tmp = new byte[4];
		for (int i = 0; i < 4; ++i){
			tmp[i] = buf[offset + i];
		}
		
		InetAddress ip = InetAddress.getByAddress(tmp);	
		
		return ip.getHostAddress();
	}

	public String getPort(byte[] buf, int offset){
		return  Integer.toString(toInt(buf, offset));
	}
}


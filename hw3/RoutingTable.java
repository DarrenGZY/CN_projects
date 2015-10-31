import java.util.ArrayList;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RoutingTable{
	private static ArrayList<String> destIps = new ArrayList<>();
	private static ArrayList<Double> costs = new ArrayList<>();
	private static ArrayList<String> linkIps = new ArrayList<>();
	private static ArrayList<Boolean> removable = new ArrayList<>();
	private static String localIp;
	private static String localPort;
	private NeighborList neighborList = new NeighborList();

	public double INFINITY = 1000;

	public void initialEntry(String dest, String port, Double cost){
		destIps.add(dest + ":" + port);
		costs.add(cost);
		linkIps.add(dest + ":" + port);	
		removable.add(false);
	}

	public void addEntry(String dest, String port, Double cost, String link, String port2){
		destIps.add(dest + ":" + port);
		costs.add(cost);
		linkIps.add(link + ":" + port2);
		removable.add(false);
	}


	//remove the neigher in route table, if it directly goes to it neighbor
	public void removeNeighbor(String dest, String port){
		int index = destIps.indexOf(dest + ":" + port);
		if (index != -1){
			if(linkIps.get(index).equals(dest + ":" + port)){
				destIps.remove(index);
				costs.remove(index);
				linkIps.remove(index);
				removable.remove(index);
			}
		}
	}

	public void removeEntry(int index){
		destIps.remove(index);
		costs.remove(index);
		linkIps.remove(index);
		removable.remove(index);
	}

	public void setRemove(int index){
		removable.set(index, true);
	}

	public void removeEntries(){
		for(int i = destIps.size() - 1; i > -1; --i){
			if(removable.get(i) == true){
				destIps.remove(i);
				costs.remove(i);
				linkIps.remove(i);
				removable.remove(i);
			}
		}
	}

	// add the local ip and port
	public void addLocal(String ip, String port){
		localIp = ip;
		localPort = port;
	}
	// judge if destination is local ip and port
	public boolean isLocal(String dest, String port){
		if(dest.equals(localIp) && port.equals(localPort))
			return true;
		else
			return false;
	}
	public void modifyEntry(int index, String link, String port, double cost){
		costs.set(index, cost);
		linkIps.set(index, link + ":" + port);
	}


	public String getDest(int index){
		return destIps.get(index);
	}
	

	public boolean isInTable(String dest, String port){
		return destIps.contains(dest + ":" + port);
	}

	public boolean isLinkedBy(int index, String link, String port){
		return linkIps.get(index).equals(link + ":" + port);
	}

	public int getIndex(String dest, String port){
		return destIps.indexOf(dest + ":" + port);
	}

	public int getSize(){
		return destIps.size();
	}

	public double getCost(int index){
		return costs.get(index);
	}

	public String getLink(int index){
		return linkIps.get(index);
	}


	public void showTable(){
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	System.out.print( "<" + sdf.format(cal.getTime()) + ">");
    	System.out.println(" Distance Vector List: ");
		for(int i = 0; i < destIps.size(); ++i){
			System.out.print("Destination = " + destIps.get(i) + ",");
			System.out.print("Cost = " + costs.get(i) + ",");
			System.out.println("Link = (" + linkIps.get(i) + ")");
		}
	}

	public boolean updateTable(){
		boolean flag = false;
		for(int i = 0; i < getSize(); ++i){
					

					String destination = getDest(i);
					double cost_before = getCost(i);
					String link = getLink(i);

					String link_min = getLink(i);
					double cost_min = INFINITY;
					double cost_min_tmp;

					//update destination in local route table
					for(int j = 0; j < neighborList.sizeofNeighbors(); ++j){
						if(destination.equals(neighborList.getNeighbor(j))){
							if((cost_min_tmp = neighborList.getCost(j)) < cost_min){
							
								link_min = neighborList.getNeighbor(j);
								String[] ip_port = link_min.split(":");
								modifyEntry(i, ip_port[0], ip_port[1], cost_min_tmp);
								cost_min = cost_min_tmp;
							}
						}
						else{
							if((cost_min_tmp = neighborList.getCost(j) + 
								neighborList.getCostInVector(j, destination)) < cost_min){
							
								link_min = neighborList.getNeighbor(j);
								String[] ip_port = link_min.split(":");
								modifyEntry(i, ip_port[0], ip_port[1], cost_min_tmp);
								cost_min = cost_min_tmp;
							}
						}
					}

					if(cost_min == INFINITY){
						setRemove(i);
						flag = true;
						//System.out.println(3);
					}

					if((cost_min != cost_before) || (!link.equals(link_min))){
						flag = true;
						//System.out.println(4);
					}

				}

				removeEntries();

				/*if(flag == true){
					neighborList.setSendFlag();
					//System.out.println(5);
				}*/
				return flag;
	}


	public void tableToBytes(byte[] buf, int index){
		int size = destIps.size();
		for(int i = 0; i < 4; ++i){
			buf[i] = (byte) ((size >> (i * 8)) & 0xFF);
		}

		double cost = neighborList.getCost(index);
		long cost_long = Double.doubleToLongBits(cost);

		for(int i = 0; i < 8; ++i){
			buf[i + 4] = (byte) ((cost_long >> (i * 8)) & 0xFF);
		}

		byte[] ipaddr = new byte[4];

		//System.out.print("TableSize: ");
		//System.out.println(size);

		for(int i = 0; i < destIps.size(); ++i){
			
			// ip in ip_port[0], port in ip_port[1]
			String[] ip_port = destIps.get(i).split(":");

			try{
				InetAddress ip = InetAddress.getByName(ip_port[0]);
				ipaddr = ip.getAddress();
				for(int j = 0; j < 4; ++j){
					buf[12 + i*16 + j] = ipaddr[j];
				}

				int port = Integer.parseInt(ip_port[1]);
				for(int j = 0; j < 4; ++j){
					buf[12 + i*16 + 4 + j] = (byte) ((port >> (j * 8)) & 0xFF);
				}

				cost = costs.get(i);
				cost_long = Double.doubleToLongBits(cost);
				for(int j = 0; j < 8; ++j){
					buf[12 + i*16 + 8 + j] = (byte) ((cost_long >> (j * 8)) & 0xFF);
				}

			}catch(IOException e){
				System.out.println("error");
			}
		}
	}







}
import java.util.TimerTask;

public class ReceiveTimeTask extends TimerTask{
	private String dest;
	private String port;
	private int timeout;
	private NeighborList neighborlist = new NeighborList();
	private RoutingTable routeTable = new RoutingTable();

	public ReceiveTimeTask(String dest, String port, int timeout){
		this.dest = dest;
		this.port = port;
		this.timeout = timeout;
	}
	@Override
	public void run(){
		//System.out.println("received timeout");
		if (neighborlist.isNeighbor(dest, port)){
			if((System.currentTimeMillis() - neighborlist.getTime(dest, port)) > timeout*3*1000){
				//System.out.println("Dead Node :" + dest + ":" + port);
				neighborlist.removeNeighbor(dest, port);
				routeTable.removeNeighbor(dest, port);
			}
		}
		
		

	}
}
import java.util.ArrayList;


public class NeighborList{
	private static ArrayList<String> neighbors = new ArrayList<>();
	private static ArrayList<Double> costs = new ArrayList<>();
	private static ArrayList<Double> backupCosts = new ArrayList<>();
	private static ArrayList<Boolean> flags = new ArrayList<>();
	private static ArrayList<Long> receivedTime = new ArrayList<>();
	private static ArrayList<NeighborVectors> neighborVectors = new ArrayList<>();
	private static boolean sendFlag = true;

	public double INFINITY = 1000;

	public String getNeighbor(int index){
		return neighbors.get(index);
	}

	public boolean isNeighbor(String dest, String port){
		return neighbors.contains(dest + ":" + port);
	}

	public void addNeighbor(String dest, String port, Double cost){
		neighbors.add(dest + ":" + port);
		costs.add(cost);
		backupCosts.add(cost);
		flags.add(true);
		receivedTime.add(System.currentTimeMillis());
		neighborVectors.add(new NeighborVectors());
		//return neighbors.indexOf(dest + ":" + port);
	}

	public void addEntryInVector(int index, String dest, String port, Double cost){
		neighborVectors.get(index).addEntry(dest, port, cost);
	}

	public double getCostInVector(int index, String dest_port){
		return neighborVectors.get(index).getCost(dest_port);
	}

	public void removeEntryInVector(int index){
		neighborVectors.get(index).removeAll();
	}

	public void removeNeighbor(String dest, String port){
		int index = neighbors.indexOf(dest + ":" + port);
		if(flags.get(index)){
			neighbors.remove(index);
			costs.remove(index);
			backupCosts.remove(index);
			flags.remove(index);
			receivedTime.remove(index);
		}		
	}

	public void modifyTime(String dest, String port){
		int index = neighbors.indexOf(dest + ":" + port);
		receivedTime.set(index, System.currentTimeMillis());
	}

	public long getTime(String dest, String port){
		int index = neighbors.indexOf(dest + ":" + port); 
		return receivedTime.get(index);
	}

	public int getIndex(String dest, String port){
		return neighbors.indexOf(dest + ":" + port);
	}

	public int sizeofNeighbors(){
		return neighbors.size();
	}

	public void setSendFlag(){
		//System.out.println("SET");
		sendFlag = true;
	}

	public void resetSendFlag(){
		//System.out.println("RESET");
		sendFlag = false;
	}

	public boolean getSendFlag(){
		//System.out.println(sendFlag);
		return sendFlag;
	}

	public boolean getFlag(int index){
		return flags.get(index);
	}


	//Link down, stop send message to this neighbor
	public void setLinkDown(String dest, String port){
		int index = neighbors.indexOf(dest + ":" + port);
		costs.set(index, INFINITY);
		flags.set(index, false);
	}

	public void setLinkUp(String dest, String port){
		int index = neighbors.indexOf(dest + ":" + port);
		costs.set(index, backupCosts.get(index));
		flags.set(index, true);
	}

	public double getCost(int index){
		return costs.get(index);
	}
}
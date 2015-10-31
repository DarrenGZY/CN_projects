import java.util.ArrayList;

public class NeighborVectors{
	private ArrayList<String> destIps = new ArrayList<>();
	private ArrayList<Double> costs = new ArrayList<>();

	public double INFINITY = 1000;

	public void addEntry(String dest, String port, double cost){
		destIps.add(dest + ":" + port);
		costs.add(cost);
	}

	public void removeAll(){
		int size = destIps.size();
		for(int i = size - 1; i > -1; --i){
			destIps.remove(i);
			costs.remove(i);
		}
	}

	public double getCost(String dest_port){
		int index = destIps.indexOf(dest_port);
		if (index == -1) return INFINITY;
		else return costs.get(index);
	}

	public int getSize(){
		return destIps.size();
	}
}
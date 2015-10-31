import java.util.TimerTask;

public class SendTimeTask extends TimerTask{
	NeighborList neighborlist = new NeighborList();

	@Override
	public void run(){
		neighborlist.setSendFlag();
	}
}

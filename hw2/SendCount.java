public class SendCount{
	private static int sent;
	private static int resent;
	private static int totalbytes;

	public void setup(){
		sent = 0;
		resent = 0;
		totalbytes = 0;
	}

	public void addSent(int bytes){
		sent ++;
		totalbytes += bytes;
	}

	public void addResent(int bytes){
		sent ++;
		resent ++;
		totalbytes += bytes;
	}

	public void summary(){
		System.out.println("Delivery Completed Successfully");
		System.out.println("sent:   " + sent);
		System.out.println("retransmission:   " + resent);
		System.out.println("Total bytes sent:   " + totalbytes);
	}
}
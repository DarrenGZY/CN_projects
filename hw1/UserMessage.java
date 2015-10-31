import java.util.ArrayList;

public class UserMessage{
	private static ArrayList<String> message = new ArrayList<String>();  //Store message send among users
	private static ArrayList<String> sender = new ArrayList<String>();   //Store senders of message
	private static ArrayList<String> reciever = new ArrayList<String>(); //Store recievers of message
	//private static ArrayList<boolean> flag = new ArrayList<String>();  //judge it is broadcast of private

	// Put messages passing through users in to specific place
	public static void addMessage(String mess, String send, String reci){
		message.add(mess);
		sender.add(send);
		reciever.add(reci);
	}
	// Remove the messages that has already sent
	public static void removeMessage(String reci){
		int index = reciever.indexOf(reci);
		message.remove(index);
		sender.remove(index);
		reciever.remove(index);
	}

	// to judge if the user have massage to recieve
	public static boolean isRecieved(String reci){
		return reciever.contains(reci);
	}

	// Pack the massage for reciever
	public static String getMessage(String reci){
		int index = reciever.indexOf(reci);
		return (sender.get(index) + " : " + message.get(index) + '\n' +"> ");
	}
}
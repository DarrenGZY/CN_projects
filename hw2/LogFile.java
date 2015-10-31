import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.lang.Thread;
import java.lang.InterruptedException;
public class LogFile{
	PrintWriter writer;
	public LogFile(String path){
		try{
			writer = new PrintWriter(new File(path));
		}catch(IOException e){
			System.out.println("Can not create file");
		}
		
	}
	//receiver side logfile initiate
	public void receiverSetup(){
		writer.printf("%-32s%-10s%-10s%-10s%-10s%-10s"  ,
		 "TimeStamp", "Source", "Dest", "Seq #", "Ack #", "Flags");
	}
	//sender side logfile initiate
	public void senderSetup(){
		writer.printf("%-32s%-10s%-10s%-10s%-10s%-10s%-10s"  ,
		 "TimeStamp", "Source", "Dest", "Seq #", "Ack #", "Flags", "EstimatedRTT");
	}
	//write into receiver side logfile
	public void receiverLog(Calendar cal, int source, int dest, int seq, int ack, String flags){
		writer.printf("%n%-32tc%-10d%-10d%-10d%-10d%-10s"  ,
		 cal, source, dest, seq, ack, flags);
	}	
	//write into sender sige logfile
	public void senderLog(Calendar cal, int source, int dest, int seq, int ack, String flags, long rtt){
		writer.printf("%n%-32tc%-10d%-10d%-10d%-10d%-10s%-10d"  ,
		 cal, source, dest, seq, ack, flags, rtt);
	}

	public void close(){
		writer.close();
	}
	public static void main(String[] args) throws InterruptedException{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
		String time = f.format(cal.getTime());
		Date a = new Date();
		System.out.printf("%s",time);
		Thread.sleep(3000);
		time = f.format(cal.getTime());
		System.out.printf("%s",time);
		//LogFile a = new LogFile("log.txt");
		//a.ReceiverSetUp();
		//a.ReceiverLog(5000,4000,2,0,"NONE");
		//a.Close();
	}
}
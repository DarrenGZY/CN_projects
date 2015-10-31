import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class TimeNoActive{
	private Timer timer;
    private Socket socket;

    private int TIME_OUT = 30;  //unit is minutes  

    public TimeNoActive(Socket socket) {
        this.socket = socket;
	}

    // Start to count on time
    void execute(){
        timer = new Timer();
        timer.schedule(new NoActiveTask(), TIME_OUT*60*1000);
    }

    // Cancel the timer
    void reset(){
        timer.cancel();
        timer = new Timer();
        timer.schedule(new NoActiveTask(), TIME_OUT*60*1000);
    }

    class NoActiveTask extends TimerTask{

        //To run when time is up
        public void run() {
            try{
                PrintWriter toClient = new PrintWriter(socket.getOutputStream(), true);
                
                toClient.println("logout");  // server as a flag the close ClientRecieve

                //System.out.println("time_out");
                
                timer.cancel();
            }catch(IOException e){
                System.err.println(e);
            }

            
        }
    }


}
import java.util.Timer;
import java.util.TimerTask;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.io.IOException;
import java.util.Calendar;

public class TimeOut{
    private Timer timer; //calculate the timeout
    private long TIMEOUT; //unit is milisecond
    private LogFile logfile;
    private Calendar cal;
    private DatagramSocket udp_socket;
    private DatagramPacket udp_packet;

    /* TimeOut(int timeout, DatagramSocket socket, DatagramPacket packet){
        TIMEOUT = timeout;
        udp_socket = socket;
        udp_packet = packet;
    }*/
    //set the value of timeout
    public void set(long timeout, DatagramSocket socket, DatagramPacket packet){
        TIMEOUT = timeout;
        udp_socket = socket;
        udp_packet = packet;
    }

    //begin to count time
    public void execute(LogFile log, int source, int dest, int seq, int ack, String flag, long rtt, int bytes){
        timer = new Timer();
        timer.schedule(new TimeOutTask(log, source, dest, seq, ack, flag, rtt, bytes), TIMEOUT);
    }
    //cancel the timer
    public void reset(){
        timer.cancel();
    }

    class TimeOutTask extends TimerTask{
        private LogFile logfile;
        private int source;
        private int dest;
        private int seq;
        private int ack;
        private String flag;
        private long rtt;
        private int bytes;
        private SendCount counter;
        //construct TimeOutTask, include values for logfile
        public TimeOutTask(LogFile log, int s, int d, int s_num, int a_num, String f, long r, int b){
            logfile = log;
            source = s;
            dest = d;
            seq = s_num;
            ack = a_num;
            flag = f;
            rtt = r;
            bytes = b;
            counter = new SendCount();
        }

        public void run(){
            try{
                udp_socket.send(udp_packet);
                counter.addResent(bytes);
                Calendar cal_s = Calendar.getInstance();
                logfile.senderLog(cal_s, source, dest, seq, ack, flag, rtt);

                timer.cancel();
                timer = new Timer();
                timer.schedule(new TimeOutTask(logfile, source, dest, seq, ack, flag, rtt, bytes), TIMEOUT);
            }catch(IOException e){
                System.err.println(e);
            }
            
        }
    }
}
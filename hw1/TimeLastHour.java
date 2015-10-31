import java.util.Timer;
import java.util.TimerTask;

public class TimeLastHour{
	private Timer timer;
    private User users;
    private int index;
    private int LAST_HOUR = 60;    // time period of user login in, unit is minutes

    public TimeLastHour() {
        users = new User();
	}

    // Execute task and begin to count time
    void execute(int index){
        timer = new Timer();
        timer.schedule(new LastHourTask(index), LAST_HOUR*60*1000);
    }
    // Cancel the timer
    void reset(){
        timer.cancel();
    }

    class LastHourTask extends TimerTask{
        private int index;

        public LastHourTask(int index){
            this.index = index;
        }

        //To run when time is up
        public void run(){
            //System.out.println("one hour");
            if(users.getRecord(index)){  //if user login again, cancel this timer
                timer.cancel();
            }
            else{
                users.resetLastHour(index);
                timer.cancel();
            } 
        }
    }


}
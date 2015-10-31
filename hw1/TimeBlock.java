import java.util.Timer;
import java.util.TimerTask;

public class TimeBlock{
	private Timer timer;      // calculate time and execute at specfic time
	private boolean[] block ; // record which user is in block, each thread has their own record
    private int index; // index of user for this specific thread
    private int size;  // size of users
    private int BLOCK_TIME = 60; //unit is seconds

    public TimeBlock(int size) {
        this.size = size;
        block = new boolean[size];
        for(int i = 0; i < size; i++){  // initiate the block array
        	block[i] = false;
        }
	}

    //Execute task and count down time
	void execute(int index){
		timer = new Timer();
		timer.schedule(new BlockTask(index), BLOCK_TIME*1000);
	}
    //record block for this user
	void setBlock(int index){
		block[index] = true;
	}
    //cancel block for this user
	boolean getBlock(int index){
		return block[index];
	} 

    class BlockTask extends TimerTask {
        private int index;

        public BlockTask(int index){
        	this.index = index;
        }
        //to run when time is up
        public void run() {
            //System.out.format("Time's up!%n");
            block[index] = false;  //cancel block
            timer.cancel(); //Terminate the timer thread
        }
    }
}
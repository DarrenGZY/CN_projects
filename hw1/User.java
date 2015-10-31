import java.io.File;
import java.io.IOException;
import java.util.Scanner;



public class User{
	private static String[] usr_name ; // store user names, initial size 20
	private static String[] usr_key;  // store user keys, initial size 20
	private static boolean[] record;  // record if the usr has already login, true for login, false for not.
	private static boolean[] lasthour; //record if the usr has login last hour
	private static int size;          // total number of users
	private int UPPER_BOUND = 20;       // the most number of users

	// intitate and load user informations
	public void init(){
		try{
			usr_name = new String[UPPER_BOUND]; 
			usr_key = new String[UPPER_BOUND]; 
			record = new boolean[UPPER_BOUND]; 
			lasthour = new boolean[UPPER_BOUND];
			// Read the usr massage file
			File usr_file = new File("usr_file.txt");
			Scanner usr_in = new Scanner(usr_file);

			int i = 0;     //calculate the number of users
			
			// Read data from file
			while(usr_in.hasNext()){
				usr_name[i] = usr_in.next();
				usr_key[i] = usr_in.next();
				++i;
			}
			size = i;

			// Close the file
			usr_in.close();

			
		}catch(IOException e){
			System.err.println(e);
		}		

		// Initiate the record array before any usr login
		for(int j = 0; j < size; ++j){
			record[j] = false;
		}

		// Initiate the lasthour array
		for(int j = 0; j < size; ++j){
			lasthour[j] = false;
		}
	}

	//judge if the name is correct, if yes return index, if not return -1.
	public static int isNameRight(String name){
		for(int i = 0; i < size; ++i){
			if(usr_name[i].equals(name))
				return i;
		}
		return -1;
	}

	//judge if the key for certain name is correct.
	public boolean isKeyRight(int index, String key){
		if(usr_key[index].equals(key))
			return true;
		else
			return false;
	}

	//for user that successfully login
	public static void setRecord(int index){
		record[index] = true;
	}

	//for user that successfully logout
	public void resetRecord(int index){
		record[index] = false;
	}

	//get the record information 
	public boolean getRecord(int index){
		return record[index];
	}


	//get user name
	public String getName(int index){
		return usr_name[index];
	}


	//set lasthour true
	public void setLastHour(int index){
		lasthour[index] = true;
	}

	//set lasthour false
	public void resetLastHour(int index){
		lasthour[index] = false;
	}

	//get if user login last hour
	public boolean getLastHour(int index){
		return lasthour[index];
	}


	public void Whoelse(){
		for(int i = 0; i < size; i++){
			if(record[i] == true) System.out.println(usr_name[i]);
		}
	}

	public int getSize(){
		return size;
	}








}
import java.lang.Object.*;
import java.nio.charset.Charset;
import java.io.*;
import java.util.Scanner;

public class DataSegment{
	private Scanner s;
	//private int breakpoint;	//record the start of next data segment in file
	private String breakline;  //record the last time parsed line
	private int SEGMENT_SIZE = 556;
	public DataSegment(String file){
		try{
			s = new Scanner(new File(file));
			breakline = "";
		}catch(IOException e){
			System.out.print("Can not find file" + file);
		}
	}

	public String getDataSegment(){
		return getDataSegment(breakline);
	}

	public String getDataSegment(String line){
		String segment = new String();
		int total = 0;
		int breakpoint = 0;
		if(line.length() != 0){
			if(line.length() > SEGMENT_SIZE){
				breakpoint = SEGMENT_SIZE;  //updata breakpoint
				segment = line.substring(0, breakpoint);
				breakline = line.substring(breakpoint); //updata breakline
				return segment;
			}
			else{
				// if reach the end of file, does not need to add '\n'
				if(s.hasNext()){
					segment += line + '\n';
					total += line.length() + 1;
					//System.out.println("huhuhu");
				}
				else{
					segment += line;
					total += line.length();
				}
			}
			
		}

		while(total < SEGMENT_SIZE + 1){
			if(s.hasNext()){
				String tmp = s.nextLine();
				if ((tmp.length() + 1 + total) < SEGMENT_SIZE + 1){
					total += tmp.length() + 1;
					segment += tmp + '\n';
				}
				else{
					breakpoint = SEGMENT_SIZE - total;
					breakline = tmp.substring(breakpoint);
					//System.out.print("point: " + breakpoint);
					segment += tmp.substring(0, breakpoint);
					//System.out.println("huhuhu");
					return segment;
				}

			}
			else {
				breakline = "";  // File was read over, set breakline to null string
				break;
			}
		}
		return segment;		
	}

	public boolean hasDataSegment(){
		if(s.hasNext() || breakline.length() != 0)
			return true;
		return false;
	}

	public void close(){
		s.close();
	}
}
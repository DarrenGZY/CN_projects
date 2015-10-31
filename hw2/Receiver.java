import java.net.*; 
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Receiver{
	public static void main(String args[]){
		String file = args[0];
		String listen_port = args[1];
		String sender_ip = args[2];
		String sender_port = args[3];
		String logfile_name = args[4];

		int listen_portnum = Integer.parseInt(listen_port);
		int sender_portnum = Integer.parseInt(sender_port);

		try{
			//Recieve packets from sender
			DatagramSocket udp_socket = new DatagramSocket(listen_portnum);

			byte recieve_segment[] = new byte[600];

			//initiate some important classes
			DatagramPacket recieve_packet = new DatagramPacket(recieve_segment, recieve_segment.length);
			PrintWriter output = new PrintWriter(new File(file));
			Translator translator = new Translator();
			Checker checker = new Checker();
			LogFile logfile = new LogFile(logfile_name);
			logfile.receiverSetup();
			//initiate header 
			byte[] header = new byte[20];  // 0--1 checksum  2--3 source port  4--5 dest port  6--9 seq# 10--13 ack# 
			for(int i = 0; i < 20; ++i){   // 14--15 flag field 16--17 receive window 18-19 Urgent data pointer
				header[i] = 0x00;
			}

			// ack_num is expected seq_num in receiver
			int ack_num = 0;
			int seq_num;

			//Calendar cal = Calendar.getInstance();
			SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
			String time_receive;
			String time_send;
			String flag = "NONE";
			while(true){
				
				udp_socket.receive(recieve_packet);
				
				Calendar cal_r = Calendar.getInstance();
				time_receive = f.format(cal_r.getTime());
				
				//data are stored in segment, but not fully stored segment.length != length
				byte[] segment = recieve_packet.getData();
				//length indicate the real length of packet
				int length = recieve_packet.getLength();
				//recalculate checksum for all parts in segment except checksum field
				short checksum = (short)(checker.Checksum(segment, 2, length));

				seq_num = translator.toInt(segment, 6);

				if(segment[15] == (byte)(0x01)) flag = "FIN";

				logfile.receiverLog(cal_r, sender_portnum, listen_portnum, seq_num, 0, flag);

				if(checker.CompareChecksum(checksum, segment, 0)){
					// if get the FIN flag
					if(segment[15] == (byte)(0x01)){
						Socket socket = new Socket(sender_ip, sender_portnum);

						DataOutputStream toSender = new DataOutputStream(socket.getOutputStream());

						header[15] = (byte)(0x01);
						translator.toBytes(header, 10, ack_num);
						toSender.write(header);

						Calendar cal_s = Calendar.getInstance();
						time_send = f.format(cal_s.getTime());
						logfile.receiverLog(cal_s, listen_portnum, socket.getPort(), 0, ack_num, flag);
						logfile.close();
						output.close();
						break;
					}

					// if seq # is just what receiver expected
					if(seq_num == ack_num){
						
						//message are extracted after header field
						String message = new String(segment, 20, length - 20);
						//write to file
						output.print(message);
						Socket socket = new Socket(sender_ip, sender_portnum);

						DataOutputStream toSender = new DataOutputStream(socket.getOutputStream());

						translator.toBytes(header, 10, ack_num);
						toSender.write(header);

						Calendar cal_s = Calendar.getInstance();
						time_send = f.format(cal_s.getTime());
						logfile.receiverLog(cal_s, listen_portnum, socket.getPort(), 0, ack_num, flag);

						ack_num += length;
					}
				}			
			}
		}catch(IOException e){
			System.err.println(e);
		}
	}
}
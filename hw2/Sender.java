import java.net.*; 
import java.util.*;
import java.net.DatagramSocket;
import java.io.*;
import java.nio.charset.Charset;
import java.net.UnknownHostException;
import java.lang.System;
import java.util.Calendar;
import java.lang.Math;

public class Sender{
	static int SEGMENT_SIZE = 556;
	static long TIMEOUT = 1000;  // denote the time period of timeout
	public static void main(String args[]){
		//String filename = args[0];
		String file = args[0];
		String dest_ip = args[1];
		String dest_port = args[2];
		String source_port = args[3];
		String logfile_name = args[4];

		int dest_portnum = Integer.parseInt(dest_port);
		int source_portnum = Integer.parseInt(source_port);
		
		
		try{
			
			InetAddress dest_ipaddr = InetAddress.getByName(dest_ip);
			//portnum here is for poxy to recognize
			DatagramSocket udp_socket = new DatagramSocket(source_portnum);
			// create TCP server socket to listen acknowledgement from reciever
			ServerSocket ack_socket = new ServerSocket(source_portnum);		
			DataSegment data = new DataSegment(file);
			TimeOut timer = new TimeOut();
			Checker checker = new Checker();
			Translator translator = new Translator();
			LogFile logfile = new LogFile(logfile_name);
			logfile.senderSetup();
			
			SendCount counter = new SendCount();
			counter.setup();

			String flag = "NONE";  // initial flag is none

			long estimatedRTT = 1000;
			long sampleRTT;
			//long devRTT = 0;
			long timeout = TIMEOUT;
			long begin;
			long finish;

			int seq_num = 0;
			int ack_num = 0; // ack_num in this assignment will not use, because receiver will not send any data to sender
			byte[] header = new byte[20];  // 0--1 checksum  2--3 source port  4--5 dest port  6--9 seq# 10--13 ack# 
			for(int i = 0; i < 20; ++i){   // 14--15 flag field 16--17 receive window 18-19 Urgent data pointer
				header[i] = 0x00;
			}
			
			translator.toBytes(header, 2, (short)(source_portnum));
			translator.toBytes(header, 4, (short)(dest_portnum));
			translator.toBytes(header, 10, ack_num);
			while(true){
				if(data.hasDataSegment()){			
					//translate seqnum to byte array and store in header	
					translator.toBytes(header, 6, seq_num);
					//Get the data from file, and translate it to bytes
					byte[] packet = data.getDataSegment().getBytes(Charset.forName("UTF-8"));
					//combine two arrays
					byte[] segment = new byte[packet.length + header.length];
					System.arraycopy(header, 0, segment, 0, header.length);
					System.arraycopy(packet, 0, segment, header.length, packet.length);
					//compute checksum
					short checksum = (short)(checker.Checksum(segment, 2, segment.length));

					translator.toBytes(segment, 0, checksum);

					//assemble the packet and send
					DatagramPacket send_packet = new DatagramPacket(segment, segment.length, dest_ipaddr, dest_portnum);
					udp_socket.send(send_packet);
					counter.addSent(segment.length); // add sent number
					//get the send time
					Date sendtime = new Date();
					begin = sendtime.getTime();
					//write into logfile
					Calendar cal_s = Calendar.getInstance();
					logfile.senderLog(cal_s, source_portnum, dest_portnum, seq_num, 0, flag, estimatedRTT);
					//set timer
					timer.set(timeout, udp_socket, send_packet);
					timer.execute(logfile, source_portnum, dest_portnum, seq_num, 0, 
						flag, estimatedRTT, segment.length);
					//update sequence number
					seq_num += segment.length;
				}
				else{
					//Set FIN flag
					header[15] = (byte)(1);
					flag = "FIN";
					//translate sequence number to bytes
					translator.toBytes(header, 6, seq_num);
					//compute checksum
					short checksum = (short)(checker.Checksum(header, 2, header.length));
					translator.toBytes(header, 0, checksum);
					//prepare to send packets
					DatagramPacket send_packet = new DatagramPacket(header, header.length, 
						dest_ipaddr, dest_portnum);
					udp_socket.send(send_packet);
					counter.addSent(header.length); // add sent number
					//get the send time
					Date sendtime = new Date();
					begin = sendtime.getTime();
					//write into logfile
					Calendar cal_s = Calendar.getInstance();
					logfile.senderLog(cal_s, source_portnum, dest_portnum, seq_num, 0, flag, estimatedRTT);
					//set timer
					timer.set(timeout, udp_socket, send_packet);
					timer.execute(logfile, source_portnum, dest_portnum, seq_num, 0, 
						flag, estimatedRTT, header.length);
				}
					
				Socket socket = ack_socket.accept();
				timer.reset();

				Date receivetime = new Date();
				finish = receivetime.getTime();
				
				DataInputStream fromReciever = new DataInputStream(socket.getInputStream());

				byte[] ack = new byte[20];
				fromReciever.readFully(ack);
				//System.out.println(translator.toInt(ack, 10));
				

				sampleRTT = finish - begin;
				estimatedRTT = (long)(0.875 * estimatedRTT) + (long)(0.125 * sampleRTT);
				//devRTT = (long)(0.75 * devRTT) + (long)(0.25 * Math.abs(sampleRTT - estimatedRTT));
				//timeout = estimatedRTT + 4 * devRTT;

				if(ack[15] == (byte)(0x01)){
					flag = "FIN";
					int recieved_ack = translator.toInt(ack, 10);

					Calendar cal_s = Calendar.getInstance();
					logfile.senderLog(cal_s, dest_portnum, source_portnum, 0, recieved_ack, flag, estimatedRTT);
					break;
				}

				int recieved_ack = translator.toInt(ack, 10);

				Calendar cal_s = Calendar.getInstance();
				logfile.senderLog(cal_s, dest_portnum, source_portnum, 0, recieved_ack, flag, estimatedRTT);
			}
			data.close();

			logfile.close();

			counter.summary();

			timer.reset();
		}catch(UnknownHostException e){
			System.out.println("Wrong IP address");
		}catch(IOException e){
			System.err.println(e);
		}
	}
}
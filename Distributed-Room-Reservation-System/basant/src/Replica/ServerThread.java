package Replica;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import Configuration.Constants;
import Server.RoomReservationSystem;
import Utility.JournalLogger;

public class ServerThread implements Runnable{
	String inputString;
	String serverLocation;
	RoomReservationSystem rms;
	String replicaName;
	JournalLogger journalLogger = null;
	DatagramPacket request;
	
	
	public ServerThread(DatagramPacket message, RoomReservationSystem rms) {
		request = message;	
	   inputString = new String(request.getData(),request.getOffset(),request.getLength()).trim();
	   this.rms= rms;
	   serverLocation = rms.location;
	   replicaName = rms.replicaName;
	 
	}
	@Override
	public void run() {
		
		System.out.println("I heard it "+ inputString);
		String method = getParameter(inputString,  "METHOD")  ; 
		String room_Number ;
		String date; 
		String[] list_Of_Time_Slots = null ;
		String timeslot;
		String campus_Name ;
		String StudentID;
		String booking_ID;
		String reply = null;
		
		System.out.println("method: "+ method);
		try {
		
			journalLogger = new JournalLogger(Constants.PROJECT_DIR + "\\" + Constants.BACKUP_DIR_NAME + "\\" +
		                Constants.JOURNAL_DIR + "\\" + serverLocation.toString() + "\\" + replicaName + "_" +
		                Constants.JOURNAL_FILE_NAME, serverLocation, replicaName);
			journalLogger.log(inputString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(method.equals("createRoom"))
				{
				room_Number = 0 + getParameter(inputString,  "ROOMNUMBER").trim()  ;
				date =  getParameter(inputString,  "DATE")  ;
				timeslot=  getParameter(inputString,  "SLOT").replaceAll("\\s+","")  ;
				  System.out.println( "RECIVED booking room_Number:" +room_Number
                		  + "date" + date + "timeslot" + timeslot  ); 
				  list_Of_Time_Slots = new String[1];
				  list_Of_Time_Slots[0] =timeslot ;
				
				  reply =  rms.createRoom(room_Number, date, list_Of_Time_Slots);
				}
		else if (method.equals("deleteRoom"))
				{
				room_Number =  getParameter(inputString,  "ROOMNUMBER")  ;
				date =  getParameter(inputString,  "DATE")  ;
				timeslot=  getParameter(inputString,  "SLOT")  ; 
				reply = rms.deleteRoom(room_Number, date, list_Of_Time_Slots);
				}
		else if (method.equals("bookRoom"))
				{
				room_Number = 0 + getParameter(inputString,  "ROOMNUMBER").trim()  ;
				campus_Name =  getParameter(inputString,  "CAMPUS").toUpperCase()  ;
				date =   getParameter(inputString,  "DATE")  ; 
				timeslot =   getParameter(inputString,  "SLOT").replaceAll("\\s+","")   ; 
				StudentID =   getParameter(inputString,  "STUDENT_ID")  ;
				 System.out.println( "RECIVED booking campus_Name " + campus_Name  + "room_Number" + room_Number
                		  + "date" + date + "timeslot" + timeslot + "StudentID" + StudentID  ); 
				reply = rms.bookRoom(campus_Name, room_Number, date, timeslot, StudentID);
				if(reply.startsWith("fail")){
				 reply = "failed";	
				}else{
				ReplicaUDPServer.bookingIds.put("BK"+ReplicaUDPServer.bookingId++, reply);
				reply = "BK"+ReplicaUDPServer.bookingId;
				}
				}
		else if (method.equals("getAvailableTimeSlot"))
				{
				date =   getParameter(inputString,  "DATE")  ;
				 reply = rms.getAvailableTimeSlot(date);
				}
		else if (method.equals("cancelBooking"))
				{
				booking_ID  =   getParameter(inputString,  "BOOKING_ID")  ; 
				booking_ID = ReplicaUDPServer.bookingIds.get(booking_ID);
				StudentID =   getParameter(inputString,  "STUDENT_ID")  ;
				 reply = rms.cancelBooking(booking_ID, StudentID);
				}
		else if (method.equals("changeReservation"))
				{
				campus_Name =  getParameter(inputString,  "CAMPUS")  ;
				room_Number =  "0"+ getParameter(inputString,  "ROOMNUMBER")  ;
				date =   getParameter(inputString,  "DATE")  ; 
				timeslot =   getParameter(inputString,  "SLOT")  ; 
				StudentID =   getParameter(inputString,  "STUDENT_ID")  ;
				booking_ID  =   getParameter(inputString,  "BOOKING_ID")  ; 
				booking_ID = ReplicaUDPServer.bookingIds.get(booking_ID);
				reply = rms.changeReservation(booking_ID, campus_Name, room_Number, timeslot, date, StudentID);
				}
		
		DatagramPacket replyPacket = new DatagramPacket(reply.getBytes(), reply.length(), 
				request.getAddress(), request.getPort());
		try {			
			DatagramSocket s = new DatagramSocket();
			s.send(replyPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getParameter(String str,String parameterName){
        if(parameterName.equals("SEQUENCE_NUMBER")){
            return str.substring( str.indexOf("SEQUENCE:")+9 , str.indexOf(";END;") ) ;
        }else if(parameterName.equals("METHOD")){
            parameterName = ";END";
            str = str.substring(str.indexOf(parameterName) + parameterName.length()+1);
            return str.substring(0, str.indexOf(":"));
        }
            str = str.substring(str.indexOf(parameterName) + parameterName.length()+1);
            return str.substring(0, str.indexOf(";"));
        
    }
	
}

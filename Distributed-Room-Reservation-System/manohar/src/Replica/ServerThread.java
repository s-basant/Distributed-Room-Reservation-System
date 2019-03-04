package Replica;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;

import Utility.JournalLogger;
import admin.AdminImpl;
import student.StudentImpl;

public class ServerThread implements Runnable{

	
	String inputString;
	String serverLocation;
	String replicaName;
	AdminImpl adminImpl;
	DatagramPacket request;
	StudentImpl studentImpl;
	JournalLogger journalLogger = null;

	public ServerThread(DatagramPacket message, AdminImpl new_adminImpl, StudentImpl new_studentImpl) {
	   request = message;
	   studentImpl = new_studentImpl;
	   adminImpl = new_adminImpl;
	   serverLocation = studentImpl.roomRecord.serverName;
	   inputString = new String(request.getData(),request.getOffset(),request.getLength()).trim();		
	   replicaName = "manohar";
	   journalLogger = new JournalLogger("C://journels/manohar.txt", "", "manohar");
	   journalLogger.log(inputString);
	}
	
	@Override
	public void run() {
		
		System.out.println("I heard it "+ inputString);
		System.out.println("and I am "+studentImpl.roomRecord.serverName);
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
		
		if(method.equals("createRoom"))
				{
				room_Number = 0 + getParameter(inputString,  "ROOMNUMBER").trim()  ;
				date =  getParameter(inputString,  "DATE")  ;
				timeslot=  getParameter(inputString,  "SLOT").replaceAll("\\s+","")  ;
				  System.out.println( "RECIVED booking room_Number:" +room_Number
                		  + "date" + date + "timeslot" + timeslot  ); 
				  list_Of_Time_Slots = new String[1];
				  list_Of_Time_Slots[0] =timeslot ;
				
				 boolean f =  adminImpl.createRoom(room_Number, date, list_Of_Time_Slots);
				 if(f)
					 reply = "success";
				 else
					 reply = "failed";
				}
		else if (method.equals("deleteRoom"))
				{
				room_Number =  getParameter(inputString,  "ROOMNUMBER")  ;
				date =  getParameter(inputString,  "DATE")  ;
				timeslot=  getParameter(inputString,  "SLOT")  ; 
				boolean f = adminImpl.deleteRoom(room_Number, date, list_Of_Time_Slots);
				if(f)
					 reply = "success";
				 else
					 reply = "failed";
				
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
				reply = studentImpl.bookRoom(campus_Name, StudentID, room_Number, date, timeslot);
				if(reply.startsWith("fail")){
				  reply = "failed";	
				}else{
				RServer.bookingIds.put("BK"+RServer.bookingId++, reply);
				reply = "BK"+RServer.bookingId;
				}
				}
		else if (method.equals("getAvailableTimeSlot"))
				{
				date =   getParameter(inputString,  "DATE")  ;
				reply = studentImpl.getAvailableTimeSlot(date);
				}
		else if (method.equals("cancelBooking"))
				{
				booking_ID  =   getParameter(inputString,  "BOOKING_ID")  ;
				booking_ID = RServer.bookingIds.get(booking_ID);
				StudentID =   getParameter(inputString,  "STUDENT_ID")  ;
				boolean f = studentImpl.cancelBooking(booking_ID, StudentID);
				if(f)
					 reply = "success";
				 else
					 reply = "failed";	
		}
		else if (method.equals("changeReservation"))
				{
				campus_Name =  getParameter(inputString,  "CAMPUS")  ;
				room_Number =  "0"+ getParameter(inputString,  "ROOMNUMBER")  ;
				date =   getParameter(inputString,  "DATE")  ; 
				timeslot =   getParameter(inputString,  "SLOT")  ; 
				StudentID =   getParameter(inputString,  "STUDENT_ID")  ;
				booking_ID  =   getParameter(inputString,  "BOOKING_ID")  ; 
				booking_ID = RServer.bookingIds.get(booking_ID);
				reply = studentImpl.changeReservation(booking_ID, campus_Name, room_Number, timeslot, StudentID, date);
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

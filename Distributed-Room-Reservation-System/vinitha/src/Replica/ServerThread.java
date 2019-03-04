package Replica;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import RoomReservation.RoomReservationImplementation;
import Utility.JournalLogger;
public class ServerThread implements Runnable{

	
	String inputString;
	String serverLocation;
	String replicaName;
	RoomReservationImplementation Impl;
	DatagramPacket request;
	JournalLogger journalLogger = null;

	public ServerThread(DatagramPacket message, RoomReservationImplementation impl) {
	   request = message;
	   Impl = impl;
	   inputString = new String(request.getData(),request.getOffset(),request.getLength()).trim();		
	   replicaName = "pallavi";
	   journalLogger = new JournalLogger("C://journels/pallavi.txt", "", "pallavi");
	   journalLogger.log(inputString);
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
		
		if(method.equals("createRoom"))
		{
				room_Number = 0 + getParameter(inputString,  "ROOMNUMBER").trim()  ;
				date =  getParameter(inputString,  "DATE")  ;
				timeslot=  getParameter(inputString,  "SLOT").replaceAll("\\s+","")  ;
				  System.out.println( "RECIVED booking room_Number:" +room_Number
                		  + "date" + date + "timeslot" + timeslot  ); 
				  list_Of_Time_Slots = timeslot.split(",");
					String tocampus = getParameter(inputString,  "TO_CAMPUS") ;
				 boolean f = Impl.fnCreateRoom( tocampus.toUpperCase()+"A" ,date, room_Number, list_Of_Time_Slots[0] );
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
				String tocampus = getParameter(inputString,  "TO_CAMPUS") ;
				boolean f = Impl.fnDeleteRoom(tocampus.toUpperCase()+"A", date, room_Number, timeslot);
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
				reply = Impl.fnBookRoom(StudentID,campus_Name, room_Number, date, timeslot);
				if(reply.contains("fail")){
					reply = "failed";
				}else{
					RServer.bookingIds.put("BK"+RServer.bookingId++, reply);
					reply = "BK"+RServer.bookingId;
				}
				}
		else if (method.equals("getAvailableTimeSlot"))
				{
				date =   getParameter(inputString,  "DATE")  ;
				campus_Name =  getParameter(inputString,  "TO_CAMPUS").toUpperCase()  ;
				try {
					reply = Impl.fnGetAvailableTimeSlot(campus_Name,date);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
		else if (method.equals("cancelBooking"))
				{
				booking_ID  =   getParameter(inputString,  "BOOKING_ID")  ; 
				booking_ID = RServer.bookingIds.get(booking_ID);
				StudentID =   getParameter(inputString,  "STUDENT_ID")  ;
				
				boolean f = Impl.fnCancelBooking(booking_ID, StudentID);
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
				reply = Impl.fnTransferRoomDetails(
						booking_ID, StudentID, booking_ID.substring(0, 2),campus_Name, date, room_Number, timeslot);
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

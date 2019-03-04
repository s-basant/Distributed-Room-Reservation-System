package student;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;


import log.FileLogger;
import model.GlobalFlags;
import model.RoomRecord;



public class StudentImpl extends StudentInterfacePOA{

	InetAddress aHost;
	FileLogger fileLogger = null;
	
    HashMap<String, Integer> REMOTE_PORTS = new HashMap<String, Integer>() {{
        put("dvl",8090);
    	put("kkl",8091);
        put("wst",8092);
    }};


	private String responseFromAllServers = "",bookingReply="",cancelReply="",isValidBooking="";
	private boolean isUDP = false;
	public RoomRecord roomRecord;
	public StudentImpl(String new_serverName, boolean isUDP,RoomRecord record) {
		roomRecord = record;
		roomRecord.serverName = new_serverName;
		fileLogger = new FileLogger(new_serverName);
		this.isUDP = isUDP;
		try {
			aHost = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String bookRoom(String campusName,String studentID, String roomNumber, String date, String timeslot){		
		
		String tmp;
		int limit = roomRecord.checkLimit(studentID);
		if(limit >= 3){
			return "overbooked";
		}
		
		if(roomRecord.serverName.equals(campusName.toLowerCase())){
			tmp = roomRecord.bookSlot(studentID,roomNumber, date, timeslot);
		}else{
			//do booking from other server
			bookingReply="";
			String message = "bookSlot:ID="+studentID+";ROOMNUMBER="+roomNumber+";DATE="+date+";SLOT="+timeslot+";";
			Integer[] arr = {REMOTE_PORTS.get(campusName)};
			contactOtherServers(message,arr);			
		    tmp = bookingReply;
		}
		
	    fileLogger.writeLog(studentID+" booked "+roomNumber+" on "+date+" with status "+tmp);
		
		if(!tmp.equals("fail")){
			roomRecord.studentBookingCounter.put(studentID, limit+1);
		}
		
		return tmp;
	}

	@Override
	public String getAvailableTimeSlot(String date) {		
		//if from UDP return only current server available slots
		
		if(GlobalFlags.isByzantinError && roomRecord.serverName.equals("dvl")){
			return "DVL 0 KKL 0 WST 0";
		}
		
		if(isUDP){
			return roomRecord.getAvailableTimeSlot(date);
		}
		
		//get data from other servers
		responseFromAllServers = "";		 
		responseFromAllServers = roomRecord.getAvailableTimeSlot(date);
		Integer[] arr = new Integer[2];
		int o = 0;
		for(String k : REMOTE_PORTS.keySet()){
			if(!k.equals(roomRecord.serverName)){
				arr[o] = REMOTE_PORTS.get(k);
				o++;
			}
		}
		contactOtherServers("getAvailabeSlot:"+date,arr);
		return responseFromAllServers;
	}

	
	
	@Override
	public boolean cancelBooking(String bookingID,String studentID) {
		
		
		Integer tmp = roomRecord.studentBookingCounter.get(studentID);
		if(tmp== null || (tmp-1) <0){
			return false;
		}
		
		String issuccess = null;
		String campusName = bookingID.split("_")[0];
		
		if(campusName.equals(roomRecord.serverName)){
			issuccess = roomRecord.cancelBooking(bookingID,studentID);
		}else{
			String message = "cancelBooking:ID="+studentID+";BOOKINGID="+bookingID+";";
    		Integer[] arr = {REMOTE_PORTS.get(campusName.toLowerCase())};
			contactOtherServers(message,arr);
			issuccess = cancelReply;
		}
		
		
		if(issuccess.equals("done")){
			roomRecord.studentBookingCounter.put(studentID,tmp-1);			
			fileLogger.writeLog("Canceled the booking successfully "+bookingID);
		}else{
			fileLogger.writeLog("Failed to cancel the booking "+bookingID);
			return false;
		}
		return true;
	}

	
	
	@Override
	public String changeReservation(String booking_id, String new_campus_name, String new_room_no,
			String new_time_slot,String studentID,String newDate) {	    
	    
		 String date = booking_id.split("_")[1];
		    String previousCampus = booking_id.split("_")[0];
		    String tmp = null,doCheck = "no";
		    
		    if(previousCampus.equals(roomRecord.serverName)){
		    	//check whether the booking id is real or not
		    	boolean issuccess = roomRecord.isValidBooking(studentID,booking_id);
			    if(!issuccess)
			    	return "fail";
		    }else if(previousCampus.equals(new_campus_name)){
		    	doCheck = "yes";
		    }else{
		        //check with other server whether booking ID is valid
		    	Integer[] arr = {REMOTE_PORTS.get(previousCampus.toLowerCase())};
		    	contactOtherServers("checkValidBooking:ID="+studentID+";BOOKINGID="+booking_id+";",arr);
		    	System.err.println(isValidBooking);
		    	if(isValidBooking.equals("fail")){
		    		return "fail";
		    	}
		    }
	    //if new campus and present campus are same do booking here
	    if(new_campus_name.toLowerCase().equals(roomRecord.serverName)){		    
	    	tmp = roomRecord.bookSlot(studentID,new_room_no, newDate, new_time_slot); 	    	    	
	    }else{
	    	bookingReply="";
	    	String message = "bookSlotAtomic:ID="+studentID+";ROOMNUMBER="+new_room_no+";DATE="+newDate+";SLOT="+new_time_slot+";BOOKINGID="+booking_id+";CHECK="+doCheck+";";
			Integer[] arr = {REMOTE_PORTS.get(new_campus_name.toLowerCase())};
			contactOtherServers(message,arr);			
	        tmp = bookingReply;
	    }
	    
	    fileLogger.writeLog("Changed timeslot for "+booking_id+" to "+new_room_no+" with status/ID "+tmp);
	    
	    if(!tmp.equals("fail") && !tmp.equals("overbooked")){
	    	if(previousCampus.equals(roomRecord.serverName)){
	    		roomRecord.cancelBooking(booking_id, studentID);
	    	}else if(!new_campus_name.equals(previousCampus)){
		    	String message = "cancelBooking:ID="+studentID+";BOOKINGID="+booking_id+";";
	    		Integer[] arr = {REMOTE_PORTS.get(previousCampus.toLowerCase())};
				contactOtherServersAsync(message,arr);
	    	}
	    }
	    
	    return tmp;
	}	
	
	


	private boolean contactOtherServers(String query, Integer[] remotePorts) {
	    //gather data from two other server
	    CountDownLatch latch = new CountDownLatch(remotePorts.length);
		for(int port : remotePorts){
			new Thread(new getDataFromUDP(latch,port,query)).start();
		}
		try {
			//wait until got response from all other servers 
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	private void contactOtherServersAsync(String query, Integer[] remotePorts) {			
		for(int port : remotePorts){
			new Thread(new getDataFromUDP(null,port,query)).start();
		}		
	}

	
	private class getDataFromUDP implements Runnable{

		CountDownLatch signal = null;
		String query = null;
		DatagramSocket aSocket = null;
		int port = 0;
		public getDataFromUDP(CountDownLatch latch,int n_port,String n_query){
			try {
				aSocket = new DatagramSocket();
			} catch (SocketException e) {
				e.printStackTrace();
			}
			this.signal = latch;
			this.port = n_port;
			this.query = n_query;
		}

		@Override
		public void run() {
			try {    
				byte [] m = this.query.getBytes();
				DatagramPacket request = new DatagramPacket(m,  m.length, aHost, this.port);
				aSocket.send(request);			                        
				byte[] buffer = new byte[1000];
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
				aSocket.receive(reply);
				String s = new String(reply.getData(), reply.getOffset(),reply.getLength());			
				switch(query.split(":",2)[0]){
				   case "getAvailabeSlot":
					   synchronized(responseFromAllServers){
				           responseFromAllServers += ", "+s;
				        }
					   break;
				   case "bookSlot":
					   bookingReply=s;
					   break;
				   case "bookSlotAtomic":
					   bookingReply=s;
					   break;
				   case "cancelBooking":
					   cancelReply = s;
				   case "checkValidBooking":
					   isValidBooking = s;
				   default:
					   break;
				}
			if(signal != null)
			    signal.countDown();			    
			}catch (SocketException e){ e.printStackTrace();
			}catch (IOException e){ e.printStackTrace(); 
			}finally {}			
		}

	}




	public boolean isValidBooking(String studentID, String bookingID) {
        return roomRecord.isValidBooking(studentID, bookingID);
	}
	
}

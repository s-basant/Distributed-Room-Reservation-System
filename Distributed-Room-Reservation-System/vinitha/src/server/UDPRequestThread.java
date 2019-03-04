package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import model.RoomRecord;
import student.StudentImpl;

public class UDPRequestThread implements Runnable{
	
	
	DatagramSocket socket = null;
    DatagramPacket request = null;
    StudentImpl studentImpl = null;
    String serverName = null;
    public UDPRequestThread(DatagramSocket socket, DatagramPacket packet, StudentImpl studentimpl, String new_serverName) {
        this.socket = socket;
        this.request = packet;
        this.serverName = new_serverName;
        this.studentImpl = studentimpl;
    }

	
	@Override
	public void run() {
		String tmp = new String(request.getData(),request.getOffset(),request.getLength()).trim();
		String[] query = tmp.split(":",2);
		String response = null;
		String string = query[0];
		if ("getAvailabeSlot".equals(string)) {
			response = studentImpl.getAvailableTimeSlot(query[1]);
		} else if ("bookSlot".equals(string)) {
			String params = query[1];
			response = studentImpl.bookRoom(serverName,getParameter(params, "ID"), getParameter(params, "ROOMNUMBER"), getParameter(params, "DATE"), getParameter(params, "SLOT"));
		} else if ("bookSlotAtomic".equals(string)) {
			String params1 = query[1];
			String bookingID = getParameter(params1, "BOOKINGID");
			String studentID = getParameter(params1, "ID");
			
			boolean issuccess = true;
			if(getParameter(params1, "CHECK").equals("yes")){
				issuccess =  studentImpl.isValidBooking(studentID,bookingID);
			}
			if(!issuccess){
			    	response =  "fail";
			   }else{ 	
				   response = studentImpl.changeReservation(bookingID,serverName, getParameter(params1, "ROOMNUMBER"), getParameter(params1, "SLOT"),studentID,getParameter(params1, "DATE"));
				   if(!response.equals("fail") || !response.equals("overbooked")){
					  if(bookingID.split("_")[0].equals(studentImpl.roomRecord.serverName)){
						  studentImpl.cancelBooking(bookingID, studentID);
					  } 
				   }
			   }
		} else if ("cancelBooking".equals(string)) {
			String params2 = query[1];
			String bookingId = getParameter(params2, "BOOKINGID");
			String studentId = getParameter(params2, "ID");
			if(studentImpl.cancelBooking(bookingId, studentId)){
				response = "done";
			}else{
				response = "fail";
			}
		}
		else if ("checkValidBooking".equals(string)) {
			String params1 = query[1];
			String bookingID = getParameter(params1, "BOOKINGID");
			String studentID = getParameter(params1, "ID");
			response  = "success";
			if(!studentImpl.isValidBooking(studentID,bookingID)){
				response = "fail";
			}
		}	
		else {
		}
		System.out.println("response "+response);
		byte[] b = response.getBytes();
		DatagramPacket reply = new DatagramPacket(b, b.length, 
				request.getAddress(), request.getPort());
		try {			
			socket.send(reply);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public String getParameter(String str,String parameterName){
		if(parameterName.equals("SEQUENCE_NUMBER")){
			return str.substring( str.indexOf("SEQUENCE:")+9 , str.indexOf(";END;") ) ;
		}
		
		if(parameterName.equals("METHOD")){
			parameterName = ";END";
		}
			str = str.substring(str.indexOf(parameterName) + parameterName.length()+1);
			System.out.println(str.substring(0, str.indexOf(";")));
			return str.substring(0, str.indexOf(";"));
		
	}
	
}

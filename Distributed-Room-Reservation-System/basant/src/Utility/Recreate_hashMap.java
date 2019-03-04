package Utility;

import Server.RoomReservationSystem;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static Configuration.Constants.REPLICA_ONE;
import static Configuration.Constants.REPLICA_TWO;

public class Recreate_hashMap {
	private String File_path;
	private RoomReservationSystem RoomReservationSystem;
	public Recreate_hashMap(String Path, String location, String replicaId) throws Exception{
		this.File_path=Path;
		this.RoomReservationSystem = new RoomReservationSystem(location.toString(), replicaId);
	}


	public RoomReservationSystem execute() throws IOException, Exception {
		BufferedReader in = new BufferedReader(
				new FileReader(this.File_path));
		String InputValue = "";
		String room_Number ;
		String date; 
		String[] list_Of_Time_Slots = null ;
		String timeslot;
		String campus_Name ;
		String StudentID;
		String booking_ID;
		String sendData = "";
		while ((InputValue = in.readLine()) != null) {
			String method = getParameter(InputValue,  "METHOD")  ;
			if(method.equals("createRoom"))
			{
			room_Number = 0 + getParameter(InputValue,  "ROOMNUMBER").trim()  ;
			date =  getParameter(InputValue,  "DATE")  ;
			timeslot=  getParameter(InputValue,  "SLOT").replaceAll("\\s+","")  ;
			  System.out.println( "RECIVED booking room_Number:" +room_Number
            		  + "date" + date + "timeslot" + timeslot  ); 
			  list_Of_Time_Slots = new String[1];
			  list_Of_Time_Slots[0] =timeslot ;
			
			  RoomReservationSystem.createRoom(room_Number, date, list_Of_Time_Slots);
			}
	else if (method.equals("deleteRoom"))
			{
			room_Number =  getParameter(InputValue,  "ROOMNUMBER")  ;
			date =  getParameter(InputValue,  "DATE")  ;
			timeslot=  getParameter(InputValue,  "SLOT")  ; 
			RoomReservationSystem.deleteRoom(room_Number, date, list_Of_Time_Slots);
			}
	else if (method.equals("bookRoom"))
			{
			room_Number = 0 + getParameter(InputValue,  "ROOMNUMBER").trim()  ;
			campus_Name =  getParameter(InputValue,  "CAMPUS").toUpperCase()  ;
			date =   getParameter(InputValue,  "DATE")  ; 
			timeslot =   getParameter(InputValue,  "SLOT").replaceAll("\\s+","")   ; 
			StudentID =   getParameter(InputValue,  "STUDENT_ID")  ;
			 System.out.println( "RECIVED booking campus_Name " + campus_Name  + "room_Number" + room_Number
            		  + "date" + date + "timeslot" + timeslot + "StudentID" + StudentID  ); 
			 RoomReservationSystem.bookRoom(campus_Name, room_Number, date, timeslot, StudentID);
			}
	else if (method.equals("getAvailableTimeSlot"))
			{
			date =   getParameter(InputValue,  "DATE")  ;
			RoomReservationSystem.getAvailableTimeSlot(date);
			}
	else if (method.equals("cancelBooking"))
			{
			booking_ID  =   getParameter(InputValue,  "BOOKING_ID")  ; 
			StudentID =   getParameter(InputValue,  "STUDENT_ID")  ;
			RoomReservationSystem.cancelBooking(booking_ID, StudentID);
			}
	else if (method.equals("changeReservation"))
			{
			campus_Name =  getParameter(InputValue,  "CAMPUS")  ;
			room_Number =  "0"+ getParameter(InputValue,  "ROOMNUMBER")  ;
			date =   getParameter(InputValue,  "DATE")  ; 
			timeslot =   getParameter(InputValue,  "SLOT")  ; 
			StudentID =   getParameter(InputValue,  "STUDENT_ID")  ;
			booking_ID  =   getParameter(InputValue,  "BOOKING_ID")  ; 
			RoomReservationSystem.changeReservation(booking_ID, campus_Name, room_Number, timeslot, date, StudentID);
			}
		}
		in.close();
		return this.RoomReservationSystem;
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



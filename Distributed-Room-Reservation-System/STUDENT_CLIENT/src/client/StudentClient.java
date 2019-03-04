package client;

import java.util.Arrays;
import java.util.HashMap;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import cmd.CMD_IO;
import log.FileLogger;
import student.StudentInterface;

public class StudentClient {

	static String SERVER_NAME = "none";
    static HashMap<String, Integer> REMOTE_PORTS = new HashMap<String, Integer>() {{
    	put("dvl",8080);
    	put("kkl",8081);
        put("wst",8082);
    }};

	static FileLogger fileLogger = null;
	static final String[] AVAILABE_SERVERS = new String[] { "dvls", "kkls", "wsts" };
	static StudentInterface remoteReference = null;
	static CMD_IO io  = null;
	static String studentID = "none";
	
	public static void main(String args[]) {
	       	
		   io = new CMD_IO();
		   while(!isValidCredential(studentID)){
			   studentID = io.askQuestion("Please Enter the Student ID");		   
		   }
		   
		   fileLogger = new FileLogger(studentID);	   
		   fileLogger.writeLog("You have login");
		   SERVER_NAME = studentID.substring(0,3);
		   print("Connecting.. to Server " +SERVER_NAME);
		   remoteReference = connectToServer(args);
		   if(remoteReference != null){		  
			   fileLogger.writeLog("Connected to server");
			   System.out.println("Successfully connected");
		       popHelp();
		       takeInput();
		   } 
	}	
	

	public static StudentInterface connectToServer(String[] args){
	  
		ORB orb = ORB.init(args, null);
		StudentInterface st;
	    try {
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
	        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
	        st = (StudentInterface) ncRef.resolve_str("fe_student_service");
	        return st;
		} catch (InvalidName | NotFound | CannotProceed | org.omg.CosNaming.NamingContextPackage.InvalidName e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		  return null;
   }	
	
	
	private static void takeInput() {
	       switch(io.askQuestion("").replaceAll("[\\[\\]]", "")){
	  		case "1":
	  			viewAvailbleSlots();
	  			break;
	  		case "2":
	  			bookSlot();
	  		case "3":
	  			cancelSlot();	
	  		case "4":
	  			changeSlot();
	  		default:
	  			takeInput();
	        }		
		}
	
	private static void changeSlot() {
		// TODO Auto-generated method stub
		String bookingId = io.askQuestion("Enter the booking ID");
		String roomNumber,campusName,success,newDate;
		roomNumber = campusName = success =  newDate = "";
		while(roomNumber.length()<1){
		    roomNumber = io.askQuestion("Enter the Room Number");
		}
		String timeslot = "";	
		if(!timeslot.matches("[0-2]?[0-9]:[0-5][0-9] - [0-2]?[0-9]:[0-5][0-9]")){
			timeslot = io.askQuestion("Enter the time slot[example: 12:30 - 14:30]");  
		}
		while(!newDate.matches("[0-3][0-9]-[01][0-9]-\\d{4}")){
			  newDate = io.askQuestion("Enter new date[dd-mm-yyyy]");
		}
		while(!(campusName.length() > 2)){
		  campusName = io.askQuestion("Enter Campus name");
		}
		try{
		   success = remoteReference.changeReservation(SERVER_NAME,bookingId,campusName,roomNumber,timeslot,studentID,newDate);		   
		   if(success.equals("fail")){
			  print("Failed to book a room");   
		   }else if(success.equals("overbooked")){
			  print("You can book only three slots a week"); 
		   }else{
			   print("Your new booking ID "+success);
			   print("Your previous booking was canceled");
		   } 
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			takeInput();
		}
	}



	private static void cancelSlot() {
		String cancelId = io.askQuestion("Enter the booking ID");
		
		try {
			boolean success;
			success = remoteReference.cancelBooking(SERVER_NAME,cancelId,studentID);
			
			if(!success){
		    	print("Failed to delete ensure your bookingID and studentID are right");
		    }else{
		    	print("Success");
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			takeInput();
		}
	}



	private static void bookSlot() {
		String roomNumber,date,campusName,success;
		roomNumber = date = campusName = success = "";
		while(roomNumber.length()<1){
		    roomNumber = io.askQuestion("Enter the Room Number");
		}
		while(!date.matches("[0-3][0-9]-[01][0-9]-\\d{4}")){
		  date = io.askQuestion("Enter date[dd-mm-yyyy] on which you want to book a slot");
		}
    	String timeslot = "";	
		if(!timeslot.matches("[0-2]?[0-9]:[0-5][0-9] - [0-2]?[0-9]:[0-5][0-9]")){
			timeslot = io.askQuestion("Enter the time slot[example: 12:30 - 14:30]");  
		}
		while(!(campusName.length() > 2)){
		  campusName = io.askQuestion("Enter Campus name");
		}
		try{
		   success = remoteReference.bookRoom(SERVER_NAME,campusName,studentID,roomNumber, date, timeslot);		   
		   if(success.equals("fail")){
			  print("Failed to book a room");   
		   }else if(success.equals("overbooked")){
			  print("You can book only three slots a week"); 
		   }else{
			   print("Your booking ID "+success);
		   } 
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			takeInput();
		}
	}



	private static void viewAvailbleSlots() {
		String date="none";
		while(!date.matches("[0-3][0-9]-[01][0-9]-\\d{4}")){
		  date = io.askQuestion("Enter date[dd-mm-yyyy] on which you want to check availabe slots");
		}        
		try {
		  String result= remoteReference.getAvailableTimeSlot(SERVER_NAME,date);
		  print(result);		    
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			takeInput();
		}
	}


	public static void popHelp(){
		   print("Choose one of the option from");
		   print("[1] To View Available Room Slots");
		   print("[2] To book a room");
		   print("[3] To cancel booking");
		   print("[4] To change booking");
	   }
	
	private static boolean isValidCredential(String input){
		   if(input.length() != 8){
			   return false;
		   }  
		   return (Arrays.asList(AVAILABE_SERVERS).contains(input.substring(0,4)) && 
		    		  input.substring(4,7).matches("^-?\\d+$") );	   
	 }	
	
	 public static void print(String message){
		   System.out.println(message);
	 }
	 
	
	 
	 
}

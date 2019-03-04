package client;

import java.util.ArrayList;
import java.util.Arrays;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import admin.AdminInterface;
import admin.AdminInterfaceHelper;
import cmd.CMD_IO;
import log.FileLogger;

public class AdminClient {

	static String SERVER_NAME = "none";
	static FileLogger fileLogger = null;
	static final String[] AVAILABE_SERVERS = new String[] { "dvla", "kkla", "wsta" };
	static CMD_IO io  = null;
	static AdminInterface remoteReference = null;
	public static void main(String args[]) {
       	
	   String adminID = "none";
	   io = new CMD_IO();
	   while(!isValidCredential(adminID)){
		   adminID = io.askQuestion("Please Enter the Administrator ID");		   
	   }
	   fileLogger = new FileLogger(adminID);	   
	   fileLogger.writeLog("You have login");
	   SERVER_NAME = adminID.substring(0,3);
	   print("Connecting.. to Server " +SERVER_NAME);
	   remoteReference = connectToServer(args);
	   if(remoteReference != null){		  
		   fileLogger.writeLog("Connected to server");
		   System.out.println("Successfully connected");
	       popHelp();
	       takeInput();
	   }
	}
	
   private static boolean isValidCredential(String input){
	   if(input.length() != 8){
		   return false;
	   }  
	   return (Arrays.asList(AVAILABE_SERVERS).contains(input.substring(0,4)) && 
	    		  input.substring(4,7).matches("^-?\\d+$") );	   
   }	
	
   private static void takeInput() {
       switch(io.askQuestion("").replaceAll("[\\[\\]]", "")){
  		case "1":
  			addRoom();
  			break;
  		case "2":
  			deleteSlot();
  			break;
  		default:
  			takeInput();
        }		
	}



private static void deleteSlot(){
	   String date, roomNumber;
	   ArrayList<String> slots = new ArrayList<String>();	
	   date = io.askQuestion("Enter date[dd-mm-yyyy] on which you want to delete the record");
	   roomNumber = io.askQuestion("Enter the Room Number");
	   slots.addAll(io.takeMultilineInput("Enter List of time slots[example: 12:30 - 13:30] in multi line "));	   
	   print("Sending data....");
	   String[] slotArr = new String[slots.size()];
	   slotArr = slots.toArray(slotArr);

	   try {
	    	boolean isSuccess = remoteReference.deleteRoom(SERVER_NAME,roomNumber, date, slotArr);
		    if(!isSuccess){
		    	fileLogger.writeLog("Unable to delete data as not yest existed on "+date+" at "+roomNumber);
		    	print("Unable to delete the slot, as slot not yet created");
		    }else{
		    	print("Deleted");
		    	fileLogger.writeLog("Unable to delete data as not yest existed on "+date+" at "+roomNumber);	
		    }	
	   } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	   } finally{
			takeInput();
	   }
	}


public static AdminInterface connectToServer(String[] args){
    ORB orb = ORB.init(args, null);
	AdminInterface st;
    try {
		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
        st = AdminInterfaceHelper.narrow(ncRef.resolve_str("fe_admin_service"));
        return st;
	} catch (InvalidName | NotFound | CannotProceed | org.omg.CosNaming.NamingContextPackage.InvalidName e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  return null;
   }	
	
 
   public static void popHelp(){
	   print("Choose one of the option from");
	   print("[1] To Add the Room Slots");
	   print("[2] To Delete Room Slots");
   }
   
   public static void addRoom(){
	   String date="none", roomNumber="";
	   ArrayList<String> slots = new ArrayList<String>();	
	   
	   while(!date.matches("[0-3][0-9]-[01][0-9]-\\d{4}")){
	    date = io.askQuestion("Enter date[dd-mm-yyyy] on which you want to enter the record");
	   }
	   
	   while(roomNumber.length()<1){
	    roomNumber = io.askQuestion("Enter the Room Number");
	   }
	   
	   slots.addAll(io.takeMultilineInput("Enter List of time slots[example: 12:30 - 14:30] in multi line"));	   
	   
	   print("Sending data....");
	    try {
	    	ArrayList<String> tmp = new ArrayList<>();
	    	for(String s: slots){
	    		if(s.length()>2)
	    			tmp.add(s);
	    	}
	    	String[] slotArr = new String[tmp.size()];
	    	slotArr = tmp.toArray(slotArr);
	    	boolean isSuccess = remoteReference.createRoom(SERVER_NAME,roomNumber, date, slotArr);
	    	fileLogger.writeLog((isSuccess?"SUCCESS":"FAILED")+" Creating room on "+date+" at "+roomNumber,slots);
            if(isSuccess){
            	print("Inserted Successfully");
            }		
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			takeInput();
		}
	    
   }   
   
   public static void print(String message){
	   System.out.println(message);
   }
   
}

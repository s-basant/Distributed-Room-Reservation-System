package Replica;


import admin.AdminImpl;
import model.RoomRecord;
import server.UDPThrift;
import student.StudentImpl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;

import Utility.JournalLogger;


public class Replica {

    public AdminImpl DVLAdminServant ;
    public StudentImpl DVLStudentServant;
    public AdminImpl KKLAdminServant ;
    public StudentImpl KKLStudentServant;
    public AdminImpl WSTAdminServant ;
    public StudentImpl WSTStudentServant;
   
    
    public String replicaId;

    public Replica(String replicaId) throws UnknownHostException {
        this.replicaId = replicaId;
    }

    //Create three replicas each one for separate server DVL, KKL, WST based on replica ID
    void create() {
        try {
            
        	    RoomRecord dvlrecord = new RoomRecord();
            	DVLAdminServant = new AdminImpl("dvl", replicaId,dvlrecord);
            	DVLStudentServant = new StudentImpl("dvl", false, dvlrecord);
            	
            	RoomRecord kklrecord = new RoomRecord();
            	KKLAdminServant = new AdminImpl("kkl", replicaId,kklrecord);
            	KKLStudentServant = new StudentImpl("kkl", false, kklrecord);
            	
            	RoomRecord wstrecord = new RoomRecord();
            	WSTStudentServant = new StudentImpl("wst", false, wstrecord);
            	WSTAdminServant = new AdminImpl("wst", replicaId,wstrecord);
                
            	RServer dvl = new RServer(DVLAdminServant, DVLStudentServant, 4001);
                new Thread(new Runnable() {					
					@Override
					public void run() {
						dvl.start();						
					}
				}).start();
            	
                
                System.out.println("started dvl");
                RServer kkl = new RServer(KKLAdminServant, KKLStudentServant, 4002);
                new Thread(new Runnable() {					
					@Override
					public void run() {
						kkl.start();						
					}
				}).start();
                
                System.out.println("started kkl");
                RServer wst = new RServer(WSTAdminServant, WSTStudentServant, 4003);
                new Thread(new Runnable() {					
					@Override
					public void run() {
						wst.start();						
					}
				}).start();
                            
                System.out.println("started wst");
                new Thread(new Runnable() {			
        			@Override
        			public void run() {
        				UDPThrift th = new UDPThrift();
        				th.init(8090,"dvl", new StudentImpl("dvl", true,dvlrecord));
        			}
        		}).start();
                
                new Thread(new Runnable() {			
        			@Override
        			public void run() {
        				UDPThrift th = new UDPThrift();
        				th.init(8091,"kkl", new StudentImpl("kkl", true,kklrecord));
        			}
        		}).start();
                
                new Thread(new Runnable() {			
        			@Override
        			public void run() {
        				UDPThrift th = new UDPThrift();
        				th.init(8092,"wst", new StudentImpl("wst", true,wstrecord));
        			}
        		}).start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //In case of any genuine error, shutdown the replica and restart it gracefully
    public void shutDown(String serverName, String replicaId) {
        
    }

    //recreate if reaches max 3
	public void recreate(String serverName) {
		
		try {
			BufferedReader in = new BufferedReader(
					new FileReader("c://journels/manohar.txt"));
			
			DVLAdminServant.roomrecord.clear();
			KKLAdminServant.roomrecord.clear();
			WSTAdminServant.roomrecord.clear();
			
			String inputString = "";
			
			while ((inputString = in.readLine()) != null) {
				String method = getParameter(inputString,  "METHOD")  ; 
				String room_Number ;
				String date; 
				String[] list_Of_Time_Slots = null ;
				String timeslot;
				String campus_Name ;
				String StudentID;
				String booking_ID;			
				String toCampus = getParameter(inputString,"TO_CAMPUS");
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
						
						getAdminObject(toCampus).createRoom(room_Number, date, list_Of_Time_Slots);
						}
				else if (method.equals("deleteRoom"))
						{
						room_Number =  getParameter(inputString,  "ROOMNUMBER")  ;
						date =  getParameter(inputString,  "DATE")  ;
						timeslot=  getParameter(inputString,  "SLOT")  ; 
						getAdminObject(toCampus).deleteRoom(room_Number, date, list_Of_Time_Slots);
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
						getStudentObject(toCampus).bookRoom(campus_Name, StudentID, room_Number, date, timeslot);
						}
				else if (method.equals("getAvailableTimeSlot"))
						{
						date =   getParameter(inputString,  "DATE")  ;
						getStudentObject(toCampus).getAvailableTimeSlot(date);
						}
				else if (method.equals("cancelBooking"))
						{
						booking_ID  =   getParameter(inputString,  "BOOKING_ID")  ; 
						StudentID =   getParameter(inputString,  "STUDENT_ID")  ;
						getStudentObject(toCampus).cancelBooking(booking_ID, StudentID);
						}
				else if (method.equals("changeReservation"))
						{
						campus_Name =  getParameter(inputString,  "CAMPUS")  ;
						room_Number =  "0"+ getParameter(inputString,  "ROOMNUMBER")  ;
						date =   getParameter(inputString,  "DATE")  ; 
						timeslot =   getParameter(inputString,  "SLOT")  ; 
						StudentID =   getParameter(inputString,  "STUDENT_ID")  ;
						booking_ID  =   getParameter(inputString,  "BOOKING_ID")  ; 
						getStudentObject(toCampus).changeReservation(booking_ID, campus_Name, room_Number, timeslot, StudentID, date);
				}
			    	
			}
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
	
	public StudentImpl getStudentObject(String serverName){
		
		if(serverName.equals("dvl")){
		  return DVLStudentServant;	
		}else if(serverName.equals("kkl")){
			return KKLStudentServant;
		} else if(serverName.equals("wst")){
			return WSTStudentServant;
		}
		return null;
	}
	
	public AdminImpl getAdminObject(String serverName){
		if(serverName.equals("dvl")){
			  return DVLAdminServant;	
			}else if(serverName.equals("kkl")){
				return KKLAdminServant;
			} else if(serverName.equals("wst")){
				return WSTAdminServant;
			}
			return null;
	}
	
}

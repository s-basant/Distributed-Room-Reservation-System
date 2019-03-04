package Replica;


import admin.AdminImpl;
import model.GlobalFlags;
import model.RoomRecord;
import reservationSystem.DVLImpl;
import reservationSystem.IRoomInterface;
import reservationSystem.KKLImpl;
import reservationSystem.WSTImpl;
import server.UDPThrift;
import student.StudentImpl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;

import Utility.JournalLogger;


public class Replica {

    public IRoomInterface DVLServant ;
    public IRoomInterface KKLServant ;
    public IRoomInterface WSTServant ;
    
    
    public String replicaId;

    public Replica(String replicaId) throws UnknownHostException {
        this.replicaId = replicaId;
    }

    //Create three replicas each one for separate server DVL, KKL, WST based on replica ID
    void create() {
        try {
            
        	    DVLServant = new DVLImpl();
            	RServer dvl = new RServer(DVLServant, 5001);
                new Thread(new Runnable() {					
					@Override
					public void run() {
						dvl.start();						
					}
				}).start();
            	
                
                System.out.println("started dvl");
                
                KKLServant = new KKLImpl();
            	RServer kkl = new RServer(KKLServant, 5002);
                new Thread(new Runnable() {					
					@Override
					public void run() {
						kkl.start();						
					}
				}).start();
                
                
                WSTServant = new WSTImpl();
            	RServer wst = new RServer(WSTServant, 5003);
                new Thread(new Runnable() {					
					@Override
					public void run() {
						wst.start();						
					}
				}).start();
                
                System.out.println("started wst");
                
                
            	final DVLImpl DImpl = new DVLImpl();

                new Thread(new Runnable() {
        			
        			@Override
        			public void run() {
        				
        				try {
        					DImpl.callUDPServer(6789, "DVL");
        				} catch (IOException e) {
        					
        					e.printStackTrace();
        				}	
        			}
        		}).start();
                
               
                final KKLImpl KImpl =new KKLImpl();
            	
           	 new Thread(new Runnable() {
           			
           			@Override
           			public void run() {
           				
           				try {
           					KImpl.callUDPServer(6000, "KKL");
           				} catch (IOException e) {
           					
           					e.printStackTrace();
           				}	
           			}
           		}).start();
           	 
           	 
           	final WSTImpl WImpl =new WSTImpl();
        	
       	 new Thread(new Runnable() {
       			
       			@Override
       			public void run() {
       				
       				try {
       					WImpl.callUDPServer(7000, "KKL");
       				} catch (IOException e) {
       					
       					e.printStackTrace();
       				}	
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
					new FileReader("c://journels/pallavi.txt"));
			
			DVLImpl.bookedRoom.clear();
			DVLImpl.bookingCounter.clear();
			DVLImpl.bookingNo = 0;
			DVLImpl.createdNewRoom.clear();
			DVLImpl.finalBookedRecords.clear();
			DVLImpl.roomRecords.clear();
			DVLImpl.submap.clear();
			
			KKLImpl.bookedRoomKKL.clear();
			KKLImpl.bookingCounterKKL.clear();
		    
			KKLImpl.createdNewRoomKKL.clear();
			KKLImpl.finalBookedRecordsKKL.clear();
			KKLImpl.roomRecordsKKL.clear();
			KKLImpl.submapKKL.clear();
			
			
			WSTImpl.bookedRoomWST.clear();
			WSTImpl.bookingCounterWST.clear();
		    
			WSTImpl.createdNewRoomWST.clear();
			WSTImpl.finalBookedRecordsWST.clear();
			WSTImpl.roomRecordsWST.clear();
			WSTImpl.submapWST.clear();
			
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
						
						getAdminObject(toCampus).createRoom(room_Number, date, list_Of_Time_Slots[0]);
						}
				else if (method.equals("deleteRoom"))
						{
						room_Number =  getParameter(inputString,  "ROOMNUMBER")  ;
						date =  getParameter(inputString,  "DATE")  ;
						timeslot=  getParameter(inputString,  "SLOT")  ; 
						getAdminObject(toCampus).deleteRoom(room_Number, date, list_Of_Time_Slots[0]);
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
						getStudentObject(toCampus).bookRoom(campus_Name, room_Number, date, timeslot, StudentID);
						}
				else if (method.equals("getAvailableTimeSlot"))
						{
						date =   getParameter(inputString,  "DATE")  ;
						getStudentObject(toCampus).getAvailableTimeSlot(toCampus,date);
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
	
	public IRoomInterface getStudentObject(String serverName){
		
		if(serverName.equals("dvl")){
		  return DVLServant;	
		}else if(serverName.equals("kkl")){
			return KKLServant;
		} else if(serverName.equals("wst")){
			return WSTServant;
		}
		return null;
	}
	
	public IRoomInterface getAdminObject(String serverName){
		if(serverName.equals("dvl")){
			  return DVLServant;	
			}else if(serverName.equals("kkl")){
				return KKLServant;
			} else if(serverName.equals("wst")){
				return WSTServant;
			}
			return null;
	}

	public void restart() {

		GlobalFlags.isServerCrash = false;
		
		try {
			BufferedReader in = new BufferedReader(
					new FileReader("c://journels/pallavi.txt"));
			
			DVLImpl.bookedRoom.clear();
			DVLImpl.bookingCounter.clear();
			DVLImpl.bookingNo = 0;
			DVLImpl.createdNewRoom.clear();
			DVLImpl.finalBookedRecords.clear();
			DVLImpl.roomRecords.clear();
			DVLImpl.submap.clear();
			
			KKLImpl.bookedRoomKKL.clear();
			KKLImpl.bookingCounterKKL.clear();
		    
			KKLImpl.createdNewRoomKKL.clear();
			KKLImpl.finalBookedRecordsKKL.clear();
			KKLImpl.roomRecordsKKL.clear();
			KKLImpl.submapKKL.clear();
			
			
			WSTImpl.bookedRoomWST.clear();
			WSTImpl.bookingCounterWST.clear();
		    
			WSTImpl.createdNewRoomWST.clear();
			WSTImpl.finalBookedRecordsWST.clear();
			WSTImpl.roomRecordsWST.clear();
			WSTImpl.submapWST.clear();
			
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
						
						getAdminObject(toCampus).createRoom(room_Number, date, list_Of_Time_Slots[0]);
						}
				else if (method.equals("deleteRoom"))
						{
						room_Number =  getParameter(inputString,  "ROOMNUMBER")  ;
						date =  getParameter(inputString,  "DATE")  ;
						timeslot=  getParameter(inputString,  "SLOT")  ; 
						getAdminObject(toCampus).deleteRoom(room_Number, date, list_Of_Time_Slots[0]);
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
						getStudentObject(toCampus).bookRoom(campus_Name, room_Number, date, timeslot, StudentID);
						}
				else if (method.equals("getAvailableTimeSlot"))
						{
						date =   getParameter(inputString,  "DATE")  ;
						getStudentObject(toCampus).getAvailableTimeSlot(toCampus,date);
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
	
}

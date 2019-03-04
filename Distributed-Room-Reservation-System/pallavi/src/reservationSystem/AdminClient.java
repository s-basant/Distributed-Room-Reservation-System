package reservationSystem;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;




public class AdminClient {

	static Scanner userInput = new Scanner(System.in);
	 static int adminChoice = 0;
	 static int studentChoice =0;
	 static Scanner studInput = new Scanner(System.in);
	 static String username ="" ;
	 static RoomDetails retunObj = new RoomDetails();
	// static bookingDetails bookEntry = new bookingDetails();
	 static HashMap<String,HashMap<String,HashMap<String,HashMap<String,HashMap<Integer,ArrayList<String>>>>>> allBookedRecords = new HashMap<String,HashMap<String,HashMap<String,HashMap<String,HashMap<Integer,ArrayList<String>>>>>>();
	 static int count = 0;
	
	public static void main(String args[]) throws Exception,MalformedURLException 
		{
		
		
		IRoomInterface interfaceObj =null;
		
		 logDetails("Admin Client Started");
		 
		 System.out.println("  ROOM RESERVATION SYSTEM FOR MONT UNIVERSITY ");
		 System.out.println("************************************************");
		    
		
		
		 System.out.println("PLEASE ENTER YOUR USERNAME :");
			
		 username = userInput.next();
		 
	
		
		 String validatedUser =  tryValidate(username);
		
		
		 System.out.println(validatedUser);
				
		/* String name1 = "Hello1";
	       interfaceObj = IRoomInterfaceHelper.narrow(ncRef.resolve_str(name1));
	       */
		
		 if(validatedUser.toUpperCase().contains("DVL"))
		 {
			 URL DVLServerURL = new URL("http://localhost:2222/DVLImpl?wsdl");
				QName qNameDVL = new QName("http://reservationSystem/","DVLImplService");
				Service serviceDVL = Service.create(DVLServerURL, qNameDVL);
				 interfaceObj =serviceDVL.getPort(IRoomInterface.class);
		 }
		 else if(validatedUser.toUpperCase().contains("KKL"))
		 {	

			 URL KKLServerURL = new URL("http://localhost:5265/KKLImpl?wsdl");
				QName qNameKKL = new QName("http://reservationSystem/","KKLImplService");
				Service serviceKKL = Service.create(KKLServerURL, qNameKKL);
				 interfaceObj =serviceKKL.getPort(IRoomInterface.class);
		 }
		 else if(validatedUser.toUpperCase().contains("WST"))
		 {
			 URL WSTServerURL = new URL("http://localhost:5266/WSTImpl?wsdl");
				QName qNameWST = new QName("http://reservationSystem/","WSTImplService");
				Service serviceWST = Service.create(WSTServerURL, qNameWST);
				 interfaceObj =serviceWST.getPort(IRoomInterface.class);
		 }
		
		 if(validatedUser.contains("admin"))
		 {
			 while(true)
				{
			 System.out.println("ADMINISTRATION OPTIONS");
			 System.out.println("*****************************************");
			 System.out.println("Select the action you want to perform");
			 System.out.println("1. Create Room");
			 System.out.println("2. Delete Room"); 
			 System.out.println("3. Exit System"); 
			
			adminChoice= userInput.nextInt();
			RoomDetails setVal = new RoomDetails();
			
			//create room option
			if(adminChoice==1)
			{
			setVal=setDataCreate();
			
			//HashMap<String,HashMap<Integer,ArrayList<String>>>  createdRoom = new  HashMap<String,HashMap<Integer,ArrayList<String>>>();		
			String createdRoomNew ="";
			System.out.println("Reach here!");
			//call server method for create room
			createdRoomNew = interfaceObj.createRoom( setVal.creatDate,setVal.roomNo, setVal.timeslots);
		
			if(createdRoomNew.contains("Duplicate"))
			{
				System.out.println(createdRoomNew);
			}
			else

			{
				//display records of all created rooms
				System.out.println("Record created for date "+setVal.creatDate.toString()+" Room No "+ setVal.roomNo + " at "+setVal.timeslots);
		
			}
		 			
			logDetails("Call by admin"+username+"created rooms : " + createdRoomNew);
		
			}
			//delete room option
			else if(adminChoice==2)
			{
				HashMap<String,HashMap<Integer,String>> allRecords = new HashMap<String,HashMap<Integer,String>>();
				
				
				//get user inputs for delete method
				setVal = setDataDelete();
				String delStatuss = "";
				
				//call server method to delete room
				delStatuss=interfaceObj.deleteRoom(setVal.roomNo,setVal.creatDate,setVal.timeslots);

				//call server method to display all remaining records
				//allRecords = interfaceObj.getRecords();
				
				//show the deletion status and remaining records to the user
				System.out.println(delStatuss);
				//System.out.println("Remaining records are: "+ allRecords);
				logDetails("Delete executed by admin"+username);
				
			
			 }
			//option to exit the system
			else if(adminChoice==3)
			{
				terminateApp();				
				
			}
			else
			{
				System.out.println("Enter a valid option");
				 adminChoice = userInput.nextInt();
				
			}
			
		 }
		}
		}
	
	//method to send data to log file
	public static void logDetails(String addMessage)
	 {
	//method to create client logs
		try
		{
		 AppLogs newLog = new AppLogs("AdminLogs.txt");
		 
		 newLog.logs.info(addMessage);
		}
		catch(Exception e)
		{
			
			
		}
	 }
	 
	//method to set user values for creating room
	 public static RoomDetails  setDataCreate()
	 {
			try {
				
				
				 System.out.println("Enter room number to book(Only Numeric)");
				 String roomNumber = userInput.next();
				 
				 
				 System.out.println("Enter date when room should be created");
				 String creationDate = userInput.next();
				 
				 
			//	 System.out.println("Available time slots are : 10am to 12 pm and 12 pm to 2pm"); 
				// System.out.println("How many time slots do you want to book ?");
				
				/* int countTime=0;
				 countTime =userInput.nextInt();*/
				
				 System.out.println("Enter the desired time slot");	 
				// ArrayList<String> timeSlots = new ArrayList<String>();
				String timeSlotsNew ="";
				 //so that user can enter more than 1 time slot
				 /*for(int i=0;i<countTime;i++)
				 {					
					// timeSlots.add(userInput.next());	
					 timeSlotsNew=timeSlotsNew+userInput.next()+",";
				 }*/
				 
				 timeSlotsNew=userInput.next();
				retunObj.roomNo=roomNumber;
				retunObj.creatDate=creationDate;
				retunObj.timeslots=	timeSlotsNew;
				
				}
			 catch (Exception e) {
				
				e.printStackTrace();
			}
			
			return retunObj;
			 
		 }
	 
	//method to set user values for deleting room
	 public static RoomDetails  setDataDelete() throws Exception
	 {
		 System.out.println("Enter room number to delete(Only Numeric)");
		 String roomNumber = userInput.next();
		 retunObj.roomNo=roomNumber;
		 
	 
		 System.out.println("Enter date when room should be deleted");
		 String deletionDate = userInput.next();
		 retunObj.creatDate = deletionDate;
		 
		 /*int countTime=0;
		 System.out.println("How many time slots you want to delete?");
		 countTime =userInput.nextInt();*/

		 System.out.println("Enter the time slots to delete");
		// ArrayList<String> delSlots = new ArrayList<String>();
		 String delSlotsNew ="";
		//so that user can enter more than 1 time slot
		/* for(int i=0;i<countTime;i++)
		 {					
			// delSlots.add(userInput.next());
			 delSlotsNew=userInput.next()+",";
		
		 }*/
		 delSlotsNew=userInput.next();
		 retunObj.timeslots=delSlotsNew;
		 
		 
		 System.out.println("Delete Room");
		 return retunObj;
		 
	 }
	
	//validate user,only admin can access options on AdminClient 
	 public static String tryValidate(String userName)
	 {
		 String userRole = "" ;
			
			if(userName!=null)
			{
			switch(userName.toUpperCase())
				{
				case "DVLA1111" :userRole ="adminDVL";
				break;
				case "KKLA1111" :userRole ="adminKKL";
				break;
				case "WSTA1111" :userRole ="adminWST";
				break;
			
				default: userRole ="You can only proceed if you are a valid user! ";
				}
			
			}
			
			return userRole;
		 
	 }
	 
	 //method to terminate app
	 public static void terminateApp()
	 {
		 System.out.println("Terminating...");
		 System.exit(0);
		 
	 }

}


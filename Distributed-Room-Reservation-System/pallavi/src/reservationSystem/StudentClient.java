package reservationSystem;


import java.net.URL;

import java.util.HashMap;

import java.util.Scanner;


import javax.xml.namespace.QName;
import javax.xml.ws.Service;



public class StudentClient {
	static Scanner userInput = new Scanner(System.in);
	static int adminChoice = 0;
	static int studentChoice =0;
	static Scanner studInput = new Scanner(System.in);
	static String username ="" ;
	static RoomDetails retunObj = new RoomDetails();
	static bookingDetails bookEntry = new bookingDetails();
	static HashMap<String,HashMap<String,HashMap<String,HashMap<String,HashMap<String,String>>>>> allBookedRecords = new HashMap<String,HashMap<String,HashMap<String,HashMap<String,HashMap<String,String>>>>>();
	static int count = 0;
	
	
	public static void main(String[] args) throws Exception
	{
		 System.out.println("  ROOM RESERVATION SYSTEM FOR MONT UNIVERSITY ");
		 System.out.println("************************************************");
		    
		 
		 IRoomInterface interfaceObj =null;
		 System.out.println("PLEASE ENTER YOUR USERNAME :");
			
		 username = userInput.next();
	
		 String validatedUser =  tryValidate(username);
		
		
		 System.out.println(validatedUser);
		 
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
		  
		 while(true) 
		  {
			 if(validatedUser.contains("OK"))
		  
		 {
			 bookingDetails studBooking =new bookingDetails();
			 System.out.println("STUDENT OPTIONS");
			 System.out.println("******************************************");
			 System.out.println("Select the action you want to perform");
			 System.out.println("1. Book Room");
			 System.out.println("2. Check available time slots");
			 System.out.println("3. Cancel booking");
			 System.out.println("4. Change reservation");
			 System.out.println("5. Exit System"); 
			 
			 studentChoice= userInput.nextInt();
			 
			 if(studentChoice==1)
			 {
				 String bookServer="";
				
				 String result ="";
				 studBooking= setBookRoomData();

				/* Runnable t1 = new Runnable() {
						public void run() {
							try {
								result =interfaceObj.bookRoom(studBooking.campusName, studBooking.roomNo, studBooking.bookDate, studBooking.bookSlots,username);
			//System.out.println(result + "\n");
				System.out.println("Successfully booked"+" "+result+ "\n");
				
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					
					Runnable t2 = new Runnable() {
						public void run() {
							try {
								result =interfaceObj.bookRoom(studBooking.campusName, studBooking.roomNo, studBooking.bookDate, studBooking.bookSlots,username);
			//System.out.println(result + "\n");
				System.out.println("Successfully booked"+" "+result+ "\n");
				
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					Runnable t3 = new Runnable() {
						public void run() {
							try {
								result =interfaceObj.bookRoom(studBooking.campusName, studBooking.roomNo, studBooking.bookDate, studBooking.bookSlots,username);
			//System.out.println(result + "\n");
				System.out.println("Successfully booked"+" "+result+ "\n");
				
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					new Thread(t1).start();
					new Thread(t2).start();
					new Thread(t3).start();*/
				 
				 result =interfaceObj.bookRoom(studBooking.campusName, studBooking.roomNo, studBooking.bookDate, studBooking.bookSlots,username);
				 
				 if(result.trim().length()!=0)
				 {
					 System.out.println(result);
					 count++;
				
				if(validatedUser.toUpperCase().contains("WST"))
				{
					bookServer="WST";
					
				}
				else if(validatedUser.toUpperCase().contains("DVL"))
				{
					bookServer="DVL";
					
				}
				else if(validatedUser.toUpperCase().contains("KKL"))
					{
						bookServer="KKL";
						
					}
					
			      System.out.println("Room Booked by student "+username + result);
				
				 logDetails("Record booked by student : "+username);


				 System.out.println("Record booked by student");
				 }
				 else
				 {
					 
					 if(count>3)
					 {
						 System.out.println("Not Booked");
					 }
					 else
					 {
						 System.out.println("Room already booked");
					 }
					 
					 System.out.println(result);
					 
				 }

			 }
			 else if(studentChoice==2) 
			 {
				 System.out.println("ENTER DATE TO GET AVAIALBLE TIME SLOTS");
				 System.out.println("********************************");
				 String allAvailableTime = "";
				 String date = userInput.next();
				String campus = "";
				
				if(validatedUser.toUpperCase().contains("DVL"))
				{
					campus="DVL";
					
				}else if (validatedUser.toUpperCase().contains("KKL"))
				{
					
					campus="KKL";
					
				}else
				{
					
					campus="WST";
					
				}
				
			
				 allAvailableTime = interfaceObj.getAvailableTimeSlot(campus, date);
				 
				 System.out.println("All available slots are: "+allAvailableTime);
	
				 
			 }
			 else if((studentChoice==3))
			 {
				 System.out.println("ENTER DETAILS TO CANCEL BOOKING");
				 System.out.println("********************************");
				 String bookingId = "booking id dummy"; 
				 System.out.println("BookingID : ");
				 bookingId = userInput.next();
				String result = interfaceObj.cancelBooking(bookingId,username);
			//	 allBookedRecords =	interfaceObj.getBookedRooms();
				// System.out.println(allBookedRecords);
				 //count--;
				if(result.equals("Success"))
				{
					System.out.println("Succesfull cancellation ");
					
				}
				else
				{
					System.out.println("Failure in cancellation ");	
				}
				
				 logDetails("Record "+bookingId +"cancelled by student "+username);
				 
			 }
			 else if((studentChoice==4))
			 {
				 String bookId ="";
				 String newRoom="";
				 //ArrayList<String> dummy = new ArrayList<String>();
				
				 String newCamp="";
				 String newTime="";
				 String newDate="";
				
				 System.out.println("Enter the booking id to check reservation");
				 bookId=userInput.next();
				 
				 System.out.println("campus name of new booking");
				 newCamp=userInput.next();
				 
				 
				 System.out.println("date of new booking");
				 newDate=userInput.next();
				 
				 System.out.println("room number for new booking");
				 newRoom=userInput.next();
				 
				 System.out.println("time slot of new booking");
				 newTime=userInput.next();
				
			String response=interfaceObj.changeReservation(bookId, newCamp, newRoom,newTime,username,newDate);
				 System.out.println("Change reservation result"+response);
				
			 }
			 else if((studentChoice==5))
			 {
				 
				 terminateApp();	
			 }
			 else
			 {
				 
				 System.out.println("Please choose a valid option");

			 }
			 
		 }
		 else
		 {
			 System.out.println("Please choose a valid option");
			 studentChoice = userInput.nextInt();
			 
		 }
		  }
		}

	
	public static String tryValidate(String userName)
	 {
		 String userRole = "blank" ;
			
			if(userName!=null)
			{
			switch(userName.toUpperCase())
				{
				
			case "DVLS1111":userRole ="DVL student OK";
			break;
			case "KKLS1111":userRole ="KKL student OK";
			break;
			case "WSTS1111":userRole ="WST student OK";
			break;
				default: userRole ="You can only proceed if you are a student! ";
				}
			
			}
			return userRole;
		 
		 
		 
	 }
	 public static bookingDetails setBookRoomData()
	 {
		 try
		 {
		 System.out.println("Enter the campus where you want to book room :");
		String campName = userInput.next();
		 bookEntry.campusName=campName;
		 
		 System.out.println("Enter room number :");
		 String room = userInput.next();
		 bookEntry.roomNo=room;
		 
		 System.out.println("Enter date to book :");
		 String bookingDate = userInput.next();
		 bookEntry.bookDate=bookingDate;
		 
		/* System.out.println("Available time slots are : 10am to 12 pm and 12 pm to 2pm"); 
		 System.out.println("How many time slots do you want to book ?");
		 int countTime=0;
		 countTime =userInput.nextInt();*/
		 
		 System.out.println("Enter the desired time slot");	 
		 
		// ArrayList<String> bookingSlots = new ArrayList<String>();
		 String bookingSlots="";
		 /*for(int i=0;i<countTime;i++)
		 {					
			 //bookingSlots.add(userInput.next());	
			 bookingSlots=userInput.next()+",";
		 }*/
		 
		 bookingSlots=userInput.next();
		 bookEntry.bookSlots=bookingSlots;
		 
		 }
		 catch(Exception e)
		 {
			 System.out.println("Please ener data in the correct format");
			 
		 }
		 return bookEntry;
		 
		 
	 }

	public static void terminateApp()
	 { 
		 System.out.println("Terminating...");
		 System.exit(0);
		 
	 }
	public static void logDetails(String addMessage)
	 {
	//method to create client logs
		try
		{
		 AppLogs newLog = new AppLogs("StudentLogs.txt");
		 
		 newLog.logs.info(addMessage);
		}
		catch(Exception e)
		{
			
			
		}
	 }

}


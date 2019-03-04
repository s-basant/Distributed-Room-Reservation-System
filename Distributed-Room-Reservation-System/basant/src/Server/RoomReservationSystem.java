package Server;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import java.util.Properties;

import Replica.ReplicaUDPServer;;
// endpointInterface = "packageName.interfaceName"

public class RoomReservationSystem implements RoomReservationInterface {
	
	public String BookingID;
	public String location;
	public HashMap<String,Integer> portMapping= new HashMap<String,Integer>();
	public HashMap<String,Integer> portMapping1= new HashMap<String,Integer>();

	private static Logger logger; 
	public String replicaName;
	public static DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	UDPServer udpServer;
	ReplicaUDPServer ReplicaUDPServer;
	public void shutDown() {
	        udpServer.setShouldStop(true);
	    }

	public RoomReservationSystem(String Location) throws Exception {
		portMapping.put("DVL", 9064);
		portMapping.put("KKL", 9065);
		portMapping.put("WST", 9066);
		this.location=Location;
		 UDPServer server = new UDPServer(portMapping.get(Location), this);
	    	System.out.println("For server at "+this.location+" thread listening to other server is created");
		 Thread t = new Thread(server);
		 t.start();
	}
	
	 public RoomReservationSystem (String loc, String replicaName) throws Exception {

	    	this(loc);
	    	portMapping1.put("DVL", 9061);
			portMapping1.put("KKL", 9062);
			portMapping1.put("WST", 9063);
	        this.replicaName = replicaName;
//	        initLogger(replicaName);
	 //       records = new HashMap<String, List<RecordBase>>();
	    	this.location=loc;
	    	ReplicaUDPServer server = new ReplicaUDPServer(portMapping1.get(location), this);
	    	System.out.println("For server at "+this.location+" thread listening to FE is created");
			 Thread t = new Thread(server);
			 t.start(); //Starts UDP server instance
//	        journalLogger = new JournalLogger(Constants.PROJECT_DIR + "\\" + Constants.BACKUP_DIR_NAME + "\\" +
	//                Constants.JOURNAL_DIR + "\\" + serverLocation.toString() + "\\" + replicaName + "_" +
//	                Constants.JOURNAL_FILE_NAME, serverLocation, replicaName);
	    }
	 
	//HashMap to store Room Record information
	HashMap<String ,HashMap<String ,HashMap< String, HashMap<String , String >>>> hDate_map = new  	HashMap<String ,HashMap<String ,HashMap< String, HashMap<String , String >>>>();
	HashMap<String ,HashMap< String, HashMap<String , String >>> hRoom_map = new HashMap<String ,HashMap< String, HashMap<String , String >>>();
	HashMap< String, HashMap<String , String >> hTimeslot_map = new HashMap< String, HashMap<String , String >>();
	HashMap<String , String > hBooking_map = new HashMap<String , String >();
	//HashMap to store Booking count of Students
	static HashMap< String, HashMap<Integer , Integer >> hStudent_Booking = new HashMap< String, HashMap<Integer , Integer >>();
	//Initialize variables
	String status= "failed";
	String message = "failed";
	String timeSlots;
	String booking_ID;
	
	//To create a room 
	
	public synchronized String createRoom (String room_Number,String date,String[]  list_Of_Time_Slots)
	{	
		String status= "failed";
		String message = "failed";
		Calendar cal = Calendar.getInstance();
		String currentTime= sdf.format(cal.getTime());
		if(hDate_map.containsKey(date))
			{
				if(hDate_map.get(date).containsKey(room_Number))
					{
						int count=0;
						for ( String nm : list_Of_Time_Slots )
							{
							if(!(hDate_map.get(date).get(room_Number).containsKey(nm)))
								{
									HashMap<String , String > hBooking_map1 = new HashMap<String , String >();
									booking_ID= createBookingID(  room_Number,  date,  nm);
									hBooking_map1.put(booking_ID, null );
									hDate_map.get(date).get(room_Number).put(nm,hBooking_map1);
									timeSlots = "," + nm ;
									count= count+1;
									status="success";
								}
							}	
							if(count==0)
								{
									message = "Slots are already avaialble";
								}
							else if(count== list_Of_Time_Slots.length)
								{
									message = "New time slot has been successfully added";
								}
							else message= "time slots"+ timeSlots+ "has been successfully added";
					}
				else
					{	
						HashMap< String, HashMap<String , String >> hTimeslot_map_temp = new HashMap< String, HashMap<String , String >>();
						for ( String nm : list_Of_Time_Slots )
							{
								HashMap<String , String > hBooking_map_temp = new HashMap<String , String >();
								booking_ID= createBookingID(  room_Number,  date,  nm);
								hBooking_map_temp.put(booking_ID, null );
								hTimeslot_map_temp.put(nm,hBooking_map_temp);
							}
							hDate_map.get(date).put(room_Number,hTimeslot_map_temp);
							message = "New slots has been successfully added";
							status="success";
					}
			}
		else
			{
				HashMap< String, HashMap<String , String >> hTimeslot_map_temp = new HashMap< String, HashMap<String , String >>();
				for ( String nm : list_Of_Time_Slots )
					{
						HashMap<String , String > hBooking_map1 = new HashMap<String , String >();
						booking_ID= createBookingID(  room_Number,  date,  nm);
						hBooking_map1.put(booking_ID, null );
						hTimeslot_map_temp.put(nm,hBooking_map1);
					}
				HashMap<String ,HashMap< String, HashMap<String , String >>> hRoom_map_temp = new HashMap<String ,HashMap< String, HashMap<String , String >>>();
				hRoom_map_temp.put(room_Number , hTimeslot_map_temp);
				hDate_map.put(date , hRoom_map_temp);
				message = "New Room record has been successfully created";
				status="success";
			}
		System.out.println("createRoom hStudent_Booking"+ hStudent_Booking);
		System.out.println( "createRoom HashMap View :" + hDate_map );
		 setLogger();
		 logger.info( "\n Request time: " + currentTime + "\n Request Type:"+ "createRoom"+ "\n Request parameters: " +" Room_Number "+ room_Number + " date:"+ date + " Timeslot: " + list_Of_Time_Slots + "\n Request Status: "+ status  +"\n Server response : "+ message);
		 return status ;
	}
	//To delete a Room
	public synchronized String deleteRoom (String room_Number,String date,String[] List_Of_Time_Slots)
	{
		
		String status= "failed";
		String message = "failed";
		Calendar cal = Calendar.getInstance();
		String currentTime= sdf.format(cal.getTime());
		int count=0;
		
			if(hDate_map.containsKey(date))
			{
				Integer week_Of_Year  = findWeekOfYear( date);
				if(hDate_map.get(date).containsKey(room_Number))
				{
					for ( String nm : List_Of_Time_Slots )
					{
						if(hDate_map.get(date).get(room_Number).containsKey(nm))
							{
								HashMap<String, String> Booking= new HashMap<String, String>();
								Booking = hDate_map.get(date).get(room_Number).get(nm);
								hDate_map.get(date).get(room_Number).remove(nm);
								count=count+1;
								message = "Room record for given time slot has been removed"; 
								status="success";						
								for (String ID : Booking.values())
								{
									if(ID != null && !ID.isEmpty())
									{
									updateStudentBookingCount(week_Of_Year, ID );
									}
								}
							}
						}
					}	
			}
		if(count>0){
			message= "Time slot has been removed from the Database";
		}
		else message="No record to delete";
	 	System.out.println("deleteRoom hStudent_Booking"+ hStudent_Booking);
		System.out.println( "deleteRoom HashMap View :" + hDate_map );
		setLogger();
		logger.info("\n Request time: " + currentTime + "\n Request Type:"+ " deleteRoom"+ "\n Request parameters: " +" Room_Number "+ room_Number + " date:"+ date + " Timeslot: " + List_Of_Time_Slots + "\n Request Status: "+ status +"\n Server response: "+ message);
		return status;
		
	}
	//To create a request to book room in all servers using UDP
	public synchronized String bookRoom( String campus_Name, String room_Number, String date, String timeslot, String StudentID)
	{
		if(this.location.equals(campus_Name))
		{
			return getBookRoom(  campus_Name,  room_Number,  date,  timeslot,  StudentID);
		}
		else {
			int port = portMapping.get(campus_Name);
			// "B" stands for the method name to be passed as a message from UDP client to UDP server to invoke respective functionality

			String InputValue= "B"+ this.location +room_Number + date + timeslot + StudentID ;
			return UDPRequest(port ,  InputValue).trim();
		}
		
	}
	// to book the room slot for the server instance
	public synchronized String getBookRoom( String campus_Name, String room_Number, String date, String timeslot, String StudentID)
	{	
		String status= "fail";
		String message = "fail";
		Calendar cal = Calendar.getInstance();
		String currentTime= sdf.format(cal.getTime());
		boolean count_Flag = false;
		boolean booking_Flag = false;
		boolean is_booked =false;
		Integer booking_Count;
		Integer week_Of_Year  = findWeekOfYear( date);
		booking_ID= createBookingID(  room_Number,  date,  timeslot);
		if(hStudent_Booking.containsKey(StudentID))
				{
					if(hStudent_Booking.get(StudentID).containsKey(week_Of_Year))
						{
							booking_Count = hStudent_Booking.get(StudentID).get(week_Of_Year);
							if(booking_Count <3 )
								{
									booking_Count = booking_Count +1;
									count_Flag= true;
									hStudent_Booking.get(StudentID).put(week_Of_Year, booking_Count);
									System.out.println("booking_Count <3 hStudent_Booking"+ hStudent_Booking);
								}
							else
								{
									booking_Flag = true;
									message = "No more booking allowed for the week";
								}
						}
				}
		if(booking_Flag==false)
				{
					if(count_Flag==false)
						{
							HashMap<Integer , Integer > hBooking_Count = new HashMap< Integer , Integer >();
							hBooking_Count.put(week_Of_Year , 1);
							if(hStudent_Booking.containsKey(StudentID))
							{
								hStudent_Booking.get(StudentID).put(week_Of_Year, 1);
							}
							else
							hStudent_Booking.put(StudentID, hBooking_Count);
							count_Flag= true;
							System.out.println("count_Flag==false hStudent_Booking"+ hStudent_Booking);
						}
					if(hDate_map.containsKey(date))
					{
						if(hDate_map.get(date).containsKey(room_Number))
						{
							if(hDate_map.get(date).get(room_Number).containsKey(timeslot))
							{
								if(hDate_map.get(date).get(room_Number).get(timeslot).containsKey(booking_ID))
								{
									String getValue= hDate_map.get(date).get(room_Number).get(timeslot).get(booking_ID);
									if(getValue=="" || getValue==null )
									{
										hDate_map.get(date).get(room_Number).get(timeslot).put(booking_ID, StudentID );
										is_booked= true;
										message = "Room has been successfully booked";
										status="success";
									}
									else message = "Room has been already booked";
								}
							}
						}
					}
				}
		if(is_booked== false&&count_Flag== true)
			{
				booking_Count = hStudent_Booking.get(StudentID).get(week_Of_Year)-1;
				hStudent_Booking.get(StudentID).put(week_Of_Year, booking_Count);
				message = "Room booking failed";
			}
		System.out.println("getBookRoom hStudent_Booking"+ hStudent_Booking);
		System.out.println( "getBookRoom HashMap View :" + hDate_map );
	 
		setLogger();
		logger.info("\n Request time: " + currentTime + "\n Request Type:"+ " bookRoom"+ "\n Request parameters: " + " campus_Name:" +  campus_Name+" Room_Number "+ room_Number + " date:"+ date + " Timeslot: " + timeslot + "\n Request Status: "+ status +"\n Server response: "+ message);	
		status = (status=="success") ? booking_ID : "fail";
		System.out.println("Studnets booking recod" + hStudent_Booking );
		return status ;
	}
	//To create request to get available slot from all servers using UDP
	public synchronized String getAvailableTimeSlot (String date)
	{
		int port1, port2;
		if(this.location.equals("DVL"))
		{
			port1 = portMapping.get("WST");
			port2 = portMapping.get("KKL");
		}else if(this.location.equals("KKL"))
		{
			port1 = portMapping.get("DVL");
			port2 = portMapping.get("WST");
		} else
		{
			port1 = portMapping.get("DVL");
			port2 = portMapping.get("KKL");
		}
		String Input_Value="A"+ date ;
		return getAllTimeslotCount(date)+", " + UDPRequest(port1  ,Input_Value).trim() +", "+ UDPRequest(port2, Input_Value).trim();
	}
	// To get the count of the slot available in the instance
	public synchronized String getAllTimeslotCount (String date)
	{ 
		int count =0;
		if(hDate_map.containsKey(date))
		{
				HashMap< String, HashMap<String, HashMap<String, String>>> h1 = hDate_map.get(date) ; 
				for(HashMap.Entry< String, HashMap<String, HashMap<String, String>>> entry1: h1.entrySet())
				{
					HashMap<String, HashMap<String, String>> h2  = new  HashMap<String, HashMap<String, String>>(); 
					h2 = entry1.getValue();
					for(HashMap.Entry< String, HashMap<String, String>> entry2: h2.entrySet())
					{
						HashMap<String, String> h3 = new HashMap<String, String>();
						h3 = entry2.getValue();
						for (HashMap.Entry<String,String> entry3 : h3.entrySet()) {
						    String value = entry3.getValue();
						    if(!(value != null && !value.isEmpty()))
						    {
						    	count = count + 1;	
						    	 System.out.println("Count: "  + count);
						    }
						}
					}
			}
		};
		Calendar cal = Calendar.getInstance();
		String currentTime= sdf.format(cal.getTime());
		String Status = this.location + ": " + count ;
		System.out.println("getAllTimeslotCount hStudent_Booking"+ hStudent_Booking);
		System.out.println( "getAllTimeslotCount HashMap View :" + hDate_map );
		logger.info("\n Request time: " + currentTime + "\n Request Type:"+ " getAvailableTimeSlot"+ "\n Request parameters: "+ " date:"+ date + "\n Request Status: "+ message  +"\n Server response"+ status);	
		return Status;
	}
	//To create request to cancel the booking for all  servers using UDP
	public synchronized String cancelBooking (String booking_ID, String Student_ID )
	{
		String campus_Name =booking_ID.substring(19); 
		if(campus_Name.equals(this.location)){
			return getCancelBooking ( booking_ID,  Student_ID );
		}
		else {
			int port = portMapping.get(campus_Name);
			// "C" stands for the method name(cancel booking) to be passed as a message from UDP client to UDP server to invoke respective functionality
			String InputValue= "C"+Student_ID +booking_ID ;
		System.out.println("cancelBooking hStudent_Booking"+ hStudent_Booking);
		System.out.println( "cancelBooking HashMap View :" + hDate_map );
			return UDPRequest(port ,  InputValue).trim();
		}
	}
	// to cancel the booking in the sever instance
	public synchronized String getCancelBooking (String booking_ID, String Student_ID )
	{ 
		 String status= "fail";
		 String message = "";
		 Calendar cal = Calendar.getInstance();
		 String currentTime= sdf.format(cal.getTime());
		 String room_Number = getBookingRoom( booking_ID);
		 String date = getBookingDate( booking_ID) ;
		 Integer week_Of_Year  = findWeekOfYear( date);
		 String timeslot =getTimeSlot( booking_ID);
		 String Booked_User_ID = null;
		 if((hDate_map.containsKey(date))&& (hDate_map.get(date).containsKey(room_Number))&& 
			(hDate_map.get(date).get(room_Number).containsKey(timeslot)) && (hDate_map.get(date).get(room_Number).get(timeslot)).containsKey(booking_ID))
		 	{
			 Booked_User_ID =  hDate_map.get(date).get(room_Number).get(timeslot).get(booking_ID);
			if(Booked_User_ID!=null)
			{
			 if(Booked_User_ID.equals(Student_ID))
			 	{
				 hDate_map.get(date).get(room_Number).get(timeslot).put(booking_ID, null);
			     updateStudentBookingCount(week_Of_Year, Student_ID );
			     message = "Room has been successfully cancelled";
			     status="success";
			 	}
			 else message= "Room is booked by other user: "+Booked_User_ID ;
			}
			 else message= "Room is not booked by any user";
		 	}
		 	else message= "The BookingID does not exist in database";
		 setLogger();
		 logger.info( "\n Request time: " + currentTime + "\n Request Type:"+ " cancelBooking"+ "\n Request parameters: "+ " booking_ID:"+ booking_ID + "\n Request Status: "+ status +"\n Server response: "+ message);
		
		 return status;	
	}
	// to change the reservation
	public synchronized String changeReservation (String booking_ID,String campus_Name,String room_Number,String timeslot, String new_Date, String StudentID)
	{
			String status1= "fail";
			String status2= "fail";
			status= "fail";
			message="";
			Calendar cal = Calendar.getInstance();
			String currentTime= sdf.format(cal.getTime());
			String isValidBooking = validateBookingWrapper( booking_ID, StudentID);
			String isSlotAavailable= checkSlotAavailableWrapper( campus_Name,  timeslot,  room_Number,  booking_ID, new_Date, StudentID);
			System.out.println("isValidBooking:"+isValidBooking +" isSlotAavailable:"+isSlotAavailable);
			if(isValidBooking.equals("true") && isSlotAavailable.equals("true") )
			{
				status1 = bookRoom(  campus_Name,  room_Number,  new_Date,  timeslot,  StudentID);
				status2 =  cancelBooking ( booking_ID,  StudentID );
			}
			System.out.println("bookRoom:"+ status1+ "cancelBooking:" + status2);
			if(status1.equals("fail")||status2.equals("fail"))
			{
				if(status1.equals("fail")){
					message += "Book room failed ";
				}
				if(status2.equals("fail")){
					message += " cancel booking failed";
				}
				status= "fail";
			} else if(!(status1.equals("fail")&&status2.equals("fail")))
				status= status1;
			System.out.println("change reservation hStudent_Booking"+ hStudent_Booking);
			System.out.println( "change reservation Primay HashMap View :" + hDate_map );
			 setLogger();
			 logger.info( "\n Request time: " + currentTime + "\n Request Type:"+ " changeReservation"+ "\n Request parameters: "+ "booking_ID:"+  booking_ID + "campus_Name"+ campus_Name+"room_Number: "+ room_Number+ "timeslot"+ timeslot+"new_Date: "+ new_Date + "StudentID :" + StudentID + "\n Request Status: "+ status +"\n Server response: "+ message);
		return status;
	};
	//Utility functions 
	private String createBookingID( String room_Number, String date, String timeslot)
	{
		String date_string = date.replaceAll("[-+.^:,]","");
		String time_string = timeslot.replaceAll("[-+.^:,]","");
		String booking_ID = date_string + room_Number+ time_string + this.location;
		return booking_ID;
	}
	private String getBookingDate(String bookingID)
	{
		String Date=  bookingID.substring(0,2) +"-"+ bookingID.substring(2,4) +"-"+ bookingID.substring(4,8);
		return Date ;
	}
	private String getBookingRoom(String bookingID)
	{
		String room_number = bookingID.substring(8,11);	
		return room_number ;
	}
	private String getTimeSlot(String bookingID)
	{
		String timeslot_From = bookingID.substring(11, 13) + ":"+ bookingID.substring(13, 15) ;	
		String timeslot_To = bookingID.substring(15, 17) + ":"+ bookingID.substring(17,19) ;	
		String timeslot = timeslot_From + "-" + timeslot_To ; 
		return timeslot ;
	}
	private Integer findWeekOfYear(String date)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date booking_Date= null;
		try {
			 booking_Date = simpleDateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String week_Number = new SimpleDateFormat("w").format(booking_Date);
		Integer week_Of_Year = Integer.valueOf(week_Number);
		return week_Of_Year;
	}
	private String updateStudentBookingCount(Integer week_Of_Year, String ID)
	{
		for (HashMap.Entry<Integer, Integer> WeekEntry : hStudent_Booking.get(ID).entrySet()) 
		{
			Integer week_Key = WeekEntry.getKey();
			Integer booking_Count = WeekEntry.getValue();
			if(week_Of_Year.equals(week_Key))
				{
					if(booking_Count> 0)
						{
							booking_Count = booking_Count-1;
							WeekEntry.setValue(booking_Count);
							}
						}
					}
			return "true";
	}
	public String validateBooking(String booking_ID, String Student_ID)
	{
		String isBookedbyStudent = "false";
		String date = getBookingDate( booking_ID) ;
		String room_Number = getBookingRoom( booking_ID);
		String timeslot =getTimeSlot( booking_ID);
		
		if(hDate_map.containsKey(date))
		{
				if(hDate_map.get(date).containsKey(room_Number))
					{
						if(hDate_map.get(date).get(room_Number).containsKey(timeslot))
							{
								if(hDate_map.get(date).get(room_Number).get(timeslot).containsKey(booking_ID))
							{
								if(hDate_map.get(date).get(room_Number).get(timeslot).get(booking_ID).equals(Student_ID))
								{
									isBookedbyStudent = "true";	 
								}
							}
						}
				}
		} 
		return isBookedbyStudent;
	}
	public String validateBookingWrapper(String booking_ID,String Student_ID)
	{	
		String isValidBooking = "false";
		String campus_Name =booking_ID.substring(19); 
		if(campus_Name.equals(this.location))
		{
			isValidBooking= validateBooking(booking_ID,  Student_ID);
		}
		else
		{
			int port = portMapping.get(campus_Name);
			// "V" stands for the method name(validateBookingWrapper) to be passed as a message from UDP client to UDP server to invoke respective functionality
			String InputValue= "V"+Student_ID +booking_ID ;
			isValidBooking = UDPRequest(port ,  InputValue).trim();
		}
		return isValidBooking;
	}
	public String checkSlotAavailable(String campus_Name, String timeslot, String room_Number,  String new_Date, String Student_ID)
	{
		String isSlotAavailable= "false";
		if(hDate_map.containsKey(new_Date))
		{
			if(hDate_map.get(new_Date).containsKey(room_Number))
				{
					if(hDate_map.get(new_Date).get(room_Number).containsKey(timeslot))
						{
						if(hDate_map.get(new_Date).get(room_Number).get(timeslot).containsValue(null))
						isSlotAavailable = "true";
						}
				}
		}
		Integer week_Of_Year  = findWeekOfYear( new_Date);
		if(hStudent_Booking.containsKey(Student_ID))
			{
				if(hStudent_Booking.get(Student_ID).containsKey(week_Of_Year))
					{
						int booking_Count = hStudent_Booking.get(Student_ID).get(week_Of_Year);
						if(booking_Count <3 )
							{
							isSlotAavailable= "true";
							}
						else isSlotAavailable= "false";
					}		
			}	
		return isSlotAavailable;
	}
	public String checkSlotAavailableWrapper(String campus_Name, String timeslot, String room_Number, String booking_ID, String new_Date, String Student_ID)
	{	

		String isSlotAavailable= "false";
		if(campus_Name.equals(this.location))
		{
			isSlotAavailable=  checkSlotAavailable( campus_Name,  timeslot,  room_Number, new_Date, Student_ID);
		}
		else
		{
			int port = portMapping.get(campus_Name);
			// "S" stands for the method name(checkSlotAavailableWrapper) to be passed as a message from UDP client to UDP server to invoke respective functionality
			String InputValue= "S"+campus_Name+ timeslot+ room_Number+booking_ID+new_Date+Student_ID;
			isSlotAavailable = UDPRequest(port ,  InputValue).trim();
		}
		return isSlotAavailable;
	}
	private void setLogger() {
		// TODO Auto-generated method stub
		String logFile = ".\\Logs\\Server\\" + this.location + ".txt";
		 logger = Logger.getLogger(logFile);
		if (logger.getHandlers().length == 0) {
			FileHandler fileHandler = null;
			try {
				fileHandler = new FileHandler(logFile, true);
				SimpleFormatter textFormatter = new SimpleFormatter();
				fileHandler.setFormatter(textFormatter);
				logger.addHandler(fileHandler);
				logger.setUseParentHandlers(false);
			} catch (SecurityException | IOException e) {
				System.out.println("Logger initialization error. Check File Permissions!!!");
				e.printStackTrace();
			} finally {
			}
		}
	}
	public String UDPRequest(int port, String inputString)
	{
		String recievedCount = "";
		try
		{
	    DatagramSocket clientSocket = new DatagramSocket();
	    InetAddress IPAddress = InetAddress.getByName("localhost");
	    byte[] sendData = new byte[1024];
	    byte[] receiveData = new byte[1024];
	    String input = inputString;
	    System.out.println("FROM Client:" + inputString);
	    sendData = input.getBytes();
	    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
	    clientSocket.send(sendPacket);
	    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	    clientSocket.receive(receivePacket);
	    recievedCount = new String(receivePacket.getData());
	    System.out.println("FROM SERVER:" + recievedCount.trim());
	    clientSocket.close();
	    return recievedCount;
		}
		catch(Exception e) {
		}
		return  recievedCount;	
		}
	}

	//end class




package reservationSystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;


public class DVLImpl implements IRoomInterface {
	

public DVLImpl() throws IOException {
	
	
	
}
AppLogs logObj = new AppLogs("DVLServer.txt");
int counter =10000;
public static int  bookingNo =0;

//int countOfBookings = 0;
public static HashMap<String ,Integer> bookingCounter = new HashMap<String ,Integer>();
static Integer countOfBookings = 0;
static String BookingIDFin = "";

public static HashMap<String, HashMap<String, HashMap<String, String>>> createdNewRoom =new HashMap<String, HashMap<String, HashMap<String, String>>>();

static HashMap<String, Map<String, Map<String, Map<String, Map<String, String>>>>> BookedRoomHashMap = new HashMap<String, Map<String, Map<String, Map<String, Map<String, String>>>>>();

public static HashMap<String,HashMap<String, HashMap<String,ArrayList<String>>>> roomRecords = new HashMap<String,HashMap<String, HashMap<String,ArrayList<String>>>>();

public static HashMap<String,HashMap<String,HashMap<String,HashMap<String,String>>>> bookedRoom = new HashMap<String,HashMap<String,HashMap<String,HashMap<String,String>>>>();

public static HashMap<String,HashMap<String,HashMap<String,HashMap<String,HashMap<String,HashMap<String,String>>>>>> finalBookedRecords = new HashMap<String,HashMap<String,HashMap<String,HashMap<String,HashMap<String,HashMap<String,String>>>>>>();	

public static HashMap<String,HashMap<String,HashMap<String,HashMap<String,HashMap<String,String>>>>> submap =new HashMap<String,HashMap<String,HashMap<String,HashMap<String,HashMap<String,String>>>>>();


public static String bookingRes ="";
	
	//method implementation to create room
	public String createRoom(String creationDate,String room_Number,
			String list_Of_Time_Slots){
		
			String output = "";

			String addValTime="";
			String addValDate="";
			String addValRoom="";
			String resultForCreate =""; 
			 HashMap<String, HashMap<String, String>> subHashmap = new HashMap<String, HashMap<String, String>>();
				Boolean OKToCreate=false;
			 HashMap<String, String> time =new HashMap<String, String>();
			try
			{
				System.out.println("Check if create map contains the entered date"+createdNewRoom);
				if(createdNewRoom.containsKey(creationDate))
				{
					System.out.println("Found existing date record");
					subHashmap = createdNewRoom.get(creationDate);
					for(String room:subHashmap.keySet())
					{
						
						String roomKey =room;
						if(roomKey.equals(room_Number))
						{
						System.out.println("Found existing room record");
						logObj.logs.info("Found existing room record");
						}else
						{
							OKToCreate=true;
						}
						time =subHashmap.get(roomKey);
						
					//condition to add time slot if room and date exist
					if(time.containsKey(list_Of_Time_Slots))
					{
						System.out.println("Don't add duplicate record !");
						resultForCreate="failed";
						logObj.logs.info(resultForCreate);
						OKToCreate=false;
					}
					else
					{
						OKToCreate=true;
					}
					
					}
					
				}
				else
				{
					OKToCreate=true;
				}
				if(OKToCreate)
				{
					//add the user entered time slots to hashmap
					 
						time.put(list_Of_Time_Slots,"0");
						
						System.out.println("Check if same key value can be added");
						
						if(createdNewRoom!=null &&(createdNewRoom.containsKey(creationDate)) && !(createdNewRoom.get(creationDate).get(room_Number).containsKey(time)))
						{
							System.out.println("Condition: date and room number are same but time is different");
							createdNewRoom.get(creationDate).get(room_Number).put(list_Of_Time_Slots,"0");
							System.out.println("Display created records"+createdNewRoom);
						}
					
						
						System.out.println("Out of If");
					//add room number to the record
				  	subHashmap.put(room_Number, time);
					   
					//add creation date and make the final created room HashMap
					createdNewRoom.put(creationDate, subHashmap);	
					/*bookedRoom.put("0", createdNewRoom);
					submap.put("0", bookedRoom);
					finalBookedRecords.put("0", submap);*/
					
					addValTime="Time is :"+time;
					addValDate = "Date is :"+creationDate;
					addValRoom="Room No is :"+room_Number;
					
					resultForCreate = "success";
					System.out.println("created room records" +createdNewRoom);
					
					//System.out.println("final booked records hashmap"+finalBookedRecords);
					//create logs
					 logObj.logs.info("Room No "+room_Number + "created by Admin");    
				}
					
				else
				{
					resultForCreate ="failed";
				}
					    
			
			}
			catch(Exception e)
			{
				
				System.out.println("Error in create");
				
			}
			System.out.println("Display created records"+createdNewRoom.get(creationDate));
			return resultForCreate;
		}

	public String deleteRoom(String delRoom_Number, String deletingDate,String del_list_Of_Time_Slots){
		String delStatus = "";
		boolean isRecordDeleted = false;
		HashMap<String, HashMap<String, String>> checkTime = new HashMap<String, HashMap<String, String>>();
		
	try 
	{
		if(createdNewRoom!=null)
		{
			System.out.println("Server delete start");
			
			checkTime =createdNewRoom.get(deletingDate);
			System.out.println("get values by date "+checkTime);
			HashMap<String, String> finalSlots=new HashMap<String, String>();
		
			if(checkTime.containsKey(delRoom_Number))
		
			{
				System.out.println("contains room "+delRoom_Number);
				finalSlots=checkTime.get(delRoom_Number);
				System.out.println("get time slots "+finalSlots+"compare with "+del_list_Of_Time_Slots);
				
				if(finalSlots.containsKey(del_list_Of_Time_Slots))
				{
					System.out.println("contains entered time ");
		/*String finalSlots="";
		
		
			finalSlots.replace(del_list_Of_Time_Slots, "");*/
				createdNewRoom.get(deletingDate).get(delRoom_Number).remove(del_list_Of_Time_Slots, "0");//.remove(del_list_Of_Time_Slots);
				System.out.println("Now created records contain "+createdNewRoom);
				isRecordDeleted=true;
				}
				else
				{
					isRecordDeleted=false;
				}
				//isRecordDeleted=createdNewRoom.get(deletingDate).remove(delRoom_Number, del_list_Of_Time_Slots);
			/*isRecordDeleted=true;
			bookingNo--;
			logObj.logs.info("Room No "+delRoom_Number + "deleted by Admin");   
			
		
		}
		else
		{
			isRecordDeleted =false;
			logObj.logs.info("Room No "+delRoom_Number + "not found in database."); 
			
		}
		}
		else
		{
			isRecordDeleted =false;
			delStatus = "Failure";
			
		}*/
				if(isRecordDeleted)
				{
					bookingNo--;
					logObj.logs.info("Room No "+delRoom_Number + "deleted by Admin"); 
				}
				else
				{
					isRecordDeleted =false;
					delStatus = "failed";
				}
							}
			else
			{
				isRecordDeleted =false;
				delStatus = "failed";
			}

				}
		else
		{
			delStatus="failed";
			
		}
	
	}
	catch(Exception e )
	{
		System.out.println("Error in delete");
		logObj.logs.info("Error in delete");
	}
	
	if(isRecordDeleted)
	{
		delStatus="success";
	}
	else
	{
		delStatus="failed";
	}
	logObj.logs.info(delStatus);
	return delStatus;
		
	}


	
	public synchronized String bookRoom(String campusName, String roomNumber, String bookingDate, String timeslot,String studentId)  
	{
		Boolean OkToBook =false;
	
	HashMap<String, HashMap<String, String>> map3 = new HashMap<String, HashMap<String, String>>();
 String bookingResult ="";
    HashMap<String, String> arraylistObj=new HashMap<String, String>();
    arraylistObj.put(timeslot,"0");
    
    //booking date and time slots added to hashmap
    map3.put(roomNumber, arraylistObj);
    
    HashMap<String,HashMap<String,HashMap<String,String>>> addToBooking =new  HashMap<String,HashMap<String,HashMap<String,String>>>();
    if(campusName.equalsIgnoreCase("DVL"))
    {
    	System.out.println("input campusName is DVL");
    	if(createdNewRoom==null || createdNewRoom.isEmpty())
    {
    		bookingResult="fail no rooms created";
    }
    else if(createdNewRoom.containsKey(bookingDate))
    	{
    	System.out.println("inside createdNewRoom.containsKey(bookingDate)");
    	
    //this hashmap contains room number ,booking date and time slot 
    	addToBooking.put(bookingDate, map3);
  
    
  			    
    if(finalBookedRecords.size()!=0)
    {
    	System.out.println(" booked records are: " +finalBookedRecords);
    	
    	if(finalBookedRecords!=null || !finalBookedRecords.isEmpty())
    	{
    		 System.out.println("now booked records are : "+finalBookedRecords);
    		    System.out.println("get key set for booked records"+finalBookedRecords.keySet());
    		    
    		 Object[] ar=  finalBookedRecords.keySet().toArray();
    		List<String> bookingVal=new ArrayList<String>();
    		 for(Object val : ar)
    		 {
    			 System.out.println("val "+val);
    			 if(val!="0")
    			 {
    				 
    				// bookingVal.add((String) val);
    				// System.out.println("bookingVal= "+bookingVal);
    				HashMap<String, HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>>> checkThese = finalBookedRecords.get((String) val);
    				
    				Object[] ar1 =checkThese.keySet().toArray();
    				for(Object val1 : ar1)
    				{
    					 System.out.println("second key "+val1);
    					 if(val1!="0")
    					 {
    					HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> checkFor3rdKey =	 checkThese.get(val1);
    					 
    					Object[] ar2 =checkFor3rdKey.keySet().toArray();
    					
    					for(Object val2 : ar2)
        				{
    						
    						 System.out.println("third key "+val2);
    						 if(val2!="0")
    						 {
    							 HashMap<String, HashMap<String, HashMap<String, String>>> checkForMain = checkFor3rdKey.get(val2);
    							 
    				System.out.println("checkForMain hashmap"+checkForMain);
    				
    				System.out.println("Compare it to this : "+addToBooking);
    				if(checkForMain.containsKey(bookingDate))
    				{
    					
    					System.out.println("Found booking date");
    					HashMap<String, HashMap<String, String>> forRoom =checkForMain.get(bookingDate);
    					if(forRoom.containsKey(roomNumber))
    					{
    						System.out.println("Found booking room");
    					
    						HashMap<String, String> forTime =forRoom.get(roomNumber);
    						if(forTime.containsKey(timeslot))
    						{
    							System.out.println("Found booking time");	
    						
    			
    			
    					System.out.println("Don't let it book");
    				
    					OkToBook=false;
    						}
    						else{
    							System.out.println("let it book");
    							OkToBook=true;
    						}
    					}
						else{
							System.out.println("let it book");
							OkToBook=true;
						}
    					}
					else{
						System.out.println("let it book");
						OkToBook=true;
					}
    						 }
        				}
    					 }
    				}
    			 }
    			 else
    			 {
    				 OkToBook=true; 
    			 }
    		 }
    		 
    		 
    		 
    		
    	}else
    	{
    		OkToBook=true;
    	}
    }
    else
    {
    	//records are null, first booking 
    	OkToBook=true;
    }
    	}
    else
    {
    	
    //room not created in DVL
    	bookingResult="fail first create this room in DVL";
    	OkToBook=false;
    	
   }
    
    	if(OkToBook)
    	{
    		
    	
    	//if(!(finalBookedRecords.containsValue(addToBooking)))
    	//{
    		System.out.println("reaching here means it is not already booked");
    	String bookingId = campusName+bookingNo; 
    	//put string and booked room

        //this hashmap contains campus name ,room number ,booking date and time slot
        bookedRoom.put(campusName, addToBooking);
    	//this hashmap contains student id ,campus name room number ,booking date and time slot
	    submap.put(studentId,bookedRoom);
	    if(bookingCounter.size()==0 || bookingCounter.get(studentId)<3)
	    {    
	    	//System.out.println("Check count of counter "+bookingCounter.get(studentId));
	    //put string and submap
	    finalBookedRecords.put (bookingId,submap);
	   
	  //  System.out.println("get all values where booking id exists"+finalBookedRecords.;
	    bookingNo++;
	    bookingCounter.put(studentId, bookingNo);
	    System.out.println("Check count of counter "+bookingCounter.get(studentId));
	    logObj.logs.info("Room No "+roomNumber + "booked by student");   
	    bookingResult= bookingId;
	    System.out.println("Final booked records "+finalBookedRecords);
	   // return bookingResult;
    	}else
    	{
    		bookingResult="fail cannot book more than 3";
    		
    	}
    	}
    	else
    	{
    		System.out.println("reaching here means it is already booked");
    		bookingResult= "fail ";
    		
    	}
    
    }
   
    
    else if (campusName.equalsIgnoreCase("WST")||campusName.equalsIgnoreCase("KKL"))
    {    	
    	try {
    		
    		bookingResult=	bookWithUDP("dummy", campusName, roomNumber,timeslot,studentId,bookingDate);
    		
			
		} catch (IOException e) {
			
			System.out.println("Error in book room");
		}
    	
    }
    logObj.logs.info(bookingResult);
    return bookingResult;
    
			}


	public HashMap<String, HashMap<String, HashMap<String, HashMap<String, HashMap<String,HashMap<String,String>>>>>> getBookedRooms() 
	{
		
		return finalBookedRecords;
	}
	
	public HashMap<String, HashMap<String,HashMap<String,String>>> getRecords() 
	{
		
		return createdNewRoom;
		
	}
public String cancelBooking2(String bookingId, String studentId)
{
	String cancellationResult="";
	if(bookingId.contains("DVL"))
{
	cancelBooking(bookingId,studentId);
	
	}
else if(bookingId.contains("KKL"))
{

cancellationResult = cancelWithUDP("KKL",bookingId,studentId);

}
else if(bookingId.contains("WST"))
{
cancellationResult = cancelWithUDP("WST",bookingId,studentId);
} 
	
else
{
	cancellationResult= "failed";
}

return cancellationResult;	

}
	
	public String cancelBooking(String bookingId, String studentId) {
		
		String cancellationResult="";
		/*if(studentId.contains("DVL"))
		{*/
			System.out.println("studentId contains DVL "+studentId);
			if(bookingId.contains("DVL"))
			{
				System.out.println("bookingId contains DVL "+bookingId);
				System.out.println("show me finalBookedRecords"+finalBookedRecords);
				boolean tryme =finalBookedRecords.containsValue(bookingId);
				System.out.println(tryme);
				if(finalBookedRecords.containsKey(bookingId))
				{	
				
					System.out.println("booked records"+finalBookedRecords+" contain "+bookingId);
					finalBookedRecords.remove(bookingId);	
					bookingNo--;
					bookingCounter.put(studentId, bookingNo);
					logObj.logs.info("Room with booking id :"+bookingId+"deleted by student "+studentId);  
		
					cancellationResult= "success";
					
					
				}
			
			}
			else 
			{
				cancellationResult= "failed";
			}
			/*else if(bookingId.contains("KKL"))
				{
				
				cancellationResult = cancelWithUDP("KKL",bookingId,studentId);
				
				}
			else if(bookingId.contains("WST"))
			{
				cancellationResult = cancelWithUDP("WST",bookingId,studentId);
			}*/
		/*}
		else
		{
			cancellationResult= "Non DVL Booking cannot be deleted from DVL";
		}
		*/
			logObj.logs.info(cancellationResult);
	return cancellationResult;	

	}
	
	public void callUDPServer(int port, String campusName) throws IOException {
		DatagramSocket socketAtDVL = new DatagramSocket(port);
		
		
		
		System.out.println("UDP server from DVL ");
		logObj.logs.info("UDP server from DVL ");
		while (true) {
			try {

				byte[] result = new byte[1024];
				DatagramPacket resultPack = new DatagramPacket(result, result.length);
				socketAtDVL.receive(resultPack);
				String dateIn = new String(resultPack.getData());
				String udpReply="";
				String input =new String(resultPack.getData());
				String inputs =input.trim();
				System.out.println("reached DVL with inputs "+inputs);
				if(inputs.contains("bookRoom"))
				{
					System.out.println("enter book loop");
					 String[] input1=inputs.split("X");
						String met = input1[0];
						
						
						String[] input2 = input1[1].split("M");
						String bookingId=input2[0];
						
						
						String[] input3 = input2[1].split("N");
						
						String campus =input3[0];
						
						String[] input4 = input3[1].split("P");
						
						
						String room = input4[0];
						String[] input5 =input4[1].split("J");


						String timeBooking ="";
						timeBooking=input5[0];
						
						
						String[] input6 = input5[1].split("Z");
						
						String studId =input6[0];
					
						String bookDate=input6[1];
						
					System.out.println("call DVL book Room with inputs"+campus+ room+bookDate+timeBooking+studId);
					udpReply=bookRoom(campus, room,bookDate,timeBooking,studId);
							
				}
				else if(inputs.contains("cancelRoom"))
				{
					System.out.println("enter cancel loop");
					 String[] input1=inputs.split("X");
						String met = input1[0];
						
						
						String[] input2 = input1[1].split("M");
						String bookingId=input2[0];
						
						
						String studId = input2[1];
						
						System.out.println("call DVL cancel Room with inputs"+bookingId+studId);
						udpReply=cancelBooking(bookingId,studId);
						
				}
				else{
					System.out.println("Call for get avail timeslots");
					udpReply = getCampusAvailableTimeSlot(campusName,dateIn).toString();
				}
				logObj.logs.info(udpReply);
				System.out.println("DVL response from UDP "+udpReply);
				byte[] answerData = udpReply.getBytes();
				DatagramPacket newPack = new DatagramPacket(answerData, answerData.length, resultPack.getAddress(), resultPack.getPort());
				socketAtDVL.send(newPack);
			} 
			catch (Exception e) {
				System.out.println("Error in call udp");
				
			}
		}
	}

	
	public synchronized Integer getCampusAvailableTimeSlot(String campusIn,String dateIn) {
		   
		   int recordCount = 0;
		   int recordsBooked=0;
		   HashMap<String,HashMap<String,HashMap<String,HashMap<String,String>>>> finalRecords = new  HashMap<String,HashMap<String,HashMap<String,HashMap<String,String>>>>();
		   dateIn=dateIn.trim();
		   
		   HashMap<String, HashMap<String, HashMap<String, String>>> creationRecords = new HashMap<String, HashMap<String, HashMap<String, String>>>();
		  // System.out.println("Check if main campus is DVL"+campusIn);
		   //create logs
		  /* if(campusIn=="DVL")
		   {*/
			   System.out.println("identified campus as DVL");
			   
			   /*if(finalRecords!=null)
			   {*/
			   
			try
			{
				System.out.println("All DVL bookings count and ID : "+finalBookedRecords.size() +" "+ finalBookedRecords.keySet());
				
			   for (String bookingDVL : finalBookedRecords.keySet())
				  {
					  if(bookingDVL.contains("DVL"))
					  {
						  recordsBooked++;  
					  }
				  }
			System.out.println("finalRecords for DVL"+finalBookedRecords);
			  // }
			   
			   if(creationRecords!=null)
			   {
				// creationRecords = createdNewRoomWST.get(dateIn); 
				 creationRecords = createdNewRoom;
			   System.out.println("creationRecords for DVL"+creationRecords +creationRecords.size());
		   }
		  	 int creationTimeSlots=0;
			   if(recordsBooked>0)
			   {
				   System.out.println("inside recordsBooked>0");
				   /* if(creationRecords.size()==1)
				   {*/
					//   creationTimeSlots =  creationRecords.get(dateIn).values().size();
					   creationTimeSlots = creationRecords.get(dateIn).values().toString().split(",").length;
					   recordCount= creationTimeSlots-recordsBooked;
				   /*}
				   else
				   {*/
				 //  recordCount=  creationRecords.size()-recordsBooked;
				 //  }
				   System.out.println("booked records are "+recordsBooked +"creation records are: "+creationRecords.size()+"available records are: "+recordCount);
			   }
			   else
			   {
				   System.out.println("inside else of recordsBooked>0");
				   recordCount= creationRecords.size();
				   System.out.println("Got record count "+ recordCount);
				   /*if(creationRecords.size()==1)
				   {*/
					 //  System.out.println(" creationRecords.get(dateIn).values().size() "+  creationRecords.get(dateIn).values().size());
					   
					   Collection<HashMap<String, String>> creationTimeSlots2 = createdNewRoom.get(dateIn).values();
					   
					   System.out.println("creationTimeSlots2"+creationTimeSlots2);
					  // String[] ar=creationTimeSlots2.toString().split(",");
					  // ar.length;
					   System.out.println("creationTimeSlots1 number of values "+creationTimeSlots2.toString().split(",").length);
					   creationTimeSlots = creationTimeSlots2.toString().split(",").length;
					   recordCount= creationTimeSlots;
				  /* }
				   else
				   {
					   System.out.println("inside else of creationRecords.size()==1");
					   
					   recordCount=creationRecords.size();
				   }*/
			   }
			   
		
			}
			catch(Exception e)
			{
				System.out.println("Catch error for DVL getcampusavailableTimeSlots");
			}
			
			logObj.logs.info(Integer.toString(recordCount));
			   return recordCount;
				   
	   }

	
	public String getAvailableTimeSlot(String campusName, String dateIn)  {
		 System.out.println(campusName);
		
		 String finalCount = "";
		 
			try {
				logObj.logs.info("get vaialble time slots hit ");
					DatagramSocket socketForTime = new DatagramSocket();
					byte[] receipt = new byte[1024];
					byte[] sendDate = dateIn.getBytes();
					
					DatagramPacket sendForServers = null;
					String outputForKKL = "";
					String outputForWST = "";

					DatagramPacket outputPack = new DatagramPacket(receipt, receipt.length);
				
					if (campusName.equalsIgnoreCase("DVL")) {
					sendForServers = new DatagramPacket(sendDate, sendDate.length, InetAddress.getLocalHost(), 6000);
					socketForTime.send(sendForServers);
					System.out.println("back from KKL");
					socketForTime.receive(outputPack);
					outputForKKL = new String(outputPack.getData(),"UTF-8");
					System.out.println("reached 1");
					
					sendForServers = new DatagramPacket(sendDate, sendDate.length, InetAddress.getLocalHost(), 7000);
					socketForTime.send(sendForServers);
					socketForTime.receive(outputPack);
					outputForWST = new String(outputPack.getData(),"UTF-8");
					System.out.println("reached 2,hit campus avail");
					finalCount = "DVL: " + getCampusAvailableTimeSlot(campusName,dateIn) + ", KKL: " + outputForKKL.trim() + ", WST: " + outputForWST.trim();
					System.out.println("received final count "+finalCount);
				} 			} catch (Exception e) {
				System.out.println("Error");
			}
			logObj.logs.info(finalCount);
		     return finalCount; 

	}

	
	public String changeReservation(String booking_id, String new_campus_name,
			String new_room_no, String new_time_slot, String username,
			 String dateIn) {
		String cancelStatus="";
		String bookingStatus="";
		String oldCampus="";
		System.out.println("Change reservation started!");
		logObj.logs.info("Change reservation started!");
		if(booking_id.contains("KKL"))
		{
			oldCampus="KKL";
		}
		else if(booking_id.contains("DVL"))
		{
			oldCampus="DVL";
		}
		else if(booking_id.contains("WST"))
		{
			oldCampus="WST";	
		}
		if(oldCampus.equalsIgnoreCase("DVL"))
		{
			System.out.println("finalBookedRecords"+finalBookedRecords);
		 if(finalBookedRecords!=null)
		 {
			 
		for (String exisitingBooking :finalBookedRecords.keySet())
		{
			System.out.println("exisitingBooking"+exisitingBooking+"cmpare with booking id "+booking_id);
			//find booking id in hashmap
			if(exisitingBooking.contains(booking_id))
			{
				//find campus
				System.out.println("entered loop");
				System.out.println("allBookedRecords" +finalBookedRecords);
				
				 HashMap<String, HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>>> studIdrecords =finalBookedRecords.get(booking_id);
				
				 System.out.println("studIdrecords values" + studIdrecords.values());
				 System.out.println("studIdrecords key set" + studIdrecords.keySet());
				 String oldCamp="";
				
				 HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> campusNameRecords = new HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>>() ;	
				 for(String bookingOwner :studIdrecords.keySet() )
				 {
					 if(bookingOwner.equals(username))
					 {
						 
						 campusNameRecords=	studIdrecords.get(bookingOwner); 
						 System.out.println("Records with campus name"+campusNameRecords);
						
						for (String recordCamp:campusNameRecords.keySet())
						{
							
							if(recordCamp!="0")
							{
								oldCamp=recordCamp;
							}
							
						}
						 
						 System.out.println("Old campus "+oldCamp); 
					
						 
					 }
					 
				 }

			try {
				if (oldCamp.equalsIgnoreCase("DVL")) {
					cancelStatus=	cancelBooking(booking_id,username);	
				
					System.out.println("cancel previous request "+cancelStatus);
					if(cancelStatus.equals("success"))	
				{
					System.out.println("hit book room with udp!");
					bookingStatus =	bookWithUDP(booking_id, new_campus_name,new_room_no,new_time_slot, username,dateIn);
				}
				 			
				} 
				else if(oldCamp.equalsIgnoreCase("KKL")||oldCamp.equalsIgnoreCase("WST"))
				{
					cancelStatus=cancelWithUDP(oldCamp,booking_id,username);
					if(cancelStatus.equals("success"))	
					{
						System.out.println("hit book room with udp!");
						bookingStatus =	bookWithUDP(booking_id, new_campus_name,new_room_no,new_time_slot, username,dateIn);
					}
				}
			}
			catch (Exception e) {
				System.out.println("Error");
			}
		     
			
			
			}
		}
		 }
		 }
		else 
		{
			System.out.println("old campus is "+oldCampus+" second loop for udp! ");
			if(oldCampus.equalsIgnoreCase("KKL")||oldCampus.equalsIgnoreCase("WST"))
			{
				cancelStatus=cancelWithUDP(oldCampus,booking_id,username);
				System.out.println("received result!"+ cancelStatus.trim());
				String checkWithThis = cancelStatus.trim();
				if(cancelStatus.trim().equalsIgnoreCase("Success"))	
				{
					System.out.println("hit book room with udp!");
					try {
						bookingStatus =	bookWithUDP(booking_id, new_campus_name,new_room_no,new_time_slot, username,dateIn);
						System.out.println("booking status "+bookingStatus.trim());
						
					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		 logObj.logs.info(bookingStatus.trim());
		 return bookingStatus.trim();
		

		
	}

	
	
	public String bookWithUDP(String booking_id, String new_campus_name,String new_room_no,String new_time_slot,String username,String dateIn) throws IOException
	{
		
		System.out.println("book room hit with udp");
		 DatagramSocket socketForTime = new DatagramSocket();
			byte[] receipt = new byte[1024];
			String methodName="bookRoomX";
			String bookMe=booking_id+"M";
			String newCamp=new_campus_name+"N";
			String newRoom=new_room_no+"P";
			String newTime=new_time_slot+"J";
			String studId=username+"Z";
			String bookDate = dateIn;
			
			String makeInput=methodName+bookMe+newCamp+newRoom+newTime+studId+bookDate;
			
			System.out.println("send inputs for book room "+makeInput);
			logObj.logs.info("send inputs for book room "+makeInput);
			byte[] sendInputs = makeInput.getBytes();
			
			System.out.println("new_campus_name is: "+new_campus_name);
			DatagramPacket sendForServers = null;
			String outputFromNewServer= "";
			
			DatagramPacket outputPack = new DatagramPacket(receipt, receipt.length);
			
			if(new_campus_name.equalsIgnoreCase("KKL"))
			{
				System.out.println("new campus is KKL ");
			//call bookroom method of KKL
				sendForServers = new DatagramPacket(sendInputs, sendInputs.length, InetAddress.getLocalHost(), 6000);
				socketForTime.send(sendForServers);
				System.out.println("back from KKL");
				socketForTime.receive(outputPack);
				outputFromNewServer = new String(outputPack.getData(),"UTF-8");
				System.out.println("reached 1");
			}
			else if(new_campus_name.equalsIgnoreCase("WST"))
			{
			//call bookroom method of WST
				System.out.println("new campus is WST ");
				sendForServers = new DatagramPacket(sendInputs, sendInputs.length, InetAddress.getLocalHost(), 7000);
				socketForTime.send(sendForServers);
				socketForTime.receive(outputPack);
				outputFromNewServer = new String(outputPack.getData(),"UTF-8");
				System.out.println("reached 2,hit campus avail");
		
			}else if ((new_campus_name.equalsIgnoreCase("DVL")))
			{
				System.out.println("new campus is DVL ");
		outputFromNewServer=	bookRoom(new_campus_name, new_room_no,dateIn,new_time_slot,username );
			}
			logObj.logs.info(outputFromNewServer);
			return outputFromNewServer.trim();
	}
	
	
	
	public String cancelWithUDP(String camp,String bookingId, String studentId)
	{
		String outputFromNewServer= "";
		System.out.println("cancel room hit with udp");
		 DatagramSocket socketForTime;
		try {
			socketForTime = new DatagramSocket();
		
			byte[] receipt = new byte[1024];
			String methodName="cancelRoomX";
			String bookMe=bookingId+"M";
			String studId=studentId;
			
			
			String makeCnclInput=methodName+bookMe+studId;
			
			System.out.println("send inputs for cancel room "+makeCnclInput);
			logObj.logs.info("send inputs for cancel room "+makeCnclInput);
			byte[] sendInputs = makeCnclInput.getBytes();
			
			
			DatagramPacket sendForServers = null;
			
			
			DatagramPacket outputPack = new DatagramPacket(receipt, receipt.length);
			
			if(camp.equalsIgnoreCase("KKL"))
			{
				System.out.println("delete booking from KKL ");
			//call bookroom method of KKL
				sendForServers = new DatagramPacket(sendInputs, sendInputs.length, InetAddress.getLocalHost(), 6000);
				socketForTime.send(sendForServers);
				System.out.println("back from KKL");
				socketForTime.receive(outputPack);
				outputFromNewServer = new String(outputPack.getData(),"UTF-8");
				System.out.println("reached 1 with output"+outputFromNewServer);
			}
			else if(camp.equalsIgnoreCase("WST"))
			{
			//call bookroom method of WST
				System.out.println("delete booking from WST ");
				sendForServers = new DatagramPacket(sendInputs, sendInputs.length, InetAddress.getLocalHost(), 7000);
				socketForTime.send(sendForServers);
				socketForTime.receive(outputPack);
				outputFromNewServer = new String(outputPack.getData(),"UTF-8");
				System.out.println("reached 2,hit campus avail");
		
			}
		} catch (Exception e) {
			
			System.out.println("Error in cancel booking with udp");
		}
		logObj.logs.info(outputFromNewServer);
			return outputFromNewServer;

		
	}


}

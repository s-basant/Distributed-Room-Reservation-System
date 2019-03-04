package RoomReservation;


import java.io.IOException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.omg.CORBA.ORB;



public class RoomReservationImplementation extends RoomReservationInterfacePOA {

	static HashMap<String, HashMap<String, HashMap<String, String>>> hmDorvalRoomDetails = new HashMap<>();
	static HashMap<String, HashMap<String, HashMap<String, String>>> hmKirklandRoomDetails = new HashMap<>();
	static HashMap<String, HashMap<String, HashMap<String, String>>> hmWestmountRoomDetails = new HashMap<>();
	static HashMap<String, List<String>> studentVsBookingList = new HashMap<String, List<String>>();

	int iBookingId = 110;
	String dateofBooking;
	String RoomNumber;
	String strStudentId;
	String iresultID;

	public RoomReservationImplementation() throws RemoteException, ParseException {
		hmDorvalRoomDetails = fnAddRoomsFromDorval();
		hmKirklandRoomDetails = fnAddDefaultRoomsFromKirkland();
		hmWestmountRoomDetails = fnAddDefaultRoomsFromWestmount();
	}

	
	
	@Override
	public String fnBookRoom(String StudentId, String campus,
			String dRoomNumber, String dtDate, String dtToTimeSlott) {
		// TODO Auto-generated method stub
		
		if (studentVsBookingList.containsKey(strStudentId)
				&& studentVsBookingList.get(StudentId).size() >= 3) {
			return "failed";
		}

		HashMap<String, HashMap<String, HashMap<String, String>>> dateWiseRoomDetails = getdateWiseRoomDetails(campus);
		HashMap<String, HashMap<String, HashMap<String, String>>> roomDetails = null;
		String camp = StudentId.substring(0, 3).toUpperCase();
		System.out.println("Student ID campus "+camp);
		switch (camp.toLowerCase()) {
		case "dvl":
			roomDetails = hmDorvalRoomDetails;
			break;

		case "kkl":
			roomDetails = hmKirklandRoomDetails;
			break;

		case "wst":
			roomDetails = hmWestmountRoomDetails;
			break;

		default:
			return "failed";
		}
		System.out.println("no date");
		if (dateWiseRoomDetails.get(dtDate) == null)
			return "failed";
		System.out.println("slot missed");
		HashMap<String, String> timeSlotsOfaRoom = dateWiseRoomDetails.get(
				dtDate).get(dRoomNumber);
		if (timeSlotsOfaRoom == null)
			return "failed";
		if (!timeSlotsOfaRoom.containsKey(dtToTimeSlott)
				|| timeSlotsOfaRoom.get(dtToTimeSlott) != null)
			return "failed";

		String bookingId = camp.substring(0,3) +"-"+ "BID" + iBookingId++;
		addBookingIdTOStudent(StudentId, bookingId);
		timeSlotsOfaRoom.put(dtToTimeSlott, bookingId);
		System.out.println("Booking done Sucessfully");
		return bookingId;
	}

	@Override
	public boolean fnDeleteRoom(String adminId, String date, String roomNumber,
			String timeSlots) {
		// TODO Auto-generated method stub
		HashMap<String, HashMap<String, HashMap<String, String>>> dateWiseRoomDetails = getdateWiseRoomDetails(adminId
				.substring(0, 3).toUpperCase());
		if (dateWiseRoomDetails.get(date) == null) {
			return false;
		}
		HashMap<String, String> timeSlotsOfARoom = dateWiseRoomDetails
				.get(date).get(roomNumber);
		if (timeSlotsOfARoom == null)
			return false;
		fnTracelog("Delete" + timeSlots);
		for (String timeSlot : timeSlots.split(",")) {
			fnTracelog("Loop" + timeSlot);
			String bookingID = timeSlotsOfARoom.get(timeSlot);
			fnRemoveBookingId(bookingID);
			timeSlotsOfARoom.remove(timeSlot);
		}
		return true;
	}

	@Override
	  public synchronized String fnGetAvailableTimeSlot(String campusName, String date) 
			  throws RemoteException, UnknownHostException, IOException {

		// TODO Auto-generated method stub
		 System.out.println(campusName);

		byte[] bReceiveData = new byte[10];
		byte[] bSendData = date.getBytes();
		String strTotalCount = "";
		// Here we are using local system so "localhost"
		InetAddress ipHostAddress = InetAddress.getByName("localhost");
		DatagramSocket dgSocket = new DatagramSocket();
		DatagramPacket dpSendPacket = null;
		String strReplyFromServerOne = "";
		String strReplyFromServerTwo = "";

		DatagramPacket dpReceivePacket = new DatagramPacket(bReceiveData, bReceiveData.length);
		try {
			if (campusName.equalsIgnoreCase("DVL")) {
				dpSendPacket = new DatagramPacket(bSendData, bSendData.length, ipHostAddress, 8000);
				dgSocket.send(dpSendPacket);
				dgSocket.receive(dpReceivePacket);
				strReplyFromServerOne = new String(trim(dpReceivePacket.getData()), "UTF-8");
		
				dpSendPacket = new DatagramPacket(bSendData, bSendData.length, ipHostAddress, 9000);
				dgSocket.send(dpSendPacket);
				dgSocket.receive(dpReceivePacket);
				strReplyFromServerTwo = new String(trim(dpReceivePacket.getData()), "UTF-8");
				strTotalCount = "DVL: " + fnGetAvailableTimeSlotOfACampus(campusName,date) + ", " + "KKL: " + strReplyFromServerOne + ", "+ "WST: " + strReplyFromServerTwo;
			
			} else if (campusName.equalsIgnoreCase("KKL")) {
				dpSendPacket = new DatagramPacket(bSendData, bSendData.length, ipHostAddress, 7011);
				dgSocket.send(dpSendPacket);
				dgSocket.receive(dpReceivePacket);
				strReplyFromServerOne = new String(trim(dpReceivePacket.getData()), "UTF-8");
		
				dpSendPacket = new DatagramPacket(bSendData, bSendData.length, ipHostAddress, 9000);
				dgSocket.send(dpSendPacket);
				dgSocket.receive(dpReceivePacket);
				strReplyFromServerTwo = new String(trim(dpReceivePacket.getData()), "UTF-8");
				strTotalCount = "KKL: " + fnGetAvailableTimeSlotOfACampus(campusName,date)  + ", " + "DVL: " + strReplyFromServerOne + ", "+ "WST: " + strReplyFromServerTwo;

			} else if (campusName.equalsIgnoreCase("WST")) {
				dpSendPacket = new DatagramPacket(bSendData, bSendData.length, ipHostAddress, 7011);
				dgSocket.send(dpSendPacket);
				dgSocket.receive(dpReceivePacket);
				strReplyFromServerOne = new String(trim(dpReceivePacket.getData()), "UTF-8");
			
				dpSendPacket = new DatagramPacket(bSendData, bSendData.length, ipHostAddress, 8000);
				dgSocket.send(dpSendPacket);
				dgSocket.receive(dpReceivePacket);
				strReplyFromServerTwo = new String(trim(dpReceivePacket.getData()), "UTF-8");
				strTotalCount = "WST: " + fnGetAvailableTimeSlotOfACampus(campusName,date)  + ", " + "DVL: " + strReplyFromServerOne + ", "+ "KKL: " + strReplyFromServerTwo;

			}
		} catch (Exception e) {
			System.out.println("Error");
		}
	     return strTotalCount; 
	} 



	@Override
	public boolean fnCancelBooking(String strBookingId, String StudentId) {

		boolean result =  false ;
		// TODO Auto-generated method stub
		List<String> bookingList = studentVsBookingList.get(StudentId);
		if (bookingList == null)
			return false;
		boolean authorized = false;
		for (String bookingId : bookingList) {
			if (strBookingId.equals(bookingId)) {
				authorized = true;
				break;
			}
		}
		if (!authorized)
			return false;
		result = cancelBooking(strBookingId, hmDorvalRoomDetails);
		if (!result) {
			result = cancelBooking(strBookingId, hmKirklandRoomDetails);
		}
		if (!result) {
			result = cancelBooking(strBookingId, hmWestmountRoomDetails);
		}
		bookingList.remove(strBookingId);
		return result;
	}

	@Override
	public boolean fnCreateRoom(String adminId, String date, String roomNumber,
			String timeSlots) {
		// TODO Auto-generated method stub
		fnTracelog("entry admmin" + adminId.substring(0, 4));
		HashMap<String, HashMap<String, HashMap<String, String>>> roomDetails = null;
		String campus = adminId.substring(0, 4);

		switch (campus.toUpperCase()) {
		case "DVLA":
			roomDetails = hmDorvalRoomDetails;
			break;

		case "KKLA":
			roomDetails = hmKirklandRoomDetails;
			break;

		case "WSTA":
			roomDetails = hmWestmountRoomDetails;
			break;

		default:
			return false;
		}

		HashMap<String, HashMap<String, String>> allRoomDetailsOfADay = roomDetails
				.get(date);
		if (allRoomDetailsOfADay == null) {
			allRoomDetailsOfADay = new HashMap<>();
			roomDetails.put(date, allRoomDetailsOfADay);
		}
		HashMap<String, String> timeSlotsOfRoom = allRoomDetailsOfADay
				.get(roomNumber);
		if (timeSlotsOfRoom == null) {
			timeSlotsOfRoom = new HashMap<>();
			allRoomDetailsOfADay.put(roomNumber, timeSlotsOfRoom);
		}
		for (String timeSlot : timeSlots.split(",")) {
			if (!timeSlotsOfRoom.containsKey(timeSlot))
				timeSlotsOfRoom.put(timeSlot, null);
		}
		fnTracelog("successful");
		return true;

	}

	private HashMap<String, HashMap<String, HashMap<String, String>>> fnAddRoomsFromDorval() {
		HashMap<String, HashMap<String, HashMap<String, String>>> dateWiseRoomDetails = new HashMap<>();
		// adding for a particulat room
		HashMap<String, HashMap<String, String>> roomDetailsOfADay = new HashMap<>();
		// adding time slots
		{
			HashMap<String, String> timeSlotsOfRoom = new HashMap<>();
			timeSlotsOfRoom.put("10:00-12:00", null);
			timeSlotsOfRoom.put("13:00-15:00", null);
			roomDetailsOfADay.put("201", timeSlotsOfRoom);
		}

		{
			HashMap<String, String> timeSlotsOfRoom = new HashMap<>();
			timeSlotsOfRoom.put("10:00-12:00", null);
			timeSlotsOfRoom.put("13:00-15:00", null);
			roomDetailsOfADay.put("202", timeSlotsOfRoom);
		}

		dateWiseRoomDetails.put("01-10-2017", roomDetailsOfADay);
		dateWiseRoomDetails.put("02-10-2017", roomDetailsOfADay);
		return dateWiseRoomDetails;
	}

	private HashMap<String, HashMap<String, HashMap<String, String>>> fnAddDefaultRoomsFromKirkland() {

		HashMap<String, HashMap<String, HashMap<String, String>>> dateWiseRoomDetails = new HashMap<>();

		// adding for a particulat room
		HashMap<String, HashMap<String, String>> roomDetailsOfADay = new HashMap<>();
		// adding time slots
		{
			HashMap<String, String> timeSlotsOfRoom = new HashMap<>();
			timeSlotsOfRoom.put("10:00-12:00", null);
			timeSlotsOfRoom.put("13:00-15:00", null);
			roomDetailsOfADay.put("301", timeSlotsOfRoom);
		}

		{
			HashMap<String, String> timeSlotsOfRoom = new HashMap<>();
			timeSlotsOfRoom.put("10:00-12:00", null);
			timeSlotsOfRoom.put("13:00-15:00", null);
			roomDetailsOfADay.put("302", timeSlotsOfRoom);
		}

		dateWiseRoomDetails.put("01-10-2017", roomDetailsOfADay);
		dateWiseRoomDetails.put("02-10-2017", roomDetailsOfADay);

		return dateWiseRoomDetails;
	}

	private HashMap<String, HashMap<String, HashMap<String, String>>> fnAddDefaultRoomsFromWestmount() {

		HashMap<String, HashMap<String, HashMap<String, String>>> dateWiseRoomDetails = new HashMap<>();

		// adding for a particular room
		HashMap<String, HashMap<String, String>> roomDetailsOfADay = new HashMap<>();

		// adding time slots
		{
			HashMap<String, String> timeSlotsOfRoom = new HashMap<>();
			timeSlotsOfRoom.put("10:00-12:00", null);
			timeSlotsOfRoom.put("13:00-15:00", null);
			roomDetailsOfADay.put("401", timeSlotsOfRoom);
		}

		{
			HashMap<String, String> timeSlotsOfRoom = new HashMap<>();
			timeSlotsOfRoom.put("10:00-12:00", null);
			timeSlotsOfRoom.put("13:00-15:00", null);
			roomDetailsOfADay.put("402", timeSlotsOfRoom);
		}

		dateWiseRoomDetails.put("01-10-2017", roomDetailsOfADay);
		dateWiseRoomDetails.put("02-10-2017", roomDetailsOfADay);

		return dateWiseRoomDetails;
	}

	private void addBookingIdTOStudent(String studentID, String bookingId) {
		List<String> bookingList = studentVsBookingList.get(studentID);
		if (bookingList == null) {
			bookingList = new ArrayList<>();
			studentVsBookingList.put(studentID, bookingList);
			System.out.println("added");
		}
		bookingList.add(bookingId);
	}

	private boolean cancelBooking(
			String bookingId,
			HashMap<String, HashMap<String, HashMap<String, String>>> dateWiseRoomDetails) {
		if (dateWiseRoomDetails.values() == null)
			return false;
		for (HashMap<String, HashMap<String, String>> allRoomDetailsOfADay : dateWiseRoomDetails
				.values()) {
			if (allRoomDetailsOfADay == null
					|| allRoomDetailsOfADay.values() == null)
				return false;
			for (HashMap<String, String> timeSlotsOfARoom : allRoomDetailsOfADay
					.values()) {
				if (timeSlotsOfARoom == null
						|| timeSlotsOfARoom.entrySet() == null)
					return false;
				for (Map.Entry<String, String> entry : timeSlotsOfARoom
						.entrySet()) {
					if (bookingId.equals(entry.getValue())) {
						entry.setValue(null);
						return true;
					}
				}
			}
		}
		return false;
	}

	public void fnOpenUDPServer(int iPortNumber, String campus)
			throws IOException {
		DatagramSocket dUDPSocket = new DatagramSocket(iPortNumber);
		byte[] receiveData = new byte[10];
		byte[] sendData;
		String strValue = "";
		System.out.println(campus + " UDP is running");
		while (true) {
			try {

				DatagramPacket receivePacket = new DatagramPacket(receiveData,
						receiveData.length);
				dUDPSocket.receive(receivePacket);
				String date = new String(receivePacket.getData());
				fnTracelog("request received" + date);
				InetAddress IPAddress = receivePacket.getAddress();
				int iPort = receivePacket.getPort();
				String strResult = ""
						+ fnGetAvailableTimeSlotOfACampus(campus, date);
				sendData = strResult.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData,
						sendData.length, IPAddress, iPort);
				dUDPSocket.send(sendPacket);
			} catch (Exception e) {
				System.out.println(e.getStackTrace());
			}
		}
		
	}

	public synchronized int fnGetAvailableTimeSlotOfACampus(String campus,
			String date) {
		fnTracelog("campus" + campus);
		HashMap<String, HashMap<String, HashMap<String, String>>> dateWiseRoomDetails = getdateWiseRoomDetails(campus);
		if (dateWiseRoomDetails.get(date) == null
				|| dateWiseRoomDetails.get(date).values() == null)
			return 0;
		int result = 0;
		for (HashMap<String, String> timeSlots : dateWiseRoomDetails.get(date)
				.values()) {
			if (timeSlots != null && timeSlots.values() != null) {
				for (String booked : timeSlots.values()) {
					if (booked == null)
						result++;
				}
			}
		}
		return result;
	}


	
	//public String fnTransferRoomDetails(String strBookingID, String strStudentId,
		//	String strNewCampusName, String strRoomNumber, String strNewTimeSlot) {
		// TODO Auto-generated method stub

		//System.out.println("transfer called");
		//List<String> BookingList = studentVsBookingList.get(strStudentId);
		//String[] parts = strBookingID.split("-");
		//String strOldSource = parts[0];    
		//if (BookingList == null) {
		//	return "No records found for the booking ID.";
		
		//} else {
	
		//	byte[] bReceiveData = new byte[100];
		//	byte[] bSendData = null;

		//	DatagramPacket dpSendPacket = null;
		//	DatagramPacket dpReceivePacket = new DatagramPacket(bReceiveData,
		//			bReceiveData.length);

		//	try {
		//		InetAddress ipHostAddress = InetAddress.getByName("localhost");
		//		DatagramSocket dgSocket = new DatagramSocket();
		//		String strReplyFromServer = "";

			//	String strRequest = strNewCampusName + "," + strRoomNumber + ","
			//			+ strNewTimeSlot;

			//	bSendData = strRequest.getBytes();

			//	if (strNewCampusName.equalsIgnoreCase("DVL")) {
			//		dpSendPacket = new DatagramPacket(bSendData,
			//				bSendData.length, ipHostAddress, 7000);
			//		dgSocket.send(dpSendPacket);
			//		dgSocket.receive(dpReceivePacket);
			//	} else if (strNewCampusName.equalsIgnoreCase("KKL")) {
			//		dpSendPacket = new DatagramPacket(bSendData,
			//				bSendData.length, ipHostAddress, 8000);
			//		dgSocket.send(dpSendPacket);
			//		dgSocket.receive(dpReceivePacket);
			//	} else if (strNewCampusName.equalsIgnoreCase("WST")) {
			//		dpSendPacket = new DatagramPacket(bSendData,
			//				bSendData.length, ipHostAddress, 9000);
			//		dgSocket.send(dpSendPacket);
			//		dgSocket.receive(dpReceivePacket);
			//	}
			//	strReplyFromServer = new String(
			//			trim(dpReceivePacket.getData()), "UTF-8");

			//	System.out.println(strReplyFromServer);
				
			//	this.studentVsBookingList.remove(strBookingID);
		
			//	fnTracelog("Record removed from old server");

			//	return strReplyFromServer;
			//}

			//catch (Exception e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			//	return "" ; }
		//}
			
	//	}
	
	
	public void fnUpdateNewRoomReservationUDP(int iPortNumber, String strSource) {
		System.out.println(strSource + " transfer server is running");
		
		System.out.println("PART-3");
		
		byte[] receiveData = new byte[500];
		byte[] sendData;
		try {
			DatagramSocket dUDPSocket = new DatagramSocket(iPortNumber);

			while (true) {
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				dUDPSocket.receive(receivePacket);
				InetAddress IPAddress = receivePacket.getAddress();
				int iPort = receivePacket.getPort();

				String strRequest = new String(trim(receivePacket.getData()), "UTF-8");

				System.out.println(strRequest + "from client");
				
				String[] requestArray =  strRequest.split(",");
				String campusName = requestArray[0];
				String roomNumber = requestArray[1];
				String timeSlot = requestArray[2];
				String studentId = requestArray[3];
				String date = requestArray[4];
                				
				String strResult = fnBookRoom(studentId, campusName, roomNumber, date, timeSlot);
				
				System.out.println("PART-4");
				
			    System.out.println("server is running");
				sendData = strResult.getBytes();

				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, iPort);
				dUDPSocket.send(sendPacket);
			}

		} catch (Exception ex) {
			System.out.println("error is transfer server UDP");
		}
		
	}
	
		@Override
	public String fnTransferRoomDetails(String strBookingId, String strStudentId, 
			String strCampusName,String strNewCampusName,String strDate1,String strRoomNumber,String strToTimeSlot) {
		byte[] bReceiveData = new byte[100];
		byte[] bSendData = null;
		
		DatagramPacket dpSendPacket = null;
		DatagramPacket dpReceivePacket = new DatagramPacket(bReceiveData, bReceiveData.length);
		
		try {
			InetAddress ipHostAddress = InetAddress.getByName("localhost");
			DatagramSocket dgSocket = new DatagramSocket();
			String strReplyFromServer = "";

			String strRequest =  strNewCampusName+","+strRoomNumber+","+strToTimeSlot+","+strStudentId + ","+ strDate1;
			
			bSendData = strRequest.getBytes();
			
			//System.out.println("Server is running");
			if (strNewCampusName.equalsIgnoreCase("DVL")) {
				dpSendPacket = new DatagramPacket(bSendData, bSendData.length, ipHostAddress, 7050);
				dgSocket.send(dpSendPacket);
				dgSocket.receive(dpReceivePacket);
			} else if (strNewCampusName.equalsIgnoreCase("KKL")) {
				dpSendPacket = new DatagramPacket(bSendData, bSendData.length, ipHostAddress, 8050);
				dgSocket.send(dpSendPacket);
				dgSocket.receive(dpReceivePacket);
			} else if (strNewCampusName.equalsIgnoreCase("WST")) {
				dpSendPacket = new DatagramPacket(bSendData, bSendData.length, ipHostAddress, 9050);
				dgSocket.send(dpSendPacket);
				dgSocket.receive(dpReceivePacket);
			}
			
			System.out.println("PART-1");
			strReplyFromServer = new String(dpReceivePacket.getData(), "UTF-8");
			System.out.println("PART-2");
			System.out.println(strReplyFromServer);
			
			this.studentVsBookingList.remove(strBookingId);
			
			fnTracelog("Record removed from old server");
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	///	String bookingId = strBookingId.substring(0,3) +"-"+ "BID" + iBookingId++;
		return strBookingId;

	}
	
	
		// TODO Auto-generated method stub
		static byte[] trim(byte[] bytes) {
			int i = bytes.length - 1;
			while (i >= 0 && bytes[i] == 0) {
				--i;
		}

			return Arrays.copyOf(bytes, i + 1);
		}
	

	public HashMap<String, HashMap<String, HashMap<String, String>>> getdateWiseRoomDetails(
			String campus) {
		switch (campus.toUpperCase()) {
		case "DVL":
			return hmDorvalRoomDetails;

		case "KKL":
			return hmKirklandRoomDetails;

		case "WST":
			return hmWestmountRoomDetails;
		}
		return null;
	}
	

	private void fnRemoveBookingId(String bookingId) {
		for (List<String> bookingList : studentVsBookingList.values()) {
			bookingList.remove(bookingId);
		}
	}

	public static void fnTracelog(String strLog) {
		try {
			java.util.logging.Logger logger = Logger.getLogger("Room Reservation Log");
			FileHandler fh;
			FileHandler fileHandler = new FileHandler(
					"C:/journels/RoomImplememtaion.log", true);
			logger.addHandler(fileHandler);
			logger.info(strLog + "\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error");
		}

	}

}

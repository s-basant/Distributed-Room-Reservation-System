package RoomReservation;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.omg.CORBA.ORB;


public class RoomReservationClient {

	public static String strCampusName;
	public static String strRoomNumber;
	public static String strDate;
	public static String strToTimeSlot;
	public static String strEditField;
	public static String strExistingRoomNumber;
	public static String strBookingId;
	public static String strAdminId;
	public static String strStudentId;
	public static String strNewCampusName;
	public static String strDate1;
	static RoomReservationInterface iDorvalRooms = null;
	static RoomReservationInterface iKirklandRooms = null;
	static RoomReservationInterface iWestmountRooms = null;
	static RoomReservationInterface iRoomReservation = null;

	
	public static void main(String[] args) throws Throwable, Exception {
		// TODO Auto-generated method stub
		ORB orb = ORB.init(args, null);

		// Dorvel server
		BufferedReader brDorvalServer = new BufferedReader(new FileReader(
				"DorvalServer.txt"));
		String iorDorval = brDorvalServer.readLine();
		brDorvalServer.close();
		org.omg.CORBA.Object objDorvalCorba = orb
				.string_to_object(iorDorval);
		iDorvalRooms = RoomReservationInterfaceHelper
				.narrow(objDorvalCorba);

		BufferedReader brKirklandServer = new BufferedReader(new FileReader(
				"KirklandServer.txt"));
		String iorKirkland = brKirklandServer.readLine();
		brKirklandServer.close();
		org.omg.CORBA.Object objKirklandCorba = orb
				.string_to_object(iorKirkland);
		iKirklandRooms = RoomReservationInterfaceHelper
				.narrow(objKirklandCorba);

		BufferedReader brWestmountServer = new BufferedReader(new FileReader(
				"WestmountServer.txt"));
		String iorWestmount = brWestmountServer.readLine();
		brWestmountServer.close();
		org.omg.CORBA.Object objWestmountCorba = orb
				.string_to_object(iorWestmount);
		iWestmountRooms = RoomReservationInterfaceHelper
				.narrow(objWestmountCorba);
		
		Integer choice = 0;
		while (choice < 3) {
			System.out.println("Welcome");
			System.out.println("Login as:");
			System.out.println("1. Student");
			System.out.println("2. Admin");
			System.out.println("3. Exit");

			Scanner in = new Scanner(System.in);
			choice = in.nextInt();

			if (choice == 1) {
				{
					fnStudent();
				}
			} else if (choice == 2)
				fnAdmin();
		}
		System.out.println("Exit");
	}

	private static void fnStudent() throws ParseException, IOException {
		// TODO Auto-generated method stub
		Integer Option = 0;
		Scanner in1 = new Scanner(System.in);
		System.out.println("Enter your student Id:");
		strStudentId = in1.nextLine();
		while (Option < 5) {
			System.out.println("Select a choice from below:");
			System.out.println("1. Book a Room");
			System.out.println("2. Get available Time Slot");
			System.out.println("3. Cancel Booking");
			System.out.println("4. Transfer Booking details");
			System.out.println("5. Exit");
			
			Option = in1.nextInt();

			if (Option == 1) {
				fnGetRoomDetails();
				setCampusBasedOnId(strStudentId);
				
				try {
					Runnable t1 = new Runnable() {
						public void run() {
							try {
				String result = iRoomReservation.fnBookRoom(strStudentId,strCampusName,
						strRoomNumber, strDate,strToTimeSlot);
				System.out.println("Successfully booked"+" "+result + "\n");
				//System.out.println(result + "\n");
				fnTracelog(result+"");
				fnTracelog(strCampusName + strRoomNumber + strDate + strToTimeSlot);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					Runnable t2 = new Runnable() {
						public void run() {
							try {
				String result = iRoomReservation.fnBookRoom(strStudentId,strCampusName,
						strRoomNumber, strDate,strToTimeSlot);
				System.out.println("Successfully booked"+" "+result+ "\n");
				//System.out.println(result + "\n");
				fnTracelog(result+"");
				fnTracelog(strCampusName + strRoomNumber + strDate + strToTimeSlot);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					Runnable t3 = new Runnable() {
						public void run() {
							try {
				String result = iRoomReservation.fnBookRoom(strStudentId,strCampusName,
						strRoomNumber, strDate,strToTimeSlot);
			//System.out.println(result + "\n");
				System.out.println("Successfully booked"+" "+result+ "\n");
				fnTracelog(result+"");
				fnTracelog(strCampusName + strRoomNumber + strDate + strToTimeSlot);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					new Thread(t1).start();
					//new Thread(t2).start();
					//new Thread(t3).start();
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Error while booking Room." + e.toString());
					fnTracelog("Error Occured while booking room" + e.toString());
				}
				
			} else if (Option == 2) {
	            setCampusBasedOnId(strStudentId);
	            System.out.println("enter the date");
	            Scanner sc = new Scanner(System.in);
	            strDate = sc.nextLine();
			   String result = iRoomReservation.fnGetAvailableTimeSlot(strStudentId.substring(0,3).toUpperCase(),strDate);
			   System.out.println("Room Availabitiy:"+ result +"\n");
			} else if (Option ==3) {
				try {
					Scanner sc = new Scanner(System.in);
					System.out.println("Enter your booking id");
					strBookingId = sc.nextLine();
					boolean result = iRoomReservation.fnCancelBooking(strBookingId, strStudentId);
					System.out.println(result?"Cancelled successfully":"Cancellation failed");
					System.out.println("\n");
				}catch(Exception e) {
	            	System.out.println(e);
	            }
				
			}
			else if (Option == 4) {
				fnUpdateRoomRecord();
				//String strResult=iRoomReservation.fnTransferRoomDetails(strBookingId,strStudentId,  strCampusName, strNewCampusName, strDate1, strRoomNumber, strToTimeSlot);
				//iRoomReservation.fnTransferRoomDetails(strBookingId, strStudentId,strCampusName,strNewCampusName,strRoomNumber,strToTimeSlot);	
				
				System.out.println("Transfer is done");
			// String bookingId = strNewCampusName.substring(0,3) +"-"+ "BID" + strBookingId++;
			}	
		}
	}

	public static void setCampusBasedOnId(String id) {
		switch (id.substring(0,3).toUpperCase()) {
		case "DVL":
			iRoomReservation = iDorvalRooms;
			break;
		case "KKL":
			iRoomReservation = iKirklandRooms;
			break;
		case "WST":
			iRoomReservation = iWestmountRooms;
			break;
		}
		
	}
	
	private static void fnGetRoomDetails() throws ParseException {
		// TODO Auto-generated method stub
		Scanner in = new Scanner(System.in);
		System.out.println("Enter Campus Name:");
		strCampusName = in.nextLine();
		System.out.println("Enter Room Number:");
		strRoomNumber = in.nextLine();
		System.out.println("Enter date:");
		strDate = in.nextLine();
		System.out.println("Enter To Time Slot:");
		DateFormat dftm = new SimpleDateFormat("HH:mm");
		strToTimeSlot =  in.nextLine();
	}
	
	private static void fnAdmin() throws IOException {
		// TODO Auto-generated method stub
		Integer choice = 0;
		Scanner in = new Scanner(System.in);

		System.out.println("Enter Admin ID");

		strAdminId = in.nextLine();

		if (strAdminId.length() >= 5) {
			if (strAdminId.substring(0, 4).equalsIgnoreCase("DVLA")
					|| strAdminId.substring(0, 4).equalsIgnoreCase("KKLA")
					|| strAdminId.substring(0, 4).equalsIgnoreCase("WSTA")) {
				System.out.println("Welcome Admin");
				switch (strAdminId.substring(0, 4)) {
				case "DVLA":
					iRoomReservation = iDorvalRooms;
					break;
				case "KKLA":
					iRoomReservation = iKirklandRooms;
					break;
				case "WSTA":
					iRoomReservation = iWestmountRooms;
					break;
				}

				while (choice < 3) {
					System.out.println("Select a choice from below:");
					System.out.println("1. Create a Room");
					System.out.println("2. Delete a Room");
					System.out.println("3. Exit");

					choice = in.nextInt();
					fnTracelog("Admin ID: " + strAdminId);

					if (choice == 1) {
						fnCreateRoomDetails();
						boolean result = iRoomReservation.fnCreateRoom(strAdminId, strDate, strRoomNumber, strToTimeSlot);
						System.out.println(result);
						fnTracelog("Admin created the room");
					} else if (choice == 2) {
						fnDeleteRoomDetails();
						boolean result = iRoomReservation.fnDeleteRoom(strAdminId,strDate,strExistingRoomNumber, strToTimeSlot);
						String operationReuslt = result?"Sucessfully Deleted":"Deletion Failed" ;
						System.out.println(operationReuslt);
					    fnTracelog("Result:" + operationReuslt +"\n");
					}
				}
			} else {
				fnTracelog("Authroization failed" + strAdminId);
				System.out.println("Authroization failed");
			}
		} else {
			fnTracelog("Authroization failed" + strAdminId);
			System.out.println("Authroization failed");
		}}
	

	private static void fnCreateRoomDetails() {
		Scanner in = new Scanner(System.in);
		System.out.println("Enter Room Number");
	    strRoomNumber =  in.nextLine();
	    System.out.println("Enter Date");
	    strDate =  in.nextLine();
	    System.out.println("Enter time slots comma separated");
	    strToTimeSlot = in.nextLine();
	}
	


	private static void fnDeleteRoomDetails() {
		// TO DO Auto-generated method stub
		Scanner in = new Scanner(System.in);
		System.out.println("Enter Room Number which you want to delete:");
		strExistingRoomNumber = in.nextLine();
		System.out.println("Enter Date");
	    strDate =  in.nextLine();
	    System.out.println("Enter time slots comma separated");
	    strToTimeSlot = in.nextLine();
		
	}

	private static void fnUpdateRoomRecord() {
		// TODO Auto-generated method stub
		Scanner in = new Scanner(System.in);
		
		System.out.println("Enter the booking ID");
		String strBookingID = in.nextLine();
		
		System.out.println("Enter the student ID");
		String strStudentID = in.nextLine();
		
		System.out.println("Enter the Old Campus Name");
		String strCampusName = in.nextLine();
		
		System.out.println("Enter the New Campus Name");
		String strNewCampusName = in.nextLine();
		
		System.out.println("Enter the date");
		String strDate1 = in.nextLine();

		
		System.out.println("Enter the RoomNumber");
		String strRoomNumber = in.nextLine();
		
		System.out.println("Enter the TimeSlot");
		String strTimeSlot = in.nextLine();

		try {
			System.out.println("Tranfer is done and the bookingID is " + " "+strNewCampusName.substring(0,3)+"-"+ "BID");
			String result =iRoomReservation.fnTransferRoomDetails(
					strBookingID, strStudentID, strDate1,strCampusName,strNewCampusName,strRoomNumber, strTimeSlot);
			//System.out.println("Transfer is done"+strNewCampusName.substring(0,3));
			  System.out.println("Room Availabitiy:"+ result +"\n");
		} catch (Exception ex) {

		}
	}
		
public static void fnTracelog(String strLog) {
try {
           Logger logger = Logger.getLogger("Room Reservation Log");
		   FileHandler fh;
		   FileHandler fileHandler = new FileHandler("F:/log/RoomReservation.log", true);
		   logger.addHandler(fileHandler);
           logger.info(strLog + "\n");
		  } catch (Exception e) {
	//	 TODO Auto-generated  catch block
			e.printStackTrace();
		System.out.println("Error");
		  }		
		
	}

}

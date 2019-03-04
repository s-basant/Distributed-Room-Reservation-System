package Server;


import java.io.IOException;
import java.rmi.RemoteException;

public interface RoomReservationInterface {
	public  String createRoom (String room_Number, String date, String[] list_Of_Time_Slots);
	public  String deleteRoom (String room_Number, String date, String[] list_Of_Time_Slots);
	public  String bookRoom (String campus_Name, String room_Number, String date, String timeslot, String StudentID);
	public  String getAvailableTimeSlot (String date);
	public  String cancelBooking (String booking_ID, String Student_ID);
	public  String changeReservation (String booking_ID, String campus_Name, String room_Number, String timeslot, String new_Date, String StudentID);
	public void shutDown();
}

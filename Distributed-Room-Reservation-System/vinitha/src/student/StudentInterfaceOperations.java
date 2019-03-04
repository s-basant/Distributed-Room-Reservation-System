package student;


/**
* student/StudentInterfaceOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from StudentIDL.idl
* Friday, December 1, 2017 12:00:27 PM EST
*/

public interface StudentInterfaceOperations 
{
  String bookRoom (String campusName, String studentID, String roomNumber, String date, String timeslot);
  String getAvailableTimeSlot (String date);
  boolean cancelBooking (String bookingID, String studentID);
  String changeReservation (String booking_id, String new_campus_name, String new_room_no, String new_time_slot, String studentID, String newDate);
} // interface StudentInterfaceOperations

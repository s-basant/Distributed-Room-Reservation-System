package admin;


/**
* admin/AdminInterfaceOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from AdminIDL.idl
* Sunday, October 29, 2017 1:08:05 PM EDT
*/

public interface AdminInterfaceOperations 
{
  boolean createRoom (String roomNumber, String date, String[] list_Of_Time_Slots);
  boolean deleteRoom (String roomNumber, String date, String[] list_Of_Time_Slots);
  boolean resetCount ();
} // interface AdminInterfaceOperations
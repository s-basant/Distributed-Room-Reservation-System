package reservationSystem;


public interface IRoomInterface {
	
	    /**
	     * Operation createRoom
	     */
	    public String createRoom(String creationDate, String room_Number, String list_Of_Time_Slots);

	    /**
	     * Operation deleteRoom
	     */
	    public String deleteRoom(String delRoom_Number, String deletingDate, String del_list_Of_Time_Slots);

	    /**
	     * Operation bookRoom
	     */
	    public String bookRoom(String campusName, String roomNumber, String bookingDate, String timeslot, String studentId);

	    /**
	     * Operation cancelBooking
	     */
	    public String cancelBooking(String bookingId, String studentId);

	    /**
	     * Operation getAvailableTimeSlot
	     */
	    public String getAvailableTimeSlot(String campusName, String dateIn);

	    /**
	     * Operation changeReservation
	     */
	    public String changeReservation(String booking_id, String new_campus_name, String new_room_no, String new_time_slot, String username, String dateInput);

	

}

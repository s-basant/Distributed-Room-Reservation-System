package reservationSystem;

public class RoomDetails {
	
	public String roomNo;
	public String creatDate;
	public String timeslots;
	
	public void returnForCreate(String room , String date , String time)
	{
		
		this.roomNo=room;
		this.creatDate=date;
		this.timeslots=time;
	
		
	}
	

}

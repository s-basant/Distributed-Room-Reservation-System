package admin;


import log.FileLogger;
import model.RoomRecord;

public class AdminImpl extends AdminInterfacePOA {
	
	private static final long serialVersionUID = 1L;

	FileLogger fileLogger = null;
	public RoomRecord roomrecord = null;
	public AdminImpl(String serverName,String replicaId,RoomRecord rcord){
		roomrecord = rcord;
		fileLogger = new FileLogger(serverName);		
	}

	@Override
	public boolean resetCount() {
		roomrecord.studentBookingCounter.clear();
		
		return false;
	}

	@Override
	public boolean createRoom(String roomNumber, String date, String[] list_Of_Time_Slots) {
		if(roomrecord.hasDate(date)) {
			if (roomrecord.hasRoom(date, roomNumber)) {
				roomrecord.addtimeSlotsToARoom(roomNumber, date, list_Of_Time_Slots);
			} else {
				roomrecord.createRoomAndInsertTimeSlots(roomNumber, date, list_Of_Time_Slots);
			}
			fileLogger.writeLog("Added time slot to "+date+" at "+roomNumber+" with ",list_Of_Time_Slots);
				
		} else {
			roomrecord.createSchedule(roomNumber, date, list_Of_Time_Slots);
			fileLogger.writeLog("Created time slot on "+date+" at "+roomNumber+" with ",list_Of_Time_Slots);
			
		}

		return true;
		
	}

	@Override
	public boolean deleteRoom(String roomNumber, String date, String[] list_Of_Time_Slots) {
		if (roomrecord.hasDate(date)) {
			if(roomrecord.hasRoom(date, roomNumber)){
				boolean done = roomrecord.deleteSlots(date,roomNumber,list_Of_Time_Slots);
				if(done){
				  fileLogger.writeLog("Deleted slot on "+date+" at "+roomNumber,list_Of_Time_Slots);					
				  return true;
				}else{
					fileLogger.writeLog("Failed to delete on "+date+" at "+roomNumber+" as not room exist");					
				    return false;
				} 
			} else {
				fileLogger.writeLog("Failed to delete on "+date+" at "+roomNumber+" as not room exist");					
			    return false;
			}
		}else{
			fileLogger.writeLog("Failed to delete on "+date+" at "+roomNumber+" as not date exist");
			return false;
		}		
	}
}

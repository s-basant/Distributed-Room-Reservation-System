package RoomReservation;

import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

/**
 * Interface definition: RoomReservationInterface.
 * 
 * @author OpenORB Compiler
 */
public interface RoomReservationInterfaceOperations
{
    /**
     * Operation fnBookRoom
     */
    public String fnBookRoom(String StudentId, String campus, String dRoomNumber, String dtDate, String dtToTimeSlott);

    /**
     * Operation fnDeleteRoom
     */
    public boolean fnDeleteRoom(String adminId, String date, String roomNumber, String timeSlots);

    /**
     * Operation fnGetAvailableTimeSlot
     * @throws IOException 
     * @throws UnknownHostException 
     * @throws RemoteException 
     */
    public String fnGetAvailableTimeSlot(String campus, String dtdate) throws RemoteException, UnknownHostException, IOException;

    /**
     * Operation fnCancelBooking
     */
    public boolean fnCancelBooking(String strBookingId, String StudentId);

    /**
     * Operation fnCreateRoom
     */
    public boolean fnCreateRoom(String adminId, String date, String roomNumber, String timeSlots);

    /**
     * Operation fnTransferRoomDetails
     */
    public String fnTransferRoomDetails(String strBookingId, String strStudentId, String strDate, String strCampusName, String strNewCampusName, String strRoomNumber, String strTimeSlot);

}

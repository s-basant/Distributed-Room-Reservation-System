package RoomReservation;

import java.rmi.RemoteException;
import java.text.ParseException;

import Replica.RServer;


public class WestmountReservationServer {

	public static void main(String[] args) throws RemoteException, ParseException {
		// TODO Auto-generated method stub
		
RoomReservationImplementation objRoomReservationImpl = new RoomReservationImplementation();
		
		RServer dvl = new RServer(objRoomReservationImpl, 3003);
		
		 new Thread(new Runnable() {					
				@Override
				public void run() {
					dvl.start();						
				}
			}).start();
	
	
		
		
		System.out.println("Westmount room Booking Server Running...");
	
		
		try {
			objRoomReservationImpl.fnOpenUDPServer(9000, "WST");
			
			objRoomReservationImpl.fnUpdateNewRoomReservationUDP(9050,"WST");

		} catch (Exception e) {
			System.out.println("Exception in Westmount Server: " + e);
		}


	}

}

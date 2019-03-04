package RoomReservation;

import java.rmi.RemoteException;
import java.text.ParseException;

import Replica.RServer;

public class DorvelRerservationServer {

	public static void main(String[] args) throws RemoteException, ParseException {
		// TODO Auto-generated method stub
		
		RoomReservationImplementation objRoomReservationImpl = new RoomReservationImplementation();
		
		RServer dvl = new RServer(objRoomReservationImpl, 3001);
		
		 new Thread(new Runnable() {					
				@Override
				public void run() {
					dvl.start();						
				}
			}).start();
	
		System.out.println("Dorvel room Booking Server Running...");
	
		try {
			objRoomReservationImpl.fnOpenUDPServer(7011, "DVL");
			
			objRoomReservationImpl.fnUpdateNewRoomReservationUDP(7050,"DVL");

		} catch (Exception e) {
			System.out.println("Exception in Dorvel Server: " + e);
		}

	}

}

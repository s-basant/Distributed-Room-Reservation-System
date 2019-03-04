package RoomReservation;

import java.rmi.RemoteException;
import java.text.ParseException;

import Replica.RServer;


public class KirklandRerservationServer {

	public static void main(String[] args) throws RemoteException, ParseException {
		// TODO Auto-generated method stub
	
		RoomReservationImplementation objRoomReservationImpl = new RoomReservationImplementation();
		
		RServer dvl = new RServer(objRoomReservationImpl, 3002);
		
		 new Thread(new Runnable() {					
				@Override
				public void run() {
					dvl.start();						
				}
			}).start();
	
		System.out.println("Kirkland room Booking Server Running...");

			
		try {
			objRoomReservationImpl.fnOpenUDPServer(8000, "KKL");
			
		    objRoomReservationImpl.fnUpdateNewRoomReservationUDP(8050,"KKL");


		} catch (Exception e) {
			System.out.println("Exception in Kirkland Server: " + e);
		}

	}

}

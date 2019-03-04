package reservationSystem;

import java.io.IOException;

import javax.xml.ws.Endpoint;

public class Publish {
	public static void main(String[] args) throws IOException 
	{
	Endpoint endpoint = Endpoint.publish("http://localhost:2222/DVLImpl", new DVLImpl());
	System.out.println("DVL Server Started : " + endpoint.isPublished());
	final DVLImpl DImpl = new DVLImpl();
	
	 new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					DImpl.callUDPServer(6789, "DVL");
				} catch (IOException e) {
					
					e.printStackTrace();
				}	
			}
		}).start();
   
	}

}

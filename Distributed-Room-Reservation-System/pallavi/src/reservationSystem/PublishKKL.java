package reservationSystem;

import java.io.IOException;

import javax.xml.ws.Endpoint;

public class PublishKKL {
	public static void main(String[] args) throws IOException 
	{
	Endpoint endpoint1 = Endpoint.publish("http://localhost:5265/KKLImpl", new KKLImpl());
	System.out.println("KKL Server Started : " + endpoint1.isPublished());
	
	final KKLImpl KImpl =new KKLImpl();
	
	 new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					KImpl.callUDPServer(6000, "KKL");
				} catch (IOException e) {
					
					e.printStackTrace();
				}	
			}
		}).start();
	}
}

package reservationSystem;

import java.io.IOException;

import javax.xml.ws.Endpoint;

public class PublishWST {
	public static void main(String[] args) throws IOException 
	{
	Endpoint endpoint = Endpoint.publish("http://localhost:5266/WSTImpl", new WSTImpl());
	System.out.println("WST Server Started : " +endpoint.isPublished());
	
	final WSTImpl WImpl =new WSTImpl();
	
	 new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					WImpl.callUDPServer(7000, "KKL");
				} catch (IOException e) {
					
					e.printStackTrace();
				}	
			}
		}).start();
	}
}

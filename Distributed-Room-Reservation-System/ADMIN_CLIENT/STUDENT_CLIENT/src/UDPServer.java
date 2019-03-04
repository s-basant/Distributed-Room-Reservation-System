


import java.net.*;
import java.io.*;
public class UDPServer{
	static DatagramSocket aSocket = null;
	
	public static void main(String args[]){ 
    	try{
	    	aSocket = new DatagramSocket(6789);
					// create socket at agreed port
			byte[] buffer = new byte[1000];
 			while(true){
 				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
				new Thread(new Runnable() { 					
					@Override
					public void run() {
					    handleRequest(request,aSocket);
					}    
				}).start();
    		}
		}catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		}catch (IOException e) {System.out.println("IO: " + e.getMessage());
		}finally {if(aSocket != null) aSocket.close();}
    }

	protected static void handleRequest(DatagramPacket request, DatagramSocket aSocket2) {
		// TODO Auto-generated method stub
		
	}
}

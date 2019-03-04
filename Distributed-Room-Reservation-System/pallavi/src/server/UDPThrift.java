package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import student.StudentImpl;

public class UDPThrift {

     StudentImpl studentImpl = null;
     String serverName = null;
     static ExecutorService threadPool = null;
	
    public UDPThrift(){
		if(threadPool == null){
			threadPool = Executors.newCachedThreadPool();
		}
	}
	
	 public void init(int port,String new_serverName, StudentImpl new_studentImpl) {		
	                	serverName = new_serverName;
	                	studentImpl = new_studentImpl;
	                	System.out.println("Started UDP Server too");		                
	                	this.startUDPServer(port);	                
	  }
	
	 
	 public void startUDPServer(int port){
		 
		 DatagramSocket aSocket = null;
		 try{
		    aSocket = new DatagramSocket(port);
			while(true){
                byte[] buffer = new byte[1000];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
	  			aSocket.receive(request);
	  			threadPool.execute( new UDPRequestThread(aSocket, request,studentImpl,serverName));  
			  }
		   }catch (SocketException e){ 
			   e.printStackTrace(); 
		   
		   }catch (IOException e) {  
			   e.printStackTrace(); 
		   
		   }
		   finally {
			   System.out.println("Socket closed");
			   if(aSocket != null) aSocket.close();
			  
		   }
	 
	 }	
}

/**
 * 
 */
package Replica;

/**
 * @author basant
 *
 */
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class UDPReplicaManagerReceiver implements Runnable
{
	String hostName;
	private int port;
	ReplicaManager replicaManager;
	public  boolean shouldStop = false;
	private String myReplicaId = "pallavi";
	public UDPReplicaManagerReceiver(String host , int port, ReplicaManager replicaManager)
	 {
		 this.hostName = host;
	     this.port = port;
	     this.replicaManager = replicaManager;
	 }
	 
	
      public void run()
      {
    	  try { 	  
            DatagramSocket serverSocket = new DatagramSocket(this.port);
            byte[] receiveData = new byte[1024];
            while(true)
               {
                  DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                  serverSocket.receive(receivePacket);
                  String InputValue = new String( receivePacket.getData());
                  InputValue=InputValue.trim();
                  System.out.println("RECEIVED: in RM Listner " + InputValue);
                  String[] arr = InputValue.split(":");    
                  String crashType = arr[0];
                  String replicaId = arr[1]; String serverName = arr[2] ; String requestId = "true";
                  
                  if(crashType.equals("CRASH") && replicaId.equals(myReplicaId)){
                	  
                	 System.out.println("Got crash ");
                	 DatagramSocket aSocket = null;
                	 
                	 try{
                	 aSocket = new DatagramSocket(); 
                	 aSocket.setSoTimeout(200);
                	 InetAddress aHost = InetAddress.getByName("localhost");
          			 DatagramPacket request = new DatagramPacket("ping".getBytes() , "ping".length(), aHost, 5001);
        			 aSocket.send(request);
        			 byte[] buffer = new byte[100];
        			 DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
        			 aSocket.receive(reply);
                	 } catch(SocketTimeoutException e){
        				System.out.println("Time out to replica ");
        				 replicaManager.restart(); 
        				//timeout so reset    
                	 }finally{
                		
                		 aSocket.close();
                	 }
                	  
                	  
                  }else if(crashType.equals("ERROR") && replicaId.equals(myReplicaId)){
                	  replicaManager.complain(replicaId,serverName ,requestId );
                  }
               }
    	  }
    	  catch(Exception e) {
    		  
    	  }finally{
    		  
    	  }
      }
      public synchronized void setShouldStop(boolean value) {
          this.shouldStop = value;
      }
}
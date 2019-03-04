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

public class UDPReplicaManagerReceiver implements Runnable
{
	String hostName;
	private int port;
	private String myReplicaId = "basanth";

	ReplicaManager replicaManager;
	public  boolean shouldStop = false;
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
            byte[] sendData = new byte[1024];
            while(true)
               {
                  DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                  serverSocket.receive(receivePacket);
                  String InputValue = new String( receivePacket.getData());
                  InputValue=InputValue.trim();
                  System.out.println("RECEIVED: " + InputValue);
                  InetAddress IPAddress = receivePacket.getAddress();
                  int port = receivePacket.getPort();
                  // to check availability
                  String date= InputValue.substring(1,11);
                  String replicaId = "true"; String serverName = "true" ; String requestId = "true";
                  if(replicaId.equals(myReplicaId)){               	  
                  
                  replicaManager.complain(replicaId,serverName ,requestId );
                  sendData = "true".getBytes();
                  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                  serverSocket.send(sendPacket);
                  }
               }
    	  }
    	  catch(Exception e) {
    		  
    	  }
      }
      public synchronized void setShouldStop(boolean value) {
          this.shouldStop = value;
      }
}
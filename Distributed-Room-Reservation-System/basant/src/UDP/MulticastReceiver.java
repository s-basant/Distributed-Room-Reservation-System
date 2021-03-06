package UDP;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
/*  ww  w .j av  a 2 s .c  o  m*/
import java.net.UnknownHostException;

import Replica.Replica;

public class MulticastReceiver implements Runnable {
	 private Replica replica;
	 public  boolean shouldStop = false;
	 public MulticastReceiver(Replica replica) throws UnknownHostException {
	        this.replica = replica;
	        new Thread(this).start();
	    }
	 public void run(){
	 	int mcPort = 12345;
	    String mcIPStr = "230.1.1.1";
	    MulticastSocket mcSocket = null;
	    InetAddress mcIPAddress = null;
	    try{
	    mcIPAddress = InetAddress.getByName(mcIPStr);
	    mcSocket = new MulticastSocket(mcPort);
	    System.out.println("Multicast Receiver running at:"
	        + mcSocket.getLocalSocketAddress());
	    mcSocket.joinGroup(mcIPAddress);

	    DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);

	    System.out.println("Waiting for a  multicast message...");
	    mcSocket.receive(packet);
	    String msg = new String(packet.getData(), packet.getOffset(),
	        packet.getLength());
	    System.out.println("[Multicast  Receiver] Received:" + msg);

	    mcSocket.leaveGroup(mcIPAddress);
	    mcSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
	

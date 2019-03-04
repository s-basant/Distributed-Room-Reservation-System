package Replica;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Server.RoomReservationSystem;

public class ReplicaUDPServer implements Runnable{

	public static int bookingId = 0;
	public static HashMap<String,String> bookingIds = new HashMap<>();
	static ExecutorService threadPool = null;
	HashMap<Long,DatagramPacket> messagesWaiting = new HashMap<>();
	int port;
	DatagramSocket aSocket = null;
	private long previousSeq = -1;
	public  boolean shouldStop = false;
	RoomReservationSystem rms;
	
	public ReplicaUDPServer(int port, RoomReservationSystem rms)
	 {
		this.port = port ;
		this.rms= rms;
		if(threadPool == null){
			threadPool = Executors.newCachedThreadPool();
		}
	 }
	 
	 public void run() {			
			 try{
			    aSocket = new DatagramSocket(port);
				while(true){
					System.out.println("started listening on "+port);
					byte[] buffer = new byte[1000];
					DatagramPacket request = new DatagramPacket(buffer, buffer.length);
					aSocket.receive(request);
					System.out.println("got req");
					execute(request);			
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
	 
	   public synchronized void setShouldStop(boolean value) {
	          this.shouldStop = value;
	   }
	   
	   
		public void execute(DatagramPacket request){
			String tmp = new String(request.getData(),request.getOffset(),request.getLength()).trim();		
			System.out.println("Message : "+tmp);
			
			long seq = Long.parseLong(tmp.substring( tmp.indexOf("SEQUENCE:")+9 , tmp.indexOf(";END;") ));
			
			System.out.println(previousSeq+" "+seq);
			
			if(previousSeq+1 == seq){						
				
				threadPool.execute(new ServerThread(request,this.rms));	
				
				previousSeq++;						
				
				System.out.println("inc seq"+previousSeq+" and seq "+seq);			
				
				while(messagesWaiting.containsKey(seq+1)){
					threadPool.execute(new ServerThread(messagesWaiting.get(seq+1),this.rms));
					messagesWaiting.remove(seq+1);
					seq = seq + 1;
					previousSeq++;
				}
				if(messagesWaiting.containsKey(seq)){
					messagesWaiting.remove(seq);
				}			
			}else{
			  	    //not reliable
				    System.out.println("missed seq");
					String message = "MISSED_SEQ:"+(seq-1)+"";
					DatagramPacket requestForMissed = new DatagramPacket(message.getBytes(), message.length(),request.getAddress(),request.getPort());							
					try {			
						aSocket.send(requestForMissed);
					} catch (IOException e) {
						e.printStackTrace();
					}
				
				messagesWaiting.put(seq, request);
			}
	   }
	   
	}



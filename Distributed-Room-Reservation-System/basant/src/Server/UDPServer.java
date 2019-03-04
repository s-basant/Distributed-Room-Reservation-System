package Server;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer implements Runnable
{
	private int port;
	private RoomReservationSystem rms;
	 public  boolean shouldStop = false;
	 public UDPServer(int port, RoomReservationSystem rms)
	 {
		this.port = port ;
		this.rms= rms;
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
                  if(InputValue.substring(0,1).equals("A"))
                  {
                   String date= InputValue.substring(1,11);
                  sendData = rms.getAllTimeslotCount(date).getBytes();
                  }
                 
                  else  if(InputValue.substring(0, 1).equals("B"))
                  {

                	  String campus_Name= InputValue.substring(1, 4);
                      String room_Number =  InputValue.substring(4, 7);
                      String date =  InputValue.substring(7, 17);
                      String timeslot =  InputValue.substring(17, 28);
                      String StudentID =  InputValue.substring(28,36);
                      
                      sendData = rms.getBookRoom(  campus_Name,  room_Number,  date,  timeslot,  StudentID).getBytes();
                  }
                  // to cancel booking cancelBooking ( booking_ID,  Student_ID );
                  else  if(InputValue.substring(0, 1).equals("C"))
                  {
                      String Student_ID= InputValue.substring(1, 9);
                      String booking_ID =  InputValue.substring(9,31); 
                      sendData = rms.getCancelBooking( booking_ID,  Student_ID ).getBytes();
                   }
                  // to validateBooking 
                  else  if(InputValue.substring(0, 1).equals("V"))
                  {
                      String Student_ID= InputValue.substring(1, 9);
                      String booking_ID =  InputValue.substring(9,31); 
                      sendData = rms.validateBooking( booking_ID,  Student_ID ).getBytes();
                   }
                  else  if(InputValue.substring(0, 1).equals("S"))
                  {
                	  System.out.println( "RECIEVED campus_Name:"+ InputValue.substring(1, 4) +
                              "timeslot :"+InputValue.substring(4, 15) +
                              "room_Number:"+InputValue.substring(15, 18) +
                              "booking_ID :"+  InputValue.substring(18,40) +
                              "new_Date:"+InputValue.substring(40, 50));
                	  String campus_Name= InputValue.substring(1, 4);
                      String timeslot =InputValue.substring(4, 15);
                      String room_Number=InputValue.substring(15, 18);
                      String booking_ID =  InputValue.substring(18,40); 
                      String new_Date=InputValue.substring(40, 50);
                      String Student_ID= InputValue.substring(50, 58);
                      String sendDataStr = rms.checkSlotAavailable(campus_Name,  timeslot,  room_Number, new_Date, Student_ID);
                      System.out.println( sendDataStr);
                      sendData = sendDataStr.getBytes();
                     
                  }
                  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                  serverSocket.send(sendPacket);
               }
    	  }
    	  catch(Exception e) {
    		  
    	  }
      }
      public synchronized void setShouldStop(boolean value) {
          this.shouldStop = value;
      }
}
package Replica;
import UDP.UdpSender;


import java.io.IOException;
import java.net.*;

public class ReplicaManagerUdpReceiver implements Runnable{
    String hostName;
    int port;
    ReplicaManager replicaManager;

    public ReplicaManagerUdpReceiver(String host, int port, ReplicaManager replicaManager){
        this.hostName = host;
        this.port = port;
        this.replicaManager = replicaManager;
    }

    @Override
    public void run() {
        DatagramSocket datagramSocket = null;
        try{
            System.out.println("Listening to recieve complaint/error from Frontend");
            datagramSocket = new DatagramSocket(port,InetAddress.getByName(hostName));
            while (true){
                byte[] data = new byte[65000];
                DatagramPacket datagramPacket = new DatagramPacket(data, data.length);
                datagramSocket.receive(datagramPacket);
                sendAcknowledgement(datagramPacket);
                complainToReplicaManager(datagramPacket);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (!datagramSocket.isClosed()){
                datagramSocket.close();
            }
        }
    }

    private void sendAcknowledgement(DatagramPacket datagramPacket){
        new Thread(new UdpSender("OK", datagramPacket.getAddress(),
                datagramPacket.getPort(), false)).start();
    }

    private void complainToReplicaManager(DatagramPacket datagramPacket){
      
    }
}
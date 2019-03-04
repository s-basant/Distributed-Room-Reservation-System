package UDP;
import java.util.Arrays;
import java.io.IOException;
import java.net.*;
import Configuration.Constants;

//reliable UDP sender

public class UdpSender implements Runnable {
    byte[] data;
    InetAddress hostIp;
    int port;
    public Object syncObject;
    public boolean needAck;

    public UdpSender(String message, String hostIp, int port, boolean needAck) {
        this(message.getBytes(), new byte[0], hostIp, port, needAck);
    }

    public UdpSender(String message, String hostIp, int port) {
        this(message.getBytes(), new byte[0], hostIp, port, true);
    }

    public UdpSender(String message, InetAddress hostIp, int port, boolean needAck){
        this(message.getBytes(), new byte[0], hostIp, port, needAck);
    }

    public UdpSender(byte[] data, byte[] prefix, InetAddress hostIp, int port, boolean needAck) {
    //    this.data = ArrayUtils.addAll(prefix, data);
        this.hostIp = hostIp;
        this.port = port;
        this.syncObject = new Object();
        this.needAck = needAck;
    }

    public UdpSender(byte[] data, byte[] prefix, String hostIp, int port, boolean needAck) {
     //   this.data = ArrayUtils.addAll(prefix, data);
        try{
            this.hostIp = InetAddress.getByName(hostIp);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.port = port;
        this.syncObject = new Object();
        this.needAck = needAck;
    }

    public UdpSender(byte[] data, byte[] prefix, String hostIp, int port) {
        this(data, prefix, hostIp, port, true);
    }

    @Override
    public void run() {
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length, hostIp, port);
            if (needAck){
                Thread recevingThread = receiveAcknowledgement(datagramSocket);
                recevingThread.start();
                int counter=0;
                synchronized (syncObject) {
                    while (counter < Constants.UDP_MAX_RETRY_ATTEMPT) {
                        datagramSocket.send(datagramPacket);
                        try {
                            syncObject.wait(Constants.RESPONSE_MAX_WAIT_TIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            if (!recevingThread.isAlive()) {
                                counter = Constants.UDP_MAX_RETRY_ATTEMPT;
                            }
                        }
                        counter++;
                    }
                }
            } else {
                datagramSocket.send(datagramPacket);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (datagramSocket != null) datagramSocket.close();
        }
    }

    private Thread receiveAcknowledgement(DatagramSocket datagramSocket) {
        return new Thread(new UdpReceiver(datagramSocket, syncObject, false));
    }
}
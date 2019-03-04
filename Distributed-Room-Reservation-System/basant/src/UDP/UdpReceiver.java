package UDP;

//package com.classmanagement.server.udp.reliable;

//import com.classmanagement.models.UdpResponseType;

import java.io.IOException;
import java.net.*;

//Reliable UDP receiver class

public class UdpReceiver implements Runnable{
    DatagramSocket datagramSocket;
    Object syncObject;
    DatagramPacket datagramPacket;
    boolean ackReceivedPacket;

    public UdpReceiver(String host, int port){
        this(host, port, null, false);
    }

    public UdpReceiver(String host, int port, boolean ackReceivedPacket){
        this(host, port, null,
                ackReceivedPacket);
    }

    public UdpReceiver(String host, int port, Object syncObject, boolean ackReceivedPacket){
        try {
            this.datagramSocket = new DatagramSocket(port, InetAddress.getByName(host));
            this.syncObject = syncObject;
            this.ackReceivedPacket = ackReceivedPacket;
            createReceivingPacket();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public UdpReceiver(DatagramSocket datagramSocket, Object syncObject){
        this(datagramSocket, syncObject, false);
    }

    public UdpReceiver(DatagramSocket datagramSocket, Object syncObject, boolean ackReceivedPacket){
        this.datagramSocket  = datagramSocket;
        this.syncObject = syncObject;
        this.ackReceivedPacket = ackReceivedPacket;
        createReceivingPacket();
    }

    private void createReceivingPacket(){
        byte[] data = new byte[65000];
        this.datagramPacket = new DatagramPacket(data, data.length);
    }

    private void acknowledgeReceivedPacket(){
        //byte[] data = UdpResponseType.OK.toString().getBytes();
        new Thread(new UdpSender("OK", this.datagramPacket.getAddress(),
                this.datagramPacket.getPort(), false)).start();
        /*try {
            datagramSocket.send(new DatagramPacket(data, data.length, this.datagramPacket.getAddress(),
                    this.datagramPacket.getPort()));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void run() {
        try {
            datagramSocket.receive(datagramPacket);
            if (ackReceivedPacket) {
                acknowledgeReceivedPacket();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            synchronized (syncObject){
                syncObject.notify();
            }
        }
    }

    public DatagramPacket getDatagramPacket() {
        return datagramPacket;
    }
}
package Replica;
import Server.RoomReservationInterface;
import Configuration.Constants;


import Server.RoomReservationSystem;
import Server.RoomReservationInterface;
import Utility.Recreate_hashMap;
import UDP.MulticastReceiver;
import UDP.UdpMulticastReceiver;

import java.io.*;
import java.net.UnknownHostException;

import static Configuration.Constants.REPLICA_ONE;
import static Configuration.Constants.REPLICA_TWO;
import static Configuration.Constants.REPLICA_FOUR;

//import org.apache.commons.lang3.*;


//Implementation specific Replica holder class

public class Replica {

    public RoomReservationInterface DVLServant ;
    public RoomReservationInterface KKLServant ;
    public RoomReservationInterface WSTServant ;
  //  private UdpMulticastReceiver replicaUdpListenerThread;
    private MulticastReceiver replicaUdpListenerThread;

    
    public String replicaId;

    public Replica(String replicaId) throws UnknownHostException {
        this.replicaId = replicaId;
        this.create();
      //  replicaUdpListenerThread = new UdpMulticastReceiver(this);
        replicaUdpListenerThread = new MulticastReceiver(this);
    }

    //Create three replicas each one for separate server DVL, KKL, WST based on replica ID
    private void create() {
        try {
            
        		DVLServant = new RoomReservationSystem("DVL", replicaId);
            	KKLServant = new RoomReservationSystem("KKL", replicaId);
            	WSTServant = new RoomReservationSystem("WST", replicaId);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //In case of any genuine error, shutdown the replica and restart it gracefully
    public void shutDown(String serverName, String replicaId) {
        try {
            if (replicaId.equals(Constants.REPLICA_FOUR)){
            	//this.replicaUdpListenerThread.setShouldStop(true);
                if (serverName.equals("DVL")) {
                    this.DVLServant.shutDown();
                    String filePath = Configuration.Constants.PROJECT_DIR +
                            "\\" + Configuration.Constants.BACKUP_DIR_NAME + "\\"
                            + Configuration.Constants.JOURNAL_DIR + "\\"
                            + "DVL" + "\\" + replicaId + "_"
                            + Configuration.Constants.JOURNAL_FILE_NAME;
                    String copyFilePath = filePath + "_temp";
                    copyFile(filePath, copyFilePath);
                    Recreate_hashMap creator =  new Recreate_hashMap(copyFilePath, "DVL", replicaId);
                    this.DVLServant = creator.execute();
                    System.out.println("Created new server for DVL at " + this.replicaId);
                } else if (serverName.equals("KKL")) {
                    this.KKLServant.shutDown();
                    String filePath = Configuration.Constants.PROJECT_DIR +
                            "\\" + Configuration.Constants.BACKUP_DIR_NAME + "\\"
                            + Configuration.Constants.JOURNAL_DIR + "\\"
                            + "KKL" + "\\" + replicaId + "_"
                            + Configuration.Constants.JOURNAL_FILE_NAME;
                    String copyFilePath = filePath + "_temp";
                    copyFile(filePath, copyFilePath);
                    Recreate_hashMap creator =  new Recreate_hashMap(copyFilePath, "DVL", replicaId);
                    this.DVLServant = creator.execute();
                    System.out.println("Created new server for KKL at " + this.replicaId);
                } else if (serverName.equals("WST")) {
                    this.WSTServant.shutDown();
                    String filePath = Configuration.Constants.PROJECT_DIR +
                            "\\" + Configuration.Constants.BACKUP_DIR_NAME + "\\"
                            + Configuration.Constants.JOURNAL_DIR +
                            "\\" + "WST" + "\\" + replicaId + "_"
                            + Configuration.Constants.JOURNAL_FILE_NAME;
                    String copyFilePath = filePath + "_temp";
                    copyFile(filePath, copyFilePath);
                    Recreate_hashMap creator =  new Recreate_hashMap(copyFilePath, "DVL", replicaId);
                    this.DVLServant = creator.execute();
                    System.out.println("Created new server for WST at " + this.replicaId);
                } else {
                    this.DVLServant.shutDown();
                    this.KKLServant.shutDown();
                    this.WSTServant.shutDown();
                    System.out.println("Created new server for DVL at " + this.replicaId);
                    System.out.println("Created new server for KKL at " + this.replicaId);
                    System.out.println("Created new server for WST at " + this.replicaId);
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Copy existing journal file to new temporary file
    //https://www.mkyong.com/java/how-to-copy-file-in-java/
    private void copyFile(String source, String dest){
        InputStream inStream = null;
        OutputStream outStream = null;
        try {
            File sourceFile = new File(source);
            File destFile = new File(dest);

            inStream = new FileInputStream(sourceFile);
            outStream = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(inStream != null){
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outStream != null){
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

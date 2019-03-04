package Replica;


import Configuration.Constants;
import model.GlobalFlags;
import model.ReplicaErrorInfo;
import model.ServerErrorInfo;

import java.util.HashMap;


// Class to implemenet Replica Manager
public class ReplicaManager {

    private UDPReplicaManagerReceiver replicaManagerUdpReceiver;
    private Thread udpThread;
    Replica replica = null;
    
    public static void main(String[] args) throws Exception {
        ReplicaManager replicaManager = new ReplicaManager();
        replicaManager.Create();
    }

    public ReplicaManager(){
        this.replicaManagerUdpReceiver = new UDPReplicaManagerReceiver(Constants.REPLICA_MANAGER_HOST_NAME,
                2001, this);
        this.udpThread = new Thread(this.replicaManagerUdpReceiver);
        this.udpThread.start();
     
    }
    private HashMap<String,ServerErrorInfo> errors = new HashMap<String, ServerErrorInfo>();

    //Create three replicas
    public void Create() {
    	if(replica == null){
        try {
        	replica = new Replica("manohar");
        	replica.create();
            errors.put("dvl", new ServerErrorInfo());
            errors.put("kkl", new ServerErrorInfo());
            errors.put("wst", new ServerErrorInfo());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    	} 
    }

    //Process any complaints received from FE
    public void complain(String replicaId, String serverName, String requestId) {
        System.out.println("Complaint recieved");
        ServerErrorInfo replicaErrorInfo = errors.get(serverName);
        System.out.println("Present error count is "+replicaErrorInfo.getCount());
        if(replicaErrorInfo.getCount() >= 2 ){
        	replicaErrorInfo.setCount(0);
        	replica.recreate(serverName);
        	GlobalFlags.isByzantinError = false;
        	
        }else{
        	replicaErrorInfo.increment();
        }            
    }

    

    private void resetServerErrorInfo(ReplicaErrorInfo replicaErrorInfo, String serverName) {
        if(serverName.equals("DVL")) {
            replicaErrorInfo.DVLserverErrorInfo = new ServerErrorInfo();
        }
        if(serverName.equals("KKL")) {
            replicaErrorInfo.KKLserverErrorInfo  = new ServerErrorInfo();
        }
        if(serverName.equals("WST")) {
            replicaErrorInfo.WSTserverErrorInfo  = new ServerErrorInfo();
        }
    }

    private boolean isNextRequestId(String currentRequestId, String newRequestId) {
        if(!currentRequestId.equals("") && !newRequestId.equals("")) {
            int currentRequestIdNum = Integer.parseInt(currentRequestId.substring(3));
            int newRequestIdNum = Integer.parseInt(newRequestId.substring(3));
            if((newRequestIdNum - currentRequestIdNum) == 1) {
                return true;
            }
        }
        return  false;
    }

    //Restart the replica
    public void restartReplica(String replicaId, String serverName){
        try {
            System.out.println("Restarting " + serverName + " Replica for: " + replicaId );
          
        }
        catch (Exception e) {
            //TODO: HANDLE IT
        }
    }
}

import java.util.ArrayList;
import java.util.Random;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import admin.AdminInterface;
import admin.AdminInterfaceHelper;

public class TestCreateSlots {

	
	public static void main(String[] args) throws InterruptedException {
		StaticData.testData.put("dvl", new ArrayList<>());
		StaticData.testData.put("kkl", new ArrayList<>());
		StaticData.testData.put("wst", new ArrayList<>());
		connectToServer(args);
		String[] servers = {"dvl","kkl","wst"};
		for(int i =0;i<100000;i++){
			int o = i;
			new Thread(new Runnable() {
				@Override
				public void run() {
					String randomServer =servers[new Random().nextInt(servers.length)]; 
					AdminInterface a = getRemotereference(randomServer);		
					String[] tmp  = {"12:30 - 13:50"};
					try {
						boolean isSucess = a.createRoom(randomServer,(10+o)+"", "23-10-2017",  tmp);
						if(isSucess){
							StaticData.writeData(randomServer,(10+o)+"");
						}
					    System.out.println("On thread "+o+" to server "+randomServer+" "+isSucess);
					       
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).run();
			//Thread.sleep(1);
		}
	   //deleteSlots();
	}
	
/*
   private static void deleteSlots() throws InterruptedException {
		

		for(String key:StaticData.testData.keySet()){
		    ArrayList<String> slots = StaticData.testData.get(key);
		    for(String s: slots){
		    new Thread( new Runnable() {
			    @Override
				public void run() {
			    	AdminInterface a = connectToServer(key);
			    	ArrayList<String> tmp  = new ArrayList<String>();
					tmp.add("12:30 - 13:50");
				    try {
				    	System.out.println(s + "23-10-2017" + tmp);
						boolean t = a.deleteRoom(s, "23-10-2017", tmp);
						if(t){
							System.out.println("deleted slot in room"+s+" on server "+key);
						}else{
							System.out.println("Failure");
						}
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}).run();
		    
		    Thread.sleep(99);
		  }
		}
	   
	}

*/
static AdminInterface dvl,kkl,wst;
public static void connectToServer(String[] args){
	ORB orb = ORB.init(args, null);
	
    try {
		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
        dvl = AdminInterfaceHelper.narrow(ncRef.resolve_str("dvl_admin_service"));
        wst = AdminInterfaceHelper.narrow(ncRef.resolve_str("wst_admin_service"));
        kkl = AdminInterfaceHelper.narrow(ncRef.resolve_str("kkl_admin_service"));
	} catch (InvalidName | NotFound | CannotProceed | org.omg.CosNaming.NamingContextPackage.InvalidName e) {
		e.printStackTrace();
	}
}	

public static AdminInterface getRemotereference(String serverName){
	if(serverName.equals("dvl")){
		return dvl;
	}else if(serverName.equals("kkl")){
		return kkl;
	}else{
		return wst;
	}
	
	
}

}

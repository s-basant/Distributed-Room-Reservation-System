import java.util.Random;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import student.StudentInterface;
import student.StudentInterfaceHelper;

public class TestSlots2 {

	
	public static void main(String[] args) throws InterruptedException {
		connectToServer(args);
		String[] bookingIds = {"dvl_23-10-2017_34_1","kkl_23-10-2017_34_1","wst_23-10-2017_34_1"};
		String[] servers = {"dvl","kkl","wst"};
		String[] serversInOrder = {"wst","wst","kkl"};
		for(int i =0;i<10000;i++){
			int o = i;
			new Thread(new Runnable() {
				@Override
				public void run() {
					int random = new Random().nextInt(servers.length);
					String randomServer =servers[random]; 
					StudentInterface st = getRemotereference(randomServer);
					System.out.println(st.changeReservation(randomServer,bookingIds[random],serversInOrder[random] , "34", "12:30 - 13:40", "kkls3456","12-12-2017")+" "+o);
				}
			}).run();
		}
		
	}
	
	
	static StudentInterface dvl,kkl,wst;
	public static void connectToServer(String[] args){
		ORB orb = ORB.init(args, null);
		
	    try {
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
	        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
	        dvl = StudentInterfaceHelper.narrow(ncRef.resolve_str("dvl_student_service"));
	        wst = StudentInterfaceHelper.narrow(ncRef.resolve_str("wst_student_service"));
	        kkl = StudentInterfaceHelper.narrow(ncRef.resolve_str("kkl_student_service"));
		} catch (InvalidName | NotFound | CannotProceed | org.omg.CosNaming.NamingContextPackage.InvalidName e) {
			e.printStackTrace();
		}
	}	

	public static StudentInterface getRemotereference(String serverName){
		if(serverName.equals("dvl")){
			return dvl;
		}else if(serverName.equals("kkl")){
			return kkl;
		}else{
			return wst;
		}		
	}



}

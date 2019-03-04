import java.util.Random;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import student.StudentInterface;
import student.StudentInterfaceHelper;

public class TestSlots {

	
	public static void main(String[] args) throws InterruptedException {
		connectToServer(args);
		String[] servers = {"dvl","kkl","wst"};
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				StudentInterface st = getRemotereference("wst");
				System.out.println(st.changeReservation("wst","dvl_05-11-2017_56_3", "kkl", "56", "14:30 - 15:30", "wst4567", "12-12-2017"));
			}
		}).run();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				StudentInterface st = getRemotereference("kkl");
				System.out.println(st.bookRoom("kkl","dvl", "kkls4567", "56", "05-11-2017", "10:30 - 11:30"));
			}
		}).run();
		
		/*for(int i =0;i<4;i++){
			int o = i;
			new Thread(new Runnable() {
				@Override
				public void run() {
					StudentInterface st = getRemotereference("wst");
					System.out.println(st.bookRoom("dvl", "wst4567", "56", "05-11-2017", "10:30 - 11:30"));
				}
			}).run();
		}*/
		
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

package server;

import admin.AdminImpl;
import admin.AdminInterface;
import admin.AdminInterfaceHelper;
import student.StudentImpl;
import student.StudentInterface;
import student.StudentInterfaceHelper;

import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;



public class Server
{

public Server() {}
  
  public static void main(String[] args)
  {
    try
    {
        ORB orb = ORB.init(args, null);
        
        POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));

        rootpoa.the_POAManager().activate();

        AdminImpl adminImpl = new AdminImpl("dvl");
        StudentImpl studentImpl = new StudentImpl("dvl");
		
        /*
         * A CORBA object reference is a handle for a particular CORBA object implemented by a server. 
         * A CORBA object reference identifies the same CORBA object each time the reference is used to invoke a method on the object. A CORBA object may have multiple, distinct object references. 
         */

    	org.omg.CORBA.Object ref = rootpoa.servant_to_reference(adminImpl);
        AdminInterface href = AdminInterfaceHelper.narrow(ref);
        
        org.omg.CORBA.Object ref1 = rootpoa.servant_to_reference(studentImpl);
        StudentInterface href1 = StudentInterfaceHelper.narrow(ref1);
        
        

        org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
        
        NameComponent path[] = ncRef.to_name( "dvl_admin_service" );
        ncRef.rebind(path, ref);
        
        NameComponent path1[] = ncRef.to_name( "dvl_student_service" );
        ncRef.rebind(path1, ref1);
        
        System.out.println("Server Started at DVL Campus");
		
    	new Thread(new Runnable() {			
			@Override
			public void run() {
				UDPThrift.init(8090);
			}
		}).start();

        orb.run();

        
        
    	
		
    }
   catch(Exception e){
    	e.printStackTrace();
    }
  }
  
}
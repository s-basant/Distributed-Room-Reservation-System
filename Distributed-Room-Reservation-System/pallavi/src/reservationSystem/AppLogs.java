package reservationSystem;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AppLogs {
	
		String file = " ";
		public Logger logs;
		
		FileHandler makeFile;
		public AppLogs(String fileName)throws SecurityException,IOException {
			
			
			File f =new File(fileName);
			if(!f.exists())
			{
				
				f.createNewFile();
				
			}
			makeFile =new FileHandler(fileName,true);
			logs = Logger.getLogger("ClientLogs");
			logs.addHandler(makeFile);
			SimpleFormatter formatter = new SimpleFormatter();
			makeFile.setFormatter(formatter);
		}
		
		
		
	




}

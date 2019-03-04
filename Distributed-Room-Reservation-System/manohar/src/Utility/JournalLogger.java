package Utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JournalLogger {

	private static BufferedWriter journalBufferedWriter = null;
	
	  public JournalLogger(String journalFile, String serverLocation, String replicaName) {
	    
		if(journalBufferedWriter == null){  
		File f = new File(journalFile);
      	try {
      		    f.delete();
				f.createNewFile();
				journalBufferedWriter = new BufferedWriter(new FileWriter(f, true));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	  }

	public synchronized void log(String inputString) {
		try {
			journalBufferedWriter.write(inputString);
			journalBufferedWriter.newLine();
			journalBufferedWriter.flush();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


 

}
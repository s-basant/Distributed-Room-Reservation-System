package Utility;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat.Field;
import java.util.logging.Logger;
import Configuration.Constants;

public class JournalLogger {

	private FileWriter journalFileWriter = null;
	private BufferedWriter journalBufferedWriter = null;
	private PrintWriter journalPrintWriter = null;
	private String replicaName;
	private Logger logger;

	  public JournalLogger(String journalFile, String serverLocation, String replicaName) throws IOException {
	        this.replicaName = replicaName;
	      //  initLogger(serverLocation);
	        journalFileWriter = new FileWriter(journalFile, true);
	        journalBufferedWriter = new BufferedWriter(journalFileWriter);
	        journalPrintWriter = new PrintWriter(journalBufferedWriter);
	    }

public synchronized void log(String inputString) throws Exception {
	String method = getFieldString(inputString,  "METHOD")  ; 
	if(!(method.equals("")) )
	{
		journalPrintWriter.println(inputString);
	}

	
	else
		throw new Exception();
	journalBufferedWriter.flush();
}


 
private String getFieldString(String str,String parameterName)
{
        if(parameterName.equals("SEQUENCE_NUMBER")){
            return str.substring( str.indexOf("SEQUENCE:")+9 , str.indexOf(";END;") ) ;
        }else if(parameterName.equals("METHOD")){
            parameterName = ";END";
            str = str.substring(str.indexOf(parameterName) + parameterName.length()+1);
            return str.substring(0, str.indexOf(":"));
        }
            str = str.substring(str.indexOf(parameterName) + parameterName.length()+1);
            return str.substring(0, str.indexOf(";"));  
    
}

private void initLogger(String location) throws IOException {
	
    logger = LoggerProvider.getLogger(location, replicaName);
}

}
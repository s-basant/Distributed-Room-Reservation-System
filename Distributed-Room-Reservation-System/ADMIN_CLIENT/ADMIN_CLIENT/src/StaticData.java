import java.util.ArrayList;
import java.util.HashMap;

public final class StaticData {

	static HashMap<String, ArrayList<String>> testData = new HashMap<>();
	
	public static void writeData(String randomServer, String roomNumber){
		synchronized (testData) {
			testData.get(randomServer).add(roomNumber);		
		}
	}
}


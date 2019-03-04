package cmd;

import java.util.ArrayList;
import java.util.Scanner;

public class CMD_IO {
	Scanner s;
	public CMD_IO(){
		s = new Scanner(System.in);
	}
	
	public String askQuestion(String question){
		System.out.println(question);		
	    if(s.hasNextLine()){	
		 return s.nextLine().trim();
	    }
	    return "";
	}
	
	public ArrayList<String> takeMultilineInput(String question){
		String line;
		System.out.println(question);
		ArrayList<String> answers = new ArrayList<String>();
		while(s.hasNextLine()){
			line = s.nextLine();
			
			if(line != null && line.length() == 0){
				break;
			}
			if(line.matches("[0-2]?[0-9]:[0-5][0-9] - [0-2]?[0-9]:[0-5][0-9]")){
			  answers.add(line);
			}else{
			  System.out.println("Should have to give slot in formate 13:02 - 14:09");	
			}
		}
		return answers;
	}
}

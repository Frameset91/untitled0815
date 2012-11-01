package test_utilities;

import utilities.Log;
import utilities.Log.LogEntry;

public class test_Log {
private Log lg;

public test_Log(){
	//ss
	lg = Log.getInstance();
	
	
	System.out.println("##Log starten");
	lg.enableLog();
	//#######################################
	for (int i = 0; i < lg.getLogEntries().size(); i++) {
		LogEntry le = lg.getLogEntries().get(i);
		System.out.println(le.getText());
		
	}
	
	System.out.println("##Eintrag verfassen");
	lg.write("erster Eintrag");
	//#######################################
	for (int i = 0; i < lg.getLogEntries().size(); i++) {
		LogEntry le = lg.getLogEntries().get(i);
		System.out.println(le.getText());
		
	}
	
	System.out.println("##Log ausschalten");
	lg.disableLog();
	//#######################################
	for (int i = 0; i < lg.getLogEntries().size(); i++) {
		LogEntry le = lg.getLogEntries().get(i);
		System.out.println(le.getText());
		
	}
	
	System.out.println("##Eintrag verfassen");
	lg.write("erster Eintrag");
	//#######################################
	for (int i = 0; i < lg.getLogEntries().size(); i++) {
		LogEntry le = lg.getLogEntries().get(i);
		System.out.println(le.getText());
		
	}
	
	
	
	
	
	
	
	
	
	
}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		test_Log tl = new test_Log();
	}

}

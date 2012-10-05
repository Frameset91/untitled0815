package utilities;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 
 * @author Alexander Busch
 * 
 */

public class Log {

	private ObservableList<String> logEntries;
	
	private static Log instance = null;
	
	private Log() {
		logEntries = FXCollections.observableArrayList();
	}
	
	/**
	 * @param args
	 */
	public static Log getInstance() {
		// Wenn noch kein Objekt besteht, Objekt erzeugen

		if (instance == null) {
			instance = new Log();
			
		}

		// Objekt zurückliefern
		return instance;

	}
	
	public void write (String text) {
		Date timestamp = new Date(System.currentTimeMillis());
		SimpleDateFormat ausgabe = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:S"); 
        String zeitstempel = ausgabe.format(timestamp);
		
		String nachricht = zeitstempel + " - " + text;
		//System.out.println(nachricht);
		logEntries.add(nachricht);
		
	}

	public ObservableList<String> getLogEntries() {
		return logEntries;
	}

	public void setLogEntries(ObservableList<String> logEntries) {
		this.logEntries = logEntries;
	}
	
	

}
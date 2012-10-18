package utilities;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 
 * @author Alexander Busch
 * 
 */

public class Log {
	
	public class LogEntry{
		private SimpleStringProperty text;
		
		public LogEntry(String text){
			this.text = new SimpleStringProperty(text);
		}

		/**
		 * @return the text
		 */
		public String getText() {
			return text.get();
		}

		/**
		 * @param text the text to set
		 */
		public void setText(String text) {
			this.text.set(text);
		}
		
		
	}

	private ObservableList<LogEntry> logEntries;
	
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
	
	public synchronized void write (String text) {
		Date timestamp = new Date(System.currentTimeMillis());
		SimpleDateFormat ausgabe = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss:S"); 
        String zeitstempel = ausgabe.format(timestamp);
		
		LogEntry nachricht = new LogEntry(zeitstempel + " - " + text);
		//System.out.println(nachricht);
		logEntries.add(nachricht);
		
	}

	public ObservableList<LogEntry> getLogEntries() {
		return logEntries;
	}

	public void setLogEntries(ObservableList<LogEntry> logEntries) {
		this.logEntries = logEntries;
	}
	
	

}
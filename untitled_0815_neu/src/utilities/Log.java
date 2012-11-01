package utilities;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Hilfsklasse für Entwickler zur Ausgabe der Programmaktionen
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
	private Boolean logEnabled;
	
	private Log() {
		logEntries = FXCollections.observableArrayList();
		logEnabled = true;
	}
	
	public static Log getInstance() {
		// Wenn noch kein Objekt besteht, Objekt erzeugen

		if (instance == null) {
			instance = new Log();
			
		}

		// Objekt zurückliefern
		return instance;

	}
	/**
	 * 
	 * @param text Text welcher im Log angezeigt wird.
	 */
	public synchronized void write (String text) {
		if(logEnabled){			
			Date timestamp = new Date(System.currentTimeMillis());
			SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss:S"); 
	        String timestampString = output.format(timestamp);
			
			LogEntry message = new LogEntry(timestampString + " - " + text);
			//Muss im ApplicationThread laufen, weil durch Binding mit UI verbunden 
			LogWorker worker = new LogWorker(message);
			Platform.runLater(worker);			
		}
	}
	
	public void enableLog(){
		logEnabled = true;
		write("Log eingeschaltet");
	}
	
	public void disableLog(){
		write("Log ausgeschaltet");
		logEnabled = false;		
	}

	public ObservableList<LogEntry> getLogEntries() {
		return logEntries;
	}

	public void setLogEntries(ObservableList<LogEntry> logEntries) {
		this.logEntries = logEntries;
	}
	
	private class LogWorker implements Runnable{
		private LogEntry msg;
		
		public LogWorker(LogEntry msg){
			this.msg = msg;
		}
		
		@Override
		public void run() {
			logEntries.add(msg);			
		}
		
	}
	
	

}
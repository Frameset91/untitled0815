package utilities;
/**
 * Diese Klasse fungiert zur Kommunikation mit dem Server und der Benachrichtigung der KI.
 * @author Bjoern List
 *
 */
import java.io.*;
import javafx.concurrent.Task;

import utilities.*;

public class CommunicationServer extends Thread {
	// Singleton Referenz
	private static CommunicationServer singleton = null;
	private String serverfilepath;
	private String agentfilepath;
	private long lastchange;
	private int timeout;
	private File serverFile;
	private File agentFile;
	private Thread bla;
//	private List<GameEventListener_old> _listeners = new ArrayList<GameEventListener_old>();
//
//	
//	
//	/**
//	 * Listener für Events hinzufügen
//	 * @param listener
//	 */
//	public synchronized void addEventListener(GameEventListener_old listener) {
//		_listeners.add(listener);
//	}
//
//	/**
//	 * Listener für Events löschen
//	 * @param listener 
//	 */
//	
//	public synchronized void removeEventListener(GameEventListener_old listener) {
//		_listeners.remove(listener);
//	}

	/**
	 * call this method whenever you want to notify
	 * the event listeners of the particular event
	 * @param type Typ des Events
	 */
	
	@SuppressWarnings("rawtypes")
	private synchronized void fireEvent(byte type) {
//		GameEvent event = new GameEvent(this, type);
//		Iterator i = _listeners.iterator();
//		while (i.hasNext()) {
//			((GameEventListener_old) i.next()).handleEvent(event);
//		}
				
		Task aufgabe = new Task<Void>() {
			protected Void call() throws Exception {
				EventDispatcher Dispatcher = EventDispatcher.getInstance();
				GameEvent e = (GameEvent)Dispatcher.triggerEvent("GameEvent", true);
				return null;
			}};
    	new Thread(aufgabe).start();	
		
		
		
	}

	public void fireGameEvent(final GameEvent.Type type, final String arg){
		Log.getInstance().write("GameEvent gefeuert: " + type.toString());
//		GameEvent event = new GameEvent(type.toString(),type, arg);
//		try {
//			EventDispatcher.getInstance().triggerEvent(event, true);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Task aufgabe = new Task<Void>() {
			protected Void call() throws Exception {
				GameEvent event = new GameEvent(type.toString(),type, arg);
				GameEvent Dispatcher = EventDispatcher.getInstance().triggerEvent(event, true);				
				return null;
			}};
    	new Thread(aufgabe).start();	
		
		
	}

	/**
	 * Methode - getInstance liefert die Referenz auf den Singleton zurück
	 * 
	 * @return CommunicationServer Instanz des Communication Servers
	 */
	public static CommunicationServer getInstance() {
		// Wenn noch kein Objekt besteht, Objekt erzeugen

		if (singleton == null) {
			singleton = new CommunicationServer();
		}

		// Objekt zurückliefern
		return singleton;

	}

	/**
	 * privater Konstruktor Erzeugung der Singletoninstanz
	 */
	private CommunicationServer() {

	}

	public int getTimeout() {
		return timeout;
	}

	/**
	 * Startet die Abfrage der Serverdatei in einem neuen Thread
	 */
	public void enableReading(int timeout, String serverFilePath, char role) {
		this.timeout = timeout;

		this.serverfilepath = serverFilePath + "/server2spieler" + role + ".xml";
		this.serverfilepath = this.serverfilepath.toLowerCase();
		this.serverFile = new File(serverfilepath);
		this.bla = new Thread(new ReadServerFileThread());
		this.bla.start();

	}
	
	public void resetLastChange() {
		this.lastchange = (Long) null;
	}

	/**
	 * Beendet die Abfrage der Serverdatei
	 */
	public void disableReading() {
		this.bla.interrupt();
//		this.bla.stop();
	}

	/**
	 * Ueberwachung der Serverdatei
	 * Meldung an alle Event Listener auslösen
	 */
	public void ueberwachen() {
		// Auslesen der Datei
		System.out.println("Ueberwachen startet");
		// ExampleListener blub = new ExampleListener();
		// this.addEventListener(blub);
		//while (true) {
			try{
			ServerMessage msg = this.read();
			
			System.out.println(msg.getFreigabe());
			System.out.println(msg.getSatzstatus());

			// Wenn Freigabe erfolgt ist - Set Objekt benachrichtigen
			if (msg.getFreigabe().equals("true")) {
				this.fireGameEvent(GameEvent.Type.OppMove, String.valueOf(msg.getGegnerzug()));
				
//				this.disableReading();
//				break;
				
			}
			// Satz ist beendet
			if (msg.getSatzstatus().equals("beendet")) {
				this.fireGameEvent(GameEvent.Type.EndSet, String.valueOf(msg.getGegnerzug()));
				
//				this.disableReading();
//				break;
			}
			// Sieger ist bestimmt
			if (!msg.getSieger().equals("offen")) {
				this.fireGameEvent(GameEvent.Type.OppMove, String.valueOf(msg.getGegnerzug()));
				
//				break;
			}
			lastchange = serverFile.lastModified();

			} catch (Exception e){
				System.out.println("Lesefehler.....");
			}
			
			
			
					//}
		System.out.println("Ende While schleife");

	}

	/**
	 * Lesen des Serverfiles
	 */

	public ServerMessage read() throws Exception{
		// Serverfile auslesen
		ServerMessage msg = null;
		while (msg == null) {
			msg = XmlParser.readXML(serverFile);
			if(this.lastchange == serverFile.lastModified()){
				msg = null;
			}
			if (msg == null){
			Thread.sleep(this.timeout);
			}
		}
		
		return msg;
	}

	/**
	 * Schreiben des Spielzuges auf den Server
	 * 
	 * @param spalte
	 *            Nummer der Spalte, in die der naechste STein gelgt wird
	 */
	public synchronized void writeMove(byte spalte, String agentFilePath, char role) {
		if (spalte > -1 && spalte < 7) {
			try {
				Log.getInstance().write("Zug schreiben im Pfad " + agentFilePath + "in Spalte " + spalte );
				this.agentfilepath = agentFilePath + "/spieler" + role + "2server.txt";
				this. agentfilepath = this.agentfilepath.toLowerCase();
				this.agentFile = new File(agentfilepath);
				FileWriter schreiber = new FileWriter(this.agentFile);
				schreiber.write(Integer.toString(spalte));
				schreiber.flush();
				schreiber.close();
				Log.getInstance().write("Schreiben erfolgreich");

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Fehler - falsche Spalte ausgewaehlt");
		}

	}

	/**
	 * Methode zum Test des CommunicationServers
	 * 
	 * Erläuterung: CommunicationServer wird durch enable Methode gestartet.
	 * Übergabeparameter bestimmt die Zeit zwischen 2 Prüfungen des Serverfiles
	 * --> Abfrage wird in einem neuem Thread gestartet, sendet ein Event bei
	 * Ereignis und beendet sich
	 * 
	 * Thread kann von außen durch disable Methode gestoppt werden
	 * 
	 * @param args
	 */
	
//	public static void main(String[] args) {
//		//
//		CommunicationServer.getInstance().enableReading(300, "server.xml");
//		try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		System.out
//				.println("----------------THREAD schließen!-------------------");
//		CommunicationServer.getInstance().writeMove((byte) 2, "spieler.txt");
//		// CommunicationServer.getInstance().disable();
//	
	}


// ###########################################################################################
// ###########################################################################################
// ###########################################################################################
// ###########################################################################################
// ###########################################################################################
// ###########################################################################################

/**
 * Threadklasse zur Überwachung des Serverfiles
 * 
 * @author Bjoern List
 * 
 */

// TODO ggf. in die CommunicationServer Klasse auslagern

class ReadServerFileThread extends Thread {
	@Override
	public void run() {
		System.out.println("Thread startet");
		Log.getInstance().write("Ueberwachung gestartet");
		CommunicationServer.getInstance().ueberwachen();
		Log.getInstance().write("Event gefeuert -- While Schleife verlassen");
		this.interrupt();
	}

}
/**
 * Diese Klasse fungiert zur Kommunikation mit dem Server und der Benachrichtigung der KI.
 * @author Bjoern List
 *
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import utilities.*;

public class CommunicationServer extends Thread {
	// Singleton Referenz
	private static CommunicationServer singleton = null;
	private String serverfilepath;
	private String agentfilepath;
	private int timeout;
	private File serverFile;
	private File agentFile;
	private Thread bla;
//	private List<GameEventListener_old> _listeners = new ArrayList<GameEventListener_old>();
//
//	
//	
//	/**
//	 * Listener f¸r Events hinzuf¸gen
//	 * @param listener
//	 */
//	public synchronized void addEventListener(GameEventListener_old listener) {
//		_listeners.add(listener);
//	}
//
//	/**
//	 * Listener f¸r Events lˆschen
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
		EventDispatcher Dispatcher = EventDispatcher.getInstance();
		try {
			GameEvent e = (GameEvent)Dispatcher.triggerEvent("GameEvent", true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	/**
	 * Methode - getInstance liefert die Referenz auf den Singleton zur¸ck
	 * 
	 * @return CommunicationServer Instanz des Communication Servers
	 */
	public static CommunicationServer getInstance() {
		// Wenn noch kein Objekt besteht, Objekt erzeugen

		if (singleton == null) {
			singleton = new CommunicationServer();
		}

		// Objekt zur¸ckliefern
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
	public void enableReading(int timeout, String serverFilePath) {
		this.timeout = timeout;

		this.serverfilepath = serverFilePath;

		this.serverFile = new File(this.serverfilepath);

		this.bla = new Thread(new ReadServerFileThread());
		this.bla.start();

	}

	/**
	 * Beendet die Abfrage der Serverdatei
	 */
	@SuppressWarnings("deprecation")
	public void disableReading() {
		this.bla.stop();
	}

	/**
	 * Ueberwachung der Serverdatei
	 * Meldung an alle Event Listener auslˆsen
	 */
	public void ueberwachen() {
		// Auslesen der Datei
		System.out.println("Ueberwachen startet");
		// ExampleListener blub = new ExampleListener();
		// this.addEventListener(blub);
		while (true) {

			ServerMessage msg = this.read();
			System.out.println(msg.getFreigabe());
			System.out.println(msg.getSatzstatus());

			// Wenn Freigabe erfolgt ist - Set Objekt benachrichtigen
			if (msg.getFreigabe().equals("true")) {
				this.fireEvent((byte) 0);
				break;
			}
			// Satz ist beendet
			if (msg.getSatzstatus().equals("beendet")) {
				this.fireEvent((byte) 1);
				break;
			}
			// Sieger ist bestimmt
			if (!msg.getSieger().equals("offen")) {
				this.fireEvent((byte) 2);
				break;
			}

			try {
				// Wartezeit zwischen 2 Zugriffen auf die Datei
				Thread.sleep(this.timeout);
			} catch (InterruptedException e) {

				e.printStackTrace();

			}
		}
		System.out.println("Ende While schleife");

	}

	/**
	 * Lesen des Serverfiles
	 */

	public ServerMessage read() {
		// Serverfile auslesen
		ServerMessage msg = null;
		while (msg == null) {
			msg = XmlParser.readXML(serverFile);
		}
		return msg;
	}

	/**
	 * Schreiben des Spielzuges auf den Server
	 * 
	 * @param spalte
	 *            Nummer der Spalte, in die der naechste STein gelgt wird
	 */
	public void writeMove(byte spalte, String agentFilePath) {
		if (spalte > -1 && spalte < 7) {
			try {
				this.agentfilepath = agentFilePath;
				this.agentFile = new File(this.agentfilepath);
				FileWriter schreiber = new FileWriter(this.agentFile);
				schreiber.write(Integer.toString(spalte));
				schreiber.flush();
				schreiber.close();

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
	 * Erl‰uterung: CommunicationServer wird durch enable Methode gestartet.
	 * ‹bergabeparameter bestimmt die Zeit zwischen 2 Pr¸fungen des Serverfiles
	 * --> Abfrage wird in einem neuem Thread gestartet, sendet ein Event bei
	 * Ereignis und beendet sich
	 * 
	 * Thread kann von auﬂen durch disable Methode gestoppt werden
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		//
		CommunicationServer.getInstance().enableReading(300, "server.xml");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out
				.println("----------------THREAD schlieﬂen!-------------------");
		CommunicationServer.getInstance().writeMove((byte) 2, "spieler.txt");
		// CommunicationServer.getInstance().disable();
	
	}

}

// ###########################################################################################
// ###########################################################################################
// ###########################################################################################
// ###########################################################################################
// ###########################################################################################
// ###########################################################################################

/**
 * Threadklasse zur ‹berwachung des Serverfiles
 * 
 * @author Bjoern List
 * 
 */

// TODO ggf. in die CommunicationServer Klasse auslagern

class ReadServerFileThread extends Thread {
	@Override
	public void run() {
		System.out.println("Thread startet");
		CommunicationServer.getInstance().ueberwachen();
	}

}
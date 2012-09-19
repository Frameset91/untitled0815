/**
 * Diese Klasse fungiert zur Kommunikation mit dem Server und der Benachrichtigung der KI.
 */

/**
 * @author Bjoern List
 *
 */
import java.io.*;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

class MyEvent extends EventObject {
	public MyEvent(Object source) {
		super(source);
	}
}

public class CommunicationServer {
	// Singleton Referenz
	private static CommunicationServer singleton = null;
	private String serverfilepath;
	private String spielerfilepath;
	private int timeout;
	private File spielerFile;
	private File serverFile;
	private Thread bla;

	/**
	 * Begin Eventsource Deklaration
	 */
	private List _listeners = new ArrayList();

	public synchronized void addEventListener(GameEventListener listener) {
		_listeners.add(listener);
	}

	public synchronized void removeEventListener(GameEventListener listener) {
		_listeners.remove(listener);
	}

	// call this method whenever you want to notify
	// the event listeners of the particular event
	private synchronized void fireEvent(int type) {
		GameEvent event = new GameEvent(this,type);
		Iterator i = _listeners.iterator();
		while (i.hasNext()) {
			((GameEventListener) i.next()).handleEvent(event);
		}
	}
	/**
	 * Ende Event Source Deklaration
	 */
	
	
	

	/**
	 * Methode zum Test des CommunicationServers
	 * 
	 * Erl�uterung: CommunicationServer wird durch enable Methode gestartet.
	 * �bergabeparameter bestimmt die Zeit zwischen 2 Pr�fungen des Serverfiles
	 * --> Abfrage wird in einem neuem Thread gestartet, sendet ein Event bei
	 * Ereignis und beendet sich
	 * 
	 * Thread kann von au�en durch disable Methode gestoppt werden
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
//		
		CommunicationServer.getInstance().enable(300);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out
				.println("----------------THREAD schlie�en!-------------------");
//		CommunicationServer.getInstance().write(2);
//		CommunicationServer.getInstance().disable();

	}

	/**
	 * privater Konstruktor Erzeugung der Singletoninstanz
	 */
	private CommunicationServer(String server, String spieler) {
		this.serverfilepath = server;
		this.spielerfilepath = spieler;
		

		spielerFile = new File(spielerfilepath);
		serverFile = new File(serverfilepath);

	}

	/**
	 * Methode - getInstance liefert die Referenz auf den Singleton zur�ck
	 * 
	 * @return CommunicationServer Instanz des Communication Servers
	 */
	public static CommunicationServer getInstance() {
		// Wenn noch kein Objekt besteht, Objekt erzeugen

		if (singleton == null) {
			singleton = new CommunicationServer("server.xml", "spieler.txt");
		}

		// Objekt zur�ckliefern
		return singleton;

	}

	public int getTimeout() {
		return timeout;
	}

	/**
	 * Startet die Abfrage der Serverdatei in einem neuen Thread
	 */
	public void enable(int timeout) {
		this.timeout = timeout;
		this.bla = new Thread(new ReadServerFile());
		this.bla.start();

	}

	/**
	 * Beendet die Abfrage der Serverdatei
	 */
	public void disable() {
		this.bla.stop();
	}

	/**
	 * Ueberwachung der Serverdatei
	 */
	public void ueberwachen() {
		// Auslesen der Datei
		System.out.println("Ueberwachen startet");
		ExampleListener blub = new ExampleListener();
		this.addEventListener(blub);
		while (true) {

			ServerMessage msg = this.read();
			 System.out.println(msg.getFreigabe());
			 System.out.println(msg.getSatzstatus());

			// Wenn Freigabe erfolgt ist - Set Objekt benachrichtigen
			if (msg.getFreigabe().equals("true")) {
				this.fireEvent(0);
				break;
			}
			// Satz ist beendet
			if (msg.getSatzstatus().equals("beendet")) {
				this.fireEvent(1);
				break;
			}
			// Sieger ist bestimmt
			if (!msg.getSieger().equals("offen")) {
				this.fireEvent(2);
				break;
			}

			try {
				Thread.sleep(this.timeout);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
	public void write(int spalte) {
		if (spalte > -1 && spalte < 7) {
			try {
				FileWriter schreiber = new FileWriter(this.spielerFile);
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

}

/**
 * Threadklasse zur �berwachung des Serverfiles
 * 
 * @author Bjoern List
 * 
 */

class ReadServerFile extends Thread {
	@Override
	public void run() {
		System.out.println("Thread startet");
		CommunicationServer.getInstance().ueberwachen();
	}

	public class MyEventListener implements MyEventClassListener {
		// ... code here

		// implement the required method(s) of the interface
		public void handleMyEventClassEvent(EventObject e) {
			// handle the event any way you see fit
		}
	}

}
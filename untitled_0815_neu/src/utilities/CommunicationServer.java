package utilities;

/**
 * Diese Klasse fungiert zur Kommunikation mit dem Server und der Benachrichtigung der KI.
 * @author Bjoern List
 *
 */
import java.io.*;

public class CommunicationServer {
	// Singleton Referenz
	private static CommunicationServer singleton = null;
	private String serverfilepath;
	private String agentfilepath;
	private long lastchange;
	private int timeout;
	private File serverFile;
	private File agentFile;
	private Thread leserthread;
	private boolean newFile = true;
	private char ownRole;
	
	/**
	 * Diese Methode löst die jeweiligen Events aus und startet deren
	 * Verarbeitung
	 * 
	 * @param type  Typ des GameEvents
	 * @param args  Argumente, die zusätzlich mit dem GameEvent übergeben werden
	 */

	public void fireGameEvent(final GameEvent.Type type, final String arg) {
		Log.getInstance().write("GameEvent gefeuert: " + type.toString());

		GameEvent event = new GameEvent(type, arg);
		try {
			EventDispatcher.getInstance().triggerEvent(event);
		} catch (Exception e) {
			Log.getInstance().write(
					"Fehler: Event konnte nicht geworfen werden!");
		}

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

	/**
	 * Liefert den eingestellten Timeoutwert zwischen 2 Zugriffen auf die
	 * Serverdatei
	 * 
	 * @return int timeout
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * Startet die Abfrage der Serverdatei in einem neuen Thread. Überprüft, ob
	 * die alte, bereits gelesene, Datei noch vorhanden ist und wartet bis diese
	 * gelöscht ist.
	 */
	public void enableReading(int timeout, String serverFilePath, char role) {
		this.timeout = timeout;
		this.serverfilepath = serverFilePath;
		this.ownRole = role;

		
		// Puefung, ob noch ein Leserthread läuft
		if (this.leserthread != null) {
			// alten Leserthread stoppen
			this.leserthread.interrupt();
			this.leserthread = null;
		} // if

		// neuen Thread starten
		this.leserthread = new Thread(new ReadServerFileThread());
		this.leserthread.start();

	}

	/**
	 * Diese Methode setzt die Variable lastchange zurück, damit eine neue Datei
	 * gelesen werden kann.
	 */

	public void resetLastChange() {
		this.lastchange = (Long) null;
	}

	/**
	 * Beendet die Abfrage der Serverdatei
	 */
	public void disableReading() {
		if (this.leserthread.isAlive())
		this.leserthread.interrupt();
	}

	/**
	 * Ueberwachung der Serverdatei Meldung an alle Event Listener auslösen
	 */
	public void ueberwachen() {
		if (!newFile) {

			// warten bis File gelöscht
			while (true) {
				File old = new File(serverfilepath + "/server2spieler" + ownRole
						+ ".xml");

				// prüfen ob File noch vorhanden und ob wirklich das alte File
				if (old.exists() && (lastchange == old.lastModified())) {

					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
					}
				} else {
					newFile = true;
					break;

				} // if

			} // while

		} // if

		// Umwandlung von backslashes im Pfad in normale Slashes
		if (serverfilepath.contains("\\")) {
			serverfilepath = serverfilepath.replace("\\", "/");
		}

		// vollstaendige Pfade mit Dateinamen bauen
		this.serverfilepath = serverfilepath + "/server2spieler" + ownRole
				+ ".xml";
		this.serverfilepath = this.serverfilepath.toLowerCase();
		this.serverFile = new File(serverfilepath);

		
		// Auslesen der Datei
		Log.getInstance().write("Communication Server:Ueberwachen startet");
		try {
			ServerMessage msg = this.read();
			newFile = false;
			// Wenn Freigabe erfolgt ist - Benachritigung, dass nächster Zug
			// gemacht werden muss
			if (msg.getFreigabe().equals("true")) {
				this.fireGameEvent(GameEvent.Type.OppMove,
						String.valueOf(msg.getGegnerzug()));
				Log.getInstance().write(
						"Communication Server: Event OppMove gesendet");
			}
			// Sieger ist bestimmt
			if (!msg.getSieger().equals("offen")) {
				char Winner = msg.getSieger()
						.substring(msg.getSieger().indexOf(" ") + 1).charAt(0);

				this.fireGameEvent(GameEvent.Type.WinnerSet,
						String.valueOf(Winner));
				Log.getInstance().write(
						"Communication Server: WinnerSet Event gesendet "
								+ Winner);
			}
			// Satz ist beendet
			if (msg.getSatzstatus().equals("beendet")) {
				this.fireGameEvent(GameEvent.Type.EndSet,
						String.valueOf(msg.getGegnerzug()));
				Log.getInstance().write(
						"Communication Server: Event EndSet gesendet");
			}

			lastchange = serverFile.lastModified();

		} catch (Exception e) {
			Log.getInstance().write("Communication Server: Lesefehler.....");
		}

		Log.getInstance().write("Communication Server: Ende Überwachung");

	}

	/**
	 * Lesen des Serverfiles
	 */

	public ServerMessage read() throws Exception {
		// Serverfile auslesen
		ServerMessage msg = null;
		// while (msg == null) {
		while (!serverFile.exists()) {
			Thread.sleep(this.timeout);
		}
		msg = XmlParser.getInstance().readXML(serverFile);
		return msg;
	}

	/**
	 * Schreiben des Spielzuges auf den Server
	 * 
	 * @param spalte
	 *            Nummer der Spalte, in die der naechste STein gelgt wird
	 */
	public synchronized void writeMove(byte spalte, String agentFilePath,
			char role) {
		if ((spalte > -1 && spalte < 7) && (agentFilePath != null)) {
			try {
				Log.getInstance().write(
						"Zug schreiben im Pfad " + agentFilePath + "in Spalte "
								+ spalte);

				if (agentFilePath.contains("\\")) {
					agentFilePath = agentFilePath.replace("\\", "/");
				}

				this.agentfilepath = agentFilePath + "/spieler" + role
						+ "2server.txt";
				this.agentfilepath = this.agentfilepath.toLowerCase();
				this.agentFile = new File(agentfilepath);
				FileWriter schreiber = new FileWriter(this.agentFile);
				schreiber.write(Integer.toString(spalte));
				schreiber.flush();
				schreiber.close();
				Log.getInstance().write("Schreiben erfolgreich");

			} catch (Exception e) {
				// e.printStackTrace();
				Log.getInstance().write(
						"Fehler - Move konnte nicht geschrieben werden!");
			}
		} else {
			Log.getInstance()
					.write("Fehler - falsche Spalte ausgewaehlt oder Pfad nicht gesetzt");
		}

	}

}

/**
 * Threadklasse zur Überwachung des Serverfiles
 * 
 * @author Bjoern List
 * 
 */

class ReadServerFileThread extends Thread {
	@Override
	public void run() {
		Log.getInstance().write("Ueberwachung gestartet");
		CommunicationServer.getInstance().ueberwachen();
		this.interrupt();
	}

}
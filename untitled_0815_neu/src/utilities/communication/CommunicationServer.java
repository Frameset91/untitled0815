package utilities.communication;

/**
 * Diese Klasse fungiert zur Kommunikation mit dem Server und der Benachrichtigung der KI.
 * @author Bjoern List
 *
 */
import java.io.*;

import utilities.Log;
import utilities.events.*;

public class CommunicationServer {
	// Singleton Referenz
	private static CommunicationServer singleton = null;
	private String serverfilepath;
	private String agentfilepath;
	private long lastchange = 0;
	private int timeout;
	private File serverFile;
	private File agentFile;
	private Thread readerthread;
	private boolean newSet;
	private char ownRole;

	/**
	 * Diese Methode löst die jeweiligen Events aus und startet deren
	 * Verarbeitung
	 * 
	 * @param type
	 *            Typ des GameEvents
	 * @param arguments
	 *            Argumente, die zusätzlich mit dem GameEvent übergeben werden
	 */

	public void fireGameEvent(final GameEvent.Type type, final String arguments) {
		Log.getInstance().write("GameEvent gefeuert: " + type.toString());

		GameEvent event = new GameEvent(type, arguments);
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
	public synchronized void enableReading(int timeout, String serverFilePath,
			char role, boolean Set) {
		this.timeout = timeout;
		this.serverfilepath = serverFilePath;
		this.ownRole = role;
		this.newSet = Set;

		// Puefung, ob noch ein Leserthread läuft
		if (this.readerthread != null) {
			// alten Leserthread stoppen
			this.readerthread.interrupt();
			this.readerthread = null;
		} // if

		// neuen Thread starten
		this.readerthread = new Thread(new ReadServerFileThread());
		this.readerthread.setDaemon(true);
		this.readerthread.setName("CommunicationServer Thread");
		this.readerthread.start();
	}

	/**
	 * Beendet die Abfrage der Serverdatei
	 */
	public void disableReading() {
		if (this.readerthread.isAlive()) {
			this.readerthread.interrupt();
		}
		this.readerthread = null;

	}

	/**
	 * Diese Methode setzt die Variable lastchange zurück, damit eine neue Datei
	 * gelesen werden kann.
	 */

	public void resetLastChange() {
		this.lastchange = (Long) null;
	}

	/**
	 * Ueberwachung der Serverdatei Meldung an alle Event Listener auslösen
	 */
	public void observe() {

		try {
			File old = new File(serverfilepath + "/server2spieler" + ownRole
					+ ".xml");
			// neuer Satz
			if (this.newSet) {
				if (old.exists()) {
					ServerMessage msg = XMLParser.getInstance().readXML(old);
					if (msg.getSetstatus().equals("beendet")) {
						old.delete();
					}
				}
			} else {
				while (true) {
					if (this.readerthread.isInterrupted()) {
						break;
					}

					if (old.exists() && (lastchange == old.lastModified())) {
						Thread.sleep(300);
					} else {
						break;
					}
				}

			}

		} catch (Exception e) {
		}

		// Umwandlung von backslashes im Pfad in normale Slashes
		if (serverfilepath.contains("\\")) {
			serverfilepath = serverfilepath.replace("\\", "/");
		}

		// Slash am Ende entfernen, falls vorhanden
		if (serverfilepath.lastIndexOf("/") == serverfilepath.length() - 1) {
			serverfilepath = serverfilepath.substring(0,
					serverfilepath.length() - 1);
		}

		// vollstaendige Pfade mit Dateinamen bauen
		this.serverfilepath = serverfilepath + "/server2spieler" + ownRole
				+ ".xml";
		this.serverfilepath = this.serverfilepath.toLowerCase();
		this.serverFile = new File(serverfilepath);

		// Auslesen der Datei
		Log.getInstance().write("Communication Server:Ueberwachen startet");
		ServerMessage msg;
		try {
			do {
				msg = this.read();
			} while (msg == null);

			if (msg != null) {
				// Auswerten des ServerFiles und werfen der entsprehenden Events
				if (msg.getRelease().equals("true")) {
					this.fireGameEvent(GameEvent.Type.OppMove,
							String.valueOf(msg.getOppmove()));
					Log.getInstance().write(
							"Communication Server: Event OppMove gesendet");
				}
				// Sieger ist bestimmt
				if (!msg.getWinner().equals("offen")) {
					char Winner = msg.getWinner()
							.substring(msg.getWinner().indexOf(" ") + 1)
							.charAt(0);

					this.fireGameEvent(GameEvent.Type.WinnerSet,
							String.valueOf(Winner));
					Log.getInstance().write(
							"Communication Server: WinnerSet Event gesendet "
									+ Winner);
				}
				// Satz ist beendet
				if (msg.getSetstatus().equals("beendet")) {
					this.fireGameEvent(GameEvent.Type.EndSet,
							String.valueOf(msg.getOppmove()));
					Log.getInstance().write(
							"Communication Server: Event EndSet gesendet");
				}

				lastchange = serverFile.lastModified();
			}
		} catch (Exception e) {
			Log.getInstance().write("Communication Server: Lesefehler.....");
			e.printStackTrace();
		}

		Log.getInstance().write("Communication Server: Ende Überwachung");

	}

	/**
	 * Lesen des Serverfiles
	 */

	public ServerMessage read() {
		// Serverfile auslesen
		ServerMessage msg = null;
		// while (msg == null) {
		while (!serverFile.exists()) {
			if (this.readerthread.isInterrupted()) {
				return msg;
			}
			try {
				Thread.sleep(this.timeout);
			} catch (Exception e) {
			}
		}
		try {
			msg = XMLParser.getInstance().readXML(serverFile);
		} catch (Exception e) {
			return null;
		}
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
				// Backslash in slash umwandeln
				if (agentFilePath.contains("\\")) {
					agentFilePath = agentFilePath.replace("\\", "/");
				}

				// Slash am Ende entfernen, falls vorhanden
				if (agentFilePath.lastIndexOf("/") == agentFilePath.length() - 1) {
					agentFilePath = agentFilePath.substring(0,
							agentFilePath.length() - 1);
				}

				this.agentfilepath = agentFilePath + "/spieler" + role
						+ "2server.txt";

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
		CommunicationServer.getInstance().observe();
		this.interrupt();
	}

}
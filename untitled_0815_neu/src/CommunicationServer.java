/**
 * Diese Klasse fungiert zur Kommunikation mit dem Server und der Benachrichtigung der KI.
 */

/**
 * @author Bjoern List
 *
 */
import java.io.*;

public class CommunicationServer {
	// Singleton Referenz
	private static CommunicationServer singleton = null;
	private String serverfilepath;
	private String spielerfilepath;
	private File spielerFile;
	private File serverFile;
	private boolean working;

	public static void main(String[] args) {
		CommunicationServer referenz;
		referenz = CommunicationServer.getInstance();
		referenz.read();
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
	 * Methode - getInstance liefert die Referenz auf den Singleton zurück
	 * 
	 * @return CommunicationServer Instanz des Communication Servers
	 */
	public static CommunicationServer getInstance() {
		// Wenn noch kein Objekt besteht, Objekt erzeugen
		if (singleton == null) {
			singleton = new CommunicationServer(
					"C:/Users/D055345/wiprojekt/WIProjektTestLokal/server.xml",
					"spieler.txt");
		}

		// Objekt zurückliefern
		return singleton;

	}

	/**
	 * Startet die Abfrage der Serverdatei
	 */
	public void enable() {
		this.working = true;
	}

	/**
	 * Beendet die Abfrage der Serverdatei
	 */
	public void disable() {
		this.working = false;

	}

	/**
	 * Ueberwachung der Serverdatei
	 */
	public void ueberwachen() {
		// Auslesen der Datei
		ServerMessage msg = this.read();
		// Wenn Freigabe erfolgt ist - Set Objekt benachrichtigen
		if (msg.getFreigabe().equals("true")) {
			// Event senden
			// Gegenerzug auslesen
			// msg.getGegnerzug();
		}
		//Satz ist beendet
		if(msg.getSatzstatus().equals("beendet")){
			//Event senden
		}
		// Sieger ist bestimmt
		if (!msg.getSieger().equals("offen")){
			
		}
		
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
		try {
			FileWriter schreiber = new FileWriter(this.spielerFile);
			schreiber.write(Integer.toString(spalte));
			schreiber.flush();
			schreiber.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

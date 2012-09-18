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
	private int timeout = 300;
	private File spielerFile;
	private File serverFile;
	private boolean working;


	public static void main(String[] args) {
	CommunicationServer.getInstance().enable(300);
	try {
		Thread.sleep(500);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("----------------THREAD schließen!-------------------");
//	CommunicationServer.getInstance().disable();
	
	}

	/**
	 * privater Konstruktor Erzeugung der Singletoninstanz
	 */
	private CommunicationServer(String server, String spieler) {
		this.serverfilepath = server;
		this.spielerfilepath = spieler;
		this.timeout = timeout;

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
					"server.xml",
					"spieler.txt");
		}
		

		// Objekt zurückliefern
		return singleton;

	}

	public int getTimeout() {
		return timeout;
	}

	/**
	 * Startet die Abfrage der Serverdatei
	 */
	public void enable(int timeout) {
		this.timeout = timeout;
		Thread bla = new Thread(new ReadServerFile() );
		bla.start();
		
	}

	/**
	 * Beendet die Abfrage der Serverdatei
	 */
	public void disable() {
		this.working =false;
	}

	/**
	 * Ueberwachung der Serverdatei
	 */
	public void ueberwachen() {
		// Auslesen der Datei
		System.out.println("Ueberwachen startet");
		while (this.working = true){
			
		ServerMessage msg = this.read();
		System.out.println(msg.getFreigabe());
		System.out.println(msg.getSatzstatus());
		
		
		
		// Wenn Freigabe erfolgt ist - Set Objekt benachrichtigen
		if (msg.getFreigabe().equals("true")) {
			// Event senden
			// Gegenerzug auslesen
			// msg.getGegnerzug();
			break;
		}
		//Satz ist beendet
		if(msg.getSatzstatus().equals("beendet")){
			//Event senden
			break;
		}
		// Sieger ist bestimmt
		if (!msg.getSieger().equals("offen")){
			break;
		}
		
		try {
			Thread.sleep(this.timeout);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}}
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

class ReadServerFile extends Thread{
	@Override
	public void run(){
		System.out.println("Thread startet");
		CommunicationServer.getInstance().ueberwachen();
	}

	



}
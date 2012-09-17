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

public static void main(String[] args){
	CommunicationServer referenz;
	referenz = CommunicationServer.getInstance();
	referenz.read();
}

/**
 * privater Konstruktor
 * Erzeugung der Singletoninstanz
 */
	private CommunicationServer(String server, String spieler){
		this.serverfilepath = server;
		this.spielerfilepath = spieler;
		
		spielerFile = new File(spielerfilepath);
		serverFile = new File(serverfilepath);
		
	}
	
	

	

	
/**
 * Methode - getInstance
 * liefert die Referenz auf den Singleton zurück
 * @return CommunicationServer
 */
	public static CommunicationServer getInstance(){
//		Wenn noch kein Objekt besteht, Objekt erzeugen
		if (singleton == null){
			singleton = new CommunicationServer("server.xml","spieler.xml");
		} 
		
//		Objekt zurückliefern
		return singleton;
				
	}
	
	
	public void enable(){
		
	}
	
	public void disable (){
		
	}
	
	
	public void read(){
		//Serverfile auslesen
		ServerMessage msg = null;
		while (msg == null){
		msg = XmlParser.readXML(serverFile);
		}
		System.out.println(msg.getFreigabe());
		
		
	}
	
	
	
	
	
}

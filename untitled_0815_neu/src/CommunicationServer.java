/**
 * Diese Klasse fungiert zur Kommunikation mit dem Server und der Benachrichtigung der KI.
 */

/**
 * @author Bjoern List
 *
 */
public class CommunicationServer {
// Singleton Referenz	
private static CommunicationServer singleton = null;

	private CommunicationServer(){
		
	}
	
	
//	Singleton Referenz zur�ckgeben
	public static CommunicationServer getInstance(){
//		Wenn noch kein Objekt besteht, Objekt erzeugen
		if (singleton == null){
			singleton = new CommunicationServer();
		} 
		
//		Objekt zur�ckliefern
		return singleton;
		
		
		
	}
	
	
	
}

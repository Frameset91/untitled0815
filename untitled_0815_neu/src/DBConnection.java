
public class DBConnection {
private static DBConnection singleton = null;

/**
 * privater Konstruktor
 */
private DBConnection(){
	
}

/**
 * Liefert die refernz auf Singleton Instanz zurueck
 * @return Referenz auf die SIngletonInstanz
 */
public static DBConnection getInstance(){
	if (singleton == null){
		singleton = new DBConnection();
	}
	return singleton;
}
}

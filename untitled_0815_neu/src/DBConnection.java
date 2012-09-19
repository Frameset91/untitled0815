import java.sql.*;

public class DBConnection {
	private static DBConnection singleton = null;
	private Connection con;

	/**
	 * privater Konstruktor
	 */
	private DBConnection() {
		connect();

	}

	/**
	 * Liefert die refernz auf Singleton Instanz zurueck
	 * 
	 * @return Referenz auf die SIngletonInstanz
	 */
	public static DBConnection getInstance() {
		if (singleton == null) {
			singleton = new DBConnection();
		}
		return singleton;
	}

	/**
	 * Verbindet sich mit Der Datenbank
	 */
	private void connect() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			// Aufbauen der Verbindung zu der Datenbank
			this.con = DriverManager.getConnection(
					"jdbc:hsqldb:hsql://localhost/", "SA", "");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sendet ein SelectStatement an die Datenbank
	 * 
	 * @param sql
	 *            Query als String
	 * @return ResultSet sendet ein Resultset zurueck - Wenn kein Ergebnis ist
	 *         null
	 */
	public ResultSet sendSelectStatement(String sql) {
		ResultSet result = null;
		try {
			Statement stmnt = this.con.createStatement();
			result = stmnt.executeQuery(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}
	

	/**
	 * Sendet ein anderes Statement, das keine Rueckgabe erzeugt
	 * 
	 * @param sql
	 *            Query als String
	 */

	public void sendOtherStatement(String sql) {
		try {
			Statement stmnt = this.con.createStatement();
			boolean result = stmnt.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	

	/**
	 * Methode, die alle Moves eines Satzes auf der DB persistiert
	 * 
	 * Es fehlt eine Beschreibunf für alle Moves --> Ein Array müsste global definiert werden
	 * @param move
	 * @return
	 */
	public boolean saveAllMove(int gameID, int setID) {
		boolean result = true;
//		try {
//			Statement stmnt = this.con.createStatement();
//			result = stmnt
//					.execute("INSERT INTO move (gameID,setID,role,column,datetime) VALUES ("
//							+ gameID
//							+ ","
//							+ setID
//							+ ","
//							+ role
//							+ ","
//							+ column
//							+ "," + datetime + ")");
//		} catch (Exception e) {
//			result = false;
//			e.printStackTrace();
//		}
		return result;
	}
	
	public void loadAllMoves(int gameID, int setID) {
		
	}

	public void saveGame() {

	}

	public void loadGame() {

	}
	
	public void saveSet() {
		
	}
	
	public void loadSet(){
		
	}


	/**
	 * nur zu Testzwecken
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		DBConnection bla = getInstance();
		bla.sendOtherStatement("CREATE TABLE blubblub (spalte1 INTEGER)");
	}

}

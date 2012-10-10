package utilities;

/**
 * @author Henny
 * 
 */

import java.sql.*;

import model.Game;

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
	 * Verbindung zur Datenbank herstellen, con initialisieren
	 */
	private void connect() {
		try {
			//Treiber laden
			Class.forName("org.hsqldb.jdbcDriver");
			// Aufbauen der Verbindung zu der Datenbank
			con = DriverManager.getConnection(
					"jdbc:hsqldb:file:database/dbfiles/DB4gewinnt;shutdown=true", "SA", "");

		}catch ( ClassNotFoundException e ) {
		      System.err.println( "Treiberklasse nicht gefunden!" );
	    }catch (SQLException e) {
	    	System.err.println( "Datenbank nicht gefunden!" );
		}
	}// Ende connect ()


	/**
	 * Liefert die Refernz auf Singleton Instanz zurueck
	 * 
	 * @return SingletonInstanz
	 */
	public static DBConnection getInstance() {
		if (singleton == null) {
			singleton = new DBConnection();
		}
		return singleton;
	}

	
	/**
	 * Sendet ein SelectStatement an die Datenbank
	 * 
	 * @param sql Query als String
	 * @return ResultSet sendet ein Resultset zurueck - Wenn kein Ergebnis ist
	 *         null
	 */
	public synchronized ResultSet sendSelectStatement(String sql) {
		ResultSet rs = null;
		try {
			Statement stmnt = con.createStatement();
			rs = stmnt.executeQuery(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;

	}
	
	/**
	 * 
	 * @param Spiel des Typs Game
	 * @return Id (Primärschlüssel) auf der DB, Typ Integer
	 */
	public synchronized int saveGame(Game game) {
	 // über getter parameter holen
		int id = 0;
		
		// Daten zum Speichern von game holen
/////////////////// hier noch die get-Methoden nutzen!!!!!!!!!!!!!!!!!!!!!!
		char role = 'x';
		String oppName = "test";
		int ownPoints = 1;
		int oppPoints = 1;
		
		// SQL Statement bauen
		try{
			DBConnection.main(null);
			System.out.println ("main ausgeführt");
			Statement stmt = con.createStatement();
			String sql = "INSERT INTO game VALUES (DEFAULT, '" + role +"','"+ oppName + "', "+ ownPoints +", " + oppPoints +");";
			System.out.println ("SQL: "+sql);
			int i = stmt.executeUpdate(sql); // i ist row count
			System.out.println("row count:" + i);
			if (i != 1)
					System.out.println("Fehler beim Schreiben in DB");
			//id = Statement.RETURN_GENERATED_KEYS;
			
		}catch ( SQLException e ) {
			System.err.println( "SQL Statement fehlgeschlagen!" );
	    }
		
		return id;

	}
	
///////////////////////////// bis hier gekommen
//	/**
//	 * Sendet ein anderes Statement, das keine Rueckgabe erzeugt
//	 * 
//	 * @param sql
//	 *            Query als String
//	 */
//
//	public void sendOtherStatement(String sql) {
//		try {
//			Statement stmnt = this.con.createStatement();
//			boolean result = stmnt.execute(sql);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	
//	
//	
//
//	/**
//	 * Methode, die alle Moves eines Satzes auf der DB persistiert
//	 * 
//	 * Es fehlt eine Beschreibunf für alle Moves --> Ein Array müsste global definiert werden
//	 * @param move
//	 * @return
//	 */
//	public boolean saveAllMove(int gameID, int setID) {
//		boolean result = true;
////		try {
////			Statement stmnt = this.con.createStatement();
////			result = stmnt
////					.execute("INSERT INTO move (gameID,setID,role,column,datetime) VALUES ("
////							+ gameID
////							+ ","
////							+ setID
////							+ ","
////							+ role
////							+ ","
////							+ column
////							+ "," + datetime + ")");
////		} catch (Exception e) {
////			result = false;
////			e.printStackTrace();
////		}
//		return result;
//	}
//	
//	public void loadAllMoves(int gameID, int setID) {
//		
//	}
//

//	public boolean saveSet() {
//	
//}
// public boolean saveMove() {
//
//	}
//
//	public void loadGame() {
//
//	}
//	
//	public void loadSet(){
//		
//	}


	/**
	 * nur zu Testzwecken
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		DBConnection test = DBConnection.getInstance();
		ResultSet rs;
		String sql = "SELECT * FROM game";
		rs= test.sendSelectStatement(sql);
		// Ergebnisse bekommen
		try{
			while ( rs.next() )
		      {
				String gameID = rs.getString(1);
		        String role = rs.getString(2);
		        String oppName = rs.getString(3);
		        String ownPoints = rs.getString(4);
		        String oppPoints = rs.getString(5);
		        String resultset = gameID + ","+ role +  ","+ oppName +  ","+ ownPoints +',' +oppPoints;
		        System.out.println (resultset);
		      }
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}

}

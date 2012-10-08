package utilities;

/**
 * @author Henny
 * 
 */

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
	 * Verbindung zur Datenbank herstellen
	 */
	private void connect() {
		try {
			//Treiber laden
			Class.forName("org.hsqldb.jdbcDriver");
			// Aufbauen der Verbindung zu der Datenbank
			con = DriverManager.getConnection(
					"jdbc:hsqldb:file:database/dbfiles/DB4gewinnt", "SA", "");

		}catch ( ClassNotFoundException e ) {
		      System.err.println( "Treiberklasse nicht gefunden!" );
	    }catch (SQLException e) {
	    	System.err.println( "Datenbank nicht gefunden!" );
		}
	}// Ende connect ()

	/**
	 * Sendet ein SelectStatement an die Datenbank
	 * 
	 * @param sql
	 *            Query als String
	 * @return ResultSet sendet ein Resultset zurueck - Wenn kein Ergebnis ist
	 *         null
	 */
	public ResultSet sendSelectStatement(String sql) {
		ResultSet rs = null;
		try {
			Statement stmnt = con.createStatement();
			rs = stmnt.executeQuery(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;

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
//	public void saveGame() {
//
//	}
//
//	public void loadGame() {
//
//	}
//	
//	public void saveSet() {
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

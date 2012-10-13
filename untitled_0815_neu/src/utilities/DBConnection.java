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
	 * @return Id (Prim�rschl�ssel) auf der DB, Typ Integer
	 */
	public synchronized int saveGame(Game game) {
		
		int id = 0; // id ist die erzeugte PrimaryKey-ID
		
		// Daten zum Speichern von game holen
/////////////////// hier noch die get-Methoden nutzen!!!!!!!!!!!!!!!!!!!!!!
		char role = 'x';
		String oppName = "test";
		int ownPoints = 1;
		int oppPoints = 1;
		
		
		try{
			
			// SQL Statement bauen
			String sql = "INSERT INTO game VALUES (DEFAULT, '" + role +"','"+ oppName + "', "+ ownPoints +", " + oppPoints +");";
			System.out.println ("SQL: "+sql);
			PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			// absenden zur DB
			stmt.execute(); 
			
			// erzeugten PrimaryKey auslesen
			ResultSet rs = stmt.getGeneratedKeys();
			try{
				while ( rs.next() )
			      {
					int gameID = rs.getInt(1);
					id = gameID;
			        
			      }
			}catch (Exception e){
				e.printStackTrace();
			}
			//Anzahl ge�nderter Zeilen lesen
			int i = stmt.getUpdateCount();
			System.out.println("row count:" + i);
			
			if (i != 1)
					System.out.println("Fehler beim Schreiben in DB");
			
			
		}catch ( SQLException e ) {
			System.err.println( "SQL Statement fehlgeschlagen!" );
			e.printStackTrace();
	    }
		
		return id;
	} // ende saveGame (Game game)
	
	
	/**
	 * 
	 * @param Set, Satz der gespeichert werden soll
	 * @return boolean true, wenn erfolgreich gespeichert, false bei Fehler
	 */
	public synchronized boolean saveSet() {
	
		boolean success = false;
		
		
		return success;
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
//	 * Es fehlt eine Beschreibunf f�r alle Moves --> Ein Array m�sste global definiert werden
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

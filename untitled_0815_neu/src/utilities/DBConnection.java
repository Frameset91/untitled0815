package utilities;

/**
 * @author Henny
 * 
 */

import java.sql.*;
import model.Game;
import model.Move;
import model.Set;

/**
 * TO DO: 
 * getter-Methoden für's Speichern: saveMove
 * alle load-Methoden
 *
 */

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
	 * @param sql, ein SQL Insert-Statement als String
	 * @return true bei erfolgreiche Einfügen, false bei Fehler
	 */
	public synchronized boolean sendInsertStatement(String sql) {
		boolean success = false;
		
		try{
			Statement stmt = con.createStatement();
			// Insert an DB schicken
			int i = stmt.executeUpdate(sql); //i ist row count
			if (i == 1)
				success = true;
	
		}catch (SQLException e){
			e.printStackTrace();
			System.out.println ("SQL Insert Set fehlgeschlagen");
		}
		return success;
	}
	
	/**
	 * 
	 * @param Spiel des Typs Game
	 * @return Id (Primärschlüssel) auf der DB, Typ Integer
	 */
	public synchronized int saveGame(Game game) {
		
		int id = 0; // id ist die erzeugte PrimaryKey-ID
		
		// Daten zum Speichern von game holen
		char role = game.getRole();
		String oppName = game.getOppName();
		int ownPoints = game.getOwnPoints();
		int oppPoints = game.getOppPoints();
		
		
		try{
			
			// SQL Statement bauen
			String sql = "INSERT INTO game VALUES (DEFAULT, '" + role +"','"+ oppName + "', "+ ownPoints +", " + oppPoints +");";
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
			//Anzahl geänderter Zeilen lesen
			int i = stmt.getUpdateCount();
			
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
	public synchronized boolean saveSet(Set set, int gameID) {
	
		boolean success = false;
		
		// Daten zum Speichern von game holen
		String setID = set.getID();
		char winner = set.getWinner();
		Timestamp starttime = set.getStarttime();
		Timestamp endtime = set.getEndtime(); 
		
		try {
			//SQL Statement bauen
			String sql = "INSERT INTO gameSet VALUES ("+ gameID + ", " + setID +",'"+ winner + "', '"+ starttime +"', '" + endtime +"');";
			System.out.println ("SQL: "+sql);
			Statement stmt = con.createStatement();
			// Insert an DB schicken
			int i = stmt.executeUpdate(sql); //i ist row count
			if (i == 1)
				success = true;
			
		}catch (SQLException e){
			e.printStackTrace();
			System.out.println ("SQL Insert Set fehlgeschlagen");
		}
		
		return success;
	}
	
	/**
	 * 
	 * @param move, ein Zug der gespeichert werden soll
	 * @return true bei erfolgreichem Speichern, false bei Fehler
	 */
	public synchronized boolean saveMove(Move move, int gameID, int setID) {
		boolean success = false;
		
		// Daten zum Speichern von game holen
/////////////////// hier noch die get-Methoden nutzen!!!!!!!!!!!!!!!!!!!!!!
		int moveID = 2;
		char role = move.getRole();
		int column = move.getColumn();
		Timestamp time = move.getTime();
			
		try {
			//SQL Statement bauen
			String sql = "INSERT INTO move VALUES ("+ gameID + ", " + setID +","+ moveID + ", '"+ role +"', " + column + ", '" + time +"');";
			System.out.println ("SQL: "+sql);
			Statement stmt = con.createStatement();
			// Insert an DB schicken
			int i = stmt.executeUpdate(sql); //i ist row count
			if (i == 1)
				success = true;
	
		}catch (SQLException e){
			e.printStackTrace();
			System.out.println ("SQL Insert Set fehlgeschlagen");
		}
		
		return success;
	}
	
	
	
///////////////////////////// bis hier gekommen
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
//
//	public void loadGame() {
//
//	}
//	
//	public void loadSet(){
//		
//	}


}

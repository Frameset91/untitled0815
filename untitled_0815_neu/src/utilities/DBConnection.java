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
 * bei save set: testaufruf für endtime enthalten
 * alle load-Methoden
 *
 */

public class DBConnection {
	private static DBConnection singleton = null;
	private Connection con;
	private boolean offlineMode;

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
		
		if (con == null){
			offlineMode = true;
		}else offlineMode = false;
		
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
		if (!offlineMode){
			try {
				Statement stmnt = con.createStatement();
				rs = stmnt.executeQuery(sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		if (!offlineMode){
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
		}
		return success;
	}
	
	/**
	 * 
	 * @param Spiel des Typs Game
	 * @return Id (Primärschlüssel) auf der DB, Typ Integer
	 */
	public synchronized int saveGame(Game game) {
		
		int id = -1; // id ist die erzeugte PrimaryKey-ID
		
		if (!offlineMode){
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
		
		if (!offlineMode){
		
			// Daten zum Speichern von game holen
			int setID = set.getID();
			char winner = set.getWinner();
			Timestamp starttime = set.getStarttime();
			Timestamp endtime = set.getEndtime(); 
			//zum testen:
			if (endtime == null)
				endtime = starttime;
			
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
		}// if (!offlineMode)
		
		return success;
	}
	
	/**
	 * 
	 * @param move, ein Zug der gespeichert werden soll
	 * @return true bei erfolgreichem Speichern, false bei Fehler
	 */
	public synchronized boolean saveMove(Move move, int gameID, int setID) {
		boolean success = false;
		
		if (!offlineMode){
		
			// Daten zum Speichern von game holen
			int moveID = move.getID();
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
		}//if	
		return success;
	}
	
	
	
///////////////////////////// bis hier gekommen
//	
//	public boolean saveAllMove(int gameID, int setID) {
//	
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

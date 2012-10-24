package utilities;

/**
 * @author Henny
 * 
 */

import java.sql.*;

import core.Constants;
import model.Game;
import model.Move;
import model.Set;

/**
 * TO DO: 
 * bei save set: testaufruf für endtime enthalten
 * bei loadGame (ID): ownPoints und oppPoints setzen
 * load-Methoden:
 * 	set [] loadSets(gameID)
 * 	move [] loadMoves (gameID, setID)
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
	 * 
	 * Sendet Select an die DB, nicht synchronized
	 * @param sql: SQL-Select-Statement
	 * @return Resultset: alle ergebnisse von der DB
	 */
	private ResultSet sendSelectStatementInternal(String sql){
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
	 * Sendet ein SelectStatement an die Datenbank
	 * 
	 * @param sql Query als String
	 * @return ResultSet: sendet ein Resultset zurueck - Wenn kein Ergebnis ist
	 *         null
	 */
	public synchronized ResultSet sendSelectStatement(String sql) {
		ResultSet rs = null;
		rs = sendSelectStatementInternal(sql);
		return rs;
	}
	
	/**
	 * 
	 * übergibt ein INsert-Statement an die Datenbank
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
	 * speichert ein Spiel in der Datenbank
	 * @param Spiel des Typs Game
	 * @return Id (Primärschlüssel) auf der DB, Typ Integer
	 */
	public synchronized int saveGame(Game game) {
		
		int id = -1; // id ist die erzeugte PrimaryKey-ID, -1 Standard für offlineMode
		String sql;
		int i; // Anzahl geänderter Zeilen
		
		if (!offlineMode){
			
			// Daten zum Speichern von game holen
			int gameID = game.getID(); // falls vorhanden: Wert, falls nicht: -1
			char role = game.getRole();
			String oppName = game.getOppName();
			int ownPoints = game.getOwnPoints();
			int oppPoints = game.getOppPoints();
			String path = game.getPath();
			int timeServer = game.getTimeoutServer();
			int timeDraw = game.getTimeoutDraw();
			
			try{
				// überprüfen ob es sich um ein update oder insert handeln muss
				if (gameID == -1){ 
					//neu in die DB schreiben
					sql = "INSERT INTO game VALUES (DEFAULT, '" + role +"','"+ oppName + "', "
							+ ownPoints +", " + oppPoints + ", '" + path + "', " + timeServer + ", "
							+ timeDraw +");";
					PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					// absenden zur DB
					pstmt.execute(); 
					// erzeugten PrimaryKey auslesen
					ResultSet rs = pstmt.getGeneratedKeys();
					while ( rs.next() )
				      {
						id = rs.getInt(1);
				      }
					//Anzahl geänderter Zeilen lesen
					i = pstmt.getUpdateCount();
				}else {
					// falls update
					sql = "UPDATE game SET (role, oppname, ownPoints, oppPoints) = ('" + role + "', '" + oppName + "', '" 
							+ ownPoints + "', '"+ oppPoints + "') WHERE gameID = " + gameID + ";"; 
					System.out.println (sql);
					Statement stmt = con.createStatement();
					// update an DB schicken
					i = stmt.executeUpdate(sql); //i ist row count
					if (i != 1)
							System.out.println("Fehler beim Schreiben in DB");
				}// else, also update
				
			}catch ( SQLException e ) {
				System.err.println( "SQL Statement fehlgeschlagen!" );
				e.printStackTrace();
		    }
		}// if (!offlineMode)		
		return id;
	} // ende saveGame (Game game)
	
	
	/**
	 * 
	 * Speichert einen Satz
	 * @param Set: Satz der gespeichert werden soll
	 * @param gameID: ID des sazugehörigen Spiels
	 * @return boolean true, wenn erfolgreich gespeichert, false bei Fehler
	 */
	public synchronized boolean saveSet(Set set, int gameID) {
	
		boolean success = false;
		String sql;
		
		if (!offlineMode){	
			// Daten zum Speichern von game holen
			int setID = set.getID();
			char winner = set.getWinner();
			Timestamp starttime = set.getStarttime();
			Timestamp endtime = set.getEndtime(); 
			//zum testen:
			if (endtime == null)
				endtime = starttime;
			
			// überprüfen ob update oder insert
			String selectSql = "SELECT * FROM gameSet WHERE gameID = " + gameID + 
					" and setID = " + setID + ";"; 
			ResultSet rs = this.sendSelectStatementInternal(selectSql);
			try{
				if (!rs.next()){ //also keine Zeile im ResultSet vorhanden
					//falls neues Insert-Statement
					sql = "INSERT INTO gameSet VALUES ("+ gameID + ", " + setID +",'"+ winner + "', '"+ starttime +"', '" + endtime +"');";
					System.out.println ("SQL: "+sql);
				}else {
					// update machen
					sql = "UPDATE gameSet SET (winner, startTime, endTime) = ('" + winner + "', '" + starttime + "', '" 
							+ endtime + "') WHERE gameID = " + gameID + " AND setID = " + setID +  ";"; 
					System.out.println (sql);
				}
				Statement stmt = con.createStatement();
				// Insert an DB schicken
				int i = stmt.executeUpdate(sql); 
				if (i == 1) //i ist row count
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
	 * Speichert einen Move
	 * @param move: ein Zug der gespeichert werden soll
	 * @param gameID: ID des dazugeörigen Games
	 * @param setID: ID des dazugehörigen Sets
	 * @return boolean: true bei erfolgreichem Speichern, false bei Fehler
	 */
	public synchronized boolean saveMove(Move move, int gameID, int setID) {
		boolean success = false;
		String sql;
		
		if (!offlineMode){
		
			// Daten zum Speichern von game holen
			int moveID = move.getID();
			char role = move.getRole();
			int column = move.getColumn();
			Timestamp time = move.getTime();
			
			// überprüfen ob update oder insert
			String selectSql = "SELECT * FROM move WHERE gameID = " + gameID + 
					" and setID = " + setID + " and moveID = " + moveID + ";"; 
			ResultSet rs = this.sendSelectStatementInternal(selectSql);
			try{
				if (!rs.next()){ //also keine Zeile im ResultSet vorhanden
					//Insert:	
					sql = "INSERT INTO move VALUES ("+ gameID + ", " + setID +","+ moveID + ", '"+ role +"', " + column + ", '" + time +"');";
				}else{ 
					// update:
					sql = "UPDATE move SET (role, column, time) = ('" + role + "', " + column + ", '" 
							+ time + "') WHERE gameID = " + gameID + " AND setID = " + setID 
							+ " AND moveID = " + moveID + ";"; 
					System.out.println (sql);
				}
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
	
	/**
	 * 
	 * lädt ein game, entsprechend einer ID
	 * @param gameID: ID des zu ladenen Games
	 * @return Game: Objekt des gewünschten Spiels
	 */
	public synchronized Game loadGame(int gameID) {
		// Daten aus DB laden
		String srole = "";
        String soppName = "";
        String sownPoints = "";
        String soppPoints = "";
        String spath = "";
        String stimeServer = "";
        String stimeDraw = "";
		
		String sql = "SELECT * FROM game WHERE gameID = " + gameID + ";"; 
		ResultSet rs = this.sendSelectStatementInternal(sql);
		
		try{
			if ( rs.next() ){
				//Infos aus rs holen und auflisten
		        srole = rs.getString(2);
		        soppName = rs.getString(3);
		        sownPoints = rs.getString(4);
		        soppPoints = rs.getString(5);
		        spath = rs.getString(6);
		        stimeServer = rs.getString(7);
		        stimeDraw = rs.getString(8);
		        String resultset = gameID + ","+ srole +  ","+ soppName +  ","+ sownPoints +',' +soppPoints +  ","
		        		+ spath +  ","+ stimeServer + ", " + stimeDraw;
		        System.out.println (resultset);
		      }
		}catch (SQLException e){
			e.printStackTrace();
		}
		
		//in game übernehmen
		int timeServer;
		int timedraw;
		
		char role = srole.charAt(0);
		String oppname = soppName; 
		String path = spath;
		if (stimeServer != null){
			timeServer = Integer.valueOf(stimeServer);
		}else timeServer = 0;
		if (stimeDraw != null){
			timedraw = Integer.valueOf(stimeDraw);
		}else timedraw = 0;
		int columns = Constants.gamefieldcolcount;
		int rows = Constants.gamefieldrowcount;
		
////////////////// hier noch die Ounkte own und opp zuweisen --> funktion von Game benötigt!
		
		Game game = new Game(columns, rows, role, oppname, path, timeServer, timedraw, gameID);
		
		return game;
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
//	
//	public void loadSet(){
//		
//	}


}

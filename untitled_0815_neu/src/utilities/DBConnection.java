package utilities;

/**
 * @author Henny
 * 
 */

import java.sql.*;
import java.util.ArrayList;

import core.Constants;
import model.Game;
import model.Move;
import model.Set;

/**
 * TO DO: 
 * 
 * an 2 Stellen großer auskemmentierter Block: erst testen
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
		Log log = Log.getInstance();
		try {
			//Treiber laden
			Class.forName("org.hsqldb.jdbcDriver");
			// Aufbauen der Verbindung zu der Datenbank
			con = DriverManager.getConnection(
					"jdbc:hsqldb:file:database/dbfiles/DB4gewinnt;shutdown=true", "SA", "");

		}catch ( ClassNotFoundException e ) {
		      log.write("Treiberklasse nicht gefunden!");
	    }catch (SQLException e) {
	    	log.write("Datenbank nicht gefunden!");
		}
		
		if (con == null){
			offlineMode = true;
			log.write("DB ist offline");
		}else{
			offlineMode = false;
			log.write("DB ist online!");
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
	 * 
	 * liefert zurück, ob die DB erreichbar oder offline ist
	 * @return true, wenn DB offline ist, false, wenn DB erreichbar ist
	 */
	public boolean isOfflineMode(){
		return offlineMode;
	}// isOfflineMode()

	/**
	 * 
	 * Sendet Select an die DB, nicht synchronized
	 * @param sql SQL-Select-Statement
	 * @return Resultset alle ergebnisse von der DB
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
	}//sendSelectStatementInternal()
	
	/**
	 * Sendet ein SelectStatement an die Datenbank
	 * 
	 * @param sql Query als String
	 * @return ResultSet sendet ein Resultset zurueck - Wenn kein Ergebnis ist
	 *         null
	 */
	public synchronized ResultSet sendSelectStatement(String sql) {
		ResultSet rs = null;
		rs = sendSelectStatementInternal(sql);
		return rs;
	}//sendSelectStatement()
	
	/**
	 * 
	 * übergibt ein INsert-Statement an die Datenbank
	 * @param sql ein SQL Insert-Statement als String
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
				Log log = Log.getInstance();
				log.write("SQL Insert-Statement fehlgeschlagen!");
			}
		}
		return success;
	}//sendInsertStatement()
	
	/**
	 * 
	 * speichert ein Spiel in der Datenbank
	 * @param game Spiel des Typs Game
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
			String ownname = game.getOwnName();
			
			Log log = Log.getInstance();
			
			try{
				// überprüfen ob es sich um ein update oder insert handeln muss
				if (gameID == -1){ 
					//neu in die DB schreiben
					sql = "INSERT INTO game VALUES (DEFAULT, '" + role +"','"+ oppName + "', "
							+ ownPoints +", " + oppPoints + ", '" + path + "', " + timeServer + ", "
							+ timeDraw +", '"+ ownname +"');";
					PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					// absenden zur DB
					pstmt.execute(); 
					// erzeugten PrimaryKey auslesen
					ResultSet rs = pstmt.getGeneratedKeys();
					while ( rs.next() )
				      {
						id = rs.getInt(1);
				      }
					// Anzahl geänderter Zeilen lesen
					i = pstmt.getUpdateCount();
					// in Logs schreiben
				}else {
					// falls update
					sql = "UPDATE game SET (role, oppname, ownPoints, oppPoints) = ('" + role + "', '" + oppName + "', '" 
							+ ownPoints + "', '"+ oppPoints + "') WHERE gameID = " + gameID + ";"; 
					Statement stmt = con.createStatement();
					// update an DB schicken
					i = stmt.executeUpdate(sql); //i ist row count
					
					if (i != 1){
							log.write("Game wurde nicht gespeichert");
					}else{
						log.write("DB erflogreich: "+sql);	
					}
				}// else, also update
				
			}catch ( Exception e ) {
				log.write("Fehler in DB bei Save Game");
		    }
		}// if (!offlineMode)		
		return id;
	} // ende saveGame (Game game)
	
	
	/**
	 * 
	 * Speichert einen Satz
	 * @param Set Satz der gespeichert werden soll
	 * @param gameID ID des sazugehörigen Spiels
	 * @return boolean true, wenn erfolgreich gespeichert, false bei Fehler
	 */
	public synchronized boolean saveSet(Set set, int gameID) {
	
		boolean success = false;
		String sql;
		
		if (!offlineMode){	
			Log log = Log.getInstance();
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
				}else {
					// update machen
					sql = "UPDATE gameSet SET (winner, startTime, endTime) = ('" + winner + "', '" + starttime + "', '" 
							+ endtime + "') WHERE gameID = " + gameID + " AND setID = " + setID +  ";"; 
				}
				Statement stmt = con.createStatement();
				// Insert an DB schicken
				int i = stmt.executeUpdate(sql); 
				if (i == 1){ //i ist row count
					success = true;
					log.write("DB ausgeführt: " + sql);
				}
				
			}catch (Exception e){
				log.write("SQL INSERT set fehlfegschlagen");
			}
		}// if (!offlineMode)
		
		return success;
	}
	
	/**
	 * 
	 * Speichert einen Move
	 * @param move ein Zug der gespeichert werden soll
	 * @param gameID ID des dazugeörigen Games
	 * @param setID ID des dazugehörigen Sets
	 * @return boolean true bei erfolgreichem Speichern, false bei Fehler
	 */
	public synchronized boolean saveMove(Move move, int gameID, int setID) {
		boolean success = false;
		String sql;
		
		if (!offlineMode){
			Log log = Log.getInstance();
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
				}
				Statement stmt = con.createStatement();
				// Insert an DB schicken
				int i = stmt.executeUpdate(sql); //i ist row count
				if (i == 1){
					success = true;
					log.write("DB erfolgreich: "+sql);			
				}
			}catch (Exception e){
				log.write("SQL Insert Set fehlgeschlagen");
			}
		}//if	
		return success;
	}
	
	/**
	 * 
	 * lädt ein game, entsprechend einer ID
	 * @param gameID ID des zu ladenen Games
	 * @return Game Objekt des gewünschten Spiels
	 */
	public synchronized Game loadGame(int gameID) {
		if (!offlineMode){
			Log log = Log.getInstance();
			// Daten aus DB laden
			String srole = "";
	        String soppName = "";
	        @SuppressWarnings("unused")
			String sownPoints = ""; //wird nur zur Überprüfung gebraucht
	        @SuppressWarnings("unused")
			String soppPoints = ""; //wird nur zur Überprüfung gebraucht
	        String spath = "";
	        String stimeServer = "";
	        String stimeDraw = "";
	        String sownname = "";
			
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
			        sownname = rs.getString(9);
			        log.write("DB: 1Game geladen");
			      }else return null; // dann wäre nichts im resultset und nichts in der DB gefunden
			}catch (SQLException e){
				log.write("Fehler in DB beim Laden eines Spiels");
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
			String ownname = sownname;
			int columns = Constants.gamefieldcolcount;
			int rows = Constants.gamefieldrowcount;
			
			Game game = new Game(columns, rows, role, oppname, ownname, path, timeServer, timedraw, gameID);
			
			return game;
		}else
			return null;
	}
	
	/**
	 * 
	 * @return Game [] alle gespeicherten Games in Array der Größe entprechend der Anzahl
	 */
	public synchronized Game [] loadAllGames(){
		if (!offlineMode){
			Log log = Log.getInstance();
			ArrayList <Game> allGameList = new ArrayList <Game> (0);
			Game [] allGame;
			int ctr = 0;
			
			int id;
			int timeServer;
			int timedraw;
			
			String sql = "SELECT * FROM game ORDER BY gameID DESC;"; 
			ResultSet rs = this.sendSelectStatementInternal(sql);
			
			try{
				while ( rs.next() ){
					ctr++;
					//Infos aus rs holen und auflisten
					String sID = rs.getString(1);
			        String srole = rs.getString(2);
			        String soppName = rs.getString(3);
			        @SuppressWarnings("unused")
					String sownPoints = rs.getString(4); // wird nur zur Überprüfung im Debug-Mode gebracuht
			        @SuppressWarnings("unused")
					String soppPoints = rs.getString(5); // wird nur zur überprüfung im Debug-Mode gebraucht
			        String spath = rs.getString(6);
			        String stimeServer = rs.getString(7);
			        String stimeDraw = rs.getString(8);
			        String sownname = rs.getString(9);
			        
			        //in game übernehmen
			        if (sID != null)
			        	id = Integer.valueOf(sID);
			        else id = -1;
					char role = srole.charAt(0);
					String oppname = soppName; 
					String path = spath;
					if (stimeServer != null){
						timeServer = Integer.valueOf(stimeServer);
					}else timeServer = 0;
					if (stimeDraw != null){
						timedraw = Integer.valueOf(stimeDraw);
					}else timedraw = 0;
					String ownname = sownname;
					int columns = Constants.gamefieldcolcount;
					int rows = Constants.gamefieldrowcount;
			        
			        // Game an die Liste hängen
			        Game newGame = new Game (columns, rows, role, oppname, ownname, path, timeServer, timedraw, id);
			        allGameList.add(newGame);
				}//while
				log.write("Anzahl der geladenen Games: "+ ctr);
			}catch (Exception e){
				log.write("Lade der Games in DB fehlgeschlagen");
			}
			int noGames = allGameList.size();
			if (noGames == 0)
					return null; // dann sind keine Sets da
	        allGame = new Game [noGames];
	        for (int i = 0; i < noGames; i++) {
				allGame [i] = allGameList.get(i);
	        }
			return allGame;
		}else 
			return null;
	}// loadAllGames()
	
	/**
	 * 
	 * @param gameID valide ID eines Spiels
	 * @return Set [] aller sets zur GameID
	 */
	public synchronized Set [] loadAllSets(int gameID){
		if (!offlineMode){
			Log log = Log.getInstance();
			ArrayList <Set> allSetList = new ArrayList <Set> (0);
			Set [] allSet;
			int ctr = 0;
			
			String sql = "Select * FROM gameSet WHERE gameID = " + gameID + ";";
			ResultSet rs = this.sendSelectStatementInternal(sql);
			try{
				while ( rs.next() ){
					ctr++;
					//Infos aus rs holen und auflisten
			        String ssetID = rs.getString(2);
			        String swinner = rs.getString(3);
			        String sstarttime = rs.getString(4);
			        String sendtime = rs.getString(5);
			        
			        // in entsprechende Typen umwandeln:
			        int setID;
			        if (ssetID != null) 
			        	setID = Integer.valueOf(ssetID);
			        else setID = 0;
			        char winner = swinner.charAt(0);
			        Timestamp starttime = Timestamp.valueOf(sstarttime);
			        Timestamp endtime = Timestamp.valueOf(sendtime);
			        int col = Constants.gamefieldcolcount;
			        int row = Constants.gamefieldrowcount;
			        
			        //Objekt erzeugen:
			        Set newSet = new Set(col, row, setID, starttime, endtime, Constants.STATE_SET_ENDED, winner);
			        // an die Liste ranhängen
			        allSetList.add(newSet);
			      }	
				log.write("Anzahl der geladenen Sets:" + ctr);
			}catch (Exception e){
				log.write("Load all sets in der DB gehlgeschlagen");
			}
			int noSets = allSetList.size();
			if (noSets == 0)
					return null; // dann sind keine Sets da
	        allSet = new Set [noSets];
	        for (int i = 0; i < noSets; i++) {
				allSet [i] = allSetList.get(i);
	        }
			return allSet;
		}else
			return null;
	}// loadAllSets()
	
	/**
	 * 
	 * @param gameID ID eines Spiels
	 * @param setID ID eines Sets zu dem Spiel
	 * @return Move [] Array aller Moves zum entsprechenden Set
	 */
	public synchronized Move [] loadAllMoves(int gameID, int setID) {
		if (!offlineMode){
			Log log = Log.getInstance();
			ArrayList <Move> allMoveList = new ArrayList <Move> (0);
			Move [] allMove;
			int ctr = 0;
	
	        String sql = "Select * FROM move WHERE gameID = " + gameID + " AND setID = "+ setID +";";
			ResultSet rs = this.sendSelectStatementInternal(sql);
			try{
				while ( rs.next() ){
					//Infos aus rs holen und auflisten
			        String smoveID = rs.getString(3);
			        String srole = rs.getString(4);
			        String scolumn = rs.getString(5);
			        String stime = rs.getString(6);
			        ctr++;
			        
			        // in entsprechende Typen umwandeln:
			        int moveID;
			        int col;
			        if (smoveID != null) 
			        	moveID = Integer.valueOf(smoveID);
			        else moveID = 0;
			        if (scolumn != null)
			        	col = Integer.valueOf(scolumn);
			        else col = 0;
			        char role = srole.charAt(0);
			        Timestamp time = Timestamp.valueOf(stime);
			        
			        //Objekt erzeugen:
			        Move newMove = new Move(role, col, moveID, time);
			        // an die Liste ranhängen
			        allMoveList.add(newMove);
			      }//while	
				log.write("Anzahl geladener Moves: "+ ctr);
			}catch (Exception e){
				log.write("Move laden in der DB fehlgeschlagen");
			}
			int noMoves = allMoveList.size();
			if (noMoves == 0)
					return null; // dann sind keine Moves da
	        allMove = new Move [noMoves];
	        for (int i = 0; i < noMoves; i++) {
				allMove [i] = allMoveList.get(i);
	        }
			return allMove;
		}else 
			return null;
	}// loadAllMoves()


} // Class DBConnection

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
		rs = sendSelectStatementInternal(sql);
		return rs;
	}
	
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
				e.printStackTrace();
				System.out.println ("SQL Insert Set fehlgeschlagen");
			}
		}
		return success;
	}
	
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
			Log log = Log.getInstance();
			
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
					// Anzahl geänderter Zeilen lesen
					i = pstmt.getUpdateCount();
					// in Logs schreiben
					log.write(sql);
					ResultSet rs1 = this.sendSelectStatementInternal("SELECT * FROM game WHERE gameID = " + id +";");
					System.out.println("SELECT * FROM game WHERE gameID = " + id +";");
					try{
						while ( rs1.next() )
					      {
							String rs_gameID = rs1.getString(1);
					        String rs_role = rs1.getString(2);
					        String rs_oppName = rs1.getString(3);
					        String rs_ownPoints = rs1.getString(4);
					        String rs_oppPoints = rs1.getString(5);
					        String rs_path = rs1.getString(6);
					        String rs_timeServer = rs1.getString(7);
					        String rs_timeDraw = rs1.getString(8);
					        String saved = rs_gameID + ","+ rs_role +  ","+ rs_oppName +  ","+ rs_ownPoints +',' +rs_oppPoints +  ","
					        		+ rs_path +  ","+ rs_timeServer + ", " + rs_timeDraw;
					        log.write("Game"+saved);
					      }
						}catch (Exception e){
								e.printStackTrace();
							}
				}else {
					// falls update
					sql = "UPDATE game SET (role, oppname, ownPoints, oppPoints) = ('" + role + "', '" + oppName + "', '" 
							+ ownPoints + "', '"+ oppPoints + "') WHERE gameID = " + gameID + ";"; 
					System.out.println (sql);
					Statement stmt = con.createStatement();
					// update an DB schicken
					i = stmt.executeUpdate(sql); //i ist row count
					
					if (i != 1){
							System.out.println("Fehler beim Schreiben in DB");
							log.write("Game wurde nicht gespeichert");
					}else{
						log.write(sql);
						ResultSet rs = this.sendSelectStatementInternal("SELECT * FROM game WHERE gameID = " + gameID +";");
						try{
							while ( rs.next() )
						      {
								String rs_gameID = rs.getString(1);
						        String rs_role = rs.getString(2);
						        String rs_oppName = rs.getString(3);
						        String rs_ownPoints = rs.getString(4);
						        String rs_oppPoints = rs.getString(5);
						        String rs_path = rs.getString(6);
						        String rs_timeServer = rs.getString(7);
						        String rs_timeDraw = rs.getString(8);
						        String saved = rs_gameID + ","+ rs_role +  ","+ rs_oppName +  ","+ rs_ownPoints +',' +rs_oppPoints +  ","
						        		+ rs_path +  ","+ rs_timeServer + ", " + rs_timeDraw;
						        log.write("Game: "+saved);
						      }
						}catch (Exception e){
							e.printStackTrace();
						}
						
					}
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
					System.out.println ("SQL: "+sql);
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
					log.write(sql);
					ResultSet rs1 = this.sendSelectStatementInternal("SELECT * FROM gameSet WHERE gameID = " + gameID +" AND setID = "+ setID+";");
					try{
						while ( rs1.next() )
					      {
							String rs_gameID = rs1.getString(1);
					        String rs_setID = rs1.getString(2);
					        String rs_winner = rs1.getString(3);
					        String rs_starttime = rs1.getString(4);
					        String rs_endtime = rs1.getString(5);
					        String saved = rs_gameID + ","+ rs_setID +  ","+ rs_winner +  ","+ rs_starttime +',' +rs_endtime;
					        log.write("Set: " + saved);
					      }
					}catch (Exception e){
						e.printStackTrace();
					}
				}
				
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
					System.out.println (sql);
				}
				Statement stmt = con.createStatement();
				// Insert an DB schicken
				int i = stmt.executeUpdate(sql); //i ist row count
				if (i == 1){
					success = true;
					log.write(sql);
					ResultSet rs1 = this.sendSelectStatementInternal("SELECT * FROM move WHERE gameID = " + gameID +" AND setID = "+ setID+" AND moveID = "+moveID+";");
					try{
						while ( rs1.next() )
					      {
							String rs_gameID = rs1.getString(1);
					        String rs_setID = rs1.getString(2);
					        String rs_moveid = rs1.getString(3);
					        String rs_role = rs1.getString(4);
					        String rs_column = rs1.getString(5);
					        String rs_time = rs1.getString(6);
					        String saved = rs_gameID + ","+ rs_setID +  ","+ rs_moveid +  ","+ rs_role +"," +rs_column + "," + rs_time ;
					        log.write("Move: " + saved);
					      }
					}catch (Exception e){
						e.printStackTrace();
					}
				}
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
	 * @param gameID ID des zu ladenen Games
	 * @return Game Objekt des gewünschten Spiels
	 */
	public synchronized Game loadGame(int gameID) {
		Log log = Log.getInstance();
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
		        log.write("Game geladen: "+ resultset);
		      }else return null; // dann wäre nichts im resultset und nichts in der DB gefunden
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
	
	/**
	 * 
	 * @return Game [] alle gespeicherten Games
	 */
//	public synchronized Game [] loadAllGames(){
//		//return
//	}
	
	/**
	 * 
	 * @param gameID valide ID eines Spiels
	 * @return Set [] aller sets zur GameID
	 */
	public synchronized Set [] loadAllSets(int gameID){
		Log log = Log.getInstance();
		ArrayList <Set> allSetList = new ArrayList <Set> (0);
		Set [] allSet;
		//Vorbereitung für Results:
        String ssetID = "";
        String swinner = "";
        String sstarttime = "";
        String sendtime = "";
        
        		
		String sql = "Select * FROM gameSet WHERE gameID = " + gameID + ";";
		ResultSet rs = this.sendSelectStatementInternal(sql);
		try{
			while ( rs.next() ){
				//Infos aus rs holen und auflisten
		        ssetID = rs.getString(2);
		        swinner = rs.getString(3);
		        sstarttime = rs.getString(4);
		        sendtime = rs.getString(5);
		        String resultset = gameID + ","+ ssetID +  ","+ swinner +  ","+ sstarttime +',' +sendtime;
		        System.out.println (resultset);
		        log.write("Set geladen: "+ resultset);
		        
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
			
			
		}catch (SQLException e){
			e.printStackTrace();
		}
		int noSets = allSetList.size();
		if (noSets == 0)
				return null; // dann sind keine Sets da
        allSet = new Set [noSets];
        for (int i = 0; i < noSets; i++) {
			allSet [i] = allSetList.get(i);
        }
		return allSet;
	}// loadAllSets()
	
	/**
	 * 
	 * @param gameID ID eines Spiels
	 * @param setID ID eines Sets zu dem Spiel
	 * @return Move [] Array aller Moves zum entsprechenden Set
	 */
	public synchronized Move [] loadAllMoves(int gameID, int setID) {
		Log log = Log.getInstance();
		ArrayList <Move> allMoveList = new ArrayList <Move> (0);
		Move [] allMove;
		//Vorbereitung für Results:
        String smoveID = "";
        String srole = "";
        String scolumn = "";
        String stime = "";
        
        String sql = "Select * FROM move WHERE gameID = " + gameID + " AND setID = "+ setID +";";
		ResultSet rs = this.sendSelectStatementInternal(sql);
		try{
			while ( rs.next() ){
				//Infos aus rs holen und auflisten
		        smoveID = rs.getString(3);
		        srole = rs.getString(4);
		        scolumn = rs.getString(5);
		        stime = rs.getString(6);
		        String resultset = gameID + ","+ setID +  ","+ smoveID +  ","+ srole +',' +scolumn +  "," + stime;
		        System.out.println (resultset);
		        log.write("Move geladen: "+ resultset);
		        
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
		      }
			
		}catch (SQLException e){
			e.printStackTrace();
		}
		int noMoves = allMoveList.size();
		if (noMoves == 0)
				return null; // dann sind keine Moves da
        allMove = new Move [noMoves];
        for (int i = 0; i < noMoves; i++) {
			allMove [i] = allMoveList.get(i);
        }
		return allMove;
	}// loadAllMoves()


} // Class DBConnection

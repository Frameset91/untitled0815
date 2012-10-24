package test_utilities;

import java.sql.ResultSet;

import utilities.DBConnection;
import model.Game;
import model.Move;
import model.Set;



public class test_DBConnector {
	
	public static DBConnection con;

/**
 * testet saveMove(...)	
 */
	public static void testMove(){
		Move myMove = new Move ('0', 3); // letztes ist ID, immer ändern
		myMove.setID(3);
		
		boolean moveIns = con.saveMove(myMove, 1000, 1);
		System.out.println ("erfolg: " + moveIns);
		ResultSet rs;
		String sql = "SELECT * FROM move";
		rs= con.sendSelectStatement(sql);
		try{
			while ( rs.next() )
		      {
				String gameID = rs.getString(1);
		        String setID = rs.getString(2);
		        String moveID = rs.getString(3);
		        String role = rs.getString(4);
		        String column = rs.getString(5);
		        String time = rs.getString(6);
		        String resultset = gameID + ","+ setID +  ","+ moveID +  ","+ role +',' +column +", " + time;
		        System.out.println (resultset);
		      }
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}// ende testMove
	
	/**
	 * testet saveSet(...)
	 */
	public static void testSet() {
		Set mySet = new Set (7,6,3); // letztes ist ID, immer ändern
		
		boolean setIns = con.saveSet(mySet, 1000);
		System.out.println ("erfolg: " + setIns);
		ResultSet rs;
		String sql = "SELECT * FROM gameSet";
		rs= con.sendSelectStatement(sql);
		try{
			while ( rs.next() )
		      {
				String gameID = rs.getString(1);
		        String setID = rs.getString(2);
		        String winner = rs.getString(3);
		        String starttime = rs.getString(4);
		        String endtime = rs.getString(5);
		        String resultset = gameID + ","+ setID +  ","+ winner +  ","+ starttime +',' +endtime;
		        System.out.println (resultset);
		      }
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}// ende testSet
	
	/**
	 * testet saveGame(...)
	 */
	public static void testGame() {
		Game game = new Game(7, 6, 'x', "abc", "", 1,1);
		
		int i = con.saveGame(game);
		System.out.println("id:" + i);
		
		ResultSet rs;
		String sql = "SELECT * FROM game";
		rs= con.sendSelectStatement(sql);
		// Ergebnisse bekommen
		try{
			while ( rs.next() )
		      {
				String gameID = rs.getString(1);
		        String role = rs.getString(2);
		        String oppName = rs.getString(3);
		        String ownPoints = rs.getString(4);
		        String oppPoints = rs.getString(5);
		        String path = rs.getString(6);
		        String timeServer = rs.getString(7);
		        String timeDraw = rs.getString(8);
		        String resultset = gameID + ","+ role +  ","+ oppName +  ","+ ownPoints +',' +oppPoints +  ","
		        		+ path +  ","+ timeServer + ", " + timeDraw;
		        System.out.println (resultset);
		      }
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * testet loadGame
	 */
	public static void testLoadGame(){
		Game myGame = null;
		myGame = con.loadGame(1001);
		
		System.out.println("Game: "+myGame.getID() +" opp: "+ myGame.getOppName() +" oppPoint: "+ myGame.getOppPoints() 
				+" ownPoints: "+ myGame.getOwnPoints() +" path: "+ myGame.getPath() +" role: "+ myGame.getRole() 
				+ " timoutDraw: "+ myGame.getTimeoutDraw() +" timoutServer: "+ myGame.getTimeoutServer());
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		con = DBConnection.getInstance();
		
		// zum testen jeweils auskommentieren (und bei Move und Set je in den Methoden eins hoch zählen):
		//testMove();
		//testSet();
		//testGame();
		testLoadGame();
				
	}

}

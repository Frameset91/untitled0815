package test_utilities;

import java.sql.ResultSet;

import utilities.DBConnection;
import model.Game;
import model.Move;
import model.Set;



public class test_DBConnector {
	
	public static DBConnection con;

	public static void testMove(){
		Move myMove = new Move ('x', 3, 3); // letztes ist ID, immer �ndern
		
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
	
	public static void testSet() {
		Set mySet = new Set (7,6,3); // letztes ist ID, immer �ndern
		
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
		        String resultset = gameID + ","+ role +  ","+ oppName +  ","+ ownPoints +',' +oppPoints;
		        System.out.println (resultset);
		      }
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		con = DBConnection.getInstance();
		
		// zum testen jeweils auskommentieren (und bei Move und Set je in den Methoden eins hoch z�hlen):
		//testMove();
		//testSet();
		testGame();
				
	}

}
package test_utilities;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import utilities.DBConnection;
import model.Game;
import model.Move;
import model.Set;



public class test_DBConnector {
	
	public static DBConnection con;

	public static void testMove(){
		Move myMove = new Move ('x', 3, new Timestamp(new Date().getTime()));
		
		boolean moveIns = con.saveMove(myMove);
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
		Set mySet = new Set (7,6);
		
		boolean setIns = con.saveSet(mySet);
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
		
		DBConnection.main(null);
		System.out.println ("main ausgeführt");
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		con = DBConnection.getInstance();
		
		// testen:
		switch (args[0]){
				case "move": testMove();
				case "set": testSet();
				case "game": testGame();
		}
		
	}

}

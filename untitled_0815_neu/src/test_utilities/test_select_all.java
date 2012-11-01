package test_utilities;

import java.sql.ResultSet;

import utilities.DBConnection;

/**
 * 
 * @author Henny
 *
 */

public class test_select_all {

	/**
	 * Die Klasse gibt alle Einträge von Game, move und Set aus
	 * zu testzwecken
	 */
	
	public static DBConnection con;
	
	public static void main(String[] args) {
		// Verbindung bauen
		ResultSet rs;
		String sql;
		con = DBConnection.getInstance();
		
		// Game:
		sql = "SELECT * FROM game";
		rs= con.sendSelectStatement(sql);
		System.out.println ("Alle Games: ");
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
		
		// Set:
		sql = "SELECT * FROM gameSet";
		rs= con.sendSelectStatement(sql);
		System.out.println ("alle Sets: ");
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
		
		// move:
		sql = "SELECT * FROM move";
		rs= con.sendSelectStatement(sql);
		System.out.println("alle Moves: ");
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

	}

}

package test_utilities;

import utilities.DBConnection;
import model.Game;



public class test_DBConnector {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Game game = new Game(7, 6, 'x', "abc", "", 1,1);
		
		DBConnection con = DBConnection.getInstance();
		int i = con.saveGame(game);
		
		System.out.println("id:" + i);
		
		DBConnection.main(null);
		System.out.println ("main ausgeführt");
		
	}

}

package model;
import java.sql.Timestamp;

/**
 * @author Sascha
 *
 */
public class Move {
	private Timestamp time;
	private String role;
	private int column;
	private int moveID; //Database primary key
	
	public Move(String role, int column, Timestamp time){
		this.role = role;
		this.column = column;
		this.time = time;
	}
	
	public Move(String role, int column){
		this.role = role;
		this.column = column;
		
		//java.util.Date date= new java.util.Date();
		this.time = new Timestamp(new java.util.Date().getTime());
	}
	
	/**
	 * @return the time
	 */
	public Timestamp getTime() {
		return time;
	}
	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}
	
	/**
	 * @return the moveID
	 */
	public int getMoveID() {
		return moveID;
	}
	
	public void save(int gameID, int setID){
		//TODO: In Datenbank speichern (Primarykey = GameID + SetID + Timestamp von Move)
	}
	
}

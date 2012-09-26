import java.sql.Timestamp;

/**
 * @author Sascha
 *
 */
public class Move {
	private Timestamp time;
	private Game.GameRole role;
	private int column;
	private int moveID; //Database primary key
	
	public Move(Game.GameRole role, int column, Timestamp time){
		this.role = role;
		this.column = column;
		this.time = time;
	}
	
	public Move(Game.GameRole role, int column){
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
	public Game.GameRole getRole() {
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

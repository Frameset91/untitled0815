import org.hsqldb.types.TimestampData;


/**
 * @author Sascha
 *
 */
public class Move {
	private TimestampData time;
	private Game.GameRole role;
	private int column;
	
	public Move(Game.GameRole role, int column, TimestampData time){
		this.role = role;
		this.column = column;
		this.time = time;
	}
	
	/**
	 * @return the time
	 */
	public TimestampData getTime() {
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
	
	public void save(){
		//TODO: In Datenbank speichern
	}
}

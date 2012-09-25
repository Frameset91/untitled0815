import java.sql.Timestamp;
import java.util.*;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;



public class Set{
	
	public enum Status{
		//TODO!
		a,b,c,d
	}
	
	private List<Move> moves;
	private Timestamp startTime;
	private Timestamp endTime;
	//private int setID; //Database primary key
	private Status status;
	private GameField field; 
	private Game.GameRole winner;
	
	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the winner
	 */
	public Game.GameRole getWinner() {
		return winner;
	}

	/**
	 * @param winner the winner to set
	 */
	public void setWinner(Game.GameRole winner) {
		this.winner = winner;
	}

	/**
	 * @return the moves
	 */
	public List<Move> getMoves() {
		return moves;
	}

	/**
	 * @return the field
	 */
	public GameField getField() {
		return field;
	}

	public Set(int cols, int rows){
		field = new GameField(cols,rows);
		startTime = new Timestamp(new Date().getTime());
		
	}
	
	public void addMove(Move move){
		moves.add(move);
		field.addMove(move);
	}
	
	public void save(int gameID){
		endTime = new Timestamp(new Date().getTime());
		//TODO: In Datenbank speichern (Primarykey = GameID + SetID), erzeugte setID an Moves weitergeben 
		
		
		//alle Moves speichern 
		ListIterator<Move> iterator = moves.listIterator();
		while (iterator.hasNext())
		{
//		    iterator.next().save(gameID, setID);
		}
	}

}

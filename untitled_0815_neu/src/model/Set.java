package model;
import java.sql.Timestamp;
import java.util.*;

import javafx.beans.property.SimpleStringProperty;


public class Set{
	
	private ArrayList<Move> moves;
	private Timestamp startTime;
	private Timestamp endTime;
	//private int setID; //Database primary key
	private SimpleStringProperty status;
	private GameField field; 
	private SimpleStringProperty winner;
	
	

	public Set(int cols, int rows){
		field = new GameField(cols,rows);
		startTime = new Timestamp(new Date().getTime());
		moves = new ArrayList<Move>();
		winner = new SimpleStringProperty();
		status = new SimpleStringProperty();
		
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
	
	/**
	 * @return the status
	 */
	public SimpleStringProperty getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status.set(status);
	}

	/**
	 * @return the winner
	 */
	public SimpleStringProperty getWinner() {
		return winner;
	}

	/**
	 * @param winner the winner to set
	 */
	public void setWinner(String winner) {
		this.winner.set(winner);
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

}

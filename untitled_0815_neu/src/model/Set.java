package model;

/**
 * ein Satz als Bestandteil des Datenmodells. "Set" beinhaltet die satzspezifischen Informationen, das Spielfeld :GameField und die Züge :Move
 *  
 * @author Sascha Ulbrich 
 */

import java.sql.Timestamp;
import java.util.*;

import javafx.beans.property.SimpleStringProperty;


public class Set{
	
	private ArrayList<Move> moves;
	private Timestamp startTime;
	private Timestamp endTime;
	private int setID; //Database primary key
	private SimpleStringProperty status;
	private GameField field; 
	private SimpleStringProperty winner;
	
	
	/**
	 * Konstruktor von Set
	 *  
	 * @param Spaltenanzahl, Zeilenanzahl des Spielfelds
	 */
	public Set(int cols, int rows){
		field = new GameField(cols,rows);
		startTime = new Timestamp(new Date().getTime());
		moves = new ArrayList<Move>();
		winner = new SimpleStringProperty();
		status = new SimpleStringProperty();
		
	}
	
	/**
	 * Methode zum Hinzufügen eines Zuges  
	 *  
	 * @param der neue Zug :Move
	 */
	public void addMove(Move move){
		moves.add(move);
		field.addMove(move);
	}
	
	/**
	 * Methode zum Speichern eines Satzes
	 *  
	 * @param ID des Games :Integer
	 */
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
	 * @return Status des Satzes :StringProperty
	 */
	public SimpleStringProperty getStatus() {
		return status;
	}

	/**
	 * @param der neue Status :String
	 */
	public void setStatus(String status) {
		this.status.set(status);
	}

	/**
	 * @return Gewinner :String
	 */
	public SimpleStringProperty getWinner() {
		return winner;
	}

	/**
	 * @param Gewinner :String
	 */
	public void setWinner(String winner) {
		this.winner.set(winner);
	}

	/**
	 * @return alle Züge des Satzes :List<Move>
	 */
	public List<Move> getMoves() {
		return moves;
	}

	/**
	 * @return Spielfeld :GameField
	 */
	public GameField getField() {
		return field;
	}

}

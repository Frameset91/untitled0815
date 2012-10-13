package model;

/**
 * ein Satz als Bestandteil des Datenmodells. "Set" beinhaltet die satzspezifischen Informationen, das Spielfeld :GameField und die Züge :Move
 *  
 * @author Sascha Ulbrich 
 */

import java.sql.Timestamp;
import java.util.*;

import core.Constants;

import utilities.Log;

public class Set extends Observable{
	
	private ArrayList<Move> moves;
	private Timestamp startTime;
	private Timestamp endTime;
	private int ID; //Database primary key
	private String status;
	private GameField field; 
	private char winner;
	
	
	/**
	 * Konstruktor von Set
	 * @param Spaltenanzahl, Zeilenanzahl des Spielfelds, Satznummer :Integer
	 */
	public Set(int cols, int rows, int id){
		this.ID = id;
		field = new GameField(cols,rows);
		startTime = new Timestamp(new Date().getTime());
		moves = new ArrayList<Move>();
	}
	
	/**
	 * Methode zum Hinzufügen eines Zuges  
	 * @param der neue Zug :Move
	 */
	public synchronized void addMove(char role, byte col){
		Move move = new Move(role, col, moves.size()+1);
		moves.add(move);
		field.addMove(move);
		setChanged();
		Log.getInstance().write("Einen Move hinzugefügt");
		notifyObservers("field");
	}
	
	/**
	 * Methode zum Speichern eines Satzes
	 *  
	 * @param ID des Games :Integer
	 */
	public void save(int gameID){
		//TODO: In Datenbank speichern (Primarykey = GameID + SetID), erzeugte setID an Moves weitergeben 
		
		
		//alle Moves speichern 
		ListIterator<Move> iterator = moves.listIterator();
		while (iterator.hasNext())
		{
//		    iterator.next().save(gameID, setID);
		}
	}
	
	/**
	 * @param der neue Status :String
	 */
	public void setStatus(String status) {
		this.status = status;
		if(status == Constants.statusSetEnd){
			endTime = new Timestamp(new Date().getTime());
		}
		setChanged();
		notifyObservers("status");		
	}
	
	/**
	 * @param Gewinner :String
	 */
	public void setWinner(char winner) {
		this.winner = winner;
		setChanged();
		notifyObservers("winner");
	}
	
	/**
	 * @return Satznummer :String
	 */
	public int getID() {
		return ID;
	}	
	/**
	 * @return Status des Satzes :String
	 */
	public String getStatus() {
		return status;
	}

	
	/**
	 * @return Gewinner :String
	 */
	public char getWinner() {
		return winner;
	}

	
	/**
	 * @return alle Züge des Satzes :List<Move>
	 */
	public List<Move> getMoves() {
		return moves;
	}

	/**
	 * @return Spielfeld :Boolean[][]
	 */
	public Boolean[][] getField() {
		return field.getField();
	}
	
	/**
	 * @return Startzeit :java.sql.Timestamp
	 */
	public Timestamp getStarttime() {
		return startTime;
	}
	
	/**
	 * @return Endzeit :java.sql.Timestamp
	 */
	public Timestamp getEndtime() {
		return endTime;
	}


}

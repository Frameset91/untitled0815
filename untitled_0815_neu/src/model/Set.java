package model;

/**
 * ein Satz als Bestandteil des Datenmodells. "Set" beinhaltet die satzspezifischen Informationen, das Spielfeld :GameField und die Züge :Move
 *  
 * @author Sascha Ulbrich 
 */

import java.sql.Timestamp;
import java.util.*;

import core.Constants;

import utilities.DBConnection;
import utilities.Log;

public class Set extends Observable{
	
	private ArrayList<Move> moves;
	private Timestamp startTime;
	private Timestamp endTime;
	private int ID; //Database primary key
	private String status;
	private GameField field; 
	private char winner;
	private boolean isSaved;
	
	/**
	 * Konstruktor von Set
	 * @param Spaltenanzahl, Zeilenanzahl des Spielfelds, Satznummer :Integer
	 */
	public Set(int cols, int rows, int id){
		this.ID = id;
		field = new GameField(cols,rows);
		startTime = new Timestamp(new Date().getTime());
		moves = new ArrayList<Move>();
		winner = '/';
		isSaved = false;
	}
	
	/**
	 * Konstruktor von Set (nur für Ladevorgang)
	 * @param Spaltenanzahl, Zeilenanzahl des Spielfelds, Satznummer, Startzeit, Endzeit, Satzstatus, Gewinner
	 */
	public Set(int cols, int rows, int id, Timestamp startTime, Timestamp endTime, String status, char winner){
		this.ID = id;
		this.endTime = endTime;
		this.startTime = startTime;
		this.status = status;
		this.winner = winner;
		field = new GameField(cols,rows);
		moves = new ArrayList<Move>();
		isSaved = true;
	}
	
	/**
	 * Methode zum Hinzufügen eines Zuges  
	 * @param Rolle die gesetzt hat, gesetzte Spalte
	 */
	public synchronized void addMove(char role, byte col){
		Move move = new Move(role, col, moves.size()+1);
		moves.add(move);
		field.addMove(move);
		setChanged();
		isSaved = false;
		Log.getInstance().write("Einen Move hinzugefügt");
		notifyObservers("field");
	}
	
	/**
	 * Methode zum Hinzufügen eines Zuges (nur für Ladevorgang)
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
		//In Datenbank speichern (Primarykey = GameID + SetID), erzeugte setID an Moves weitergeben 
		if(!isSaved){
			DBConnection.getInstance().saveSet(this,gameID);
		}
		
		//alle Moves speichern 
		ListIterator<Move> iterator = moves.listIterator();
		while (iterator.hasNext())
		{
		    Move move = iterator.next();
		    if(!move.isSaved()){
		    	move.save(gameID, ID);
		    }
		}
		isSaved = true;
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
		isSaved = false;
		notifyObservers("status");		
	}
	
	/**
	 * @param Gewinner :String
	 */
	public void setWinner(char winner) {
		this.winner = winner;
		setChanged();
		isSaved = false;
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

	/**
	 * @return Endzeit :java.sql.Timestamp
	 */
	public Boolean isSaved() {
		return isSaved;
	}
}

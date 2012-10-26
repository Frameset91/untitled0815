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
	 * @param cols Spaltenanzahl
	 * @param rows Zeilenanzahl des Spielfelds
	 * @param id Satznummer
	 */
	public Set(int cols, int rows, int id){
		this.ID = id;
		field = new GameField(cols,rows);
		startTime = new Timestamp(new Date().getTime());
		moves = new ArrayList<Move>();
		winner = Constants.noRole;
		isSaved = false;
	}
	
	/**
	 * Konstruktor von Set (nur für Ladevorgang)
	 * @param cols Spaltenanzahl
	 * @param rows Zeilenanzahl des Spielfelds
	 * @param id Satznummer
	 * @param startTime Startzeit des Satzes 
	 * @param endTime Endzeit des Satzes
	 * @param status Satzstatus
	 * @param winner Gewinner (Konstante oRole|xRole|noRole)
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
	 * Methode zum Hinzufügen eines Zuges, falls die ID des Zuges nicht gesetzt ist (-1), wird eine ID vergeben (= Position in Liste)
	 * @param move der einzufügende Zug
	 */
	public synchronized void addMove(Move move){
		if(move.getID() == -1){
			move.setID(moves.size()+1);
		}
		moves.add(move);
		field.addMove(move);
		setChanged();
		isSaved = false;
		Log.getInstance().write("Einen Move hinzugefügt");
		notifyObservers("field");
	}
	
	/**
	 * Methode zum Entfernen eines Zuges
	 * @param move der zu entfernende Zug
	 */
	public synchronized void removeMove(Move move){

		moves.remove(move);
		field.removeMove(move);
		setChanged();
		Log.getInstance().write("Einen Move entfernt");
		notifyObservers("field");
	}
	
	/**
	 * Methode zum Speichern eines Satzes
	 *  
	 * @param gameID ID des Games 
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
	 * @param status der neue Status
	 */
	public void setStatus(String status) {
		this.status = status;
		if(status == Constants.STATE_SET_ENDED){
			endTime = new Timestamp(new Date().getTime());
		}
		setChanged();
		isSaved = false;
		notifyObservers("status");		
	}
	
	/**
	 * @param winner Gewinner des Satzes 
	 */
	public void setWinner(char winner) {
		this.winner = winner;
		setChanged();
		isSaved = false;
		notifyObservers("winner");
	}
	
	/**
	 * @return Satznummer 
	 */
	public int getID() {
		return ID;
	}	
	/**
	 * @return Status des Satzes
	 */
	public String getStatus() {
		return status;
	}

	
	/**
	 * @return Gewinner des Satzes
	 */
	public char getWinner() {
		return winner;
	}

	
	/**
	 * @return Liste aller Züge des Satzes
	 */
	public List<Move> getMoves() {
		return moves;
	}

	/**
	 * @return Spielfeld 
	 */
	public Boolean[][] getField() {
		return field.getField();
	}
	
	/**
	 * @return Startzeit 
	 */
	public Timestamp getStarttime() {
		return startTime;
	}
	
	/**
	 * @return Endzeit
	 */
	public Timestamp getEndtime() {
		return endTime;
	}

	/**
	 * @return Aussage ob der aktuelle Zustand des Satzes gespeichert wurde
	 */
	public Boolean isSaved() {
		return isSaved;
	}
}

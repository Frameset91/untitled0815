package model;

/**
 * Ein einzelner Zug als Bestandteil eines Satzes im Datenmodell. 
 * 
 * @author Sascha
 *
 */

import java.sql.Timestamp;

import utilities.DBConnection;

public class Move {
	private Timestamp time;
	private char role;
	private int column;
	private int ID;
	private boolean isSaved;
	
	/**
	 * Konstruktor von Move 
	 *  
	 * @param role Rolle die gezogen hat
	 * @param column Spalte in die gesetzt wird
	 */
	public Move(char role, int column){
		this.role = role;
		this.column = column;
		this.ID = -1;
		this.time = new Timestamp(new java.util.Date().getTime());
		isSaved = false;
	}
	
	/**
	 * @deprecated
	 * Konstruktor von Move 
	 * bitte entfernen, für Ladeprozess die Zeit mit übergeben
	 * 
	 * @param role Rolle die gezogen hat
	 * @param column Spalte in die gesetzt wird
	 * @param id ID (Primarykey) des Satzes
	 */
	public Move(char role, int column, int id){
		this.role = role;
		this.column = column;
		this.ID = id;
		this.time = new Timestamp(new java.util.Date().getTime());
		isSaved = false;
	}
	
	/**
	 * Konstruktor von Move (nur für Ladeprozess)
	 * 
	 * @param role Rolle die gezogen hat
	 * @param column Spalte in die gesetzt wird
	 * @param id ID (Primarykey) des Satzes
	 * @param time Zugzeitpunkt 
	 */
	public Move(char role, int column, int id, Timestamp time){
		this.role = role;
		this.column = column;
		this.ID = id;
		this.time = time;
		isSaved = true;
	}
	
	/**
	 * Methode zum Speichern eines Zuges
	 *  
	 * @param gameID ID des dazugehörigen Spiels
	 * @param setID ID des  dazugehörigen Satzes
	 */
	public void save(int gameID, int setID){
		//In Datenbank speichern (Primarykey = GameID + SetID + ID von Move)
		DBConnection.getInstance().saveMove(this, gameID, setID);
		isSaved = true;
	}
	
	/**
	 * @return Zugnummer
	 */
	public int getID() {
		return ID;
	}
	
	/**
	 * @return Aussage, ob der aktuelle Zustand des Zuges gespeichert wurde
	 */
	public Boolean isSaved() {
		return isSaved;
	}
	
	/**
	 * @return Zugnummer 
	 */
	public Timestamp getTime() {
		return time;
	}
	/**
	 * @return Rolle die gezogen hat
	 */
	public char getRole() {
		return role;
	}
	/**
	 * @return Spalte in die gesetzt werden soll
	 */
	public int getColumn() {
		return column;
	}
	/**
	 * @param id Zugnummer
	 */
	public void setID(int id) {
		this.ID = id;		
	}
}

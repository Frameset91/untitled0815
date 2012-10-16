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
	 * @param Rolle die gezogen hat :String, Spalte :Integer, Zugzeitpunkt :Timestamp
	 */
	public Move(char role, int column, int id){
		this.role = role;
		this.column = column;
		this.ID = id;
		this.time = new Timestamp(new java.util.Date().getTime());
		isSaved = false;
	}
	
	/**
	 * Konstruktor von Move 
	 *  
	 * @param Rolle die gezogen hat :String, Spalte :Integer, Zugzeitpunkt :Timestamp
	 */
	public Move(char role, int column, int id, Timestamp time){
		this.role = role;
		this.column = column;
		this.ID = id;
		this.time = time;
		isSaved = true;
	}
//	
//	/**
//	 * Konstruktor von Move 
//	 *  
//	 * @param Rolle die gezogen hat :String, Spalte :Integer
//	 */
//	public Move(char role, int column){
//		this.role = role;
//		this.column = column;		
////		this.time = new Timestamp(new java.util.Date().getTime());
//	}
	
	/**
	 * Methode zum Speichern eines Zuges
	 *  
	 * @param ID des Games :Integer, ID des Satzes :Integer
	 */
	public void save(int gameID, int setID){
		//In Datenbank speichern (Primarykey = GameID + SetID + ID von Move)
		DBConnection.getInstance().saveMove(this, gameID, setID);
		isSaved = true;
	}
	
	/**
	 * @return Zugnummer :Integer
	 */
	public int getID() {
		return ID;
	}
	
	/**
	 * @return Gespeichert :Boolean
	 */
	public Boolean isSaved() {
		return isSaved;
	}
	
	/**
	 * @return Zugnummer :Integer
	 */
	public Timestamp getTime() {
		return time;
	}
	/**
	 * @return Rolel die gezogen hat :String
	 */
	public char getRole() {
		return role;
	}
	/**
	 * @return Spalte :Integer
	 */
	public int getColumn() {
		return column;
	}
	
	
	
}

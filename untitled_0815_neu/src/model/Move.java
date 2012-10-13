package model;

/**
 * Ein einzelner Zug als Bestandteil eines Satzes im Datenmodell. 
 * 
 * @author Sascha
 *
 */

import java.sql.Timestamp;

public class Move {
	private Timestamp time;
	private char role;
	private int column;
	private int ID;
	
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
	}
	
	/**
	 * Konstruktor von Move 
	 *  
	 * @param Rolle die gezogen hat :String, Spalte :Integer
	 */
	public Move(char role, int column){
		this.role = role;
		this.column = column;		
//		this.time = new Timestamp(new java.util.Date().getTime());
	}
	
	/**
	 * @return Zugnummer :Integer
	 */
	public int getID() {
		return ID;
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
	
	/**
	 * Methode zum Speichern eines Zuges
	 *  
	 * @param ID des Games :Integer, ID des Satzes :Integer
	 */
	public void save(int gameID, int setID){
		//TODO: In Datenbank speichern (Primarykey = GameID + SetID + Timestamp von Move)
	}
	
}

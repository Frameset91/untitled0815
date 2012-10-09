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
	private String role;
	private int column;
	private int moveID; //Database primary key
	
	/**
	 * Konstruktor von Move 
	 *  
	 * @param Rolle die gezogen hat :String, Spalte :Integer, Zugzeitpunkt :Timestamp
	 */
	public Move(String role, int column, Timestamp time){
		this.role = role;
		this.column = column;
		this.time = time;
	}
	
	/**
	 * Konstruktor von Move 
	 *  
	 * @param Rolle die gezogen hat :String, Spalte :Integer
	 */
	public Move(String role, int column){
		this.role = role;
		this.column = column;
		
		//java.util.Date date= new java.util.Date();
		this.time = new Timestamp(new java.util.Date().getTime());
	}
	
	/**
	 * @return Zugzeitpunkt :Timestamp
	 */
	public Timestamp getTime() {
		return time;
	}
	/**
	 * @return Rolel die gezogen hat :String
	 */
	public String getRole() {
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

package core;
/**
 * Einfaches Datenobjekt zur Darstellung von Spielen in einer Tabelle
 *  
 * @author Sascha Ulbrich 
 */


import javafx.beans.property.SimpleStringProperty;

public class GameProperty {
	private SimpleStringProperty gameID;
	private SimpleStringProperty players;
	private SimpleStringProperty points;
	
	/**
	 * Konstruktor von GameProperty
	 * @param gameID ID des Spiels
	 * @param oppName Name des Gegners
	 */
	public GameProperty(String gameID, String players, String time){
		this.gameID = new SimpleStringProperty(gameID);
		this.players = new SimpleStringProperty(players);
		this.points = new SimpleStringProperty(time);
	}

	/**
	 * @return ID von Game
	 */
	public String getGameID() {
		return gameID.get();
	}
	
	/**
	 * @return Namen der Gegner
	 */
	public String getPlayers() {
		return players.get();
	}
	
	/**
	 * @return Startzeit des Spiels
	 */
	public String getPoints() {
		return points.get();
	}
}

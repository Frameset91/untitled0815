package core;
/**
 * Einfaches Datenobjekt zur Darstellung von Spielen in einer Tabelle
 *  
 * @author Sascha Ulbrich 
 */


import javafx.beans.property.SimpleStringProperty;

public class GameProperty {
	private SimpleStringProperty gameID;
	private SimpleStringProperty oppName;
	
	/**
	 * Konstruktor von GameProperty
	 * @param gameID ID des Spiels
	 * @param oppName Name des Gegners
	 */
	public GameProperty(String gameID, String oppName){
		this.gameID = new SimpleStringProperty(gameID);
		this.oppName = new SimpleStringProperty(oppName);
	}

	/**
	 * @return ID von Game
	 */
	public String getGameID() {
		return gameID.get();
	}
	
	/**
	 * @return Name des Gegners
	 */
	public String getOppName() {
		return oppName.get();
	}
}

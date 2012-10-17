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
	 * @param ID des Spiels :String, Gegnername :String
	 */
	public GameProperty(String gameID, String oppName){
		this.gameID = new SimpleStringProperty(gameID);
		this.oppName = new SimpleStringProperty(oppName);
	}

	/**
	 * @return die ID von Game :String
	 */
	public String getGameID() {
		return gameID.get();
	}
	
	/**
	 * @return den Gegner :String
	 */
	public String getOppName() {
		return oppName.get();
	}
}

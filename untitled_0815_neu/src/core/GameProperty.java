package core;

import javafx.beans.property.SimpleStringProperty;

public class GameProperty {
	private SimpleStringProperty gameID;
	private SimpleStringProperty oppName;
	
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

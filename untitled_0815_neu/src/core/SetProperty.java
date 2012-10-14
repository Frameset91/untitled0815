package core;

import javafx.beans.property.SimpleStringProperty;

public class SetProperty {
	private SimpleStringProperty setNr;
	private SimpleStringProperty winner;
	
	public SetProperty(String setNr, String winner){
		this.setNr = new SimpleStringProperty(setNr);
		this.winner = new SimpleStringProperty(winner);
	}

	/**
	 * @return die Satznummer :String
	 */
	public String getSetNr() {
		return setNr.get();
	}
	
	/**
	 * @return den Gewinner :String
	 */
	public String getWinner() {
		return winner.get();
	}
}

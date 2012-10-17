package core;
/**
 * Einfaches Datenobjekt zur Darstellung von Spielen in einer Tabelle
 *  
 * @author Sascha Ulbrich 
 */

import javafx.beans.property.SimpleStringProperty;

public class SetProperty {
	private SimpleStringProperty setNr;
	private SimpleStringProperty winner;
	
	
	/**
	 * Konstruktor von SetProperty
	 * @param Satznummer :String, Gewinner :String
	 */
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

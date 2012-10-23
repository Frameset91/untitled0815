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
	 * @param setNr Satznummer
	 * @param winner Gewinner
	 */
	public SetProperty(String setNr, String winner){
		this.setNr = new SimpleStringProperty(setNr);
		this.winner = new SimpleStringProperty(winner);
	}

	/**
	 * @return Satznummer
	 */
	public String getSetNr() {
		return setNr.get();
	}
	
	/**
	 * @return Gewinner
	 */
	public String getWinner() {
		return winner.get();
	}
}

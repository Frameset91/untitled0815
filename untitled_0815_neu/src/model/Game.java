package model;
/**
 * Das Datenmodell "Game" beinhaltet die Spieleinstellungen und Sätze
 *  
 * @author Sascha Ulbrich 
 */

import java.util.*;

import utilities.Log;

import core.Constants;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Game extends Observable implements Observer{
//	public enum GameRole{
//		x,o
//	}
	
	private ArrayList<Set> sets;
	private int cols;
	private int rows;
	
	private char role;
	private int ownPoints;
	private int oppPoints;
	private String oppName;
	private String path;
	private int timeoutServer;
	private int timeoutDraw;
	
	private int ID;
	
	/**
	 * Konstruktor von Game 
	 *  
	 * @param Spaltenanzahl, Zeilenanzahl des Spielfelds
	 */
	public Game(int cols, int rows, char role, String oppName, String path, int timeoutServer, int timeoutDraw){
		this.cols = cols;
		this.rows = rows;
		this.role = role;
		this.oppName = oppName;
		this.path = path;
		this.timeoutServer = timeoutServer;
		this.timeoutDraw = timeoutDraw;
//		oppName = new SimpleStringProperty();
//		role = new SimpleStringProperty();
//		role.addListener(new ChangeListener<String>() {
//
//			/* (non-Javadoc)
//			 * @see javafx.beans.value.ChangeListener#changed(javafx.beans.value.ObservableValue, java.lang.Object, java.lang.Object)
//			 */
//			@Override
//			public void changed(ObservableValue<? extends String> arg0,
//					String arg1, String arg2) {
//				String newRole = arg0.getValue();
//				if(newRole.equals(Constants.oRole)){
//					ownToken.setValue(Constants.oToken);
//					oppToken.setValue(Constants.xToken);
//				}else{
//					ownToken.setValue(Constants.xToken);
//					oppToken.setValue(Constants.oToken);
//				}
//				
//			}
//			
//		});
//		timeoutDraw = new SimpleIntegerProperty(2000);
//		timeoutServer = new SimpleIntegerProperty(300);
//		path = new SimpleStringProperty();
//		ownPoints = new SimpleIntegerProperty(0);
//		oppPoints = new SimpleIntegerProperty(0);
		sets = new ArrayList<Set>();
//		sets.add(new Set(cols,rows));
	}
	
	/**
	 * Methoden zum Erstellen und Hinzufügen eines weiteren Satzes zum Datenmodell
	 *  
	 * @return der neu erstellte Satz :Set
	 */
	public Set newSet(){
		Set set = new Set(cols, rows); 
		set.addObserver(this);
		sets.add(set);
		setChanged();
		notifyObservers("sets");		
		return set;
	}
	
	//Observer Methode um bei Veränderungen von Gewinnern die Punkte neu zu berechnen
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		String changed = (String)arg1;
		
		switch (changed) {
		case "winner":
			Log.getInstance().write("Game: recalculate Points");
			int opp = 0;
			int own = 0;
			char win;
			ListIterator<Set> iterator = sets.listIterator();
			while (iterator.hasNext())
			{
				if((win = iterator.next().getWinner()) == role){
					//Gewonnen
					own++;
				}
				else if(win != role && (win == Constants.oRole || win == Constants.xRole)){
					//Verloren
					opp++;
				}else{
					//Unentschieden
				}
			}
			oppPoints = opp;
			ownPoints = own;
//			break;
			
		default:
			//Change Events in jedem Fall weiterreichen
			setChanged();
			notifyObservers(changed);			
			break;
		}
	}
	
	/**
	 * Methode zur Abfrage des letzten Satzes
	 *  
	 * @return der zuletzt erstellte Satz :Set
	 */
	public Set getLatestSet(){
		if(sets.size() > 0)
			return sets.get(sets.size()-1);		
		else
			return null;
	}
	
	/**
	 * Methode zum Hinzufügen eines Zuges  
	 *  
	 * @param der neue Zug :Move
	 */
	public void addMove(Move move){
		sets.get(sets.size()-1).addMove(move);
	}
	
	/**
	 * Methode zum Speichern des Datenmodells
	 */
	public void save(){
		//TODO: In Datenbank speichern (Primarykey = GameID), erzeugte GameID an Sets weitergeben, ID speichern
		
		//alle Moves speichern 
		// ListIterator<Set> iterator = sets.listIterator();
		//		
//		while (iterator.hasNext())
//		{
////		    iterator.next().save(gameID);
//		}
	}
	//get set
	
	/**
	 * @return Alle Sätze :ObservableList<Set>
	 */
	public ArrayList<Set> getSets() {
		return sets;
	}
//	
//	/**
//	 * @return Style für den gegnerischen Stein :StringProperty
//	 */
//	public SimpleStringProperty getOppToken() {
//		return oppToken;
//	}
//
//	/**
//	 * @return Style für den eigenen Stein :StringProperty
//	 */
//	public SimpleStringProperty getOwnToken() {
//		return ownToken;
//	}
	/**
	 * @return Minimales Intervall für die Serverabfrage in ms :IntegerProperty  
	 */
	public int getTimeoutServer() {
		return timeoutServer;
	}

	/**
	 * @return Maximale Zeit zur Berechnung eines Zuges in ms :IntegerProperty
	 */
	public int getTimeoutDraw() {
		return timeoutDraw;
	}
	
	/**
	 * @return eigene Rolle :StringProperty
	 */
	public char getRole() {
		return role;
	}
	
	/**
	 * @return eigene Punkte :IntegerProperty
	 */
	public int getOwnPoints() {
		return ownPoints;
	}
	
	/**
	 * @return gegnerische Punkte :IntegerProperty
	 */
	public int getOppPoints() {
		return oppPoints;
	}
	
	/**
	 * @return Name des Gegner :StringProperty
	 */
	public String getOppName() {
		return oppName;
	}

	/**
	 * @return Pfad für Serverdateien :StringProperty
	 */
	public String getPath() {
		return path;
	}	

}



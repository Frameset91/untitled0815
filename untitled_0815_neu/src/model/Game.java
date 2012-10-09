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


public class Game {
//	public enum GameRole{
//		x,o
//	}
	
	private ObservableList<Set> sets;
	private int cols;
	private int rows;
	private SimpleStringProperty role;
	private SimpleIntegerProperty ownPoints;
	private SimpleIntegerProperty oppPoints;
	private SimpleStringProperty oppName;
	private SimpleStringProperty path;
	private SimpleIntegerProperty timeoutServer;
	private SimpleIntegerProperty timeoutDraw;
	private SimpleStringProperty oppToken;
	private SimpleStringProperty ownToken;
	
	private int ID;
	
	/**
	 * Konstruktor von Game 
	 *  
	 * @param Spaltenanzahl, Zeilenanzahl des Spielfelds
	 */
	public Game(int cols, int rows){
		this.cols = cols;
		this.rows = rows;
		oppName = new SimpleStringProperty();
		oppToken = new SimpleStringProperty(Constants.oToken);
		ownToken = new SimpleStringProperty(Constants.xToken);
		role = new SimpleStringProperty();
		role.addListener(new ChangeListener<String>() {

			/* (non-Javadoc)
			 * @see javafx.beans.value.ChangeListener#changed(javafx.beans.value.ObservableValue, java.lang.Object, java.lang.Object)
			 */
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				String newRole = arg0.getValue();
				if(newRole.equals(Constants.oRole)){
					ownToken.setValue(Constants.oToken);
					oppToken.setValue(Constants.xToken);
				}else{
					ownToken.setValue(Constants.xToken);
					oppToken.setValue(Constants.oToken);
				}
				
			}
			
		});
		timeoutDraw = new SimpleIntegerProperty(2000);
		timeoutServer = new SimpleIntegerProperty(300);
		path = new SimpleStringProperty();
		ownPoints = new SimpleIntegerProperty(0);
		oppPoints = new SimpleIntegerProperty(0);
		sets = FXCollections.observableArrayList();
//		sets.add(new Set(cols,rows));
	}
	
	/**
	 * Methoden zum Erstellen und Hinzufügen eines weiteren Satzes zum Datenmodell
	 *  
	 * @return der neu erstellte Satz :Set
	 */
	public Set newSet(){
		Set set = new Set(cols, rows); 
		//ChangeListener um bei Veränderungen von Gewinnern die Punkte neu zu berechnen
		set.getWinner().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1,
					String arg2) {
				//Im UI Thread starten!
				Platform.runLater(new Runnable() {
				
					@Override
					public void run() {
				
						Log.getInstance().write("Game: recalculate Points");
						int opp = 0;
						int own = 0;
						String win;
						ListIterator<Set> iterator = sets.listIterator();
						while (iterator.hasNext())
						{
							if((win = iterator.next().getWinner().get()).equals(role.get())){
								//Gewonnen
								own++;
							}
							else if(!(win.equals(role.get())) && (win.equals(Constants.oRole) || win.equals(Constants.xRole))){
								//Verloren
								opp++;
							}else{
								//Unentschieden
							}
						}
						oppPoints.setValue(opp);
						ownPoints.setValue(own);
					}
				});
			}
		});
		
		sets.add(set);
		return set;
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
	public ObservableList<Set> getSets() {
		return sets;
	}
	
	/**
	 * @return Style für den gegnerischen Stein :StringProperty
	 */
	public SimpleStringProperty getOppToken() {
		return oppToken;
	}

	/**
	 * @return Style für den eigenen Stein :StringProperty
	 */
	public SimpleStringProperty getOwnToken() {
		return ownToken;
	}

	/**
	 * @return Minimales Intervall für die Serverabfrage in ms :IntegerProperty  
	 */
	public SimpleIntegerProperty getTimeoutServer() {
		return timeoutServer;
	}

	/**
	 * @return Maximale Zeit zur Berechnung eines Zuges in ms :IntegerProperty
	 */
	public SimpleIntegerProperty getTimeoutDraw() {
		return timeoutDraw;
	}
	
	/**
	 * @return eigene Rolle :StringProperty
	 */
	public SimpleStringProperty getRole() {
		return role;
	}
	
	/**
	 * @return eigene Punkte :IntegerProperty
	 */
	public SimpleIntegerProperty getOwnPoints() {
		return ownPoints;
	}
	
	/**
	 * @return gegnerische Punkte :IntegerProperty
	 */
	public SimpleIntegerProperty getOppPoints() {
		return oppPoints;
	}
	
	/**
	 * @return Name des Gegner :StringProperty
	 */
	public SimpleStringProperty getOppName() {
		return oppName;
	}

	/**
	 * @return Pfad für Serverdateien :StringProperty
	 */
	public SimpleStringProperty getPath() {
		return path;
	}	

}



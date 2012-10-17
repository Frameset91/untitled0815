package model;
/**
 * Das Datenmodell "Game" beinhaltet die Spieleinstellungen und S�tze
 *  
 * @author Sascha Ulbrich 
 */

import java.util.*;

import utilities.DBConnection;
import utilities.Log;
import core.Constants;

public class Game extends Observable implements Observer{
	
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
	 * @param Spaltenanzahl :Integer, Zeilenanzahl des Spielfelds :Integer, Rolle :Char, Gegnername :String, Serverpfad :String, 
	 * Min Intervall zur Serverabfrage :Integer, Zeit f�r einen Zug :Integer
	 */
	public Game(int cols, int rows, char role, String oppName, String path, int timeoutServer, int timeoutDraw){
		this.cols = cols;
		this.rows = rows;
		this.role = role;
		this.oppName = oppName;
		this.path = path;
		this.timeoutServer = timeoutServer;
		this.timeoutDraw = timeoutDraw;
		sets = new ArrayList<Set>();
		ID = -1;
	}
	
	/**
	 * Konstruktor von Game (nur f�r die Verwendung beim Laden)
	 *  
	 * @param Spaltenanzahl :Integer, Zeilenanzahl des Spielfelds :Integer, Rolle :Char, Gegnername :String, Serverpfad :String, 
	 * Min Intervall zur Serverabfrage :Integer, Zeit f�r einen Zug :Integer, PrimaryKey aus DB :Integer
	 */
	public Game(int cols, int rows, char role, String oppName, String path, int timeoutServer, int timeoutDraw, int ID){
		this.cols = cols;
		this.rows = rows;
		this.role = role;
		this.oppName = oppName;
		this.path = path;
		this.timeoutServer = timeoutServer;
		this.timeoutDraw = timeoutDraw;
		sets = new ArrayList<Set>();
		this.ID = ID;
	}
	
	/**
	 * Methoden zum Erstellen und Hinzuf�gen eines weiteren Satzes zum Datenmodell
	 *  
	 * @return der neu erstellte Satz :Set
	 */
	public Set newSet(){
		Set set = new Set(cols, rows, sets.size()+1); 
		set.addObserver(this);
		sets.add(set);
		setChanged();
		notifyObservers("sets");		
		return set;
	}
	
	/**
	 * Methoden zum Hinzuf�gen eines weiteren Satzes zum Datenmodell (nur f�r Ladevorgang)
	 *  
	 * @param der geladene Satz :Set
	 */
	public void addSet(Set set){
		set.addObserver(this);
		sets.add(set);		
		update(set, "winner");
	}
	
	//Observer Methode um bei Ver�nderungen von Gewinnern die Punkte neu zu berechnen
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	/**
	 * Methode um auf Ver�nderungen in Set zu reagieren
	 * Als Argument wird der Name der Variable �bergeben, die sich ge�ndert hat
	 * 
	 * @param das Objekt das sich ver�nder hat :Observable, Argumente die mit �bergeben werden :Object
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
	 * Methode zum Verwerfen des letzten Satzes
	 */
	public void discardLatestSet(){
		if(sets.size() > 0){
			sets.remove(sets.size()-1);	
			setChanged();
			notifyObservers("sets");	
		}
	}
	
	/**
	 * Methode zum Hinzuf�gen eines Zuges  
	 *  
	 * @param Rolle :Char, Spalte :Byte
	 */
	public void addMove(char role, byte col){
		sets.get(sets.size()-1).addMove(role, col);
	}
	
	/**
	 * Methode zum Speichern des Datenmodells
	 */
	public void save(){
		//In Datenbank speichern (Primarykey = GameID), erzeugte GameID an Sets weitergeben, ID speichern
		if(ID == -1){
			ID = DBConnection.getInstance().saveGame(this);
		}
		
		//alle Moves speichern 
		ListIterator<Set> iterator = sets.listIterator();
				
		while (iterator.hasNext())
		{	
			Set set = iterator.next();
			if(set.getStatus() == Constants.STATE_SET_ENDED && !set.isSaved())
				set.save(ID);
		}
	}
	
	//-------------------get set	
	/**
	 * @return Alle S�tze :ArrayList<Set>
	 */
	public ArrayList<Set> getSets() {
		return sets;
	}

	/**
	 * @return Minimales Intervall f�r die Serverabfrage in ms :Integer  
	 */
	public int getTimeoutServer() {
		return timeoutServer;
	}

	/**
	 * @return Maximale Zeit zur Berechnung eines Zuges in ms :Integer
	 */
	public int getTimeoutDraw() {
		return timeoutDraw;
	}
	
	/**
	 * @return eigene Rolle :String
	 */
	public char getRole() {
		return role;
	}
	
	/**
	 * @return eigene Punkte :Integer
	 */
	public int getOwnPoints() {
		return ownPoints;
	}
	
	/**
	 * @return gegnerische Punkte :Integer
	 */
	public int getOppPoints() {
		return oppPoints;
	}
	
	/**
	 * @return Name des Gegner :String
	 */
	public String getOppName() {
		return oppName;
	}

	/**
	 * @return Pfad f�r Serverdateien :String
	 */
	public String getPath() {
		return path;
	}	
	
	/**
	 * @return ID/PrimaryKey :String
	 */
	public int getID() {
		return ID;
	}

}



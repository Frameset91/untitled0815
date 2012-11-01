package model;
/**
 * Das Datenmodell "Game" beinhaltet die Spieleinstellungen und Sätze
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
	private String ownName;
	private String path;
	private int timeoutServer;
	private int timeoutDraw;
	
	private int ID;
	private boolean isSaved;
	
	
	/**
	 * Konstruktor von Game 
	 *  
	 * @param cols Spaltenanzahl
	 * @param rows Zeilenanzahl des Spielfelds
	 * @param role Rolle
	 * @param oppName Name des Gegners
	 * @param ownName eigener Name
	 * @param path Serverpfad
	 * @param timeoutServer Min Intervall zur Serverabfrage
	 * @param timeoutDraw Zeit für einen Zug
	 */
	public Game(int cols, int rows, char role, String oppName, String ownName, String path, int timeoutServer, int timeoutDraw){
		this.cols = cols;
		this.rows = rows;
		this.role = role;
		this.oppName = oppName;
		this.ownName = ownName;
		this.path = path;
		this.timeoutServer = timeoutServer;
		this.timeoutDraw = timeoutDraw;
		this.oppPoints = 0;
		this.ownPoints = 0;
		sets = new ArrayList<Set>();
		ID = -1;
		isSaved = false;
	}
	
	/**
	 * Konstruktor von Game (nur für die Verwendung beim Laden)
	 *  
	 * @param cols Spaltenanzahl
	 * @param rows Zeilenanzahl des Spielfelds
	 * @param role Rolle
	 * @param oppName Name des Gegners
	 * @param ownName eigener Name
	 * @param path Serverpfad
	 * @param timeoutServer Min Intervall zur Serverabfrage
	 * @param timeoutDraw Zeit für einen Zug
	 * @param ID Primarykey von Game
	 * @param oppPoints Punkte des Gegners
	 * @param ownPoints Eigene Punkte
	 */
	public Game(int cols, int rows, char role, String oppName, String ownName, String path, int timeoutServer, int timeoutDraw, int ID, int oppPoints, int ownPoints){
		this.cols = cols;
		this.rows = rows;
		this.role = role;
		this.oppName = oppName;
		this.ownName = ownName;
		this.path = path;
		this.timeoutServer = timeoutServer;
		this.timeoutDraw = timeoutDraw;
		this.oppPoints = oppPoints;
		this.ownPoints = ownPoints;
		sets = new ArrayList<Set>();
		this.ID = ID;
		isSaved = true;
	}
	
	/**
	 * Methoden zum Erstellen und Hinzufügen eines weiteren Satzes zum Datenmodell
	 *  
	 * @return der neu erstellte Satz
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
	 * Methoden zum Hinzufügen eines weiteren Satzes zum Datenmodell (nur für Ladevorgang)
	 *  
	 * @param set der geladene Satz
	 */
	public void addSet(Set set){
		set.addObserver(this);
		sets.add(set);		
		update(set, "winner");
		setChanged();
		notifyObservers("sets");
	}
	
	//Observer Methode um bei Veränderungen von Gewinnern die Punkte neu zu berechnen
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	/**
	 * Methode um auf Veränderungen im Datenmodell zu reagieren
	 * Umsetzung des Interfaces Observer
	 * 
	 * @param arg0 das Objekt das sich veränder hat
	 * @param arg1 Argumente die mit übergeben werden: der Name der Variable, die sich geändert hat
	 */		
	@Override
	public void update(Observable arg0, Object arg1) {
		String changed = (String)arg1;
		
		switch (changed) {
		case "status":
		case "winner":
			calcPoints();
//			break;
			
		default:
			//Change Events in jedem Fall weiterreichen
			setChanged();
			notifyObservers(changed);			
			break;
		}
	}
	
	//hilfsmethode zum kalkulieren der Punkte
	private void calcPoints(){
		Log.getInstance().write("Game: recalculate Points");
		int opp = 0;
		int own = 0;
		char win;
		Set set;
		ListIterator<Set> iterator = sets.listIterator();
		while (iterator.hasNext())
		{
			if((set = iterator.next()).getStatus().equals(Constants.STATE_SET_ENDED)){
				if((win = set.getWinner()) == role){
					//Gewonnen
					own += Constants.pointsWin;
					opp += Constants.pointsLoose;
				}
				else if(win != role && (win == Constants.oRole || win == Constants.xRole)){
					//Verloren
					opp += Constants.pointsWin;
					own += Constants.pointsLoose;
				}else{
					//Unentschieden
					own += Constants.pointsTie;
					opp += Constants.pointsTie;
				}
			}
		}
		oppPoints = opp;
		ownPoints = own;
		isSaved = false;
	}
	
	/**
	 * Methode zur Abfrage des letzten Satzes
	 *  
	 * @return der zuletzt erstellte Satz
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
			calcPoints();
			setChanged();
			notifyObservers("sets");			
		}
	}
	
	/**
	 * Methode zum Hinzufügen eines Zuges  
	 *  
	 * @param move der Zug, der hinzugefügt werden soll
	 * @return Ob Zug eingetragen wurde
	 */
	public boolean addMove(Move move){
		return sets.get(sets.size()-1).addMove(move);
	}
	
	/**
	 * Methode zum Entfernen eines Zuges  
	 *  
	 * @param move der Zug, der entfernt werden soll
	 */
	public void removeMove(Move move){
		sets.get(sets.size()-1).removeMove(move);
	}
	
	/**
	 * Methode zum Speichern des Datenmodells
	 */
	public void save(){
		calcPoints();
		//In Datenbank speichern (Primarykey = GameID), erzeugte GameID an Sets weitergeben, ID speichern
		if(ID == -1){
			ID = DBConnection.getInstance().saveGame(this);
		}else if(!isSaved){
			DBConnection.getInstance().saveGame(this);
		}
		
		//alle Moves speichern 
		ListIterator<Set> iterator = sets.listIterator();
				
		while (iterator.hasNext())
		{	
			Set set = iterator.next();
			if(set.getStatus() == Constants.STATE_SET_ENDED && !set.isSaved())
				set.save(ID);
		}
		isSaved = true;
	}
	
	//-------------------get set	
	/**
	 * @return Liste aller Sätze
	 */
	public ArrayList<Set> getSets() {
		return sets;
	}

	/**
	 * @return Minimales Intervall für die Serverabfrage in ms  
	 */
	public int getTimeoutServer() {
		return timeoutServer;
	}

	/**
	 * @return Maximale Zeit zur Berechnung eines Zuges in ms 
	 */
	public int getTimeoutDraw() {
		return timeoutDraw;
	}
	
	/**
	 * @return eigene Rolle: Konstante xRole|oRole
	 */
	public char getRole() {
		return role;
	}
	
	/**
	 * @return eigene Punkte
	 */
	public int getOwnPoints() {
		return ownPoints;
	}
	
	/**
	 * @return gegnerische Punkte
	 */
	public int getOppPoints() {
		return oppPoints;
	}
	
	/**
	 * @return Name des Gegners
	 */
	public String getOppName() {
		return oppName;
	}
	
	/**
	 * @return eigener Name
	 */
	public String getOwnName() {
		return ownName;
	}

	/**
	 * @return Pfad für Serverdateien
	 */
	public String getPath() {
		return path;
	}	
	
	/**
	 * @return ID/PrimaryKey 
	 */
	public int getID() {
		return ID;
	}

}



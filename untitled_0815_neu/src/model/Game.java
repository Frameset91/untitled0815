package model;
/**
 * Das Datenmodell "Game" beinhaltet die Spieleinstellungen und Sätze
 *  
 * @author Sascha Ulbrich 
 */

import java.util.*;
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
		sets = new ArrayList<Set>();
	}
	
	/**
	 * Methoden zum Erstellen und Hinzufügen eines weiteren Satzes zum Datenmodell
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
	public void addMove(char role, byte col){
		sets.get(sets.size()-1).addMove(role, col);
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
	
	//-------------------get set	
	/**
	 * @return Alle Sätze :ArrayList<Set>
	 */
	public ArrayList<Set> getSets() {
		return sets;
	}

	/**
	 * @return Minimales Intervall für die Serverabfrage in ms :Integer  
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
	 * @return Pfad für Serverdateien :String
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



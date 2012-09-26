import java.util.*;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Game {
//	public enum GameRole{
//		x,o
//	}
	
	private ArrayList<Set> sets;
	private int cols;
	private int rows;
	private SimpleStringProperty role;
	private SimpleIntegerProperty ownPoints;
	private SimpleIntegerProperty oppPoints;
	private SimpleStringProperty oppName;
	private SimpleStringProperty path;
	//private int numberOfSets;
	
	public Game(int cols, int rows, String role, String oppName){
		this.cols = cols;
		this.rows = rows;
		this.role.setValue(role);
		this.oppName.setValue(oppName);
		sets = new ArrayList<Set>();
		sets.add(new Set(cols,rows));
	}
	
	public Game(int cols, int rows){
		this.cols = cols;
		this.rows = rows;
//		this.role = 
		this.oppName = new SimpleStringProperty();
		sets = new ArrayList<Set>();
		sets.add(new Set(cols,rows));
	}
	
	public Set newSet(){
		Set set = new Set(cols, rows); 
		sets.add(set);
		return set;
	}
	
	public Set getLatestSet(){
		return sets.get(sets.size()-1);		
	}
	
	public void addMove(Move move){
		sets.get(sets.size()-1).addMove(move);
	}
	
	public void save(){
		//TODO: In Datenbank speichern (Primarykey = GameID), erzeugte GameID an Sets weitergeben 
		
		
		//alle Moves speichern 
		ListIterator<Set> iterator = sets.listIterator();
		while (iterator.hasNext())
		{
//		    iterator.next().save(gameID);
		}
	}
	//get set

	public SimpleStringProperty getRole() {
		return role;
	}

	public SimpleIntegerProperty getOwnPoints() {
		return ownPoints;
	}

	public SimpleIntegerProperty getOppPoints() {
		return oppPoints;
	}

	public SimpleStringProperty getOppName() {
		return oppName;
	}

	public SimpleStringProperty getPath() {
		return path;
	}
	
	
	

}



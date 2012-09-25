import java.util.*;


public class Game {
	public enum GameRole{
		x,o
	}
	
	private ArrayList<Set> sets;
	private int cols;
	private int rows;
	private GameRole role;
	private int ownPoints;
	private int oppPoints;
	private String oppName;
	//private int numberOfSets;
	
	public Game(int cols, int rows, GameRole role, String oppName){
		this.cols = cols;
		this.rows = rows;
		this.role = role;
		this.oppName = oppName;
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

}



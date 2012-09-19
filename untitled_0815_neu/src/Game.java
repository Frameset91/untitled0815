import java.util.*;


public class Game {
	public enum GameRole{
		x,o
	}
	
	private List<Set> sets;
	private int cols;
	private int rows;
	private GameRole role;
	private int ownPoints;
	private int oppPoints;
	//private int numberOfSets;
	
	public Game(int cols, int rows, GameRole role){
		this.cols = cols;
		this.rows = rows;
		this.role = role;
	}
	
	public void startSet(){
		sets.add(new Set(cols, rows));
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



import java.util.*;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


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
	private SimpleIntegerProperty timeoutServer;
	private SimpleIntegerProperty timeoutDraw;
	private SimpleStringProperty oppToken;
	private SimpleStringProperty ownToken;
	
	private int ID;
	
//	public Game(int cols, int rows, String role, String oppName){
//		this.cols = cols;
//		this.rows = rows;
//		this.role.setValue(role);
//		this.oppName.setValue(oppName);
//		sets = new ArrayList<Set>();
//		sets.add(new Set(cols,rows));
//	}
	
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
		//TODO: In Datenbank speichern (Primarykey = GameID), erzeugte GameID an Sets weitergeben, ID speichern
		
		
		//alle Moves speichern 
//		ListIterator<Set> iterator = sets.listIterator();
//		
//		while (iterator.hasNext())
//		{
////		    iterator.next().save(gameID);
//		}
	}
	//get set
	
	/**
	 * @return the oppToken
	 */
	public SimpleStringProperty getOppToken() {
		return oppToken;
	}

	/**
	 * @return the ownToken
	 */
	public SimpleStringProperty getOwnToken() {
		return ownToken;
	}

	/**
	 * @return the timeoutServer
	 */
	public SimpleIntegerProperty getTimeoutServer() {
		return timeoutServer;
	}

	/**
	 * @return the timeoutDraw
	 */
	public SimpleIntegerProperty getTimeoutDraw() {
		return timeoutDraw;
	}
	
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



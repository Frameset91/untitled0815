import java.util.*;
import java.sql.Timestamp;

import javafx.application.Application;


public class GameController implements GameEventListener{

	
	//private GameView gameView;
	private Game game;
	private CommunicationServer comServ;
	private Game.GameRole role;
	private GUI02 gui2;

	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		new GameController();
				

	}
	
	public GameController(){
		// TODO: Hauptmenü GUI erzeugen, Handler registrieren
		

		Application.launch(GUI02.class);
		
		comServ = CommunicationServer.getInstance();
		comServ.addEventListener(this);
	}
	
	private void newGame(int cols, int rows, Game.GameRole role, int sets){		
		game = new Game(cols, rows, role); 		
	}
	
	/* (non-Javadoc)
	 * @see GameEventListener#handleEvent(GameEvent)
	 */
	@Override
	public void handleEvent(GameEvent event) {
		//TODO: Behandlung der Events vom ComServ. 
		
	}
	
	private Move calculateNextMove(){
		
		//TODO: Algorithmus zur Bestimmung des nächsten Zuges
		
		return new Move(role, 1, new Timestamp(new Date().getTime())); 
	}
}

import java.util.*;
import java.sql.Timestamp;

import javafx.application.Application;


public class GameController implements GameEventListener{

	
	//private GameView gameView;
	private Game model;
	private CommunicationServer comServ;
	private Game.GameRole role;
	private GUI03 view;

	
	
	
	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		new GameController();	
//	}
	
	public GameController(GUI03 view){
		//initialisierung des Controllers
		this.view = view;
		
		//Registrierung für Events beim View
		//TODO
		
		//Weitere Objekte erzeugen und für Events registrieren
		
		//Communication Server
		comServ = CommunicationServer.getInstance();
		comServ.addEventListener(this);
		
		//KI 
		//TODO
		
	    //-------TEST--------
		//Spielstart
		newGame(Constants.gamefieldcolcount,Constants.gamefieldrowcount,Game.GameRole.x, "looserOpp");
		//Satz starten
		view.bindField(model.newSet().getField());
		//Moves ausführen
	    model.addMove(new Move(Game.GameRole.o, 1));
	    model.addMove(new Move(Game.GameRole.x, 1));
	}
	
	private void newGame(int cols, int rows, Game.GameRole role, String oppName){		
		model = new Game(cols, rows, role, oppName); 		
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

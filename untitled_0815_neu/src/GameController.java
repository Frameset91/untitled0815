import java.util.*;
import java.sql.Timestamp;

import javafx.application.Application;


public class GameController implements GameEventListener{

	
	//private GameView gameView;
	private Game model;
	private CommunicationServer comServ;
	private String role;
//	private GUI03 view;
	private IGameView view;


	
	
	
	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		new GameController();	
//	}
	
//	public GameController(GUI03 view){
		public GameController(IGameView view){
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
		newGame(Constants.gamefieldcolcount,Constants.gamefieldrowcount);
		//Satz starten
		view.bindField(model.newSet().getField());
		//Moves ausführen
	    model.addMove(new Move(Constants.oRole, 1));
	    model.addMove(new Move(Constants.xRole, 1));
	}
	
	private void newGame(int cols, int rows){		
//		model = new Game(cols, rows, role, oppName); 
		model = new Game(cols, rows); 
	}
	
	/* (non-Javadoc)
	 * @see GameEventListener#handleEvent(GameEvent)
	 */
	@Override
	public void handleEvent(GameEvent event) {
		//TODO: Behandlung der Events vom ComServ. 
		
	}
	
}

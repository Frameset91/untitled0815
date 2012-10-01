import java.util.*;

public class GameController implements GameEventListener, IUIEventListener{

	
	//private GameView gameView;
	private Game model;
	private CommunicationServer comServ;

	private IGameView view;


	
	
	
	public GameController(IGameView view){
		//initialisierung des Controllers
		this.view = view;
		
		//Game Objekt initialisieren und an das UI binden (zur Eingabe der Spieleinstellungen)
		newGame(Constants.gamefieldcolcount,Constants.gamefieldrowcount);
		
		//Registrierung für Events beim View
		this.view.addEventListener(this);
		
		//Weitere Objekte erzeugen und für Events registrieren
		
		//Communication Server
		comServ = CommunicationServer.getInstance();
		comServ.addEventListener(this);
		
		
		//KI 
		//TODO
		
	    //-------TEST--------
		//Spielstart
		
		
		//Satz starten
//		this.view.bindField(model.newSet().getField());
//		//Moves ausführen
//	    model.addMove(new Move(Constants.oRole, 1));
//	    model.addMove(new Move(Constants.xRole, 1));
	    
	    
	}
	
	private void newGame(int cols, int rows){		
		//unbind old model
		if(model != null){
			view.unbindField(model.getLatestSet().getField());
			view.unbindGame(model);
			model.save();
		}
		
		//create new model		
		model = new Game(cols, rows);
		
		//bind new model		
		view.bindGame(model);
	}
	
	/* (non-Javadoc)
	 * @see GameEventListener#handleEvent(GameEvent)
	 */
	@Override
	public void handleEvent(GameEvent event) {
		//TODO: Behandlung der Events vom ComServ. 
		
	}
	
	@Override
	public void handleEvent(UIEvent event) {
		switch (event.getType()) {
		case StartGame:
			model.save();			
			break;
		case StartSet:
			if(model.getLatestSet() != null){
				view.unbindField(model.getLatestSet().getField());
				model.save();
			}
			
			view.bindField(model.newSet().getField());
			//TODO ComServer starten
			
			
			//TEST
			//Moves ausführen
			Random r = new Random();
			
			for(int i = 0; i < 15; i++){
				if(i%2 == 1){
					model.addMove(new Move(Constants.oRole, r.nextInt(7)));
				}else{
					model.addMove(new Move(Constants.xRole, r.nextInt(7)));
				}
				
			}
		    
			break;
		case EndSet:
//			view.unbindField(model.getLatestSet().getField());
			model.save();
			break;
		case EndGame:
			
			break;
		default:
			break;
		}
		
	}
	
}

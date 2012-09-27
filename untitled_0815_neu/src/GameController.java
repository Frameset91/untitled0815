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
		this.view.bindGame(model);
		
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
		model = new Game(cols, rows); 
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
			view.bindField(model.newSet().getField());
			//TODO ComServer starten
			
			
			//TEST
			//Moves ausführen
		    model.addMove(new Move(Constants.oRole, 1));
		    model.addMove(new Move(Constants.xRole, 1));
			break;
		default:
			break;
		}
		
	}
	
}

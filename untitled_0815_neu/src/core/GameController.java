package core;

import java.util.Random;

import model.*;
import utilities.*;
import view.*;

import javafx.application.Application;
import javafx.stage.Stage;


public class GameController extends Application implements GameEventListener, IUIEventListener{

	
	//private GameView gameView;
	private Game model;
	private CommunicationServer comServ;

	private IGameView view;
	
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
		
		switch (event.getType()) {
			case StartGame:
				Log.getInstance().write("Controller: Event empfangen ( " + event.getType().toString() + " )");
				model.save();				
				break;			
			case StartSet:
				if(model.getLatestSet() != null){
					view.unbindField(model.getLatestSet().getField());
					model.save();
				}
				
				view.bindField(model.newSet().getField());
				//TODO ComServer starten
		//			comServ.enableReading(model.getTimeoutServer().getValue(), model.getPath().getValue());
				
				//TEST
				//Moves ausführen
				
				Random r = new Random();
				
				for(int i = 0; i < 15; i++){
					if(i%2 == 1){
						model.addMove(new Move(Constants.oRole, r.nextInt(7)));
					}else{
						model.addMove(new Move(Constants.xRole, r.nextInt(7)));
					}
		//				model.save();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		//		    model.getOppPoints().setValue(1);
				
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
		
	

	@Override
	public void handleEvent(UIEvent e) {
		// TODO Auto-generated method stub
//		System.out.println("GameEvent erhalten");
	}

	
	//App Part
	@Override 
	public void start (Stage mainstage) throws Exception{
		
		view = new MainGUI();
		
		view.init(mainstage);
		mainstage.setHeight(550);
		mainstage.setWidth(820);
		mainstage.setTitle("4 Gewinnt - untitled0815");
		mainstage.show();
		
		//Game Objekt initialisieren und an das UI binden (zur Eingabe der Spieleinstellungen)
		newGame(Constants.gamefieldcolcount,Constants.gamefieldrowcount);
		
	
		//Communication Server
		comServ = CommunicationServer.getInstance();
		
		//Dispatcher
		EventDispatcher Dispatcher = EventDispatcher.getInstance();
		try {
			Dispatcher.addListener(GameEvent.Type.StartGame.toString(), this);
			Dispatcher.addListener(GameEvent.Type.EndGame.toString(), this);
			Dispatcher.addListener(GameEvent.Type.EndSet.toString(), this);
			Dispatcher.addListener(GameEvent.Type.LoadGame.toString(), this);
			Dispatcher.addListener(GameEvent.Type.StartSet.toString(), this);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		//KI 
		//TODO
		
	    
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
		

}



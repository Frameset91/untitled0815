package core;

import java.net.URL;
import java.security.acl.Owner;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.ResourceBundle;

import model.*;
import utilities.*;
import view.*;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.stage.Stage;


public class GameController extends Application implements GameEventListener, Observer, Initializable{

	
	//private GameView gameView;
	private Game model;
	private CommunicationServer comServ;
	private KI ki;

	private IGameView view;
	
	
	//Properties für DataBinding
	private SimpleStringProperty role;
	private SimpleIntegerProperty ownPoints;
	private SimpleIntegerProperty oppPoints;
	private SimpleStringProperty oppName;
	private SimpleStringProperty path;
	private SimpleIntegerProperty timeoutServer;
	private SimpleIntegerProperty timeoutDraw;
	private SimpleStringProperty oppToken;
	private SimpleStringProperty ownToken;
	
	private SimpleStringProperty status;
	private SimpleStringProperty winner;
	
	private SimpleStringProperty[][] styleField;
	
	
	private void newGame(int cols, int rows){		
		//unbind old model
		if(model != null){			
			model.save();
		}
		
		//create new model		
		model = new Game(cols, rows, role.get().charAt(0), oppName.get(), path.get(), timeoutServer.get(), timeoutDraw.get());
		model.addObserver(this);
		
		if(role.get().charAt(0) == Constants.oRole){
			ownToken.setValue(Constants.oToken);
			oppToken.setValue(Constants.xToken);
		}else{
			ownToken.setValue(Constants.xToken);
			oppToken.setValue(Constants.oToken);
		}
		
		for(int i = 0; i < cols; i++){
			for(int j = 0; j< rows; j++){
				styleField[i][j].set(Constants.emptyToken);
			}
		}		
		ki = new KI(model);	
		model.save();			
	}
	
	/* (non-Javadoc)
	 * @see GameEventListener#handleEvent(GameEvent)
	 */
	
	
	@Override
	public void handleEvent(GameEvent event) {
		
		switch (event.getType()) {
			case StartGame:
				Log.getInstance().write("Controller: Event empfangen ( " + event.getType().toString() + " )");
				newGame(Constants.gamefieldcolcount, Constants.gamefieldrowcount);							
				break;			
			case StartSet:
				Log.getInstance().write("Controller: Event empfangen ( " + event.getType().toString() + " )");
				if(model.getLatestSet() != null){
					model.save();
				}
				
				model.newSet();
				
				//ComServer starten
				comServ.enableReading(model.getTimeoutServer(), model.getPath(), model.getRole());
				
				//TEST
				
				
				//Moves ausführen
				
//				Random r = new Random();
//				
//				for(int i = 0; i < 15; i++){
//					if(i%2 == 1){
//						model.addMove(new Move(Constants.oRole, r.nextInt(7)));
//					}else{
//						model.addMove(new Move(Constants.xRole, r.nextInt(7)));
//					}
//		//				model.save();
//					try {
//						Thread.sleep(100);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
		//		    model.getOppPoints().setValue(1);
				
				break;
			case EndSet:				
				comServ.disableReading();
				
				//zu Testzwecken
				char arg = Constants.oRole;
				if(event.getArg() != "") arg = event.getArg().charAt(0); 	
				
				Log.getInstance().write("Controller: Event empfangen ( " + event.getType().toString() + " )");
				model.getLatestSet().setWinner(arg);
				model.save();
				break;
			case EndGame:
				Log.getInstance().write("Controller: Event empfangen ( " + event.getType().toString() + " )");
				
				break;
				
			case OppMove:
				Log.getInstance().write("Controller: Event empfangen ( " + event.getType().toString() + " )");
//				comServ.disableReading();
				Move newMove;
				
				if (Integer.parseInt(event.getArg()) > -1){
					Move move;				
					if(model.getRole() == Constants.xRole)
						move = new Move(Constants.oRole, Integer.parseInt(event.getArg()));
					else
						move = new Move(Constants.xRole, Integer.parseInt(event.getArg()));
					model.addMove(move);
					newMove = ki.calculateNextMove(move);
				}else{
					newMove = ki.calculateNextMove();
				}
				
				//Zug auf Server schreiben und Server wieder überwachen
				comServ.writeMove((byte)newMove.getColumn(), model.getPath(), model.getRole());
				comServ.enableReading(model.getTimeoutServer(), model.getPath(), model.getRole());
				
				model.addMove(newMove);			
				
				break;				
			default:
				break;
			}
		}
		
	//Verarbeitung von Veränderungen im Model
	@Override
	public void update(Observable o, Object arg) {
		String changed = (String)arg;
		switch (changed) {
		case "winner":
			
			ownPoints.setValue(model.getOwnPoints());
			oppPoints.setValue(model.getOppPoints());
			Log.getInstance().write("Controller: Winner changed empfangen, Stand: " +ownPoints.get()+":"+oppPoints.get());
			break;
		case "status":
			status.set(model.getLatestSet().getStatus());
			break;
		case "sets":	
			for(int i = 0; i < Constants.gamefieldcolcount; i++){
				for(int j = 0; j< Constants.gamefieldrowcount; j++){
					if(styleField[i][j].get() != Constants.emptyToken)
						styleField[i][j].set(Constants.emptyToken); 
				}
			}
			break;
		case "field":
			Log.getInstance().write("Controller: Field changed empfangen");
			Boolean[][] boolField = model.getLatestSet().getField();
			for(int i = 0; i < Constants.gamefieldcolcount; i++){
				for(int j = 0; j< Constants.gamefieldrowcount; j++){
					String newStyle;
					if(boolField[i][j] == null)
						newStyle  = Constants.emptyToken;
					else if(boolField[i][j])
						newStyle = Constants.xToken;
					else
						newStyle = Constants.oToken;
					
					if(styleField[i][j].getValue() != newStyle) styleField[i][j].set(newStyle);
				}
			}
			break;
		default:
			break;
		}
		
	}

	//App Part
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Game Objekt initialisieren und an das UI binden (zur Eingabe der Spieleinstellungen)
//		newGame(Constants.gamefieldcolcount,Constants.gamefieldrowcount);
		
		//Variable Initialisierung
		styleField = new SimpleStringProperty[Constants.gamefieldcolcount][Constants.gamefieldrowcount];
		for(int i = 0; i < Constants.gamefieldcolcount; i++){
			for(int j = 0; j< Constants.gamefieldrowcount; j++){
				styleField[i][j] = new SimpleStringProperty(Constants.emptyToken); 
			}
		}
		
		role = new SimpleStringProperty();
		ownPoints = new SimpleIntegerProperty(0);
		oppPoints = new SimpleIntegerProperty(0);
		oppName = new SimpleStringProperty();
		path = new SimpleStringProperty();
		timeoutServer = new SimpleIntegerProperty(Constants.defaultTimeoutServer);
		timeoutDraw = new SimpleIntegerProperty(Constants.defaultTimeoutDraw);
		oppToken = new SimpleStringProperty(Constants.oToken);
		ownToken = new SimpleStringProperty(Constants.xToken);		
		status = new SimpleStringProperty();
		winner = new SimpleStringProperty();
			
		//Communication Server
		comServ = CommunicationServer.getInstance();
				
		//KI 
//		ki = new KI(model);		
		
		//Dispatcher
		EventDispatcher Dispatcher = EventDispatcher.getInstance();
		try {			
			Dispatcher.addListener(GameEvent.Type.StartGame.toString(), this);
			Dispatcher.addListener(GameEvent.Type.EndGame.toString(), this);
			Dispatcher.addListener(GameEvent.Type.EndSet.toString(), this);
			Dispatcher.addListener(GameEvent.Type.LoadGame.toString(), this);
			Dispatcher.addListener(GameEvent.Type.StartSet.toString(), this);
			Dispatcher.addListener(GameEvent.Type.OppMove.toString(), this);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
	}
	
	
	@Override 
	public void start (Stage mainstage) throws Exception{
		
		view = new MainGUI();
		initialize(null, null);
		view.init(mainstage, this);
		mainstage.setHeight(550);
		mainstage.setWidth(820);
		mainstage.setTitle("4 Gewinnt - untitled0815");
		mainstage.show();
		
		
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	
	//----- Getter für Properties
	/**
	 * @return Style für den gegnerischen Stein :StringProperty
	 */
	public SimpleStringProperty getOppToken() {
		return oppToken;
	}

	/**
	 * @return Style für den eigenen Stein :StringProperty
	 */
	public SimpleStringProperty getOwnToken() {
		return ownToken;
	}
	/**
	 * @return Minimales Intervall für die Serverabfrage in ms :IntegerProperty  
	 */
	public SimpleIntegerProperty getTimeoutServer() {
		return timeoutServer;
	}

	/**
	 * @return Maximale Zeit zur Berechnung eines Zuges in ms :IntegerProperty
	 */
	public SimpleIntegerProperty getTimeoutDraw() {
		return timeoutDraw;
	}
	
	/**
	 * @return eigene Rolle :StringProperty
	 */
	public SimpleStringProperty getRole() {
		return role;
	}
	
	/**
	 * @return eigene Punkte :IntegerProperty
	 */
	public SimpleIntegerProperty getOwnPoints() {
		return ownPoints;
	}
	
	/**
	 * @return gegnerische Punkte :IntegerProperty
	 */
	public SimpleIntegerProperty getOppPoints() {
		return oppPoints;
	}
	
	/**
	 * @return Name des Gegner :StringProperty
	 */
	public SimpleStringProperty getOppName() {
		return oppName;
	}

	/**
	 * @return Pfad für Serverdateien :StringProperty
	 */
	public SimpleStringProperty getPath() {
		return path;
	}	
	
	/**
	 * @return Status des Satzes :StringProperty
	 */
	public SimpleStringProperty getStatus() {
		return status;
	}

	/**
	 * @return Gewinner :String
	 */
	public SimpleStringProperty getWinner() {
		return winner;
	}

	/**
	 * @return Spielfeld :GameField
	 */
	public SimpleStringProperty[][] getStyleField() {
		return styleField;
	}
	

	//Main Method zum starten des Programms 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	
}
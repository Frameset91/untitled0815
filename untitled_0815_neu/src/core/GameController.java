package core;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import model.*;
import utilities.*;
import view.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.stage.Stage;


public class GameController extends Application implements GameEventListener, Observer, Initializable{

	private Game model;
	private CommunicationServer comServ;
	private KI ki;

	//Konstanten für Zugriff auf Property Array
	public final int ROLE_PROPERTY = 0;
	public final int OWNPOINTS_PROPERTY = 1;
	public final int OPPPOINTS_PROPERTY = 2;
	public final int OPPNAME_PROPERTY = 3;
	public final int PATH_PROPERTY = 4;
	public final int TIMEOUTSERVER_PROPERTY = 5;
	public final int TIMEOUTDRAW_PROPERTY = 6;
	public final int OPPTOKEN_PROPERTY = 7;
	public final int OWNTOKEN_PROPERTY = 8;	
	public final int STATUS_PROPERTY = 9;
	public final int WINNER_PROPERTY = 10;
	
	//Properties für DataBinding	
	private SimpleStringProperty[] properties;
	private SimpleStringProperty[][] styleField;
	private ObservableList<Log.LogEntry> logItems;
	
	//---------------------Verarbeitung von Events-----------------------------------------
	/* (non-Javadoc)
	 * @see GameEventListener#handleEvent(GameEvent)
	 */	
	/**
	 * Methode um auf GameEvents zu reagieren
	 * @param das geworfene Event :GameEvent
	 */	
	@Override
	public void handleEvent(GameEvent event) {
		
		switch (event.getType()) {
			case StartGame:
				Log.getInstance().write("Controller: Event empfangen ( " + event.getType().toString() + " ) FxThread:" + Platform.isFxApplicationThread());
				newGame(Constants.gamefieldcolcount, Constants.gamefieldrowcount);							
				break;			
			case StartSet:
				Log.getInstance().write("Controller: Event empfangen ( " + event.getType().toString() + " ) FxThread:" + Platform.isFxApplicationThread());
				if(model.getLatestSet() != null){
					model.save();
				}
				
				model.newSet();
				
				//ComServer starten
				comServ.enableReading(model.getTimeoutServer(), model.getPath(), model.getRole());
		
				break;
			case EndSet:	
				Log.getInstance().write("Controller: Event empfangen ( " + event.getType().toString() + " ) FxThread:" + Platform.isFxApplicationThread());
				comServ.disableReading();
				
				//zu Testzwecken
				char arg = Constants.oRole;
				if(event.getArg() != "") arg = event.getArg().charAt(0); 	
				
				model.getLatestSet().setWinner(arg);
				model.save();
				break;
			case EndGame:
				Log.getInstance().write("Controller: Event empfangen ( " + event.getType().toString() + " ) FxThread:" + Platform.isFxApplicationThread());
				
				break;
				
			case OppMove:
				Log.getInstance().write("Controller: Event empfangen ( " + event.getType().toString() + " ) FxThread:" + Platform.isFxApplicationThread());
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
	
	//Hilfsmethoden 
	
	/**
	 * Methode um ein neues Spiel zu starten
	 * @param Spaltenanzahl :Integer, Zeilenanzahl :Integer
	 */	
	private void newGame(int cols, int rows){		
		//unbind old model
		if(model != null){			
			model.save();
		}
		
		//create new model		
		model = new Game(cols, rows, properties[ROLE_PROPERTY].get().charAt(0), 
				properties[OPPNAME_PROPERTY].get(), 
				properties[PATH_PROPERTY].get(), 
				Integer.parseInt(properties[TIMEOUTSERVER_PROPERTY].get()), 
				Integer.parseInt(properties[TIMEOUTDRAW_PROPERTY].get()));
		model.addObserver(this);
		
		if(model.getRole() == Constants.oRole){
			properties[OWNTOKEN_PROPERTY].setValue(Constants.oToken);
			properties[OPPTOKEN_PROPERTY].setValue(Constants.xToken);
		}else{
			properties[OWNTOKEN_PROPERTY].setValue(Constants.xToken);
			properties[OPPTOKEN_PROPERTY].setValue(Constants.oToken);
		}
		
		for(int i = 0; i < cols; i++){
			for(int j = 0; j< rows; j++){
				styleField[i][j].set(Constants.emptyToken);
			}
		}		
		ki = new KI(model);	
		model.save();			
	}
		
	//---------------------Verarbeitung von Veränderungen im Model---------------------------------------------
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	/**
	 * Methode um auf Veränderungen im Datenmodell zu reagieren
	 * Als Argument wird der Name der Variable übergeben, die sich geändert hat
	 * 
	 * @param das Objekt das sich veränder hat :Observable, Argumente die mit übergeben werden :Object
	 */	
	@Override
	public void update(Observable o, Object arg) {
		String changed = (String)arg;
		switch (changed) {
		case "winner":
			Log.getInstance().write(
					"Controller: Winner changed empfangen, Stand: " +model.getOwnPoints()+":"+model.getOppPoints() 
					+ "; FxThread:" + Platform.isFxApplicationThread());
			
			//Sicherstellen, dass updates von TextProperties im UI Thread stattfinden
			Platform.runLater(new Runnable() {					
				@Override
				public void run() {
					properties[OWNPOINTS_PROPERTY].setValue(String.valueOf(model.getOwnPoints()));
					properties[OPPPOINTS_PROPERTY].setValue(String.valueOf(model.getOppPoints()));						
				}
			});				
			break;
		case "status":
			Log.getInstance().write("Controller: Status changed empfangen, FxThread:" + Platform.isFxApplicationThread());
			properties[STATUS_PROPERTY].set(model.getLatestSet().getStatus());
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
			Log.getInstance().write("Controller: Field changed empfangen; FxThread:" + Platform.isFxApplicationThread());
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
	
	//------------------- Getter für Properties -----------------------------------------------
	
	/**
	 * @return Properties für DataBinding mit UI:StringProperty
	 */
	public SimpleStringProperty[] properties() {
		return properties;
	}
	
	/**
	 * @return Spielfeld :GameField
	 */
	public SimpleStringProperty[][] styleField() {
		return styleField;
	}	
	
	/**
	 * @return Logeinträge :ObservableList<Log.LogEntry>
	 */
	public ObservableList<Log.LogEntry> logItems() {
		return logItems;
	}

	//---------------- Methoden zum starten und initialisieren des Programms -------------------
	
	/**
	 * 1. Main Methode zum Starten des Programms  
	 * @param Argumente :String[]
	 */	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * 2. start Method von Application, wird aufgerufen, nach dem durch launch ein JavaFX Programm aufgebaut wurde  
	 * @param Stage von JavaFX :Stage
	 */
	@Override 
	public void start (Stage mainstage) throws Exception{
		
		IGameView view = new MainGUI();
		initialize(null, null);
		view.init(mainstage, this);
		mainstage.setHeight(550);
		mainstage.setWidth(820);
		mainstage.setTitle("4 Gewinnt - untitled0815");
		mainstage.show();
		
		
	}	
	
	/**
	 * 3. Initialisierungs Methode die durch das laden der FXML in der Startmethode ausgelöst wird, nach dem das UI Konstrukt erstellt wurde
	 * @param TODO
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Property Initialisierung
		styleField = new SimpleStringProperty[Constants.gamefieldcolcount][Constants.gamefieldrowcount];
		for(int i = 0; i < Constants.gamefieldcolcount; i++){
			for(int j = 0; j< Constants.gamefieldrowcount; j++){
				styleField[i][j] = new SimpleStringProperty(Constants.emptyToken); 
			}
		}
		
		properties = new SimpleStringProperty[11];
		properties[ROLE_PROPERTY] = new SimpleStringProperty();
		properties[OWNPOINTS_PROPERTY] = new SimpleStringProperty("0");
		properties[OPPPOINTS_PROPERTY] = new SimpleStringProperty("0");
		properties[OPPNAME_PROPERTY] = new SimpleStringProperty();
		properties[PATH_PROPERTY] = new SimpleStringProperty();
		properties[TIMEOUTSERVER_PROPERTY] = new SimpleStringProperty(String.valueOf(Constants.defaultTimeoutServer));
		properties[TIMEOUTDRAW_PROPERTY] = new SimpleStringProperty(String.valueOf(Constants.defaultTimeoutDraw));
		properties[OPPTOKEN_PROPERTY] = new SimpleStringProperty(Constants.oToken);
		properties[OWNTOKEN_PROPERTY] = new SimpleStringProperty(Constants.xToken);		
		properties[STATUS_PROPERTY] = new SimpleStringProperty();
		properties[WINNER_PROPERTY] = new SimpleStringProperty();
		
		logItems = Log.getInstance().getLogEntries();
			
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
			Dispatcher.addListener(GameEvent.Type.OppMove.toString(), this);
			
		} catch (Exception e) {
			e.printStackTrace();
		}			
	}

	
}
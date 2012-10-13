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
	
	
	
	
//	private SimpleStringProperty role;
//	private SimpleStringProperty ownPoints;
//	private SimpleStringProperty oppPoints;
//	private SimpleStringProperty oppName;
//	private SimpleStringProperty path;
//	private SimpleStringProperty timeoutServer;
//	private SimpleStringProperty timeoutDraw;
//	private SimpleStringProperty oppToken;
//	private SimpleStringProperty ownToken;
//	
//	private SimpleStringProperty status;
//	private SimpleStringProperty winner;
	
	
	private SimpleStringProperty[] properties;
	private SimpleStringProperty[][] styleField;
	
	
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
	
	/* (non-Javadoc)
	 * @see GameEventListener#handleEvent(GameEvent)
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
		
	//Verarbeitung von Veränderungen im Model
	@Override
	public void update(Observable o, Object arg) {
		String changed = (String)arg;
		switch (changed) {
		case "winner":
			Log.getInstance().write("Controller: Winner changed empfangen, Stand: " +model.getOwnPoints()+":"+model.getOppPoints() + "; FxThread:" + Platform.isFxApplicationThread());
			
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
		
		//Property initialisierung
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
	public SimpleStringProperty[] properties() {
		return properties;
	}
	
//	/**
//	 * @return Style für den gegnerischen Stein :StringProperty
//	 */
//	public SimpleStringProperty getOppToken() {
//		return oppToken;
//	}
//
//	/**
//	 * @return Style für den eigenen Stein :StringProperty
//	 */
//	public SimpleStringProperty getOwnToken() {
//		return ownToken;
//	}
//	/**
//	 * @return Minimales Intervall für die Serverabfrage in ms :IntegerProperty  
//	 */
//	public SimpleStringProperty getTimeoutServer() {
//		return timeoutServer;
//	}
//
//	/**
//	 * @return Maximale Zeit zur Berechnung eines Zuges in ms :IntegerProperty
//	 */
//	public SimpleStringProperty getTimeoutDraw() {
//		return timeoutDraw;
//	}
//	
//	/**
//	 * @return eigene Rolle :StringProperty
//	 */
//	public SimpleStringProperty getRole() {
//		return role;
//	}
//	
//	/**
//	 * @return eigene Punkte :IntegerProperty
//	 */
//	public SimpleStringProperty getOwnPoints() {
//		return ownPoints;
//	}
//	
//	/**
//	 * @return gegnerische Punkte :IntegerProperty
//	 */
//	public SimpleStringProperty getOppPoints() {
//		return oppPoints;
//	}
//	
//	/**
//	 * @return Name des Gegner :StringProperty
//	 */
//	public SimpleStringProperty getOppName() {
//		return oppName;
//	}
//
//	/**
//	 * @return Pfad für Serverdateien :StringProperty
//	 */
//	public SimpleStringProperty getPath() {
//		return path;
//	}	
//	
//	/**
//	 * @return Status des Satzes :StringProperty
//	 */
//	public SimpleStringProperty getStatus() {
//		return status;
//	}
//
//	/**
//	 * @return Gewinner :String
//	 */
//	public SimpleStringProperty getWinner() {
//		return winner;
//	}

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
package testViews;

import java.net.URL;
import java.util.ResourceBundle;

import utilities.Log;
import utilities.Log.LogEntry;

import core.Constants;
import core.GameController;
import core.SetProperty;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
//import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FieldTestController implements Initializable {
	
	private GameController viewModel; 
	
	//Elemente die in der FXML definiert sind	
	//Bereiche
	@FXML
	private GridPane gameSettings;
	@FXML
	private VBox gameField;
	@FXML
	private VBox setSettings;
	@FXML
	private Button btnEndGame;
	@FXML
	private Button btnEndSet;
	
	//Men�
	@FXML
	private MenuItem menuSchlie�en; 
	@FXML
	private MenuItem menuAnleitung;
	
	//F�r Binding relevant
	@FXML
	private GridPane feld;
	@FXML
	private ChoiceBox<String> rolle;	
	@FXML
	private TextField timeoutAbfrage;
	@FXML
	private TextField timeoutZugzeit;
	@FXML
	private TextField gegnername;
	@FXML
	private TextField verzeichnispfad;
	@FXML
	private Label satzstatus;
	@FXML
	private Label punkteGegner;
	@FXML
	private Label punkteSpieler;	
	@FXML
	private Circle tokenGegner;
	@FXML
	private Circle tokenSpieler;
	@FXML
	private TableView<SetProperty> tableStatistic;
	@FXML
	private TableColumn<SetProperty, String> tableColumnSet;
	@FXML
	private TableColumn<SetProperty, String> tableColumnWinner;

	//Elemente die nicht im FXML definiert sind
	private TableView<LogEntry> logTabelle; 
	private ChoiceBox<String> winner;
	

	//Methode die f�r "Controller" vorgeschrieben ist und nach dem Aufbau des UI Konstrukts aufgerufen wird
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		viewModel = new GameController();
//		viewModel.initialize(null, null);
		
		logTabelle = new TableView<LogEntry>();
		
		
		//GridPane mit Circles f�llen
		Circle[][] spielfeld = new Circle[Constants.gamefieldcolcount][Constants.gamefieldrowcount];
		for (int i = 0; i < Constants.gamefieldcolcount; i++)
	    {
	      for (int j = 0; j < Constants.gamefieldrowcount; j++)
	      {
	        spielfeld[i][j] = new Circle(20.0f);
	        spielfeld[i][j].styleProperty().bind(viewModel.styleField()[i][Constants.gamefieldrowcount -1 -j]);
	        feld.add(spielfeld[i][j], i, j);
	      }
	    }
		
		//manuelles Spielen:
		spielfeld[0][0].setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {viewModel.oppMove((byte)0);}
		});
		spielfeld[1][0].setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {viewModel.oppMove((byte)1);}
		});
		spielfeld[2][0].setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {viewModel.oppMove((byte)2);}
		});
		spielfeld[3][0].setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {viewModel.oppMove((byte)3);}
		});
		spielfeld[4][0].setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {viewModel.oppMove((byte)4);}
		});
		spielfeld[5][0].setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {viewModel.oppMove((byte)5);}
		});
		spielfeld[6][0].setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {viewModel.oppMove((byte)6);}
		});
	
		//-------------------------------------- weiteres Binding------------------------------
			
		//Binding f�r Zustand
		viewModel.properties()[viewModel.STATE_PROPERTY].addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {	updateState(); }});
		
		//Rollen Auswahl
		rolle.valueProperty().bindBidirectional(viewModel.properties()[viewModel.ROLE_PROPERTY]);
		rolle.getItems().addAll(String.valueOf(Constants.xRole), String.valueOf(Constants.oRole));
		
		//Properties
		timeoutAbfrage.textProperty().bindBidirectional(viewModel.properties()[viewModel.TIMEOUTSERVER_PROPERTY]);
		timeoutZugzeit.textProperty().bindBidirectional(viewModel.properties()[viewModel.TIMEOUTDRAW_PROPERTY]);
		gegnername.textProperty().bindBidirectional(viewModel.properties()[viewModel.OPPNAME_PROPERTY]);
		verzeichnispfad.textProperty().bindBidirectional(viewModel.properties()[viewModel.PATH_PROPERTY]);
		punkteGegner.textProperty().bindBidirectional(viewModel.properties()[viewModel.OPPPOINTS_PROPERTY]);
		punkteSpieler.textProperty().bindBidirectional(viewModel.properties()[viewModel.OWNPOINTS_PROPERTY]);
		tokenGegner.styleProperty().bindBidirectional(viewModel.properties()[viewModel.OPPTOKEN_PROPERTY]);
		tokenSpieler.styleProperty().bindBidirectional(viewModel.properties()[viewModel.OWNTOKEN_PROPERTY]);		
		satzstatus.textProperty().bind(viewModel.properties()[viewModel.STATE_PROPERTY]);
		
		//Tabelle f�r die Logs
		TableColumn<Log.LogEntry, String> spalte1 = new TableColumn<Log.LogEntry, String>("Log-Eintrag");
		spalte1.setEditable(false);
		logTabelle.getColumns().clear();
		logTabelle.getColumns().add(spalte1);
		logTabelle.setMinWidth(384);
		spalte1.setCellValueFactory(
				new PropertyValueFactory<Log.LogEntry, String>("text"));
		logTabelle.setItems(viewModel.logItems());
		Log.getInstance().write("Binding fuer Log erstellt");
		
		tableColumnSet.setCellValueFactory(
				new PropertyValueFactory<SetProperty, String>("setNr"));
		tableColumnWinner.setCellValueFactory(
				new PropertyValueFactory<SetProperty, String>("winner"));
		tableStatistic.setItems(viewModel.sets());
		
		winner = new ChoiceBox<String>();
		winner.valueProperty().bindBidirectional(viewModel.properties()[viewModel.ROLE_PROPERTY]);
		winner.getItems().addAll(String.valueOf(Constants.xRole), String.valueOf(Constants.oRole));
		
		viewModel.initialize(null, null);
	 }
	
	private void updateState(){
		//UI bei Zustands�nderungen anpassen
		switch (viewModel.properties()[viewModel.STATE_PROPERTY].getValue()) {
		case Constants.STATE_APP_RUNNING:
			gameSettings.disableProperty().set(false);
			gameField.disableProperty().set(true);
			setSettings.disableProperty().set(true);
			btnEndSet.disableProperty().set(true);
			btnEndGame.disableProperty().set(true);
			break;
		case Constants.STATE_GAME_RUNNING:
			gameSettings.disableProperty().set(true);
			gameField.disableProperty().set(true);
			setSettings.disableProperty().set(false);
			btnEndSet.disableProperty().set(true);
			btnEndGame.disableProperty().set(false);
			break;
		case Constants.STATE_SET_RUNNING:
			gameSettings.disableProperty().set(true);
			gameField.disableProperty().set(false);
			setSettings.disableProperty().set(true);	
			btnEndSet.disableProperty().set(false);
			btnEndGame.disableProperty().set(true);
			break;
		case Constants.STATE_SET_ENDED:	
			gameSettings.disableProperty().set(true);
			gameField.disableProperty().set(false);
			setSettings.disableProperty().set(true);
			btnEndSet.disableProperty().set(true);
			btnEndGame.disableProperty().set(true);
			showConfirmWinner();
			break;
		default:
			
			break;
		}	
	}	
	
	private void showConfirmWinner() {
		final Stage stageConfirmWinner = new Stage();
		Group rootLog = new Group();
		Scene sceneLog = new Scene(rootLog, 484,500, Color.WHITESMOKE);
//		rootLog.getChildren().add(logTabelle);
		stageConfirmWinner.setScene(sceneLog);
		stageConfirmWinner.centerOnScreen();
		stageConfirmWinner.show();
						
	//Inhalt
		Text ueberschrift = new Text(20, 20,"Der Satz wurde beendet, bitte den Gewinner best�tigen:");
		Button close = new Button("Best�tigen");
		close.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent close){
				stageConfirmWinner.close();
				viewModel.confirmSetWinner();
			}
		});
		//Anordnen
		VBox Logs = new VBox(20);
		Logs.getChildren().addAll(ueberschrift, winner, close);
		Logs.setLayoutX(50);
		rootLog.getChildren().add(Logs);
		
	}

	//----------------------- Methoden zum handeln von UI Input ----------------------------
	//Spiel starten gedr�ckt
	@FXML
	public void handleStartGame(MouseEvent e){
		if (rolle.getValue() != null && gegnername.getText() != null && verzeichnispfad.getText() != null){
			viewModel.startGame();
		}
		else{
			final Stage stage = new Stage();
			Group rootEinstellungen = new Group();
			Scene scene = new Scene(rootEinstellungen, 250, 80, Color.WHITESMOKE);
			stage.setScene(scene);
			stage.centerOnScreen();
			Text text = new Text(20,40, "Bitte alle Spieleinstellungen definieren!");
			rootEinstellungen.getChildren().add(text);
			stage.show();
		}
	}
	//Spiel beenden gedr�ckt
	@FXML
	public void handleEndGame(MouseEvent e){
		viewModel.endGame();
	}
	//Satz starten gedr�ckt
	@FXML
	public void handleStartSet(MouseEvent e){
		viewModel.startSet();
	}
	//Satz beenden gedr�ckt
	@FXML
	public void handleEndSet(MouseEvent e){
		viewModel.endSet((byte) -1);		
	}
	
	//Spiel laden
	@FXML
	public void handleLoadGame(MouseEvent e){
//		viewModel.loadGame(gameID);	
	}
	
	@FXML
    // Men�: Schlie�en des Programms
	public void handleSchliessen(ActionEvent close){System.exit(0);}
	@FXML
	// Men�: Spielanleitung aufrufen
	public void handleAnleitung(ActionEvent anleitung){
		//Fenster mit Anleitung �ffnen
		final Stage stageAnleitung = new Stage();
		Group rootAnleitung = new Group();
		Scene sceneAnleitung = new Scene(rootAnleitung, 400,400, Color.WHITESMOKE);
		stageAnleitung.setScene(sceneAnleitung);
		stageAnleitung.centerOnScreen();
		stageAnleitung.show();
		//Inhalt
		Text ueberschrift = new Text(20, 20,"\"4 Gewinnt\"");
		ueberschrift.setFill(Color.BLACK);
		ueberschrift.setEffect(new Lighting());
		ueberschrift.setFont(Font.font(Font.getDefault().getFamily(), 20));
		Label text = new Label("Ententententententententententententente");
		Button close = new Button("Schlie�en");
		close.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent close){
				stageAnleitung.close();
			}});
		VBox textUndButton = new VBox(100);
		textUndButton.getChildren().addAll(ueberschrift,text, close);
		rootAnleitung.getChildren().add(textUndButton);
	}
	@FXML
	// Einstellungen: Timeouts hoch/ runter setzen
	public void handleHoch1(MouseEvent arg0){
		int timeoutFileabfruf;
		timeoutFileabfruf = Integer.parseInt(timeoutAbfrage.getText());
		timeoutFileabfruf = timeoutFileabfruf + 25;
		String zeitz = String.valueOf(timeoutFileabfruf);
		timeoutAbfrage.setText(zeitz);
	}
	@FXML
	public void handleRunter1(MouseEvent arg0){
		int timeoutFileabruf;
		timeoutFileabruf = Integer.parseInt(timeoutAbfrage.getText());
		timeoutFileabruf = timeoutFileabruf - 25;
		String zeitz = String.valueOf(timeoutFileabruf);
		timeoutAbfrage.setText(zeitz);
	}
	@FXML
	public void handleHoch2(MouseEvent arg0){
		int timeoutFileabfruf;
		timeoutFileabfruf = Integer.parseInt(timeoutZugzeit.getText());
		timeoutFileabfruf = timeoutFileabfruf + 100;
		String zeitz = String.valueOf(timeoutFileabfruf);
		timeoutZugzeit.setText(zeitz);
	}
	@FXML
	public void handleRunter2(MouseEvent arg0){
		int timeoutFileabruf;
		timeoutFileabruf = Integer.parseInt(timeoutZugzeit.getText());
		timeoutFileabruf = timeoutFileabruf - 100;
		String zeitz = String.valueOf(timeoutFileabruf);
		timeoutZugzeit.setText(zeitz);
	}
	// Log anzeigen
	@FXML
	public void handleLogAnzeigen(MouseEvent arg0){
		//Fenster mit Log �ffnen
		final Stage stageAnleitung = new Stage();
		Group rootLog = new Group();
		Scene sceneLog = new Scene(rootLog, 484,500, Color.WHITESMOKE);
		rootLog.getChildren().add(logTabelle);
		stageAnleitung.setScene(sceneLog);
		stageAnleitung.centerOnScreen();
		stageAnleitung.show();
						
	//Inhalt
		Text ueberschrift = new Text(20, 20,"Log");
		Button close = new Button("Schlie�en");
		close.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent close){
				stageAnleitung.close();
			}
		});
		//Anordnen
		VBox Logs = new VBox(20);
		Logs.getChildren().addAll(ueberschrift, logTabelle, close);
		Logs.setLayoutX(50);
		rootLog.getChildren().add(Logs);
	}
}

package view;

import java.net.URL;
import java.util.ResourceBundle;

import utilities.Log;
import utilities.Log.LogEntry;

import core.Constants;
import core.GameController;
import core.GameProperty;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainUIController implements Initializable{
	
	private GameController viewModel; 
	
	//Elemente die in der FXML definiert sind	
	//Bereiche
	@FXML
	private GridPane gameSettings;
	@FXML
	private VBox gameField;
	@FXML
	private Button btnNewSet;
	@FXML
	private Button btnEndGame;
	@FXML
	private Button btnEndSet;
	
	//Men�
	@FXML
	private MenuItem menuSchliessen; 
	@FXML
	private MenuItem menuSteuerung;
	@FXML
	private MenuItem logAnzeigen;
	
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
	private Button timeoutHochZugzeit;
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
	private Label tokenGegner;
	@FXML
	private Label tokenSpieler;
	@FXML
	private TableView<SetProperty> tableStatistic;
	@FXML
	private TableColumn<SetProperty, String> tableColumnSet;
	@FXML
	private TableColumn<SetProperty, String> tableColumnWinner;
	@FXML
	private Button btnNextMove;

	//Elemente die nicht im FXML definiert sind
	private TableView<LogEntry> logTabelle; 
	private ChoiceBox<String> winner;
	private TableView<GameProperty> savedGamesTable; 

	//Methode die f�r "Controller" vorgeschrieben ist und nach dem Aufbau des UI Konstrukts aufgerufen wird
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		viewModel = new GameController();
//		viewModel.initialize(null, null);
		
				
		//GridPane mit Circles f�llen
//		Circle[][] spielfeld = new Circle[Constants.gamefieldcolcount][Constants.gamefieldrowcount];
//		for (int i = 0; i < Constants.gamefieldcolcount; i++)
//	    {
//	      for (int j = 0; j < Constants.gamefieldrowcount; j++)
//	      {
//	        spielfeld[i][j] = new Circle(20.0f);
//	        spielfeld[i][j].styleProperty().bind(viewModel.styleField()[i][Constants.gamefieldrowcount -1 -j]);
//	        feld.add(spielfeld[i][j], i, j);
//	      }
//	    }
//		
		feld.minHeightProperty().set(265);
		feld.minWidthProperty().set(308);
		Label[][] spielfeld = new Label[Constants.gamefieldcolcount][Constants.gamefieldrowcount];
		for (int i = 0; i < Constants.gamefieldcolcount; i++)
	    {
	      for (int j = 0; j < Constants.gamefieldrowcount; j++)
	      {
	        spielfeld[i][j] = new Label(" ");
            spielfeld[i][j].prefHeightProperty().set(40);
	        spielfeld[i][j].prefWidthProperty().set(40); 
	        spielfeld[i][j].styleProperty().bindBidirectional(viewModel.field()[i][Constants.gamefieldrowcount -1 -j], new StyleConverter());
	        spielfeld[i][j].textProperty().bindBidirectional(viewModel.field()[i][Constants.gamefieldrowcount -1 -j]);
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
		
		btnNextMove.visibleProperty().bind(viewModel.isReplay());
		
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
		tokenGegner.styleProperty().bindBidirectional(viewModel.properties()[viewModel.OPPTOKEN_PROPERTY], new StyleConverter());
		tokenGegner.textProperty().bindBidirectional(viewModel.properties()[viewModel.OPPTOKEN_PROPERTY]);
		tokenSpieler.styleProperty().bindBidirectional(viewModel.properties()[viewModel.OWNTOKEN_PROPERTY], new StyleConverter());		
		tokenSpieler.textProperty().bindBidirectional(viewModel.properties()[viewModel.OWNTOKEN_PROPERTY]);
		satzstatus.textProperty().bind(viewModel.properties()[viewModel.STATE_PROPERTY]);
		
		//Tabelle f�r die Logs
		logTabelle = new TableView<LogEntry>();
		TableColumn<Log.LogEntry, String> spalte1 = new TableColumn<Log.LogEntry, String>("Log-Eintrag");
		spalte1.setEditable(false);
		spalte1.prefWidthProperty().bind(logTabelle.widthProperty().subtract(2));
		logTabelle.getColumns().clear();
		logTabelle.getColumns().add(spalte1);
		logTabelle.setMinWidth(384);
		spalte1.setCellValueFactory(
				new PropertyValueFactory<Log.LogEntry, String>("text"));
		logTabelle.setItems(Log.getInstance().getLogEntries());
		Log.getInstance().write("Binding fuer Log erstellt");
		
		//Tabelle f�r die gespeicherten Spiele
		savedGamesTable = new TableView<GameProperty>();
		savedGamesTable.getColumns().clear();
			//Erste Spalte
		TableColumn<GameProperty, String> c1 = new TableColumn<GameProperty, String>("Spiel Nr.");
		c1.setEditable(false);
		c1.prefWidthProperty().bind(savedGamesTable.widthProperty().subtract(2).divide(2));

		savedGamesTable.getColumns().add(c1);
		savedGamesTable.setMinWidth(100);
		c1.setCellValueFactory(
				new PropertyValueFactory<GameProperty, String>("gameID"));
			//Zweite Spalte
		TableColumn<GameProperty, String> c2 = new TableColumn<GameProperty, String>("Gegner");
		c2.setEditable(false);
		c2.prefWidthProperty().bind(savedGamesTable.widthProperty().subtract(2).divide(2));
		savedGamesTable.getColumns().add(c2);
		savedGamesTable.setMinWidth(100);
		c2.setCellValueFactory(
				new PropertyValueFactory<GameProperty, String>("oppName"));
		savedGamesTable.setItems(viewModel.savedGames());
				
		//Tabelle f�r Sets
		tableColumnSet.setCellValueFactory(
				new PropertyValueFactory<SetProperty, String>("setNr"));
		tableColumnWinner.setCellValueFactory(
				new PropertyValueFactory<SetProperty, String>("winner"));
		tableStatistic.setItems(viewModel.sets());
		
		winner = new ChoiceBox<String>();
		winner.valueProperty().bindBidirectional(viewModel.properties()[viewModel.WINNER_PROPERTY]);
		winner.getItems().addAll(String.valueOf(Constants.noRole), String.valueOf(Constants.xRole), String.valueOf(Constants.oRole));
		
		viewModel.initialize();
	 }
	
	private void updateState(){
		//UI bei Zustands�nderungen anpassen
		switch (viewModel.properties()[viewModel.STATE_PROPERTY].getValue()) {
		case Constants.STATE_APP_RUNNING:
			menuSchliessen.disableProperty().set(false);
			gameSettings.disableProperty().set(false);
			gameField.disableProperty().set(true);
			btnNewSet.disableProperty().set(true);
			btnEndSet.disableProperty().set(true);
			btnEndGame.disableProperty().set(true);
			break;
		case Constants.STATE_GAME_RUNNING:
			menuSchliessen.disableProperty().set(true);
			gameSettings.disableProperty().set(true);
			gameField.disableProperty().set(true);
			btnNewSet.disableProperty().set(false);
			btnEndSet.disableProperty().set(true);
			btnEndGame.disableProperty().set(false);
			//break;
		case Constants.STATE_SET_RUNNING:
			gameSettings.disableProperty().set(true);
			gameField.disableProperty().set(false);
			btnNewSet.disableProperty().set(true);	
			btnEndSet.disableProperty().set(false);
			btnEndGame.disableProperty().set(true);
			//break;
		case Constants.STATE_SET_ENDED:	
			gameSettings.disableProperty().set(true);
			gameField.disableProperty().set(false);
			btnNewSet.disableProperty().set(true);
			btnEndSet.disableProperty().set(true);
			btnEndGame.disableProperty().set(true);
			if(!viewModel.isReplay().get()) showConfirmWinner();
			break;
		default:
			if(viewModel.isReplay().get()){
				gameSettings.disableProperty().set(true);
				gameField.disableProperty().set(false);
				btnNewSet.disableProperty().set(true);	
				btnEndSet.disableProperty().set(true);
				btnEndGame.disableProperty().set(true);
			}
			break;
		}	
	}	
	
	private void showConfirmWinner() {
		final Stage stageConfirmWinner = new Stage();
		Group rootLog = new Group();
		Scene sceneLog = new Scene(rootLog, 500,180, Color.WHITESMOKE);
		stageConfirmWinner.setScene(sceneLog);
		stageConfirmWinner.centerOnScreen();
		stageConfirmWinner.show();
						
	//Inhalt
		Text ueberschrift = new Text(20, 20,"Der Satz wurde beendet, bitte den Gewinner best�tigen oder den Satz verwerfen:");
		Button confirm = new Button("Best�tigen");		
		confirm.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent close){
				stageConfirmWinner.close();
				viewModel.confirmSetWinner();
			}
		});
		Button discard = new Button("Verwerfen");
		discard.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent close){
				stageConfirmWinner.close();
				viewModel.discardSet();
			}
		});
		//Anordnen
		VBox Logs = new VBox(20);
		Logs.getChildren().addAll(ueberschrift, winner, confirm, discard);
		Logs.setLayoutX(50);
		rootLog.getChildren().add(Logs);
		
	}

	//----------------------- Methoden zum handeln von UI Input ----------------------------
	@FXML
	private void handleNextMove(MouseEvent e){
		viewModel.loadNextMove();
	}
	
	
	//Spiel starten gedr�ckt
	@FXML
	private void handleStartGame(MouseEvent e){
		if (rolle.getValue() != null && gegnername.getText() != null && verzeichnispfad.getText() != null){
			viewModel.startGame();
		}
		else{
			final Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			Group rootEinstellungen = new Group();
			Scene scene = new Scene(rootEinstellungen, 250, 80, Color.WHITESMOKE);
			stage.setScene(scene);
			stage.centerOnScreen();
			Text text = new Text(20,40, "Bitte alle Spieleinstellungen definieren!");
			Button button = new Button("OK");
			button.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent close){
					stage.close();
				}
			});
			VBox vbox = new VBox(10);
			vbox.getChildren().addAll(text, button);
			rootEinstellungen.getChildren().add(vbox);
			stage.show();
		}
	}
	//Spiel beenden gedr�ckt
	@FXML
	private void handleEndGame(MouseEvent e){
		viewModel.endGame();
	}
	//Satz starten gedr�ckt
	@FXML
	private void handleStartSet(MouseEvent e){
		viewModel.startSet();
	}
	//Satz beenden gedr�ckt
	@FXML
	private void handleEndSet(MouseEvent e){
		viewModel.endSet((byte) -1);		
	}
	
	@FXML
	private void handleLoadGame(ActionEvent e){
//		viewModel.loadGame(gameID);	
		//Fenster mit gespeicherten Spielen �ffnen
		final Stage stageLoad = new Stage();
		stageLoad.initModality(Modality.APPLICATION_MODAL);
		Group rootLoad = new Group();
		Scene sceneLog = new Scene(rootLoad, 500,600, Color.WHITESMOKE);
//		rootLog.getChildren().add(logTabelle);
		stageLoad.setScene(sceneLog);
		stageLoad.centerOnScreen();
		stageLoad.show();
						
	//Inhalt
		Text ueberschrift = new Text(20, 20,"W�hlen Sie ein gespeichertes Spiel aus:");
		Button close = new Button("Abbrechen");
		close.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent close){
				stageLoad.close();
			}
		});
		
		Button load = new Button("Laden");
		load.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent close){
				String gameID = savedGamesTable.getSelectionModel().getSelectedItem().getGameID();
				stageLoad.close();
				viewModel.loadGame(Integer.parseInt(gameID));
			}
		});
	
		//Anordnen
		VBox Loads = new VBox(20);
		/**
		 * @TODO Tabelle der gespeicherten Spiele einbinden
		 */
		savedGamesTable.prefWidthProperty().bind(sceneLog.widthProperty().subtract(100));
		Loads.getChildren().addAll(ueberschrift, savedGamesTable, close, load);
		Loads.setLayoutX(50);
		rootLoad.getChildren().add(Loads);
		savedGamesTable.getSelectionModel().selectFirst();
	}
	
	@FXML
    // Men�: Schlie�en des Programms
	private void handleSchliessen(ActionEvent close){System.exit(0);}
	@FXML
	// Men�: Spielsteuerung aufrufen
	private void handleSteuerung(ActionEvent steuerung){
		//Fenster mit Steuerung �ffnen
		final Stage stageSteuerung = new Stage();
		Group rootSteuerung = new Group();
		Scene sceneSteuerung = new Scene(rootSteuerung, 400,400, Color.WHITESMOKE);
		stageSteuerung.setScene(sceneSteuerung);
		stageSteuerung.centerOnScreen();
		stageSteuerung.show();
		//Inhalt
		Text ueberschrift = new Text(20, 20,"\"4 Gewinnt\"");
		ueberschrift.setFill(Color.BLACK);
		ueberschrift.setEffect(new Lighting());
		ueberschrift.setFont(Font.font(Font.getDefault().getFamily(), 20));
		Label text = new Label("Ententententententententententententente");
		Button close = new Button("Schlie�en");
		close.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent close){
				stageSteuerung.close();
			}});
		VBox textUndButton = new VBox(100);
		textUndButton.getChildren().addAll(ueberschrift,text, close);
		rootSteuerung.getChildren().add(textUndButton);
	}
	
	// Einstellungen: Timeouts hoch/ runter setzen
	@FXML
	private void handleHoch1(MouseEvent arg0){
		int timeoutFileabfruf;
		timeoutFileabfruf = Integer.parseInt(timeoutAbfrage.getText());
		timeoutFileabfruf = timeoutFileabfruf + 25;
		String zeitz = String.valueOf(timeoutFileabfruf);
		timeoutAbfrage.setText(zeitz);
	}
	@FXML
	private void handleRunter1(MouseEvent arg0){
		int timeoutFileabruf;
		timeoutFileabruf = Integer.parseInt(timeoutAbfrage.getText());
		timeoutFileabruf = timeoutFileabruf - 25;
		String zeitz = String.valueOf(timeoutFileabruf);
		timeoutAbfrage.setText(zeitz);
	}
	@FXML
	private void handleHoch2(MouseEvent arg0){
		int timeoutFileabfruf;
		timeoutFileabfruf = Integer.parseInt(timeoutZugzeit.getText());
		timeoutFileabfruf = timeoutFileabfruf + 100;
		String zeitz = String.valueOf(timeoutFileabfruf);
		timeoutZugzeit.setText(zeitz);
		System.out.println(timeoutHochZugzeit.getWidth()+"h�he: "+ timeoutHochZugzeit.getHeight());
	}
	@FXML
	private void handleRunter2(MouseEvent arg0){
		int timeoutFileabruf;
		timeoutFileabruf = Integer.parseInt(timeoutZugzeit.getText());
		timeoutFileabruf = timeoutFileabruf - 100;
		String zeitz = String.valueOf(timeoutFileabruf);
		timeoutZugzeit.setText(zeitz);
	}
	// Log anzeigen
	@FXML
	private void handleLogAnzeigen(ActionEvent log){
		//Fenster mit Log �ffnen
		final Stage stageLog = new Stage();
		Group rootLog = new Group();
		Scene sceneLog = new Scene(rootLog, 500,500, Color.WHITESMOKE);
		stageLog.setScene(sceneLog);
		stageLog.centerOnScreen();
		stageLog.show();
						
	//Inhalt
		Text ueberschrift = new Text(20, 20,"Log");
		Button close = new Button("Schlie�en");
		close.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent close){
				stageLog.close();
			}
		});
		//Anordnen
		VBox Logs = new VBox(20);
		logTabelle.prefWidthProperty().bind(sceneLog.widthProperty().subtract(100));
		Logs.getChildren().addAll(ueberschrift, logTabelle, close);
		Logs.setLayoutX(50);
		rootLog.getChildren().add(Logs);
	}
}

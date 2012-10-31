package view;

import java.io.File;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

public class MainUIController implements Initializable{
	
	private GameController viewModel; 
	
	//Elemente die in der FXML definiert sind	
	//Bereiche
	@FXML
	private BorderPane borderPane;
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
	
	//Menü
	@FXML
	private MenuItem menuSpielStarten;
	@FXML
	private MenuItem menuSchliessen; 
	@FXML
	private MenuItem menuSpielLaden;
	@FXML
	private MenuItem menuSteuerung;
	@FXML
	private MenuItem logAnzeigen;
	@FXML
	private MenuItem menuSpielBeenden;
	
	//Für Binding relevant
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
	private TextField oppName;
	@FXML
	private TextField ownName;
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
	@FXML
	private Button btnRemoveMove;
	@FXML
	private CheckBox cbWithoutServer;
	@FXML
	private Button btnDirectory;
	@FXML
	private HBox boxTimeoutDraw;
	@FXML
	private HBox boxTimeoutServer;
	@FXML
	private HBox boxDirectory;
	@FXML
	private HBox boxColButtons;
	@FXML
	private Button btnLoadGame;
	@FXML
	private Label labelName;
	@FXML
	private Label labelOpp;


	//Elemente die nicht im FXML definiert sind
	private TableView<LogEntry> logTabelle; 
	private ChoiceBox<String> winner;
	private TableView<GameProperty> savedGamesTable; 

	//Methode die für "Controller" vorgeschrieben ist und nach dem Aufbau des UI Konstrukts aufgerufen wird
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		viewModel = new GameController();
	
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
	        spielfeld[i][j].getStyleClass().add("token");
	        spielfeld[i][j].styleProperty().bindBidirectional(viewModel.field()[i][Constants.gamefieldrowcount -1 -j], new StyleConverter());
	        spielfeld[i][j].textProperty().bindBidirectional(viewModel.field()[i][Constants.gamefieldrowcount -1 -j], new TokenRoleConverter());
	        feld.add(spielfeld[i][j], i, j);
	      }
	    }
	
		//-------------------------------------- weiteres Binding------------------------------
			
		//Binding für Zustand
		viewModel.properties()[viewModel.STATE_PROPERTY].addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {	updateState(); }});
		
		btnNextMove.visibleProperty().bind(viewModel.isReplay());
		btnRemoveMove.visibleProperty().bind(viewModel.isReplay());
		
		//Menü-Einträge an Buttons binden
		menuSpielStarten.disableProperty().bind(gameSettings.disableProperty());
		menuSpielBeenden.disableProperty().bind(btnEndGame.disableProperty());
		menuSpielLaden.disableProperty().bind(gameSettings.disableProperty());
		menuSchliessen.disableProperty().bind(gameSettings.disabledProperty());
		
		//Rollen Auswahl
//		rolle.valueProperty().bindBidirectional(viewModel.properties()[viewModel.ROLE_PROPERTY]);
		rolle.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				updateRole();						
			}		
		});
		rolle.getItems().addAll(String.valueOf(Constants.xRole), String.valueOf(Constants.oRole));
		
		labelOpp.textProperty().bind(oppName.textProperty());
		
		//manuelles Spiel ohne Server oder gegen Server?
		cbWithoutServer.selectedProperty().bindBidirectional(viewModel.isWithoutServer());
		boxDirectory.disableProperty().bind(cbWithoutServer.selectedProperty());
//		boxTimeoutDraw.disableProperty().bind(cbWithoutServer.selectedProperty());
		boxTimeoutServer.disableProperty().bind(cbWithoutServer.selectedProperty());
		boxColButtons.disableProperty().bind(cbWithoutServer.selectedProperty().not());
		cbWithoutServer.onActionProperty().set(new EventHandler<ActionEvent>() {			
			@Override
			public void handle(ActionEvent arg0) {
				updateRole();
				
				if(cbWithoutServer.selectedProperty().get()){
					labelName.setText("Name: ");
					
				}else{
					labelName.setText("Gegner: ");
				}
				
			}
		});		
		
		//nur laden wenn DB verfügbar
		btnLoadGame.disableProperty().bind(viewModel.isDBAvailable().not());
		
		//Properties
		timeoutAbfrage.textProperty().bindBidirectional(viewModel.properties()[viewModel.TIMEOUTSERVER_PROPERTY]);
		timeoutZugzeit.textProperty().bindBidirectional(viewModel.properties()[viewModel.TIMEOUTDRAW_PROPERTY]);
		oppName.textProperty().bindBidirectional(viewModel.properties()[viewModel.OPPNAME_PROPERTY]);
		ownName.textProperty().bindBidirectional(viewModel.properties()[viewModel.OWNNAME_PROPERTY]);
		verzeichnispfad.textProperty().bindBidirectional(viewModel.properties()[viewModel.PATH_PROPERTY]);
		punkteGegner.textProperty().bindBidirectional(viewModel.properties()[viewModel.OPPPOINTS_PROPERTY]);
		punkteSpieler.textProperty().bindBidirectional(viewModel.properties()[viewModel.OWNPOINTS_PROPERTY]);
		tokenGegner.styleProperty().bindBidirectional(viewModel.properties()[viewModel.OPPTOKEN_PROPERTY], new StyleConverter());
		tokenGegner.textProperty().bindBidirectional(viewModel.properties()[viewModel.OPPTOKEN_PROPERTY]);
		tokenSpieler.styleProperty().bindBidirectional(viewModel.properties()[viewModel.OWNTOKEN_PROPERTY], new StyleConverter());		
		tokenSpieler.textProperty().bindBidirectional(viewModel.properties()[viewModel.OWNTOKEN_PROPERTY]);
		satzstatus.textProperty().bind(viewModel.properties()[viewModel.STATE_PROPERTY]);
		
		//Tabelle für die Logs
		logTabelle = new TableView<LogEntry>();
		TableColumn<Log.LogEntry, String> spalte1 = new TableColumn<Log.LogEntry, String>("Log-Eintrag");
		spalte1.setEditable(false);
		spalte1.prefWidthProperty().bind(logTabelle.widthProperty().subtract(2));
		logTabelle.getColumns().clear();
		logTabelle.getColumns().add(spalte1);
		logTabelle.setMinWidth(384);
		spalte1.setCellValueFactory(
				new PropertyValueFactory<Log.LogEntry, String>("text"));
		logTabelle.setItems(viewModel.logEntries());
		Log.getInstance().write("Binding fuer Log erstellt");
		
		//Tabelle für die gespeicherten Spiele
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
				
		//Tabelle für Sets
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
		//UI bei Zustandsänderungen anpassen
		switch (viewModel.properties()[viewModel.STATE_PROPERTY].getValue()) {
		case Constants.STATE_APP_RUNNING:
			gameSettings.disableProperty().set(false);
			gameField.disableProperty().set(true);
			btnNewSet.disableProperty().set(true);
			btnEndSet.disableProperty().set(true);
			btnEndGame.disableProperty().set(true);
			break;
		case Constants.STATE_SET_ENDED:	
			gameSettings.disableProperty().set(true);
			gameField.disableProperty().set(false);
			btnNewSet.disableProperty().set(true);
			btnEndSet.disableProperty().set(true);
			btnEndGame.disableProperty().set(true);
			if(!viewModel.isReplay().get()) showConfirmWinner();
			break;
		case Constants.STATE_SET_RUNNING:
			gameSettings.disableProperty().set(true);
			gameField.disableProperty().set(false);
			btnNewSet.disableProperty().set(true);	
			btnEndSet.disableProperty().set(false);
			btnEndGame.disableProperty().set(true);
			if(!viewModel.isReplay().get()) break;
		case Constants.STATE_GAME_RUNNING:
			gameSettings.disableProperty().set(true);
			gameField.disableProperty().set(true);
			btnNewSet.disableProperty().set(false);
			btnEndSet.disableProperty().set(true);
			btnEndGame.disableProperty().set(false);
			if(!viewModel.isReplay().get()) break;
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
		Group rootConfirm = new Group();
		Scene sceneConfirm = new Scene(rootConfirm, 500,180, Color.WHITESMOKE);
		stageConfirmWinner.setScene(sceneConfirm);
		stageConfirmWinner.centerOnScreen();
		stageConfirmWinner.show();
		
		stageConfirmWinner.setOnCloseRequest(new EventHandler<WindowEvent>(){
			public void handle(WindowEvent e){
				viewModel.discardSet();
			}
		});
						
	//Inhalt
		Text ueberschrift = new Text(20, 20,"Der Satz wurde beendet, bitte den Gewinner bestätigen oder den Satz verwerfen:");
		Button confirm = new Button("Bestätigen");		
		confirm.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent close){
				/**
				 * @TODO "Bestätigen" nur möglich, wenn eine Rolle ausgewählt wurde
				 */
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
		GridPane gpconfirm = new GridPane();
		gpconfirm.setLayoutX(20);
		gpconfirm.setVgap(20);
		gpconfirm.setHgap(10);
		gpconfirm.add(ueberschrift, 0, 0);
		GridPane.setColumnSpan(ueberschrift, 3);
//		gpconfirm.setColumnSpan(ueberschrift, 3);
		gpconfirm.add(new Label("Gewinner:"), 0, 1);
		gpconfirm.add(winner, 1, 1);
		gpconfirm.add(confirm, 1, 2);
		gpconfirm.add(discard, 2, 2);
		rootConfirm.getChildren().add(gpconfirm);
		
	}
	
	private void updateRole() {
		if(viewModel.isWithoutServer().get()){
			if(rolle.getValue().equals(String.valueOf(Constants.xRole))) {
				viewModel.properties()[viewModel.ROLE_PROPERTY].set(String.valueOf(Constants.oRole));
			}else{
				viewModel.properties()[viewModel.ROLE_PROPERTY].set(String.valueOf(Constants.xRole));
			}
		}		
		
	}

	//------------------------------------------------------------------ Methoden zum handeln von UI Input ----------------------------
	
	@FXML
	private void handleColButton(MouseEvent e){
		try{
			String data = (String)((Button) e.getSource()).getUserData();
			viewModel.oppMove((byte)Integer.parseInt(data));
		}catch(Exception ex){ ex.printStackTrace();}
	}
	
	@FXML
	private void handleCbLog(ActionEvent e){
		if(((CheckBox)e.getSource()).isSelected()){
			Log.getInstance().enableLog();
		}else{
			Log.getInstance().disableLog();
		}
	}
	
	@FXML
	private void handleNextMove(MouseEvent e){
		viewModel.loadNextMove();
	}
	
	@FXML
	private void handleRemoveMove(MouseEvent e){
		viewModel.removeLastMove();
	}
	
	@FXML
	private void handleChooseDirectory(ActionEvent e){
		DirectoryChooser dc = new DirectoryChooser();
		dc.setTitle("Pfad auswählen:");
		String initial = "C:\\";
		if(verzeichnispfad.getText() != null)
			initial = verzeichnispfad.getText();
		dc.setInitialDirectory(new File(initial));
		File f = dc.showDialog(borderPane.getScene().getWindow());
		if(f != null)
			verzeichnispfad.setText(f.getPath());		
	}
	
	//Spiel starten gedrückt
	@FXML
	private void handleStartGame(ActionEvent e){
		if ((rolle.getValue() != null && oppName.getText() != null && verzeichnispfad.getText() != null) || viewModel.isWithoutServer().get()){
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
	//Spiel beenden gedrückt
	@FXML
	private void handleEndGame(ActionEvent e){
		viewModel.endGame();
	}
	//Satz starten gedrückt
	@FXML
	private void handleStartSet(MouseEvent e){
		viewModel.startSet();
	}
	//Satz beenden gedrückt
	@FXML
	private void handleEndSet(MouseEvent e){
		viewModel.endSet((byte) -1);		
	}
	
	@FXML
	private void handleLoadGame(ActionEvent e){
		//Fenster mit gespeicherten Spielen öffnen
		final Stage stageLoad = new Stage();
		stageLoad.initModality(Modality.APPLICATION_MODAL);
		Group rootLoad = new Group();
		Scene sceneLog = new Scene(rootLoad, 500,600, Color.WHITESMOKE);
		stageLoad.setScene(sceneLog);
		stageLoad.centerOnScreen();
		stageLoad.show();
						
	//Inhalt
		Text ueberschrift = new Text(20, 20,"Wählen Sie ein gespeichertes Spiel aus:");
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
		Loads.getChildren().addAll(ueberschrift, savedGamesTable, load, close);
		Loads.setLayoutX(50);
		rootLoad.getChildren().add(Loads);
		savedGamesTable.getSelectionModel().selectFirst();
	}
	
	@FXML
    // Menü: Schließen des Programms
	private void handleSchliessen(ActionEvent close){System.exit(0);}
	@FXML
	// Menü: Spielsteuerung aufrufen
	private void handleSteuerung(ActionEvent steuerung){
		//Fenster mit Steuerung öffnen
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
		Button close = new Button("Schließen");
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
		//Fenster mit Log öffnen
		final Stage stageLog = new Stage();
		Group rootLog = new Group();
		Scene sceneLog = new Scene(rootLog, 500,500, Color.WHITESMOKE);
		stageLog.setScene(sceneLog);
		stageLog.centerOnScreen();
		stageLog.show();
						
	//Inhalt
		Text ueberschrift = new Text(20, 20,"Log");
		Button close = new Button("Schließen");
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
	
//---------------------------------------------------------------------------- Konverter Klassen ----------------------------------
	private class TokenRoleConverter extends StringConverter<String>{

		@Override
		public String fromString(String arg0) {
			// nicht nötig
			return null;
		}

		@Override
		public String toString(String arg0) {
			if(arg0 != null && (arg0.equals(String.valueOf(Constants.noRole)) || arg0.equals(String.valueOf(Constants.noRole) + Constants.winMarker))){
				//Leeres Feld -> kein Text!
				return " ";
			}
			else if (arg0 != null && arg0.length() > 1 && arg0.charAt(1) == Constants.winMarker){	
				//Gewinn Markierung aus Text entfernen
				return String.valueOf(arg0.charAt(0));
			}else{
				return arg0;
			}
		}
		
	}
	
	private class StyleConverter extends StringConverter<String> {	

		@Override
		public String fromString(String arg0) {
			//nicht nötig
			return null;
		}

		@Override
		public String toString(String arg0) {
			//Property hat sich verändert -> UI anpassen
			String style ="";
			if(arg0 != null){
				switch (arg0.charAt(0)) {
					case Constants.xRole:
						style="-fx-background-color: red;";
						break;
					case Constants.oRole:
						style="-fx-background-color: yellow;";
						break;
					default:
						break;
				}
				if( arg0.length() > 1 && arg0.charAt(1) == Constants.winMarker){
					style += "-fx-text-fill: RGB(0,0,0); -fx-effect: innershadow(gaussian, green, 10, 0.5, 0, 0);";
				}
			}
			return style;
		}

	}
}

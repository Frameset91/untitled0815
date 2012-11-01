package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.ResourceBundle;

import utilities.Log;
import utilities.Log.LogEntry;

import core.Constants;
import core.GameController;
import core.GameProperty;
import core.SetProperty;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
	private HBox setSettings;
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
//	@FXML
//	private HBox boxColButtons;
	@FXML
	private Button btnLoadGame;
	@FXML
	private Label labelOwnName;
	@FXML
	private Label labelOppName;
	@FXML
	private Label labelOpp;
	@FXML
	private Label labelOwn;
	@FXML
	private CheckBox replay;


	//Elemente die nicht im FXML definiert sind
	private TableView<LogEntry> logTabelle; 
	private ChoiceBox<String> winner;
	private TableView<GameProperty> savedGamesTable; 
	private SimpleStringProperty tempWinner;

	//Methode die für "Controller" vorgeschrieben ist und nach dem Aufbau des UI Konstrukts aufgerufen wird
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		viewModel = new GameController();
	
		feld.minHeightProperty().set(265*1.5);
		feld.minWidthProperty().set(300*1.5);
		Label[][] spielfeld = new Label[Constants.gamefieldcolcount][Constants.gamefieldrowcount];
		for (int i = 0; i < Constants.gamefieldcolcount; i++)
	    {
	      for (int j = 0; j < Constants.gamefieldrowcount; j++)
	      {
	        spielfeld[i][j] = new Label(" ");
            spielfeld[i][j].prefHeightProperty().set(40*1.5);
	        spielfeld[i][j].prefWidthProperty().set(40*1.5); 
	        spielfeld[i][j].getStyleClass().add("token");
	        spielfeld[i][j].styleProperty().bindBidirectional(viewModel.field()[i][Constants.gamefieldrowcount -1 -j], new StyleConverter());
	        spielfeld[i][j].textProperty().bindBidirectional(viewModel.field()[i][Constants.gamefieldrowcount -1 -j], new TokenRoleConverter());
	        spielfeld[i][j].setUserData(i + "," + (Constants.gamefieldrowcount -1 -j));
	        spielfeld[i][j].setOnMouseReleased(new TokenHandler());
	        feld.add(spielfeld[i][j], i, j);
	      }
	    }
	
		//-------------------------------------- weiteres Binding------------------------------
			
		//Binding für Zustand
		viewModel.properties()[viewModel.STATE_PROPERTY].addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {	updateState(); }});
		
		btnNextMove.disableProperty().bind(viewModel.isReplay().not());
		btnRemoveMove.disableProperty().bind(viewModel.isReplay().not());
		
		//Menü-Einträge an Buttons binden
		menuSpielStarten.disableProperty().bind(gameSettings.disableProperty());
		menuSpielBeenden.disableProperty().bind(btnEndGame.disableProperty());
		menuSpielLaden.disableProperty().bind(gameSettings.disableProperty());
		
		//Rollen Auswahl
//		rolle.valueProperty().bindBidirectional(viewModel.properties()[viewModel.ROLE_PROPERTY]);
		rolle.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				updateRole();						
			}		
		});
		
		viewModel.properties()[viewModel.ROLE_PROPERTY].addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				if(!viewModel.isWithoutServer().get())
						rolle.setValue(viewModel.properties()[viewModel.ROLE_PROPERTY].get());
				
			}			
		});
		rolle.getItems().addAll(String.valueOf(Constants.xRole), String.valueOf(Constants.oRole));
		
		labelOpp.textProperty().bind(oppName.textProperty());
		labelOwn.textProperty().bind(ownName.textProperty());
		
		//manuelles Spiel ohne Server oder gegen Server?
		cbWithoutServer.selectedProperty().bindBidirectional(viewModel.isWithoutServer());
		boxDirectory.disableProperty().bind(cbWithoutServer.selectedProperty());
//		boxTimeoutDraw.disableProperty().bind(cbWithoutServer.selectedProperty());
		boxTimeoutServer.disableProperty().bind(cbWithoutServer.selectedProperty());
//		boxColButtons.disableProperty().bind(cbWithoutServer.selectedProperty().not());
		ownName.disableProperty().bind(cbWithoutServer.selectedProperty());
		
		cbWithoutServer.onActionProperty().set(new EventHandler<ActionEvent>() {			
			@Override
			public void handle(ActionEvent arg0) {
				updateRole();
				
				if(cbWithoutServer.selectedProperty().get()){
					labelOwnName.setText("Gegner: ");
					ownName.setText("Computer");
					labelOppName.setText("Dein Name: ");
				}else{
					labelOwnName.setText("Dein Name: ");
					ownName.setText(Constants.defaultOwnName);
					labelOppName.setText("Gegner: ");
				}
				
			}
		});		
						
		//nur laden wenn DB verfügbar
		btnLoadGame.disableProperty().bind(viewModel.isDBAvailable().not());
		replay = new CheckBox();
		replay.setText("Wiederholung manuell abspielen");
		replay.selectedProperty().bindBidirectional(viewModel.isReplay());
		
		tempWinner = new SimpleStringProperty();
		tempWinner.bindBidirectional(viewModel.properties()[viewModel.WINNER_PROPERTY], new WinnerRoleConverter());		
		winner = new ChoiceBox<String>();
		winner.valueProperty().bindBidirectional(tempWinner);
		
		//Properties
		timeoutAbfrage.textProperty().bindBidirectional(viewModel.properties()[viewModel.TIMEOUTSERVER_PROPERTY]);
		timeoutZugzeit.textProperty().bindBidirectional(viewModel.properties()[viewModel.TIMEOUTDRAW_PROPERTY]);
		oppName.textProperty().bindBidirectional(viewModel.properties()[viewModel.OPPNAME_PROPERTY]);
		oppName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				winner.getItems().clear();
				winner.getItems().addAll(Constants.textTie, ownName.getText(), oppName.getText());		
//				winner.setValue(Constants.textTie);
			}
		});
		
		ownName.textProperty().bindBidirectional(viewModel.properties()[viewModel.OWNNAME_PROPERTY]);
		ownName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				winner.getItems().clear();
				winner.getItems().addAll(Constants.textTie, ownName.getText(), oppName.getText());
//				winner.setValue(Constants.textTie);
			}
		});
		verzeichnispfad.textProperty().bindBidirectional(viewModel.properties()[viewModel.PATH_PROPERTY]);
		punkteGegner.textProperty().bindBidirectional(viewModel.properties()[viewModel.OPPPOINTS_PROPERTY]);
		punkteSpieler.textProperty().bindBidirectional(viewModel.properties()[viewModel.OWNPOINTS_PROPERTY]);
		tokenGegner.styleProperty().bindBidirectional(viewModel.properties()[viewModel.OPPTOKEN_PROPERTY], new StyleConverter());
		tokenGegner.textProperty().bindBidirectional(viewModel.properties()[viewModel.OPPTOKEN_PROPERTY]);
		tokenSpieler.styleProperty().bindBidirectional(viewModel.properties()[viewModel.OWNTOKEN_PROPERTY], new StyleConverter());		
		tokenSpieler.textProperty().bindBidirectional(viewModel.properties()[viewModel.OWNTOKEN_PROPERTY]);
		satzstatus.textProperty().bind(viewModel.properties()[viewModel.STATE_PROPERTY]);
		
		//----------------------------------------------- Tabellen ----------------------------------------------
		
		//------Tabelle für die Logs
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
		
		//----------Tabelle für die gespeicherten Spiele
		savedGamesTable = new TableView<GameProperty>();
		savedGamesTable.getColumns().clear();
		savedGamesTable.setMinWidth(300);
			//Erste Spalte
		TableColumn<GameProperty, String> c1 = new TableColumn<GameProperty, String>("Spiel Nr.");
		c1.setEditable(false);
		c1.prefWidthProperty().bind(savedGamesTable.widthProperty().subtract(2).divide(4));

		savedGamesTable.getColumns().add(c1);
		savedGamesTable.setMinWidth(100);
		c1.setCellValueFactory(
				new PropertyValueFactory<GameProperty, String>("gameID"));
			//Zweite Spalte
		TableColumn<GameProperty, String> c2 = new TableColumn<GameProperty, String>("Spieler");
		c2.setEditable(false);
		c2.prefWidthProperty().bind(savedGamesTable.widthProperty().subtract(2).divide(2));
		savedGamesTable.getColumns().add(c2);
		savedGamesTable.setMinWidth(100);
		c2.setCellValueFactory(
				new PropertyValueFactory<GameProperty, String>("players"));
			//Dritte Spalte
		TableColumn<GameProperty, String> c3 = new TableColumn<GameProperty, String>("Punkte");
		c3.setEditable(false);
		c3.prefWidthProperty().bind(savedGamesTable.widthProperty().subtract(2).divide(4));
		savedGamesTable.getColumns().add(c3);
		savedGamesTable.setMinWidth(100);
		c3.setCellValueFactory(
				new PropertyValueFactory<GameProperty, String>("points"));		
		//Elemente hinzufügen
		savedGamesTable.setItems(viewModel.savedGames());
				
		//------------Tabelle für Sets
		tableColumnSet.setCellValueFactory(
				new PropertyValueFactory<SetProperty, String>("setNr"));
		tableColumnSet.prefWidthProperty().bind(tableStatistic.widthProperty().divide(2).subtract(20));
		tableColumnWinner.setCellValueFactory(
				new PropertyValueFactory<SetProperty, String>("winner"));
		tableColumnWinner.prefWidthProperty().bind(tableStatistic.widthProperty().divide(2).add(20));
		tableStatistic.setItems(viewModel.sets());
	
		
//		winner.getItems().addAll(String.valueOf(Constants.noRole), String.valueOf(Constants.xRole), String.valueOf(Constants.oRole));
//		winner.getItems().addAll(Constants.textTie, ownName.getText(), oppName.getText());
		
		viewModel.initialize();
	 }
	
	//------Hilfsmethoden
	
	private void updateState(){
		//UI bei Zustandsänderungen anpassen
		switch (viewModel.properties()[viewModel.STATE_PROPERTY].getValue()) {
		case Constants.STATE_APP_RUNNING:
			gameSettings.disableProperty().set(false);
			gameField.disableProperty().set(true);
			btnEndGame.disableProperty().set(true);
			setSettings.disableProperty().set(true);
			break;
		case Constants.STATE_SET_ENDED:	
			gameSettings.disableProperty().set(true);
			gameField.disableProperty().set(false);
//			btnNewSet.disableProperty().set(true);
//			btnEndSet.disableProperty().set(true);
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
			setSettings.disableProperty().set(false);
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
//		stageConfirmWinner.setResizable(false);
		Group rootConfirm = new Group();
		Scene sceneConfirm = new Scene(rootConfirm, 300,160, Color.WHITESMOKE);
		sceneConfirm.getStylesheets().add("view/MainUIStyle.css");
		stageConfirmWinner.setScene(sceneConfirm);
		stageConfirmWinner.centerOnScreen();
		stageConfirmWinner.initModality(Modality.APPLICATION_MODAL); 
		stageConfirmWinner.show();
		
		stageConfirmWinner.setOnCloseRequest(new EventHandler<WindowEvent>(){
			public void handle(WindowEvent e){
				viewModel.discardSet();
			}
		});
						
	//Inhalt
		Text ueberschrift = new Text(20, 20,"Bitte den Gewinner bestätigen:");
		ueberschrift.getStyleClass().add("ueberschrift2");
		Button confirm = new Button("Bestätigen");		
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
		VBox vconfirm = new VBox(30);
		vconfirm.setLayoutX(20);
		HBox hconfirmwinner = new HBox(15);
		hconfirmwinner.getChildren().addAll(new Label("Gewinner:"), winner);
		HBox hconfirmbuttons = new HBox(15);
		hconfirmbuttons.getChildren().addAll(confirm, discard);
		vconfirm.getChildren().addAll(ueberschrift,hconfirmwinner, hconfirmbuttons);
		rootConfirm.getChildren().add(vconfirm);
		
		if(winner.getValue() == null) 
			winner.setValue(Constants.textTie);
		
	}
	
	private void updateRole() {
		if(viewModel.isWithoutServer().get()){
			if(rolle.getValue().equals(String.valueOf(Constants.xRole))) {
				viewModel.properties()[viewModel.ROLE_PROPERTY].set(String.valueOf(Constants.oRole));
			}else{
				viewModel.properties()[viewModel.ROLE_PROPERTY].set(String.valueOf(Constants.xRole));
			}
		}else{
			viewModel.properties()[viewModel.ROLE_PROPERTY].set(rolle.getValue());
		}
		
	}

	//------------------------------------------------------------------ Methoden zum handeln von UI Input ----------------------------
	
//	@FXML
//	//reagieren auf Buttons über den Spalten (zum manuellen Spielen)
//	private void handleColButton(MouseEvent e){
//		try{
//			String data = (String)((Button) e.getSource()).getUserData();
//			viewModel.oppMove((byte)Integer.parseInt(data));
//		}catch(Exception ex){ ex.printStackTrace();}
//	}
	
	@FXML
	//Reagieren auf CheckBox für Log 
	private void handleCbLog(ActionEvent e){
		if(((CheckBox)e.getSource()).isSelected()){
			Log.getInstance().enableLog();
		}else{
			Log.getInstance().disableLog();
		}
	}
	
	@FXML
	//Replay: einen Zug vor
	private void handleNextMove(MouseEvent e){
		viewModel.loadNextMove();
	}
	
	@FXML
	//Replay: einen Zug zurück
	private void handleRemoveMove(MouseEvent e){
		viewModel.removeLastMove();
	}
	
	@FXML
	//Pfad-Auswahlhilfe
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
		int timeoutZ;
		int timeoutF;
		if (((rolle.getValue() != null && oppName.getText() != null && ownName.getText() != null && verzeichnispfad.getText() != null && !verzeichnispfad.getText().equals("") && !oppName.getText().equals("") && !ownName.getText().equals("")) || (viewModel.isWithoutServer().get() && oppName.getText() != null))){
			try{
				timeoutZ = Integer.parseInt(timeoutZugzeit.getText());
				timeoutF = Integer.parseInt(timeoutAbfrage.getText());
			}
			catch(Exception wrongTimeout){
				timeoutF=-1;
				timeoutZ=-1;
			}
			if (timeoutZ>0 && timeoutF>0 && !timeoutZugzeit.getText().equals("") && !timeoutAbfrage.getText().equals("")){
				viewModel.startGame();
			}
			else{
				final Stage stage = new Stage();
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.setTitle("Achtung!");
				stage.setResizable(false);
				Group rootEinstellungen = new Group();
				Scene scene = new Scene(rootEinstellungen, 350, 90, Color.WHITESMOKE);
				scene.getStylesheets().add("view/MainUIStyle.css");
				stage.setScene(scene);
				stage.centerOnScreen();
				Text text = new Text("Ungültige Timeout-Zeiten!");
				text.getStyleClass().add("ueberschrift");
				Text text2 = new Text("Timeout-Zeiten müssen zwischen 1.000 und 10.000 ms liegen.");
				text.getStyleClass().add("ueberschrift2");
				Button button = new Button("OK");
				button.setOnAction(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent close){
						stage.close();
					}
				});
				VBox vbox = new VBox(10);
				vbox.setAlignment(Pos.CENTER);
				vbox.setLayoutX(10);
				vbox.getChildren().addAll(text, text2, button);
				rootEinstellungen.getChildren().add(vbox);
				stage.show();
			}
		}
		else{
			final Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Achtung!");
			stage.setResizable(false);
			Group rootEinstellungen = new Group();
			Scene scene = new Scene(rootEinstellungen, 248, 80, Color.WHITESMOKE);
			stage.setScene(scene);
			stage.centerOnScreen();
			Text text = new Text("Bitte alle Spieleinstellungen definieren!");
			text.setFont(new Font(13));
			Button button = new Button("OK");
			button.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent close){
					stage.close();
				}
			});
			VBox vbox = new VBox(10);
			vbox.setAlignment(Pos.CENTER);
			vbox.setLayoutX(10);
			vbox.setLayoutY(15);
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
		Loads.getChildren().addAll(ueberschrift, savedGamesTable, load, replay, close);
		Loads.setLayoutX(50);
		rootLoad.getChildren().add(Loads);
		savedGamesTable.getSelectionModel().selectFirst();
	}
	
	@FXML
    // Menü: Schließen des Programms
	private void handleSchliessen(ActionEvent close){
		final Stage closing = new Stage();
		Group rootClosing = new Group();
		Scene sceneClosing = new Scene(rootClosing, 420,120, Color.WHITESMOKE);
		closing.setScene(sceneClosing);
		closing.centerOnScreen();
		Text text = new Text("Wollen Sie das Programm wirklich beenden?");
		Text text2 = new Text("Falls noch ein Satz oder Spiel läuft, kann dies zu Datenverlust führen.");
		text.setFont(new Font(14));
		text2.setFont(new Font(13));
		text2.setFill(Color.rgb(187, 0, 0));
		Button button = new Button("Beenden");
		button.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent close){
				System.exit(0);
			}
		});
		Button button2 = new Button("Abbrechen");
		button2.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent close){
				closing.close();
			}
		});
		VBox vbox = new VBox(10);
		vbox.setLayoutX(10);
		vbox.setLayoutY(10);
		HBox hbox = new HBox(10);
		hbox.setAlignment(Pos.CENTER);
		VBox.setMargin(hbox, new Insets(10,10,10,10));
		hbox.getChildren().addAll(button, button2);
		vbox.getChildren().addAll(text, text2, hbox);
		rootClosing.getChildren().add(vbox);
		closing.initModality(Modality.APPLICATION_MODAL);
		closing.showAndWait();
		close.consume();
	}
	@FXML
	// Menü: Spielsteuerung aufrufen
	private void handleSteuerung(ActionEvent steuerung){
		//Fenster mit Steuerung öffnen
		final Stage stageSteuerung = new Stage();
		stageSteuerung.setResizable(false);
		Group rootSteuerung = new Group();
		Scene sceneSteuerung = new Scene(rootSteuerung, 700,600, Color.WHITESMOKE);
		stageSteuerung.setScene(sceneSteuerung);
		stageSteuerung.centerOnScreen();
		stageSteuerung.show();
		//Inhalt
		Text ueberschrift = new Text(20, 20,"\"4 Gewinnt\"");
		ueberschrift.setFill(Color.BLACK);
		ueberschrift.setEffect(new Lighting());
		ueberschrift.setFont(Font.font(Font.getDefault().getFamily(), 20));
		TextArea area = new TextArea();
		File instructions = new File("bin/view/GameInstructions.txt");
		FileReader fr;
		try {
			fr = new FileReader(instructions);
			BufferedReader br = new BufferedReader(fr);
			String line="";
			String text="";
			while((line = br.readLine())!=null){
				text = text + "\n \r" + line;
			}
			area.setText(text);
		} catch (Exception e) {
			e.printStackTrace();
		}
		area.setMinWidth(700);
		area.setMinHeight(500);
		area.editableProperty().set(false);
		area.setWrapText(true);
		Button close = new Button("Schließen");
		close.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent close){
				stageSteuerung.close();
			}});
		VBox textUndButton = new VBox(20);
		textUndButton.setAlignment(Pos.CENTER);
		textUndButton.getChildren().addAll(ueberschrift, area, close);
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
		stageLog.setTitle("Log");
		stageLog.setResizable(false);
		Group rootLog = new Group();
		Scene sceneLog = new Scene(rootLog, 500,480, Color.WHITESMOKE);
		stageLog.setScene(sceneLog);
		stageLog.centerOnScreen();
		stageLog.show();
						
	//Inhalt
		Button close = new Button("Schließen");
		close.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent close){
				stageLog.close();
			}
		});
		//Anordnen
		VBox logs = new VBox(20);
		logs.setLayoutY(10);
		logs.setAlignment(Pos.CENTER);
		logTabelle.prefWidthProperty().bind(sceneLog.widthProperty().subtract(100));
		logs.getChildren().addAll(logTabelle, close);
		logs.setLayoutX(50);
		rootLog.getChildren().add(logs);
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
	
	private class WinnerRoleConverter extends StringConverter<String>{

		@Override
		public String fromString(String arg0) {
			//Von Text zu char konvertieren
			String value = String.valueOf(Constants.noRole);
			if(arg0 != null){
				if(arg0.equals(viewModel.properties()[viewModel.OWNNAME_PROPERTY].get())){
					value = viewModel.properties()[viewModel.ROLE_PROPERTY].get();
				}else if (arg0.equals(viewModel.properties()[viewModel.OPPNAME_PROPERTY].get())){
					if(viewModel.properties()[viewModel.ROLE_PROPERTY].get().charAt(0) == Constants.oRole)
						value = String.valueOf(Constants.xRole);
					else
						value = String.valueOf(Constants.oRole);
				}
			}
			return value;
		}

		@Override
		public String toString(String arg0) {
			//Von char zu Text konvertieren
			String value = Constants.textTie;
			if(arg0 != null){		
				char winner = arg0.charAt(0);
				if( winner == viewModel.properties()[viewModel.ROLE_PROPERTY].get().charAt(0)){
					value = viewModel.properties()[viewModel.OWNNAME_PROPERTY].get();
				}else if(winner != viewModel.properties()[viewModel.ROLE_PROPERTY].get().charAt(0) && ( winner == Constants.oRole || winner == Constants.xRole)){
					value = viewModel.properties()[viewModel.OPPNAME_PROPERTY].get();
				}
			}
			return value;
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
	
	private class TokenHandler implements EventHandler<MouseEvent>{

		@Override
		public void handle(MouseEvent e) {
			if(viewModel.isWithoutServer().get()){
				try{
					String data = (String)((Label) e.getSource()).getUserData();
					viewModel.oppMove((byte)Integer.parseInt(data.split(",")[0]));
				}catch(Exception ex){ ex.printStackTrace();}			
			}			
		}		
	}
}

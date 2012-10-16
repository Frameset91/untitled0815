package testViews;

import java.net.URL;
import java.util.ResourceBundle;

import utilities.Log;
import utilities.Log.LogEntry;

import core.Constants;
import core.GameController;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FieldTestController extends Application implements Initializable {
	
	private GameController viewModel; 
	
	//Attribut das in der FXML definiert ist	
	@FXML
	private GridPane feld;
	@FXML
	private HBox leftBox;
	@FXML
	private VBox centerBox;
	@FXML
	private VBox rightBox;
	
	@FXML
	private ChoiceBox<String> rolle;
	
	@FXML
	private MenuItem menuSchließen; 
	@FXML
	private MenuItem menuAnleitung;
//	@FXML
//	private Button timeoutHochAbfrage;
//	@FXML
//	private Button timeoutRunterAbfrage;
	@FXML
	private TextField timeoutAbfrage;
//	@FXML
//	private Button timeoutHochZugzeit;
//	@FXML
//	private Button timeoutRunterZugzeit;
	@FXML
	private TextField timeoutZugzeit;
	@FXML
	private TableView<LogEntry> logTabelle; 
	

	//Methode die für "Controller" vorgeschrieben ist und nach dem Aufbau des UI Kontrukts aufgerufen wird
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		viewModel = new GameController();
		viewModel.initialize(null, null);
		
		
		//GridPane mit Circles füllen
		Circle[][] spielfeld = new Circle[Constants.gamefieldcolcount][Constants.gamefieldrowcount];
		for (int i = 0; i < Constants.gamefieldcolcount; i++)
	    {
	      for (int j = 0; j < Constants.gamefieldrowcount; j++)
	      {
	        spielfeld[i][j] = new Circle(20.0f);
//	        spielfeld[i][j].getStyleClass().add("token");
	        spielfeld[i][j].styleProperty().bind(viewModel.styleField()[i][Constants.gamefieldrowcount -1 -j]);
	        feld.add(spielfeld[i][j], i, j);
	      }
	    }
//		
		//Tabelle für die Logs
		TableColumn spalte1 = new TableColumn("Log-Eintrag");
		spalte1.setEditable(false);
		logTabelle.getColumns().clear();
		logTabelle.getColumns().add(spalte1);
		logTabelle.setMinWidth(384);
				
		//Binding
		spalte1.setCellValueFactory(
				new PropertyValueFactory<Log.LogEntry, String>("text"));
		logTabelle.setItems(viewModel.logItems());
		Log.getInstance().write("Binding fuer Log erstellt");
		
		//Binding für Zustand
		viewModel.properties()[viewModel.STATE_PROPERTY].addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {	updateState(); }});
		
		rolle.valueProperty().bindBidirectional(viewModel.properties()[viewModel.ROLE_PROPERTY]);
		rolle.getItems().addAll(String.valueOf(Constants.xRole), String.valueOf(Constants.oRole));
		
			
	 }
	
	private void updateState(){
		//UI bei Zustandsänderungen anpassen
		switch (viewModel.properties()[viewModel.STATE_PROPERTY].getValue()) {
		case Constants.STATE_APP_RUNNING:
			leftBox.disableProperty().set(false);
			centerBox.disableProperty().set(true);
			rightBox.disableProperty().set(true);
	//					array[DISABLE_SET_ABORT] = true;
	//					array[DISABLE_GAME_ABORT] = true;
	//					array[SHOW_SETEND_POPUP] = false;
			break;
		case Constants.STATE_GAME_RUNNING:
			leftBox.disableProperty().set(true);
			centerBox.disableProperty().set(true);
			rightBox.disableProperty().set(false);
	//					array[DISABLE_SET_ABORT] = true;
	//					array[DISABLE_GAME_ABORT] = false;
	//					array[SHOW_SETEND_POPUP] = false;
			break;
		case Constants.STATE_SET_RUNNING:
			leftBox.disableProperty().set(true);
			centerBox.disableProperty().set(false);
			rightBox.disableProperty().set(true);	
	//					array[DISABLE_SET_ABORT] = false;
	//					array[DISABLE_GAME_ABORT] = true;
	//					array[SHOW_SETEND_POPUP] = false;
			break;
		case Constants.STATE_SET_ENDED:	
			leftBox.disableProperty().set(true);
			centerBox.disableProperty().set(false);
			rightBox.disableProperty().set(true);
	//					array[DISABLE_SET_ABORT] = true;
	//					array[DISABLE_GAME_ABORT] = true;
	//					array[SHOW_SETEND_POPUP] = true;
			break;
		default:
			
			break;
		}	
	}	

	//Methoden um das Prog zu starten

	@Override
	public void start(Stage stage) throws Exception {
        stage.setTitle("Test für Spielfeld mit FXML"); 
        //Parent root = FXMLLoader.load(getClass().getResource("FieldTest.fxml")); 
        Parent root = FXMLLoader.load(getClass().getResource("fxmlTest.xml"));
        stage.setScene(new Scene(root)); 
        stage.show();
	}
	
    
    // Menü: Schließen des Programms
	public void handleSchließen(ActionEvent close){System.exit(0);}
	
	// Menü: Spielanleitung aufrufen
	public void handleAnleitung(ActionEvent anleitung){
		//Fenster mit Anleitung öffnen
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
		Button close = new Button("Schließen");
		close.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent close){
				stageAnleitung.close();
			}});
		VBox textUndButton = new VBox(100);
		textUndButton.getChildren().addAll(ueberschrift,text, close);
		rootAnleitung.getChildren().add(textUndButton);
	}
	
	// Einstellungen: Timeouts hoch/ runter setzen
	public void handleHoch1(MouseEvent arg0){
		int timeoutFileabfruf;
		timeoutFileabfruf = Integer.parseInt(timeoutAbfrage.getText());
		timeoutFileabfruf = timeoutFileabfruf + 25;
		String zeitz = String.valueOf(timeoutFileabfruf);
		timeoutAbfrage.setText(zeitz);
	}
	
	public void handleRunter1(MouseEvent arg0){
		int timeoutFileabruf;
		timeoutFileabruf = Integer.parseInt(timeoutAbfrage.getText());
		timeoutFileabruf = timeoutFileabruf - 25;
		String zeitz = String.valueOf(timeoutFileabruf);
		timeoutAbfrage.setText(zeitz);
	}
	
	public void handleHoch2(MouseEvent arg0){
		int timeoutFileabfruf;
		timeoutFileabfruf = Integer.parseInt(timeoutZugzeit.getText());
		timeoutFileabfruf = timeoutFileabfruf + 100;
		String zeitz = String.valueOf(timeoutFileabfruf);
		timeoutZugzeit.setText(zeitz);
	}
	
	public void handleRunter2(MouseEvent arg0){
		int timeoutFileabruf;
		timeoutFileabruf = Integer.parseInt(timeoutZugzeit.getText());
		timeoutFileabruf = timeoutFileabruf - 100;
		String zeitz = String.valueOf(timeoutFileabruf);
		timeoutZugzeit.setText(zeitz);
	}

	// Log anzeigen
	public void handleLogAnzeigen(MouseEvent arg0){
		//Fenster mit Log öffnen
		final Stage stageAnleitung = new Stage();
		Group rootLog = new Group();
		Scene sceneLog = new Scene(rootLog, 484,500, Color.WHITESMOKE);
		stageAnleitung.setScene(sceneLog);
		stageAnleitung.centerOnScreen();
		stageAnleitung.show();
						
	//Inhalt
		Text ueberschrift = new Text(20, 20,"Log");
		Button close = new Button("Schließen");
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
	
	public static void main(String[] args) {
		launch(args);
	}
}

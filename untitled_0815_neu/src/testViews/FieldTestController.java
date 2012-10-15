package testViews;

import java.net.URL;
import java.util.ResourceBundle;

import core.Constants;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FieldTestController extends Application implements Initializable {
	
	//Attribut das in der FXML definiert ist
	
	@FXML
	private GridPane feld;
	public MenuItem menuSchlie�en; //bei private wird gemeckert
	public MenuItem menuAnleitung;
	public Button timeoutHochAbfrage;
	public Button timeoutRunterAbfrage;
	public TextField timeoutAbfrage;
	public Button timeoutHochZugzeit;
	public Button timeoutRunterZugzeit;
	public TextField timeoutZugzeit;
	

	//Methode die f�r "Controller" vorgeschrieben ist und nach dem Aufbau des UI Kontrukts aufgerufen wird
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//GridPane mit Circles f�llen
		Circle[][] spielfeld = new Circle[Constants.gamefieldcolcount][Constants.gamefieldrowcount];
		for (int i = 0; i < Constants.gamefieldcolcount; i++)
	    {
	      for (int j = 0; j < Constants.gamefieldrowcount; j++)
	      {
	        spielfeld[i][j] = new Circle(20.0f);
	        spielfeld[i][j].getStyleClass().add("token");
	        feld.add(spielfeld[i][j], i, j);
	      }
	    }
	 }
	
	//Methoden um das Prog zu starten

	@Override
	public void start(Stage stage) throws Exception {
        stage.setTitle("Test f�r Spielfeld mit FXML"); 
        //Parent root = FXMLLoader.load(getClass().getResource("FieldTest.fxml")); 
        Parent root = FXMLLoader.load(getClass().getResource("fxmlTest.xml"));
        stage.setScene(new Scene(root)); 
        stage.show();
	}
	
    
    // Men�: Schlie�en des Programms
	public void handleSchlie�en(ActionEvent close){System.exit(0);}
	
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
		//Fenster mit Log �ffnen
		final Stage stageAnleitung = new Stage();
		Group rootLog = new Group();
		Scene sceneLog = new Scene(rootLog, 484,500, Color.WHITESMOKE);
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
	
	public static void main(String[] args) {
		launch(args);
	}
}

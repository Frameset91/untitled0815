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
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class FieldTestController extends Application implements Initializable {
	
	//Attribut das in der FXML definiert ist
	
	@FXML
	private GridPane feld;
	public MenuItem menuSchließen; //bei private wird gemeckert

	//Methode die für "Controller" vorgeschrieben ist und nach dem Aufbau des UI Kontrukts aufgerufen wird
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//GridPane mit Circles füllen
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
        stage.setTitle("Test für Spielfeld mit FXML"); 
        //Parent root = FXMLLoader.load(getClass().getResource("FieldTest.fxml")); 
        Parent root = FXMLLoader.load(getClass().getResource("fxmlTest.xml"));
        stage.setScene(new Scene(root)); 
        stage.show();
	}
	
    
    // Schließen des Programms über das Menü
	public void handle(ActionEvent close){System.exit(0);}
	
	public static void main(String[] args) {
		launch(args);
	}
}

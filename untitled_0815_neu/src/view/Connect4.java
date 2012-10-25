package view;

import javafx.application.Application; 
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Connect4 extends Application {
	/**
	 * Start Methode für das UI
	 */
	@Override
	public void start(final Stage stage) throws Exception {
        stage.setTitle("Test für Spielfeld mit FXML"); 
        Parent root = FXMLLoader.load(getClass().getResource("MainUI.fxml")); 
        stage.setScene(new Scene(root)); 
        stage.setResizable(false);	
        stage.show();
        
        /**
         * @TODO setOnCloseRequest für Schließen der App
         */
        stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
        	public void handle(WindowEvent e){
        		final Stage closing = new Stage();
        		Group rootClosing = new Group();
        		Scene sceneClosing = new Scene(rootClosing, 250,80, Color.WHITESMOKE);
        		closing.setScene(sceneClosing);
        		closing.centerOnScreen();
        		Text text = new Text(20,40,"Bitte erst das Spiel/ den Satz beenden!");
        		Text text2 = new Text(20,40,"Oder über das Menü schließen.");
        		Button button = new Button("OK");
        		button.setOnAction(new EventHandler<ActionEvent>(){
        			public void handle(ActionEvent close){
        				closing.close();
        			}
        		});
        		VBox vbox = new VBox(10);
        		vbox.getChildren().addAll(text, button);
        		rootClosing.getChildren().add(vbox);
        		closing.initModality(Modality.APPLICATION_MODAL);
        		closing.showAndWait();
        		e.consume();
        	}
        });
        
	}

	/**
	 * Start der 4-Gewinnt Applikation
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Stop Methode, wenn Programm geschlossen wird
	 * 
	 */
//	@Override
//	public void stop() throws Exception {
//		//CommunicationServer.getInstance().disableReading();
//		super.stop();
//	}

}

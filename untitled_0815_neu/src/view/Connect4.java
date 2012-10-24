package view;

import javafx.application.Application; 
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.*;

public class Connect4 extends Application {

	
	/**
	 * Start Methode für das UI
	 */
	@Override
	public void start(Stage stage) throws Exception {
        stage.setTitle("Test für Spielfeld mit FXML"); 
        Parent root = FXMLLoader.load(getClass().getResource("MainUI.fxml")); 
        stage.setScene(new Scene(root)); 
        stage.setResizable(false);	
        stage.show();
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

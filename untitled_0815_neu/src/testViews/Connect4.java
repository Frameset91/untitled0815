package testViews;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Connect4 extends Application {

	//Methoden um das Prog zu starten

	@Override
	public void start(Stage stage) throws Exception {
        stage.setTitle("Test für Spielfeld mit FXML"); 
        Parent root = FXMLLoader.load(getClass().getResource("fxmlTest.fxml")); 
        stage.setScene(new Scene(root)); 
        stage.show();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);

	}

}

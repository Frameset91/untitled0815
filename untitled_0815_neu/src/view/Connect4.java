package view;

import javafx.application.Application; 
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Connect4 extends Application {
	/**
	 * Start Methode für das UI
	 */

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("4 Gewinnt - untitled0815"); 
        Parent root = FXMLLoader.load(getClass().getResource("MainUI.fxml")); 
        stage.setScene(new Scene(root)); 
        //stage.setResizable(false);
        stage.minWidthProperty().set(850);
        stage.minHeightProperty().set(730);
        stage.show();
        
        /**
         * @TODO setOnCloseRequest noch mit Statusabfrage versehen
         */
        stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
        	public void handle(WindowEvent e){
        		final Stage closing = new Stage();
        		Group rootClosing = new Group();
        		Scene sceneClosing = new Scene(rootClosing, 420, 120, Color.WHITE);
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

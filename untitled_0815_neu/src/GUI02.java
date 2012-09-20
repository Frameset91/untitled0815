	/**
	 * @author NHerentrey
	 * @param args
	 */

import javafx.application.*;
import javafx.scene.*;				//Scene bildet "Leinw�nde" in dem Rahmen
import javafx.stage.*;				//Stage ist der "Rahmen" der Applikation
import javafx.scene.control.*;		//F�r das Men�
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;

public class GUI02 extends Application {	
	
	public void init(Stage stage){
		
		Group group = new Group();
		Scene scene = new Scene(group);
		scene.getStylesheets().add("test.css");
		stage.setScene(scene);
		
		BorderPane borderpane = new BorderPane();
		borderpane.setPrefSize(400, 450);
		
	// Das Men�
		MenuBar menuBar = new MenuBar();
		menuBar.setMinWidth(500);
		
		//1. Men�punkt
		final MenuItem schlie�en = new MenuItem("Schlie�en"); // Untermen�
		final Menu datei = MenuBuilder.create().text("Datei").items(schlie�en).build();
		
		//2. Men�punkt
		final Menu optionen = new Menu("Optionen");
		
		//3. Men�punkt
		final Menu hilfe = new Menu("Hilfe");
		
				
		//Men�punkte zusammenf�hren
		menuBar.getMenus().addAll(datei, optionen,hilfe);

		
		//Menup�nkt "Schlie�en"
		schlie�en.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent close){System.exit(0);}
		});
		
		borderpane.setTop(menuBar);
		
		
	//Spielfeld
	    GridPane grid = new GridPane();
		Circle spielfeld[][] = new Circle[7][6];
	    
	    //Abst�nde zwischen Feldern
	    grid.setHgap(3);
	    grid.setVgap(3);
	    grid.setMaxHeight(200);
	    grid.setMaxWidth(250);
		grid.getStyleClass().add("grid");
		
	    for (int i = 0; i < 7; i++)
	    {
	      for (int j = 0; j < 6; j++)
	      {
	        spielfeld[i][j] = new Circle(20.0f);
	        spielfeld[i][j].getStyleClass().add("token");
	        spielfeld[i][j].setId("token" + i + j);
	        grid.add(spielfeld[i][j], i, j);
	      }
	    }
	    
	    BorderPane.setAlignment(grid, Pos.TOP_LEFT);
	    borderpane.setCenter(grid);
	    Label status = new Label("Satzstatus: Satz spielen");
	    BorderPane.setAlignment(status, Pos.CENTER);
	    borderpane.setBottom(status);


	    
	    
		// Gruppe f�llen
		group.getChildren().add(borderpane);
	}
	
	@Override public void start (Stage stage) throws Exception{
		init(stage);
		stage.setHeight(500);
		stage.setWidth(500);
		stage.setTitle("4 Gewinnt - untitled0815");
		stage.show();
		
	}
	
	public static void main(String[] args) {launch(args);}

	
}

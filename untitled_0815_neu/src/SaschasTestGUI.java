/**
 * @author NHerentrey / Sascha
 * @param args
 */

import javafx.application.*;
import javafx.scene.*;				//Scene bildet "Leinwände" in dem Rahmen
import javafx.stage.*;				//Stage ist der "Rahmen" der Applikation
import javafx.scene.control.*;		//Für das Menü
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;

public class SaschasTestGUI extends Application {
	
	private Circle spielfeld[][];

public void init(Stage stage){
	Group root = new Group();
	Scene scene = new Scene(root);
	scene.getStylesheets().add("test.css");
	stage.setScene(scene);
	
	BorderPane borderpane = new BorderPane();
	borderpane.setPrefSize(500,450);
	
// Das Menü
	MenuBar menuBar = new MenuBar();
	menuBar.setMinWidth(500);
	
	//1. Menüpunkt
	final MenuItem schließen = new MenuItem("Schließen"); // Untermenü
	final Menu datei = MenuBuilder.create().text("Datei").items(schließen).build();
	
	//2. Menüpunkt
	final Menu optionen = new Menu("Optionen");
	
	//3. Menüpunkt
	final Menu hilfe = new Menu("Hilfe");
	
			
	//Menüpunkte zusammenführen
	menuBar.getMenus().addAll(datei, optionen,hilfe);

	
	//Menupünkt "Schließen"
	schließen.setOnAction(new EventHandler<ActionEvent>(){
		public void handle(ActionEvent close){System.exit(0);}
	});
	
	borderpane.setTop(menuBar);
	
//Anzeige, welcher Spieler man ist
	HBox hSpieler = new HBox(10);
	Circle tokenSpieler = new Circle(20.0f);
	Circle tokenGegner = new Circle(20.0f);
	tokenSpieler.getStyleClass().add("token");
	tokenGegner.getStyleClass().add("token");
	hSpieler.getChildren().addAll(new Label("Spieler:"), tokenSpieler, new Label("Gegner:"), tokenGegner);
	hSpieler.setLayoutX(50);
	hSpieler.setLayoutY(100);
//Spielfeld
    GridPane grid = new GridPane();
	spielfeld = new Circle[7][6];
    
    //Abstände zwischen Feldern
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
    BorderPane.setAlignment(grid, Pos.CENTER_LEFT);
    borderpane.setLeft(grid);

    // Statusanzeige
    Label status = new Label("Satzstatus: Satz spielen");
    status.getStyleClass().add("satzstatus");
    BorderPane.setAlignment(status, Pos.CENTER);
    borderpane.setBottom(status);

    // Statistik: Satznummer, Zeit, ...
    VBox boxstats = new VBox(2);
    BorderPane.setAlignment(boxstats, Pos.TOP_LEFT);
    
    Label stats = new Label("Statistik:");
    boxstats.getChildren().add(stats);
    
    GridPane statistik = new GridPane();
    
    
    borderpane.setCenter(boxstats);
    
    
	// Gruppe füllen
	root.getChildren().addAll(hSpieler, borderpane);
}

@Override public void start (Stage stage) throws Exception{
	init(stage);
	stage.setHeight(500);
	stage.setWidth(500);
	stage.setTitle("4 Gewinnt - untitled0815");
	stage.show();
	
}

public void setFieldBinding(GameField field){
  for (int i = 0; i < 7; i++)
    {
      for (int j = 0; j < 6; j++)
      {
        //spielfeld[i][j].fillProperty(). = new    bind(field.getField()[i][j]);
      }
    }
}

public static void main(String[] args) {launch(args);}


}

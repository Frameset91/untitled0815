	/**
	 * @author NHerentrey
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

public class GUI03 extends Application {	
	
	public void init(Stage stage){
		Group root = new Group();
		Scene scene = new Scene(root);
		scene.getStylesheets().add("test.css");
		stage.setScene(scene);
		stage.setResizable(false);
		
		BorderPane borderpane = new BorderPane(); //setzt Layout/ Anordnung fest
		
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
		VBox spielanzeige = new VBox(); // zeigt Spieleranzeige und Spielfeld untereinander an
		
		HBox hSpieler = new HBox(20);
		hSpieler.getStyleClass().add("spielanzeige");
		Circle tokenSpieler = new Circle(15.0f);
		Circle tokenGegner = new Circle(15.0f);
		tokenSpieler.getStyleClass().add("token-red");
		tokenGegner.getStyleClass().add("token-yellow");
		hSpieler.getChildren().addAll(new Label("Spieler:"), tokenSpieler, new Label("Gegner:"), tokenGegner);
		
	//Spielfeld
	    GridPane grid = new GridPane();
		Circle spielfeld[][] = new Circle[7][6];
	    
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
	    
		spielanzeige.getChildren().addAll(hSpieler, grid);
	    borderpane.setLeft(spielanzeige);

	// Rechte Spalte
	    VBox boxrechts = new VBox(10);
	    
	    Button neuerSatz = new Button("neuen Satz spielen");
	     
		// Spielstand
	    VBox spielstand = new VBox(0);
	    spielstand.getStyleClass().add("spielanzeige");
		Label ergebnis = new Label("Spielstand:");
		ergebnis.getStyleClass().add("punkte");
		HBox punkte = new HBox(10);
	    Label punkteSpieler = new Label("0");
	    punkteSpieler.getStyleClass().add("punkte");
	    Label punkteGegner = new Label("0");
	    punkteGegner.getStyleClass().add("punkte");
	    Label vs = new Label(" : ");
	    vs.getStyleClass().add("punkte");
	    punkte.getChildren().addAll(punkteSpieler, vs, punkteGegner);
	    spielstand.getChildren().addAll(ergebnis, punkte);
	    
	    Label statistikLabel = new Label("Statistik:");
	    
	    // Tabelle
	    GridPane statistik = new GridPane();
	    statistik.setGridLinesVisible(true);
	    statistik.getColumnConstraints().add(new ColumnConstraints(60));	
	    statistik.getColumnConstraints().add(new ColumnConstraints(80));	
	    int zeilen=3;
	    for(int i=0; i<=zeilen; i++){
		     statistik.getRowConstraints().add(new RowConstraints(20));
	    }
	    statistik.add(new Label("Satznr."), 0, 0);
	    statistik.add(new Label("Zeit"), 0, 1);
	    statistik.add(new Label("..."), 0, 2);
	    
	    boxrechts.getChildren().addAll(spielstand, neuerSatz, statistikLabel, statistik);
	    boxrechts.setMaxHeight(0);
	    borderpane.setCenter(boxrechts);
	    
	// Statusanzeige
	    Label status = new Label("Satzstatus: Satz spielen");
	    status.getStyleClass().add("satzstatus");
	    BorderPane.setAlignment(status, Pos.CENTER_LEFT);
	    borderpane.setBottom(status);
	    
	    
		// Gruppe füllen
		root.getChildren().addAll(borderpane);
	}
	
	@Override public void start (Stage stage) throws Exception{
		init(stage);
		stage.setHeight(450);
		stage.setWidth(500);
		stage.setTitle("4 Gewinnt - untitled0815");
		stage.show();
		
	}
	
	public static void main(String[] args) {launch(args);}

	
}

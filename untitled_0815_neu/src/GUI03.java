	/**
	 * @author NHerentrey
	 * @param args
	 */

import javafx.application.*;
import javafx.scene.*;				//Scene bildet "Leinw�nde" in dem Rahmen
import javafx.stage.*;				//Stage ist der "Rahmen" der Applikation
import javafx.scene.control.*;		//F�r das Men�
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;

public class GUI03 extends Application {	
	
	public void init(Stage mainstage){
		Group root = new Group();
		Scene scene = new Scene(root);
		scene.getStylesheets().add("test.css");
		mainstage.setScene(scene);
		mainstage.setResizable(false);
		
		BorderPane borderpane = new BorderPane(); //setzt Layout/ Anordnung fest
		
	// Das Men�
		MenuBar menuBar = new MenuBar();
		menuBar.setMinWidth(500);
		
		//1. Men�punkt
		final MenuItem schlie�en = new MenuItem("Schlie�en"); // Untermen�
		final Menu datei = MenuBuilder.create().text("Datei").items(schlie�en).build();
		
		//2. Men�punkt
		final Menu optionen = new Menu("Optionen");
		
		//3. Men�punkt
		final MenuItem anleitung = new MenuItem("Spielanleitung");
		final Menu hilfe = MenuBuilder.create().text("Hilfe").items(anleitung).build();
				
		//Menup�nkt "Schlie�en"
		schlie�en.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent close){System.exit(0);}
		});
		
		//Men�punkt "Spielanleitung"
		anleitung.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent anleitung){
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
					}
				});
				
				//Anordnen
				VBox textUndButton = new VBox(100);
				textUndButton.getChildren().addAll(ueberschrift,text, close);
				rootAnleitung.getChildren().add(textUndButton);
			}
		});
		
		//Men�punkte zusammenf�hren
		menuBar.getMenus().addAll(datei, optionen,hilfe);
		
		borderpane.setTop(menuBar);
		
	//Anzeige, welcher Spieler man ist
		VBox spielanzeige = new VBox(); // zeigt Spieleranzeige und Spielfeld untereinander an
		
		HBox hSpieler = new HBox(20);
		hSpieler.setStyle("-fx-padding: 10; -fx-font-size: 16; -fx-font-weight: bold;");
		Circle tokenSpieler = new Circle(15.0f);
		Circle tokenGegner = new Circle(15.0f);
		tokenSpieler.getStyleClass().add("token-red");
		tokenGegner.getStyleClass().add("token-yellow");
		hSpieler.getChildren().addAll(new Label("Spieler:"), tokenSpieler, new Label("Gegner:"), tokenGegner);
		
	//Spielfeld
	    GridPane grid = new GridPane();
		Circle spielfeld[][] = new Circle[7][6];
	    
	    //Abst�nde zwischen Feldern
	    grid.setHgap(3);
	    grid.setVgap(3);
	    grid.setMaxHeight(200);
	    grid.setMaxWidth(250);
	    grid.setStyle("-fx-padding: 20;");
		
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
	    spielstand.setStyle("-fx-padding: 10;");
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
	    status.setStyle("-fx-padding: 20; -fx-font-size: 15; -fx-font-weight: bold;");
	    BorderPane.setAlignment(status, Pos.CENTER_LEFT);
	    borderpane.setBottom(status);
	    
	    
		// Gruppe f�llen
		root.getChildren().addAll(borderpane);
	}
	
	@Override public void start (Stage mainstage) throws Exception{
		init(mainstage);
		mainstage.setHeight(450);
		mainstage.setWidth(500);
		mainstage.setTitle("4 Gewinnt - untitled0815");
		mainstage.show();
		
	}
	
	public static void main(String[] args) {launch(args);}

	
}

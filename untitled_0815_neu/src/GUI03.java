	/**
	 * @author NHerentrey
	 * @param args
	 */

import javafx.application.*;
import javafx.scene.*;				//Scene bildet "Leinwände" in dem Rahmen
import javafx.stage.*;				//Stage ist der "Rahmen" der Applikation
import javafx.scene.control.*;		//Für das Menü
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
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
		//mainstage.setResizable(false);
		
		BorderPane borderpane = new BorderPane(); //setzt Layout/ Anordnung fest
		

	// Das Menü
		MenuBar menuBar = new MenuBar();
		menuBar.setMinWidth(820);
		
		//1. Menüpunkt
		final MenuItem neuesSpiel = new MenuItem("Neues Spiel");
		final MenuItem spielLaden = new MenuItem("Spiel laden");
		final MenuItem schließen = new MenuItem("Schließen");
		final Menu datei = MenuBuilder.create().text("Datei").items(neuesSpiel, spielLaden, schließen).build();
		
		//2. Menüpunkt
		final Menu optionen = new Menu("Optionen"); //Spielsteuerung einbinden
		
		//3. Menüpunkt
		final MenuItem anleitung = new MenuItem("Spielanleitung");
		final Menu hilfe = MenuBuilder.create().text("Hilfe").items(anleitung).build();
				
		//Menupünkt "Schließen"
		schließen.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent close){System.exit(0);}
		});
		
		//Menüpunkt "Spielanleitung"
		anleitung.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent anleitung){
				//Fenster mit Anleitung öffnen
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
				Button close = new Button("Schließen");
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
		
		//Menüpunkte zusammenführen
		menuBar.getMenus().addAll(datei, optionen,hilfe);
		
		borderpane.setTop(menuBar);
		
		
	//Spieleinstellungen
		HBox links = new HBox(5);
		
		GridPane einstellungen = new GridPane();
		einstellungen.setStyle("-fx-padding: 10;");
		einstellungen.setVgap(10);
		Label spieleinstellungen = new Label("Spieleinstellungen");
		spieleinstellungen.setStyle("-fx-font-size: 15; -fx-font-weight: bold;");
		
		//Spielerrolle definieren (X oder O)
		final ToggleButton rolleX = new ToggleButton("X");
		final ToggleButton rolleO = new ToggleButton("O");
		ToggleGroup rolleSetzen = new ToggleGroup();
		rolleX.setToggleGroup(rolleSetzen);
		rolleO.setToggleGroup(rolleSetzen);
		HBox rolle = new HBox();
		rolle.getStyleClass().addAll("textfeld");
		rolle.getChildren().addAll(rolleX, rolleO);
		
		//Spielstand definieren
		HBox spielstandEinstellen = new HBox();
	    final TextField spielstandSpieler = new TextField("0");
	    spielstandSpieler.setMaxWidth(30);
	    spielstandSpieler.getStyleClass().addAll("textfeld", "timeout");
		final TextField spielstandGegner = new TextField("0");
		spielstandGegner.setMaxWidth(30);
		spielstandGegner.getStyleClass().addAll("textfeld", "timeout");
		spielstandEinstellen.getChildren().addAll(spielstandSpieler, spielstandGegner);
		
		//Eingabefelder
		final TextField gegnername = new TextField("Name...");
		gegnername.getStyleClass().add("textfeld");
		gegnername.setMaxWidth(150);
		final TextField verzeichnispfad = new TextField("C:\\...");
		verzeichnispfad.getStyleClass().add("textfeld");
		verzeichnispfad.setMaxWidth(150);
		
		//Stepper Field für File-Abfrage
        HBox timeout1 = new HBox();
    	VBox pfeile1 = new VBox();
    	Button hoch1 = new Button("ᴧ"); hoch1.setMaxSize(10, 10);
    	hoch1.getStyleClass().add("timeoutButton");
    	Button runter1 = new Button("v"); runter1.setMaxSize(10, 10);
    	runter1.getStyleClass().add("timeoutButton");
    	pfeile1.getChildren().addAll(hoch1, runter1);
		final TextField fileabfrage = new TextField("0");
		fileabfrage.setMaxWidth(30);
		fileabfrage.getStyleClass().addAll("textfeld", "timeout");
    	timeout1.getChildren().addAll(fileabfrage, pfeile1, new Label("s"));

    	hoch1.setOnMouseClicked(new EventHandler<MouseEvent>(){
    		public void handle(MouseEvent arg0){
    			int timeoutFileabfruf;
    			timeoutFileabfruf = Integer.parseInt(fileabfrage.getText());
    			timeoutFileabfruf++;
    			String zeitz = String.valueOf(timeoutFileabfruf);
    			fileabfrage.setText(zeitz);
    		}
    	});
    	runter1.setOnMouseClicked(new EventHandler<MouseEvent>(){
    		public void handle(MouseEvent arg0){
    			int timeoutFileabfruf;
    			timeoutFileabfruf = Integer.parseInt(fileabfrage.getText());
    			timeoutFileabfruf--;
    			String zeitz = String.valueOf(timeoutFileabfruf);
    			fileabfrage.setText(zeitz);
    		}
    	});
    
    	//Stepper Field für Zugzeit
    	HBox timeout2 = new HBox();
    	VBox pfeile2 = new VBox();
    	Button hoch2 = new Button("ᴧ"); hoch2.setMaxSize(10, 10);
    	hoch2.getStyleClass().add("timeoutButton");
    	Button runter2 = new Button("v"); runter2.setMaxSize(10, 10);
    	runter2.getStyleClass().add("timeoutButton");
    	pfeile2.getChildren().addAll(hoch2, runter2);
    	final TextField zugzeit = new TextField("0");
    	zugzeit.setMaxWidth(30);
    	zugzeit.getStyleClass().addAll("textfeld", "timeout");
    	timeout2.getChildren().addAll(zugzeit, pfeile2, new Label("s"));

    	hoch2.setOnMouseClicked(new EventHandler<MouseEvent>(){
    		public void handle(MouseEvent arg0){
    			int timeoutZugzeit;
    			timeoutZugzeit = Integer.parseInt(zugzeit.getText());
    			timeoutZugzeit++;
    			String zeitstr = String.valueOf(timeoutZugzeit);
    			zugzeit.setText(zeitstr);
    		}
    	});
	
    	runter2.setOnMouseClicked(new EventHandler<MouseEvent>(){
    		public void handle(MouseEvent arg0){
    			int timeoutZugzeit;
    			timeoutZugzeit = Integer.parseInt(zugzeit.getText());
    			timeoutZugzeit--;
    			String zeitstr = String.valueOf(timeoutZugzeit);
    			zugzeit.setText(zeitstr);
    		}
    	});
		
		final Button spielStarten = new Button("Spiel starten");

		einstellungen.add(spieleinstellungen, 1, 1);
		einstellungen.add(new Label("Rolle:"), 1, 2);
		einstellungen.add(new Label("Spielstand"), 1, 3);
		einstellungen.add(new Label("Gegnername"), 1, 4);
		einstellungen.add(new Label("Verzeichnispfad:"), 1, 5);
		einstellungen.add(new Label("Timeout File-Abfrage:"), 1, 6);
		einstellungen.add(new Label("Timeout Zugzeit"), 1, 7);

		einstellungen.add(rolle, 2, 2);
		einstellungen.add(spielstandEinstellen, 2, 3);
		einstellungen.add(gegnername, 2,4);
		einstellungen.add(verzeichnispfad, 2,5);
		einstellungen.add(timeout1, 2,6);
		einstellungen.add(timeout2, 2, 7);
		einstellungen.add(spielStarten, 1, 8);
		
		
		//Trennung zwischen Einstellungen und Spielfeld	
		Line trennlinie = new Line(110, 0, 110, 500);
		links.getChildren().addAll(einstellungen, trennlinie);
		
		borderpane.setLeft(links);
		
		
	//Pane für die Anzeige des Spielstands, Spielfläche, Statistik etc.
		BorderPane spielflaeche = new BorderPane();		
		
	//Anzeige, welcher Spieler man ist
		VBox spielanzeige = new VBox(); // zeigt Spieleranzeige, Spielstand, Spielfeld untereinander an
		spielanzeige.setStyle("-fx-padding:20");
		
		//Spieleranzeige
		HBox hSpieler = new HBox(20);
		hSpieler.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
		Circle tokenSpieler = new Circle(15.0f);
		Circle tokenGegner = new Circle(15.0f);
		tokenSpieler.getStyleClass().add("token-red");
		tokenGegner.getStyleClass().add("token-yellow");
		hSpieler.getChildren().addAll(new Label("Spieler:"), tokenSpieler, new Label("Gegner:"), tokenGegner);
		
		
		// Spielstand
	    HBox spielstandAnzeige = new HBox();
		Label spielstand = new Label("Spielstand:");
		spielstand.getStyleClass().add("punkte");
	    final Label punkteSpieler = new Label();
	    punkteSpieler.getStyleClass().add("punkte");
	    final Label punkteGegner = new Label();
	    punkteGegner.getStyleClass().add("punkte");
		Label vs = new Label(" : ");
	    vs.getStyleClass().add("punkte");
	    spielstandAnzeige.getChildren().addAll(spielstand, punkteSpieler, vs, punkteGegner);
	    
	//Spielfeld
	    GridPane feld = new GridPane();
		Circle spielfeld[][] = new Circle[7][6];
	    
	    //Abstände zwischen Feldern
	    feld.setHgap(3);
	    feld.setVgap(3);
	    feld.setMaxHeight(200);
	    feld.setMaxWidth(250);
	    feld.setStyle("-fx-padding:5; -fx-background-color: blue;");
		
	    for (int i = 0; i < 7; i++)
	    {
	      for (int j = 0; j < 6; j++)
	      {
	        spielfeld[i][j] = new Circle(20.0f);
	        spielfeld[i][j].getStyleClass().add("token");
	        String ii = String.valueOf(i);
	        String jj = String.valueOf(j);
	        spielfeld[i][j].setId("token" + ii + jj);
	        feld.add(spielfeld[i][j], i, j);
	      }
	    }
	    
	    for (int i = 0; i < 7; i++)
	    {
	      for (int j = 0; j < 6; j++)
	      {
	    	  spielfeld[i][0].getStyleClass().add("token-red");
	    	  spielfeld[i][1].getStyleClass().add("token-yellow");
	    	  spielfeld[i][2].getStyleClass().add("token-red");
	    	  spielfeld[i][3].getStyleClass().add("token-yellow");
	    	  spielfeld[i][4].getStyleClass().add("token-red");
	    	  spielfeld[i][5].getStyleClass().add("token-yellow");
	      }
	    }
	    
		spielanzeige.getChildren().addAll(hSpieler, spielstandAnzeige, feld);
	    spielflaeche.setLeft(spielanzeige);

	// Rechte Spalte
	    VBox boxrechts = new VBox(10);
	    
	    final Button neuerSatz = new Button("neuen Satz spielen");
	    neuerSatz.setDisable(true);
	    	    
		final Button satzAbbrechen = new Button("Satz abbrechen");
		satzAbbrechen.setDisable(true);
		
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
	    
	    final Button logAnzeigen = new Button("Log anzeigen");
	    logAnzeigen.setOnMouseClicked(new EventHandler<MouseEvent>(){
	    	public void handle(MouseEvent arg0){
	    		//Fenster mit Log öffnen
				final Stage stageAnleitung = new Stage();
				Group rootLog = new Group();
				Scene sceneLog = new Scene(rootLog, 400,400, Color.WHITESMOKE);
				stageAnleitung.setScene(sceneLog);
				stageAnleitung.centerOnScreen();
				stageAnleitung.show();
				
				//Inhalt
				Text ueberschrift = new Text(20, 20,"Log");
				ueberschrift.setFill(Color.BLACK);
				ueberschrift.setEffect(new Lighting());
				Button close = new Button("Schließen");
				close.setOnAction(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent close){
						stageAnleitung.close();
					}
				});
				//Anordnen
				VBox textUndButton = new VBox(100);
				textUndButton.getChildren().addAll(ueberschrift, close);
				rootLog.getChildren().add(textUndButton);
	    	}
	    });
	    logAnzeigen.setDisable(true);
	    
	    boxrechts.getChildren().addAll(neuerSatz, satzAbbrechen, statistikLabel, statistik, logAnzeigen);
	    boxrechts.setMaxHeight(0);
	    spielflaeche.setCenter(boxrechts);
	    
	    
	// Statusanzeige
	    Label status = new Label("Satzstatus: Satz spielen");
	    status.setStyle("-fx-padding: 20; -fx-font-size: 15; -fx-font-weight: bold;");
	    BorderPane.setAlignment(status, Pos.TOP_LEFT);
	    spielflaeche.setBottom(status);
	    
	    borderpane.setCenter(spielflaeche);
	    
	    
		// Gruppe füllen
		root.getChildren().addAll(borderpane);
		
		
	//neues Spiel starten/ Spiel beenden (Eingaben und Buttons aktivieren/ deaktivieren
		spielStarten.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent arg0){
				if (spielStarten.getText()=="Spiel starten"){
					rolleX.setDisable(true);
					rolleO.setDisable(true);
					spielstandSpieler.setDisable(true);
					spielstandGegner.setDisable(true);
					gegnername.setDisable(true);
					verzeichnispfad.setDisable(true);
					fileabfrage.setDisable(true);
					zugzeit.setDisable(true);
					spielStarten.setText("Spiel beenden");
					punkteSpieler.setText(spielstandSpieler.getText());
					punkteGegner.setText(spielstandGegner.getText());
					neuerSatz.setDisable(false);
					satzAbbrechen.setDisable(false);
					logAnzeigen.setDisable(false);
				}
				else{
					rolleX.setDisable(false);
					rolleO.setDisable(false);
					spielstandSpieler.setDisable(false);
					spielstandGegner.setDisable(false);
					gegnername.setDisable(false);
					verzeichnispfad.setDisable(false);
					fileabfrage.setDisable(false);
					zugzeit.setDisable(false);
					spielStarten.setText("Spiel starten");
					neuerSatz.setDisable(true);
					satzAbbrechen.setDisable(true);
					logAnzeigen.setDisable(true);
				}

			}
		});
	}
	
	
	@Override public void start (Stage mainstage) throws Exception{
		init(mainstage);
		mainstage.setHeight(550);
		mainstage.setWidth(820);
		mainstage.setTitle("4 Gewinnt - untitled0815");
		mainstage.show();
	}
	
	public static void main(String[] args) {launch(args);}

	
}
package test_utilities;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import utilities.*;
import utilities.communication.CommunicationServer;
import utilities.events.EventDispatcher;
import utilities.events.GameEvent;
import utilities.events.GameEventListener;
import core.Constants;


public class test_CommunicationServer implements GameEventListener {
	private GameEvent[] events;
	private CommunicationServer comServer;
	private String path;
	private char role;
	private int timeoutServer;
	
	public test_CommunicationServer() throws Exception{
		// Testdaten definieren {Datei Inhalt, Eventtyp, Argument, Eventtyp2, Argument2}
		String [][] testdata = {
								{"<?xml version='1.0' encoding='utf-8'?><content><freigabe>true</freigabe>"+
										"<satzstatus>Satz spielen</satzstatus><gegnerzug>-1</gegnerzug><sieger>offen</sieger></content>", 
										GameEvent.Type.OppMove.toString(),"-1",null,null},
								{"<?xml version='1.0' encoding='utf-8'?><content><freigabe>true</freigabe>"+
										"<satzstatus>Satz spielen</satzstatus><gegnerzug>2</gegnerzug><sieger>offen</sieger></content>", 
										GameEvent.Type.OppMove.toString(),"2",null,null},
								{"<?xml version='1.0' encoding='utf-8'?><content><freigabe>false</freigabe>"+
										"<satzstatus>beendet</satzstatus><gegnerzug>-1</gegnerzug><sieger>Spieler X</sieger></content>", 
										GameEvent.Type.EndSet.toString(),"-1",GameEvent.Type.WinnerSet.toString(),String.valueOf(Constants.xRole)} 
								};		
		//Pfad, eigene Rolle, TimeoutServer
		path = "C:/viergewinnt2/";
		role = Constants.xRole;
		timeoutServer = 300;
		
		events = new GameEvent[2];
		
		//ComServer initialisieren
		comServer = CommunicationServer.getInstance();
		EventDispatcher.getInstance().addListener(this);
		
		System.out.println("TEST - DATEN LESEN");
		for(int i = 0; i < testdata.length; i++){
			processTestData(testdata[i]);
		}
		
		int[] resp = {2,4,6,8,0,2};
		
		//Daten schreiben
		System.out.println("TEST - DATEN SCHREIBEN");
		for (int i = 0; i < resp.length; i++) {
			char role = "X".charAt(0);
			System.out.println("zu schreiben: " + resp[i]);
			comServer.writeMove((byte) resp[i], path,role);
			
			//Datei lesen
			File antwort = new File(path +"spielerx2server.txt");
			FileReader reader = new FileReader(antwort);
			BufferedReader r = new BufferedReader(reader);
			System.out.println("gelesen: " + r.readLine());
			System.out.println("###########");
			antwort.delete();
			reader.close();
			r.close();
			
		}
		
	}
	
	
	private void processTestData(String[] data){
		//ComServer starten
				
	
		events[0] = null;
		events[1] = null;
		
		//Datei schreiben ("Server2spielerx") -> write(data[0]);
		System.out.println("Schreibe Datei --> " + path);
		try{
			File file = new File(path + "server2spielerx.xml");
			FileWriter schreiber = new FileWriter(file);
			schreiber.write(data[0]);
			schreiber.flush();
			schreiber.close();
		}catch (Exception e){
			System.err.println(e);
		}

		
		comServer = CommunicationServer.getInstance();
		comServer.init(timeoutServer, path, role);
		comServer.enableReading(false);
		//Auf Event warten
		int ctr = 0;
		System.out.println("Warte auf Events");
		while(events[0] == null){ 
			try {
				if(ctr < 20){
					ctr ++;
					Thread.sleep(500);
					//System.out.println("Warte auf Events");
				}else{
					//System.out.println("Breche warten ab");
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//Event mit soll abgleichen
		if(events[0] != null){
			if(events[0].getType().toString().equals(data[1]) &&  events[0].getArg().equals(data[2])){
				System.out.println("Event " + events[0].getType().toString() + " korrekt angekommen");
			}else{
				System.out.println("Event " + events[0].getType().toString() + " nicht korrekt angekommen");
			}
		}else{
			System.out.println("Event " + data[1] + " nicht angekommen");
		}
		//Thread.yield(); //Dem zweiten Event eine Chance geben
		if(data[3] != null){
			if(events[1] != null){
				if(events[1].getType().toString().equals(data[3]) &&  events[1].getArg().equals(data[4])){
					System.out.println("Event " + events[0].getType().toString() + " korrekt angekommen");
				}else{
					System.out.println("Event " + events[0].getType().toString() + " nicht korrekt angekommen");
				}
			}else{
				System.out.println("Event " + data[3] + " nicht angekommen");
			}
		}
		System.out.println("###############################################");
		comServer.disableReading();
		
		//Datei wieder löschen
		try{
			File file = new File(path + "\\server2spielerx.xml");
			file.delete();
		}catch (Exception e){
			System.err.println(e);
		}
	}

	@Override
	public void handleEvent(GameEvent e) throws Exception {
		if(events[0] == null){
			events[0] = e;
		}else if(events[1] == null){
			events[1] = e;
		}
		
		
	}
	
	public static void main(String[] args) {		
		try {
			new test_CommunicationServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

}

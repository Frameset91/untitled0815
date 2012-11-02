/**
 * kapselt den Thread, der bis zum Zug-Timeout läuft und den nächsten KI-Zug berechnet
 * 
 * @author Johannes Riedel
 */

package utilities.KI;

import java.util.ArrayList;
import core.Constants;

import utilities.*;
import utilities.events.*;

public class KIThread extends Thread {
	
	private Boolean self;
	private Boolean opp;
	private int suchtiefe = 0;

	private Boolean[][] spielfeld;
	private Byte[] watermark = new Byte[Constants.gamefieldcolcount];
	private ArrayList<Byte> moeglichezuege;

	private KI ki; 
	
	private byte oppMove;
	
	private int searchedvertices;
	
	public KIThread(KI ki, Byte[] watermark, Boolean[][] spielfeld, ArrayList<Byte> moeglichezuege, boolean self, byte oppMove) {
		setDaemon(true);
		setName("KI-Thread calculateNextMove");
		
		this.ki = ki;
		this.self = self;
		this.opp = !this.self;
		this.spielfeld = spielfeld;
		this.moeglichezuege = moeglichezuege;
		this.watermark = watermark;
		this.oppMove = oppMove;
		this.searchedvertices = 0;
		
	}
	
public void run(){
	int i=1;
	while(!isInterrupted() && i<=20)
		{
		Log.getInstance().write("Starte Alpha-Beta-Suche Tiefe " + i);
		alphabetasuche(i);
		Log.getInstance().write("Alpha-Beta-Suche Tiefe " + i + " abgeschlossen. Ergebnis: " + ki.getNextMove());
		i += 2;
		}
}

private void alphabetasuche(int searchdepth){
	this.suchtiefe = searchdepth;
	
	RatingResult bewertung = null;
		
	if(oppMove != -1)
		{
		bewertung = Bewertung(true);
		// prüfe ob Gegner gewonnen
		if(java.lang.Math.abs(bewertung.getRating())==Constants.KImaxbewertung)
			{
			String ausgabe = "";
			for(Position onechip: bewertung.getWinningchips())
				ausgabe += onechip + ";";
			if(bewertung.getRating()==Constants.KImaxbewertung)
				if(self)
					ausgabe += Constants.xRole;
				else
					ausgabe += Constants.oRole;
			else
				if(self)
					ausgabe += Constants.oRole;
				else
					ausgabe += Constants.xRole;
			
			Log.getInstance().write("Feuere WinDeteced-Event mit Parameter: " + ausgabe);
			try {
				EventDispatcher.getInstance().triggerEvent(new GameEvent(GameEvent.Type.WinDetected, ausgabe));
				} 
			catch (Exception e) {
				e.printStackTrace();
				}
			ki.setNextMove((byte) -1);
			}
		}
	// nur Spielzug berechnen, wenn Gegner noch nicht gewonnen
	if(oppMove == -1 || bewertung.getRating()!=-Constants.KImaxbewertung)
		{

		
		long laufzeit = System.nanoTime();
		byte spalte = (byte) Max(suchtiefe, -Constants.KImaxbewertung, Constants.KImaxbewertung);
		GameFieldStatistics.insertchipandupdate(watermark, spielfeld, moeglichezuege, self, spalte);
		Log.getInstance().write("KI hat Stein in Spalte " + String.valueOf(spalte)
				+ " gesetzt! Rechenzeit in ms: " + ((System.nanoTime()-laufzeit)/1000000) + ". Durchsuchte "
				+ "Knoten :" + searchedvertices);
		bewertung = Bewertung(true);
		//Log.getInstance().write("KI: Bewertung des Feldes:" + bewertung.getRating());
		if(java.lang.Math.abs(bewertung.getRating())==Constants.KImaxbewertung)
			{
			
			String ausgabe = "";
			for(Position onechip: bewertung.getWinningchips())
				ausgabe += onechip + ";";
			if(bewertung.getRating()==Constants.KImaxbewertung)
				if(self)
					ausgabe += Constants.xRole;
				else
					ausgabe += Constants.oRole;
			else
				if(self)
					ausgabe += Constants.oRole;
				else
					ausgabe += Constants.xRole;
			
			Log.getInstance().write("Feuere WinDeteced-Event mit Parameter: " + ausgabe);
			try {
				EventDispatcher.getInstance().triggerEvent(new GameEvent(GameEvent.Type.WinDetected, ausgabe));
				} 
			catch (Exception e) {
				e.printStackTrace();
				}
			}
		ki.setNextMove(spalte);
		GameFieldStatistics.removechipandupdate(watermark, spielfeld, moeglichezuege, spalte);
		}
	
}

/**
 * 
 * @return Bewertungszahl des aktuellen Feldes: Je höher, umso besser ist die 
 * Situation für uns
 */
private RatingResult Bewertung(){
	return Bewertung(false);
}
 private RatingResult Bewertung(boolean findwinningchips){
	 // Tabelle, gibt an, ob für bestimmtes Feld schon Falle existiert und
	 // wie groß die Falle ist, die durch dieses Feld ermöglicht wird
	 int[][] noetigfuerfalleself = new int[Constants.gamefieldcolcount]
			 [Constants.gamefieldrowcount];
	 for (int i = 0; i < noetigfuerfalleself.length; i++) {
		for (int j = 0; j < noetigfuerfalleself[0].length; j++) {
			noetigfuerfalleself[i][j]=0;
		}
	}
	 
	 int[][] noetigfuerfalleopp = new int[Constants.gamefieldcolcount]
			 [Constants.gamefieldrowcount];
	 for (int i = 0; i < noetigfuerfalleopp.length; i++) {
		for (int j = 0; j < noetigfuerfalleopp[0].length; j++) {
			noetigfuerfalleopp[i][j]=0;
		}
	}
	 
	 // ordne Spielfeld + neuem Zug Bewertungszahl zu
	int bewertung = 0;
	// #####################################
	// zähle offene Fallen horizontal:
	// 	#####################################
	 for (byte i = 0; i < Constants.gamefieldrowcount; i++) 
	 //for (byte i = 0; i <=0; i++) // nur untere Reihe prüfen
	 	{
		// leere Position, an der man setzen könnte
		ArrayList<Position> freiesteine= new ArrayList<Position>(); 
		ArrayList<Trap> bisherbesteFalleSelf = new ArrayList<Trap>(0);
		bisherbesteFalleSelf.add(new Trap((byte) 0));
		ArrayList<Trap> bisherbesteFalleOpp = new ArrayList<Trap>(0);
		bisherbesteFalleOpp.add(new Trap((byte) 0));
		byte anzself=0;
		byte anzopp=0;
		// ersten drei Steine befüllen
		for (byte k = 0; k <= 2; k++) 
		 	{
			if(spielfeld[k][i]==null)
				freiesteine.add(new Position(k,i));
			else
				if(spielfeld[k][i]==opp)
					anzopp++;
				else
					anzself++;
		 	}
		
		byte j = 3;		// Ende der aktuell untersuchten 4er-Kette
		do
		 	{ 
			// alte Position löschen
			if(j!=3)
				if(spielfeld[j-4][i]==null)
					freiesteine.remove(0);
				else
					if(spielfeld[j-4][i]==opp)
						anzopp--;
					else
						anzself--;

			// neue Position hinzuzählen
			if(spielfeld[j][i]==null)
				freiesteine.add(new Position(j,i));
			else
				if(spielfeld[j][i]==opp)
					anzopp++;
				else
					anzself++;
			
			// neue 4er-Falle gefunden?
			// ich selber
			if(anzopp==0 && anzself>0)
				{
				// größere Falle als bisher in dieser Reihe gefunden?
				if(anzself>bisherbesteFalleSelf.get(0).getSize())
					{
					// prüfen, ob neue Falle besser ist als die an den freien
					// Chips schon bekannten
					boolean besseralsanderefallen=besseralsanderefallen(freiesteine,
							anzself,noetigfuerfalleself);
					if(besseralsanderefallen)
						{
						if(anzself==4 && findwinningchips)
							{
							ArrayList<Position> winningchips = new ArrayList<Position>(0);
							winningchips.add(new Position((byte) (j-3),i));
							winningchips.add(new Position((byte) (j-2),i));
							winningchips.add(new Position((byte) (j-1),i));
							winningchips.add(new Position((byte) (j-0),i));
							
							return new RatingResult(Constants.KImaxbewertung, winningchips);
							}
						// dann (kleinere) löschen und neue speichern
						bisherbesteFalleSelf.clear();
						bisherbesteFalleSelf.add(new Trap(anzself,freiesteine));
						
						// vermerken, dass bestimmte freie Steine jetzt
						// für Falle der Größe X gebraucht werden
						for (Position einfreierstein : freiesteine) 
							{
							noetigfuerfalleself[einfreierstein.getX()]
									[einfreierstein.getY()]=anzself;
							}
						}
					}
				else
					// falls neue Falle genauso groß wie bisher bekannte trotzdem speichern ...
					if(anzself==bisherbesteFalleSelf.get(0).getSize())
						{
						boolean besseralsanderefallen=besseralsanderefallen(freiesteine,
								anzself,noetigfuerfalleself);
						// ... aber nur, falls die Steine auf neue freie Steine fußt
						if(besseralsanderefallen)
							{
							bisherbesteFalleSelf.add(new Trap(anzself,freiesteine));
							}
						}
				}
			// mögliche Gegner-Falle gefunden
			if(anzself==0 && anzopp>0)
				{
				// größere Falle als bisher gefunden?
				if(anzopp>bisherbesteFalleOpp.get(0).getSize())
					{
					// prüfen, ob neue Falle besser ist als die an den freien
					// Chips schon bekannten
					boolean besseralsanderefallen=besseralsanderefallen(freiesteine,
							anzopp,noetigfuerfalleopp);
					if(besseralsanderefallen)
						{
						if(anzopp==4 && findwinningchips)
						{
						ArrayList<Position> winningchips = new ArrayList<Position>(0);
						winningchips.add(new Position((byte) (j-3),i));
						winningchips.add(new Position((byte) (j-2),i));
						winningchips.add(new Position((byte) (j-1),i));
						winningchips.add(new Position((byte) (j-0),i));
						
						return new RatingResult(-Constants.KImaxbewertung, winningchips);
						}
						// dann (kleinere) löschen und neue speichern
						bisherbesteFalleOpp.clear();
						bisherbesteFalleOpp.add(new Trap(anzopp,freiesteine));
						
						// vermerken, dass bestimmte freie Steine jetzt
						// für Falle der Größe X gebraucht werden
						for (Position einfreierstein : freiesteine) 
							{
							noetigfuerfalleopp[einfreierstein.getX()]
									[einfreierstein.getY()]=anzopp;
							}
						}
					}
				else
					// falls neue Falle genauso groß wie bisher bekannte trotzdem speichern
					if(anzopp==bisherbesteFalleOpp.get(0).getSize())
						{
						boolean besseralsanderefallen=besseralsanderefallen(freiesteine,
								anzopp,noetigfuerfalleopp);
						// ... aber nur, falls die Steine auf neue freie Steine fußt
						if(besseralsanderefallen)
							{
							bisherbesteFalleOpp.add(new Trap(anzopp,freiesteine));
							}
						}
				}
			j++;
		 	} // Ende jede-Position-durchgehen-do
		while(j<Constants.gamefieldcolcount);
		// beste gefundene(n) Falle(n) ausgeben
		for (Trap einefalle : bisherbesteFalleSelf)
			if(einefalle.getSize()>0)
				{
				if(einefalle.getSize()==4)
					return new RatingResult(Constants.KImaxbewertung);
				else
					bewertung += einefalle.getSize()*einefalle.getSize();
					}
		for (Trap einefalle : bisherbesteFalleOpp)
			if(einefalle.getSize()>0)
				{
				if(einefalle.getSize()==4)
					return new RatingResult(-Constants.KImaxbewertung);
				else
					bewertung -= einefalle.getSize()*einefalle.getSize();
				}
	 	}
	// #####################################
	// zähle offene Fallen vertikal:
	// #####################################
	// jede Spalte durchgehen
	for (byte i = 0; i < Constants.gamefieldcolcount; i++) 
		{
		// Spalte überhaupt belegt?
		if(watermark[i] != -1)
			{
			// von oben nach unten gleiche Steine zählen
			byte anzsteine = 1; // wie viele gleiche sind übereinander gestapelt
			byte j = watermark[i];
			while (j>=1 && anzsteine<4 && spielfeld[i][j-1]==spielfeld[i][j]) 
				{
				anzsteine++;
				j--;
				}
			// ist in dieser Reihe noch eine 4er-Kette möglich?
			if(watermark[i]+(4-anzsteine)<=Constants.gamefieldrowcount - 1)
				{
				// für wen ist die Falle?
				if(spielfeld[i][watermark[i]]==self)
					if(anzsteine==4)
						{
						ArrayList<Position> winningchips = new ArrayList<Position>(0);
						winningchips.add(new Position(i,(byte) (j+3)));
						winningchips.add(new Position(i,(byte) (j+2)));
						winningchips.add(new Position(i,(byte) (j+1)));
						winningchips.add(new Position(i,(byte) (j)));
						return new RatingResult(Constants.KImaxbewertung, winningchips);
						}
					else
						bewertung += anzsteine * anzsteine;
				else
					if(anzsteine==4)
						{
						ArrayList<Position> winningchips = new ArrayList<Position>(0);
						winningchips.add(new Position(i,(byte) (j+3)));
						winningchips.add(new Position(i,(byte) (j+2)));
						winningchips.add(new Position(i,(byte) (j+1)));
						winningchips.add(new Position(i,(byte) (j)));
						return new RatingResult(-Constants.KImaxbewertung, winningchips);
						}
					else
						bewertung -= anzsteine * anzsteine;
				}
			}
		} 
	
	// #####################################
	// zähle diagonale Fallen Richtung nach rechts oben:
	// #####################################
	RatingResult ratingresult;
	ratingresult = ueberpruefediagonale(bewertung,(byte)1,(byte)1,(byte) 0,(byte) (Constants.gamefieldrowcount-4),
			findwinningchips);
	if(ratingresult.getRating() == Constants.KImaxbewertung || ratingresult.getRating() == -Constants.KImaxbewertung)
		return ratingresult;
	
	ratingresult = ueberpruefediagonale(ratingresult.getRating(),(byte)-1,(byte)1,
			(byte)(Constants.gamefieldcolcount-1),(byte) (Constants.gamefieldrowcount-4),findwinningchips);
	if(ratingresult.getRating() == Constants.KImaxbewertung || ratingresult.getRating() == -Constants.KImaxbewertung)
		return ratingresult;
	else
		bewertung = ratingresult.getRating();
	
	return new RatingResult(bewertung);
 }
 
private RatingResult ueberpruefediagonale(int bewertung, byte deltax, byte deltay,byte _startx, byte _starty,
		boolean findwinningchips) {
	byte startx = _startx;
	byte starty = _starty;
	byte i;
	byte j;
	do{
		i = startx;
		j = starty;
		ArrayList<Position> freiesteine= new ArrayList<Position>();
		byte anzself=0;
		byte anzopp=0;
		// ersten drei Steine befüllen
		for (byte k = 0; k <= 2; k++) 
		 	{
			if(spielfeld[i+k*deltax][j+k*deltay]==null)
				freiesteine.add(new Position((byte) (i+k*deltax), (byte) (j+k*deltay)));
			else
				if(spielfeld[i+k*deltax][j+k*deltay]==opp)
					anzopp++;
				else
					anzself++;
		 	}
		i = (byte) (startx+3*deltax);
		j = (byte) (starty+3*deltay);
		do
	 	{ 
		// alte Position löschen
		if(i!=startx+3*deltax && j!=starty+3*deltay)
			if(spielfeld[i-4*deltax][j-4*deltay]==null)
				freiesteine.remove(0);
			else
				if(spielfeld[i-4*deltax][j-4*deltay]==opp)
					anzopp--;
				else
					anzself--;
		
		// neue Position hinzuzählen
		if(spielfeld[i][j]==null)
			freiesteine.add(new Position(i,j));
		else
			if(spielfeld[i][j]==opp)
				anzopp++;
			else
				anzself++;
		
		// mögliche Falle gefunden
		if(anzopp==0 && anzself>0)
			{
		
			if(anzself==4)
				{
				ArrayList<Position> winningchips = new ArrayList<Position>(0);
				
				winningchips.add(new Position((byte) (i),j));
				winningchips.add(new Position((byte) (i-deltax),(byte) (j-deltay)));
				winningchips.add(new Position((byte) (i-2*deltax),(byte) (j-2*deltay)));
				winningchips.add(new Position((byte) (i-3*deltax),(byte) (j-3*deltay)));
				return new RatingResult(Constants.KImaxbewertung, winningchips);
				}
			else
				bewertung += anzself*anzself;
			}
			
					
		// mögliche Gegner-Falle gefunden
		if(anzself==0 && anzopp>0)
			{
			if(anzopp==4)
				{
				ArrayList<Position> winningchips = new ArrayList<Position>(0);
				
				winningchips.add(new Position((byte) (i),j));
				winningchips.add(new Position((byte) (i-deltax),(byte) (j-deltay)));
				winningchips.add(new Position((byte) (i-2*deltax),(byte) (j-2*deltay)));
				winningchips.add(new Position((byte) (i-3*deltax),(byte) (j-3*deltay)));
				return new RatingResult(-Constants.KImaxbewertung, winningchips);
				}
			else
				bewertung -= anzopp*anzopp;
			}
		
			
		i += deltax;
		j += deltay;
		}
		while (i>=0 && i <= Constants.gamefieldcolcount-1 && j <= Constants.gamefieldrowcount-1);
		
		if(starty!=0)
			starty--;
		else
			if(deltax>0)
				startx++;
			else
				startx--;
	}
	while((startx<=3 && deltax>0) || (startx>=3 && deltax<0));
	return new RatingResult(bewertung);
	
}
private boolean besseralsanderefallen(
		ArrayList<Position> freiesteine, byte anzself, int[][] noetigfuerfalleself) {
boolean result=true;
for (Position einfreierstein : freiesteine)
	if(noetigfuerfalleself[einfreierstein.getX()]
		[einfreierstein.getY()]>=
		anzself)
			result=false;
return result;
}	 

/**
 * 
 * @param tiefe Wie tief soll gesucht werden?
 * @param alpha Aktueller alpha-Wert
 * @param beta Aktueller Beta-Wert
 * @return Bewertungszahl des besten Spielzuges
 */
private int Max(int tiefe, int alpha, int beta) {
   if(isInterrupted())
		return -1;
   if (tiefe == 0)
       return Bewertung().getRating();
   // GeneriereMoeglicheZuege();
   ArrayList<Byte> moeglichezuegelokal = new ArrayList<Byte>(0);
   for (int i = 0; i < moeglichezuege.size(); i++)
	   moeglichezuegelokal.add(moeglichezuege.get(i));
    int localAlpha = -Constants.KImaxbewertung; //solange wir noch keine bessere Idee haben
    											// sind alle Züge für uns extrem schlecht
    byte i=0; //aktuell geprüfter Zug
    byte besterspielzug=-1;
    byte aktzuginspalte=-1;
    int wert;
    // solange noch Züge möglich sind
    while (i < moeglichezuegelokal.size())
	    {
    	searchedvertices++;
	    aktzuginspalte = moeglichezuegelokal.get(i);
	    GameFieldStatistics.insertchipandupdate(watermark, spielfeld, moeglichezuege, self, aktzuginspalte);
	    // Der Gegner ist dran und wird sich für den Zug entscheiden, bei dem er am besten ist
	    // (== Bewertungsfunktion minimal)
	    wert = Bewertung().getRating();
	    if(wert<Constants.KImaxbewertung)
	    	wert = (int) (Min(tiefe-1, alpha, beta));    
	    GameFieldStatistics.removechipandupdate(watermark, spielfeld, moeglichezuege, aktzuginspalte);
		if (wert > (int) (localAlpha))       
			{          
			besterspielzug = aktzuginspalte;
			
			if (wert >= beta)   
				if(tiefe==suchtiefe)
			    	return besterspielzug;
				else
					return wert;   // Beta-Cutoff-> Dieser Knoten muss nicht weiter verfolgt werden
				// weil im letzten untersuchten Zug ein so guter Wert für den Gegner gefunden 
				// wurde, das wir uns lieber für einen schon bekannten Spielzug, bei dem
				// der Gegner schlechter dasteht, entscheiden
			localAlpha = wert;
			
			if (wert > alpha)
				alpha = wert;       
			}   
		i++;
    }
    if(besterspielzug==-1)
    	besterspielzug = moeglichezuegelokal.get(0);
    if(tiefe==suchtiefe)
    	return besterspielzug;
    return localAlpha;
 }
/**
 * 
 * @param tiefe Wie tief soll gesucht werden?
 * @param alpha Aktueller alpha-Wert
 * @param beta Aktueller Beta-Wert
 * @return Bewertungszahl des schlechtesten Spielzuges
 */
int Min(int tiefe, int alpha, int beta) {
	if(isInterrupted())
		return -1;
	if (tiefe == 0 )
		return Bewertung().getRating();
	ArrayList<Byte> moeglichezuegelokal = new ArrayList<Byte>(0);
	for (int i = 0; i < moeglichezuege.size(); i++)
		   moeglichezuegelokal.add(moeglichezuege.get(i));
	int localBeta = Constants.KImaxbewertung;   
	byte i=0; //aktuell geprüfter Zug
	byte aktzuginspalte=-1;
	int wert;
	while (i < moeglichezuegelokal.size())
		{
		searchedvertices++;
		aktzuginspalte = moeglichezuegelokal.get(i);
		GameFieldStatistics.insertchipandupdate(watermark, spielfeld, moeglichezuege, opp, aktzuginspalte);
	    wert = Bewertung().getRating();
	    if(wert> -Constants.KImaxbewertung)
	    	wert = (int) (Max(tiefe-1, alpha, beta));       
	    GameFieldStatistics.removechipandupdate(watermark, spielfeld, moeglichezuege, aktzuginspalte);
		if (wert < (int) (localBeta))       
			{          
			   if (wert <= alpha)             
				   return wert;      // Alpha-Cutoff    
			   localBeta = wert;          
			   if (wert < beta)             
				   beta = wert;       
			}
		i++;
		}
	return localBeta;
	}

	
}

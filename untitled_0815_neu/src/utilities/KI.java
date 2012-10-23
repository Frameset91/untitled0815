package utilities;
import java.util.ArrayList;
import java.util.Random;

import utilities.ki.*;

import core.Constants;

import model.*;

/**
 * Klasse enthält alle Methoden für die KI des Spiels
 * @author Johannes Riedel
 */

public class KI{
	private Boolean self;
	private Boolean opp;
	private final Boolean empty = null;
	private final byte suchtiefe = 4;

	private Game gameobject;
	private Boolean[][] spielfeld;
	private Byte[] watermark = new Byte[Constants.gamefieldcolcount];
	private ArrayList<Byte> moeglichezuege;
	

	public KI(Game currentgame){
		gameobject = currentgame;
		moeglichezuege = new ArrayList<Byte>(0);
		for (Byte i=0; i < Constants.gamefieldcolcount; i++)
			{
			watermark[i]=-1;
			moeglichezuege.add(i);
			}
	}
	/**
	 * 
	 * @return Bewertungszahl des aktuellen Feldes: Je höher, umso besser ist die 
	 * Situation für uns
	 */
	 private int Bewertung(/*byte neuerzug*/){
		 // Tabelle, gibt an, ob für bestimmtes Feld schon Falle existiert und
		 // wie groß die Falle ist, die durch dieses Feld ermöglicht wird
		 long startzeit = System.nanoTime();
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
			//Boolean fuerwen = null; // für wen wird gerade mögliche Falle gezählt
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
					//Log.getInstance().write("KI: Reihen-Falle für KI." + einefalle);
					if(einefalle.getSize()==4)
						return Constants.KImaxbewertung;
					else
						bewertung += einefalle.getSize()*einefalle.getSize();
					}
			for (Trap einefalle : bisherbesteFalleOpp)
				if(einefalle.getSize()>0)
					{
					//Log.getInstance().write("KI: Reihen-Falle für Gegner." + einefalle);
					if(einefalle.getSize()==4)
						return -Constants.KImaxbewertung;
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
					// TODO: wirklich neue Falle?
					// für wen ist die Falle?
					//Log.getInstance().write("KI: Spalten-Falle in " + i + " für jemanden. Größe: " + anzsteine
					//		+ " für mich? " + (spielfeld[i][j]==self));
					if(spielfeld[i][watermark[i]]==self)
						if(anzsteine==4)
							return Constants.KImaxbewertung;
						else
							bewertung += anzsteine * anzsteine;
					else
						if(anzsteine==4)
							return -Constants.KImaxbewertung;
						else
							bewertung -= anzsteine * anzsteine;
					}
				}
			} 
		
		// #####################################
		// zähle diagonale Fallen Richtung nach rechts oben:
		// #####################################
		bewertung = ueberpruefediagonale(bewertung,(byte)1,(byte)1,(byte) 0,(byte) (Constants.gamefieldrowcount-4));
		if(bewertung != Constants.KImaxbewertung && bewertung != -Constants.KImaxbewertung)
		bewertung = ueberpruefediagonale(bewertung,(byte)-1,(byte)1,
				(byte)(Constants.gamefieldcolcount-1),(byte) (Constants.gamefieldrowcount-4));
		 
		/*Log.getInstance().write("Bewertung des Feldes: " + String.valueOf(bewertung) + 
			 " Ausführungszeit: " + String.valueOf((System.nanoTime() - startzeit)/1000000));*/
		return bewertung;
	 }
	 
	private int ueberpruefediagonale(int bewertung, byte deltax, byte deltay,byte _startx, byte _starty) {
		// TODO Auto-generated method stub
		byte startx = _startx;
		byte starty = _starty;
		byte i;
		byte j;
		do{
			i = startx;
			j = starty;
			Boolean fuerwen = null; 
			byte anzsteine = 0;
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
				String message = "KI: KI-Diagonale der Größe " + anzself + " gefunden. Noch benötigt: ";
				
				for (Position einfreierstein : freiesteine) 
					message += "(" + einfreierstein.getX() + "," + einfreierstein.getY() + "), "; 
				//Log.getInstance().write(message);
				if(anzself==4)
					return Constants.KImaxbewertung;
				else
					bewertung += anzself*anzself;
				}
				
						
			// mögliche Gegner-Falle gefunden
			if(anzself==0 && anzopp>0)
				{
				String message = "KI: Gegner-Diagonale der Größe " + anzopp + " gefunden. Noch benötigt: ";
				
				for (Position einfreierstein : freiesteine) 
					message += "(" + einfreierstein.getX() + "," + einfreierstein.getY() + "), "; 
				//Log.getInstance().write(message);	
				if(anzopp==4)
					return -Constants.KImaxbewertung;
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
		return bewertung;
		
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
	//Log.getInstance().write("KI: Max " + tiefe);
	/*return beta;*/
	   if (tiefe == 0)
	       return Bewertung();
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
		    //FuehreNaechstenZugAus();
		    aktzuginspalte = moeglichezuegelokal.get(i);
		    setzestein(self,aktzuginspalte);
		    // Der Gegner ist dran und wird sich für den Zug entscheiden, bei dem er am besten ist
		    // (== Bewertungsfunktion minimal)
		    wert = Bewertung();
		    if(wert<Constants.KImaxbewertung)
		    	wert = (int) (0.9 * Min(tiefe-1, alpha, beta));       
		    //MacheZugRueckgaengig();
		    loeschestein(aktzuginspalte);
			if (wert > (int) (0.9 * localAlpha))       
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
		//Log.getInstance().write("KI: Min " + tiefe);
		if (tiefe == 0)
			return Bewertung();
		// GeneriereMoeglicheZuege();
		ArrayList<Byte> moeglichezuegelokal = new ArrayList<Byte>(0);
		for (int i = 0; i < moeglichezuege.size(); i++)
			   moeglichezuegelokal.add(moeglichezuege.get(i));
		int localBeta = Constants.KImaxbewertung;   
		byte i=0; //aktuell geprüfter Zug
		byte aktzuginspalte=-1;
		int wert;
		while (i < moeglichezuegelokal.size())
			{
			//FuehreNaechstenZugAus();
			aktzuginspalte = moeglichezuegelokal.get(i);
			setzestein(opp,aktzuginspalte);
		    wert = Bewertung();
		    if(wert> -Constants.KImaxbewertung)
		    	wert = (int) (0.9 * Max(tiefe-1, alpha, beta));       
			//wert = Max(tiefe-1,alpha, beta);       
			//MacheZugRueckgaengig();
		    loeschestein(aktzuginspalte);
			if (wert < (int) (0.9*localBeta))       
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
	/**
	 * ermittelt nächstes Spielzug
	 * @param oppMove Spalte in die der Gegner gesetzt hat
	 * @return Spalte in die die KI setzt
	 */
	public byte calculateNextMove(byte oppMove) {

		self = (gameobject.getRole()==(Constants.xRole));
		opp = !self;
		
		spielfeld = gameobject.getLatestSet().getField();
		if(oppMove != -1)
			{
			watermark[oppMove]++;
			if(watermark[oppMove]+1 >= Constants.gamefieldrowcount)
				moeglichezuege.remove(moeglichezuege.indexOf(oppMove));
			Bewertung(/*(byte) oppMove.getColumn()*/);
			}
		
		Random r = new Random();
		
		//byte spalte = 6;
		//byte spalte = (byte) (5 + r.nextInt(2));
		//byte spalte = (byte) (r.nextInt(7));
		long laufzeit = System.nanoTime();
		byte spalte = (byte) Max(suchtiefe, -Constants.KImaxbewertung, Constants.KImaxbewertung);
		// TODO
		setzestein(self, spalte);
		Log.getInstance().write("KI hat Stein in Spalte " + String.valueOf(spalte)
				+ " gesetzt! Rechenzeit in ms: " + ((System.nanoTime()-laufzeit)/100000));
		int bewertungvar = Bewertung();
		Log.getInstance().write(""+bewertungvar);
		if(Bewertung()==Constants.KImaxbewertung)
			Log.getInstance().write("KI HAT GEWONNEN!");
		
		
		return spalte;
	}

	private void setzestein(/*Boolean[][] gamefield, */Boolean role, byte col) {
		watermark[col]++;
		spielfeld[col][watermark[col]] = role;
		if(watermark[col]+1 >= Constants.gamefieldrowcount)
			moeglichezuege.remove(moeglichezuege.lastIndexOf(col));
		/*for(int i=0; i<Constants.gamefieldrowcount; i++)
			if (gamefield[col][i] == null)
				{
				gamefield[col][i]= self;
				break;
				}
		return gamefield;*/
	}
	private void loeschestein(byte col){
		spielfeld[col][watermark[col]] = empty;
		watermark[col]--;
		if(watermark[col]+2 == Constants.gamefieldrowcount)
			moeglichezuege.add(col);
	}


}
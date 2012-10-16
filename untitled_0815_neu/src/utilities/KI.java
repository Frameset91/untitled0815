package utilities;
import java.util.ArrayList;
import utilities.ki.*;

import core.Constants;

import model.*;

/**
 * @author Johannes Riedel
 *
 */

public class KI{
	private static Boolean self;
	private static Boolean opp;
	private static final Boolean empty = null;
	private static final byte suchtiefe = 6;

	private Game gameobject;
	private static Boolean[][] spielfeld;
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
	
	 private static int Bewertung(/*byte neuerzug*/){
		 // Tabelle, gibt an, ob f�r bestimmtes Feld schon Falle existiert und
		 // wie gro� die Falle ist, die durch dieses Feld erm�glicht wird
		 long startzeit = System.nanoTime();
		 int[][] noetigfuerfalleself = new int[Constants.gamefieldcolcount]
				 [Constants.gamefieldrowcount];
		 for (int i = 0; i < noetigfuerfalleself.length; i++) {
			for (int j = 0; j < noetigfuerfalleself[0].length; j++) {
				noetigfuerfalleself[i][j]=0;
			}
		}
		 /* todo
		 int[][] noetigfuerfalleopp = new int[Constants.gamefieldcolcount]
				 [Constants.gamefieldrowcount];
		 for (int i = 0; i < noetigfuerfalleopp.length; i++) {
			for (int j = 0; j < noetigfuerfalleopp[0].length; j++) {
				noetigfuerfalleopp[i][j]=0;
			}
		}*/
		 
		 // ordne Spielfeld + neuem Zug Bewertungszahl zu
		int bewertung = 0;
		 
		 // z�hle offene Fallen horizontal:
		 for (byte i = 0; i < Constants.gamefieldrowcount; i++) 
		 //for (byte i = 0; i <=0; i++) // nur untere Reihe pr�fen
		 	{
			//Boolean fuerwen = null; // f�r wen wird gerade m�gliche Falle gez�hlt
			// leere Position, an der man setzen k�nnte
			ArrayList<Position> freiesteine= new ArrayList<Position>(); 
			ArrayList<Falle> bisherbesteFalleSelf = new ArrayList<Falle>(0);
			bisherbesteFalleSelf.add(new Falle((byte) 0));
			ArrayList<Falle> bisherbesteFalleOpp = new ArrayList<Falle>(0);
			bisherbesteFalleOpp.add(new Falle((byte) 0));
			byte anzself=0;
			byte anzopp=0;
			// ersten drei Steine bef�llen
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
				// alte Position l�schen
				if(j!=3)
					if(spielfeld[j-4][i]==null)
						freiesteine.remove(0);
					else
						if(spielfeld[j-4][i]==opp)
							anzopp--;
						else
							anzself--;

				// neue Position hinzuz�hlen
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
					// gr��ere Falle als bisher in dieser Reihe gefunden?
					if(anzself>bisherbesteFalleSelf.get(0).getGroesse())
						{
						// pr�fen, ob neue Falle besser ist als die an den freien
						// Chips schon bekannten
						boolean besseralsanderefallen=besseralsanderefallen(freiesteine,
								anzself,noetigfuerfalleself);
						if(besseralsanderefallen)
							{
							// dann (kleinere) l�schen und neue speichern
							bisherbesteFalleSelf.clear();
							bisherbesteFalleSelf.add(new Falle(anzself,freiesteine));
							
							// vermerken, dass bestimmte freie Steine jetzt
							// f�r Falle der Gr��e X gebraucht werden
							for (Position einfreierstein : freiesteine) 
								{
								noetigfuerfalleself[einfreierstein.getX()]
										[einfreierstein.getY()]=anzself;
								}
							}
						}
					else
						// falls neue Falle genauso gro� wie bisher bekannte trotzdem speichern
						if(anzself==bisherbesteFalleSelf.get(0).getGroesse())
							{
							boolean besseralsanderefallen=besseralsanderefallen(freiesteine,
									anzself,noetigfuerfalleself);
							if(besseralsanderefallen)
								{
								bisherbesteFalleSelf.add(new Falle(anzself,freiesteine));
								}
							}
					}
				if(anzself==0 && anzopp>0)
				{
				// gr��ere Falle als bisher gefunden?
				if(anzopp>bisherbesteFalleOpp.get(0).getGroesse())
					{
					// dann (kleinere) l�schen und neue speichern
					bisherbesteFalleOpp.clear();
					bisherbesteFalleOpp.add(new Falle(anzopp,freiesteine));
					}
				else
					// falls neue Falle genauso gro� wie bisher bekannte trotzdem speichern
					if(anzopp==bisherbesteFalleOpp.get(0).getGroesse())
						bisherbesteFalleOpp.add(new Falle(anzopp,freiesteine));
				}
				j++;
			 	} // Ende jede-Position-durchgehen-do
			while(j<Constants.gamefieldcolcount);
			// beste gefundene(n) Falle(n) ausgeben
			for (Falle einefalle : bisherbesteFalleSelf)
				if(einefalle.getGroesse()>0)
					{
					Log.getInstance().write("KI: Reihen-Falle f�r KI." + einefalle);
					if(einefalle.getGroesse()==4)
						bewertung += Constants.KImaxbewertung;
					else
						bewertung += einefalle.getGroesse()*einefalle.getGroesse();
					}
			for (Falle einefalle : bisherbesteFalleOpp)
				if(einefalle.getGroesse()>0)
					{
					Log.getInstance().write("KI: Reihen-Falle f�r Gegner." + einefalle);
					if(einefalle.getGroesse()==4)
						bewertung -= Constants.KImaxbewertung;
					else
						bewertung -= einefalle.getGroesse()*einefalle.getGroesse();
					}
		 	}
		 Log.getInstance().write("Bewertung des Feldes: " + String.valueOf(bewertung) + 
				 " Ausf�hrungszeit: " + String.valueOf((System.nanoTime() - startzeit)/1000000));
		 return bewertung;
	 }
	 
	private static boolean besseralsanderefallen(
			ArrayList<Position> freiesteine, byte anzself, int[][] noetigfuerfalleself) {
	boolean result=true;
	for (Position einfreierstein : freiesteine)
		if(noetigfuerfalleself[einfreierstein.getX()]
			[einfreierstein.getY()]>=
			anzself)
				result=false;
	return result;
	}	 


	private int Max(int tiefe, int alpha, int beta) {
	/*return beta;*/
	   if (tiefe == 0)
	       return Bewertung();
	   // GeneriereMoeglicheZuege();
	   ArrayList<Byte> moeglichezuegelokal = new ArrayList<Byte>(0);
	   for (int i = 0; i < moeglichezuege.size(); i++)
		   moeglichezuegelokal.add(moeglichezuege.get(i));
	    int localAlpha = -Constants.KImaxbewertung; //solange wir noch keine bessere Idee haben
	    											// sind alle Z�ge f�r uns extrem schlecht
	    byte i=0; //aktuell gepr�fter Zug
	    byte besterspielzug=-1;
	    byte aktzuginspalte=-1;
	    int wert;
	    // solange noch Z�ge m�glich sind
	    while (i < moeglichezuegelokal.size())
		    {
		    //FuehreNaechstenZugAus();
		    aktzuginspalte = moeglichezuegelokal.get(i);
		    setzestein(self,aktzuginspalte);
		    // Der Gegner ist dran und wird sich f�r den Zug entscheiden, bei dem er am besten ist
		    // (== Bewertungsfunktion minimal)
		    wert = Min(tiefe-1, alpha, beta);       
		    //MacheZugRueckgaengig();
		    loeschestein(aktzuginspalte);
			if (wert > localAlpha)       
				{          
				if (wert >= beta)             
					return wert;   // Beta-Cutoff-> Dieser Knoten muss nicht weiter verfolgt werden
					// weil im letzten untersuchten Zug ein so guter Wert f�r den Gegner gefunden 
					// wurde, das wir uns lieber f�r einen schon bekannten Spielzug, bei dem
					// der Gegner schlechter dasteht, entscheiden
				localAlpha = wert;
				besterspielzug = aktzuginspalte;
				if (wert > alpha)
					alpha = wert;       
				}   
			i++;
	    }
	    if(tiefe==suchtiefe)
	    	return besterspielzug;
	    return localAlpha;
	 }

	int Min(int tiefe, int alpha, int beta) {
		if (tiefe == 0)
			return Bewertung();
		// GeneriereMoeglicheZuege();
		ArrayList<Byte> moeglichezuegelokal = new ArrayList<Byte>(0);
		for (int i = 0; i < moeglichezuege.size(); i++)
			   moeglichezuegelokal.add(moeglichezuege.get(i));
		int localBeta = Constants.KImaxbewertung;   
		byte i=0; //aktuell gepr�fter Zug
		byte aktzuginspalte=-1;
		int wert;
		while (i < moeglichezuegelokal.size())
			{
			//FuehreNaechstenZugAus();
			aktzuginspalte = moeglichezuegelokal.get(i);
			setzestein(opp,aktzuginspalte);
			wert = Max(tiefe-1,alpha, beta);       
			//MacheZugRueckgaengig();
		    loeschestein(aktzuginspalte);
			if (wert < localBeta)       
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
	
	public byte calculateNextMove(byte oppMove) {

		self = (gameobject.getRole()==(Constants.xRole));
		opp = !self;
		
		spielfeld = gameobject.getLatestSet().getField();
		if(oppMove != -1)
			{
			watermark[oppMove]++;
			if(watermark[oppMove]+1 >= Constants.gamefieldrowcount)
				moeglichezuege.remove(moeglichezuege.indexOf(oppMove));
			//Bewertung(/*(byte) oppMove.getColumn()*/);
			}
		
		/*Random r = new Random();
		byte spalte;
		spalte = moeglichezuege.get(r.nextInt(moeglichezuege.size()));*/
		/*}
		//byte spalte = 6;*/
		byte spalte = (byte) Max(suchtiefe, -10*Constants.KImaxbewertung, 10*Constants.KImaxbewertung);
		//sMove generierterZug = new Move(gameobject.getRole().get(), spalte);
		
		setzestein(self, spalte);
		Log.getInstance().write("KI hat Stein in Spalte " + String.valueOf(spalte)
				+ " gesetzt!");
		//Bewertung(/*aktspielfeld, (byte) generierterZug.getColumn()*/);

		
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
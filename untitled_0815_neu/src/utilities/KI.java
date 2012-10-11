package utilities;
import java.util.Random;

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
	
	private Game gameobject;

	public KI(Game currentgame){
		gameobject = currentgame;
	}
	
	 private static void Bewertung(Boolean[][] spielfeld, byte neuerzug){
		 // ######
		 // TODO: Wenn Stein in letzter Spalte liegt
		 // ######
		 
		 // ordne Spielfeld + neuem Zug Bewertungszahl zu FÜR MICH
		 int bewertung = 0;
		 
		 // zähle offene Fallen horizontal:
		 for (byte i = 0; i < Constants.gamefieldrowcount; i++) 
		 //for (byte i = 0; i <=0; i++) // nur untere Reihe prüfen
		 	{
			 Boolean fuerwen = null; // für wen wird gerade mögliche Falle gezählt
			 byte laenge=0;		// bisherige (maximal)Länge von Steinen in einer Reihe
			 byte j = 0;		// aktuelle Spalte
			 byte freiesteine=0; // leere Position, an der man setzen könnte
			 
			 do
			 	{
				if(spielfeld[j][i]==null)			
					{
					freiesteine++;
					if(fuerwen!=null) // potentielle offene Falle gefunden
						{
						// prüfe, ob in dieser Reihe ab dieser Position überhaupt noch 4er
						// Reihe möglich ist
						// dazu muss neben diesem aktuellen Feld noch 4-laenge-freiesteine
						// frei sein
						int wievielefehlennachrechts = 4-laenge-freiesteine;
						Boolean viersindmoeglich=(j+wievielefehlennachrechts<
						   Constants.gamefieldcolcount);
						if(viersindmoeglich)
							for (int k=j+1; k <= j+wievielefehlennachrechts; k++) 
								{
								if(spielfeld[k][i]!=null)
									{
									if(spielfeld[k][i]!=fuerwen)
										{
										viersindmoeglich=false;
										break;
										}
									else
										laenge++;
									}
								}
						if(viersindmoeglich) 
							{
							Log.getInstance().write("KI: 4er-Reihe möglich! Fehlend: "
								+ String.valueOf(4-laenge) + ", Reihe: "
								+ String.valueOf(i) + ", (leere) Spalte: "
								+ String.valueOf(j) + " Für die KI? + " 
								+ String.valueOf(fuerwen==self));
							// !! Bewertungszahl anpassen
							//j = 100; // nicht weiter in dieser Reihe suchen
							// zurücksetzen
							laenge=0; 
							freiesteine=1;
							fuerwen = null;
							}

						}
					}
				else // hier liegt ein Stein
					{
					if(fuerwen==null)
						{
						fuerwen=spielfeld[j][i];
						laenge=1;
						}
					else //
						{
						if(spielfeld[j][i]==fuerwen)
							{
							laenge++;
							if(laenge==4)
								{
								Log.getInstance().write("KI: 4er-Reihe gefunden!");
								j = 100; // nicht weiter in dieser Reihe suchen

								}
							}
						else
							{
							// bisherige Falle war wohl doch nix!
							/*Log.getInstance().write("KI: 4er-Reihen-Ansatz unterbrochen!"
								+ "Reihe: " + String.valueOf(i)
								+ " Spalte: " + String.valueOf(j)
								+ " Unterbrochen durch KI? " +
								String.valueOf(fuerwen==opp));*/
							fuerwen=spielfeld[j][i];
							laenge=1;
							}
						}
					}
				j++;
			 	} // Ende jede-Position-durchgehen-do
			 while(j<Constants.gamefieldcolcount);
		 	}
	 }
	 
	 private static void alphabeta(){
		 
	 }
	 


	private int Max(int tiefe, int alpha, int beta) {
		return beta;
	    /*if (tiefe == 0)
	       return Bewerten();
	    GeneriereMoeglicheZuege();
	    localAlpha = -unendlich;    
	    while (ZuegeUebrig())
	    {
	       FuehreNaechstenZugAus();
	       wert = Min(tiefe-1,   alpha, beta;       
	       MacheZugRueckgaengig();
	       if (wert > localAlpha)       
	       	{          
	    	   if (wert >= beta)             
	    		   return wert;          
	    	   localAlpha = wert;          
	    	   if (wert > alpha)
	    		   alpha = wert;       
	    	 }    
	    }
	    return localAlpha;
	 }

	 int Min(int tiefe, int alpha, int beta) {
	    if (tiefe == 0)
	       return Bewerten();
	    GeneriereMoeglicheZuege();
	    localBeta = unendlich;    while (ZuegeUebrig())
	    {
	       FuehreNaechstenZugAus();
	       wert = Max(tiefe-1,alpha, beta);       
	       MacheZugRueckgaengig();
	       if (wert < localBeta)       
	       	{          
	    	   if (wert <= alpha)             
	    		   return wert;          
	    	   localBeta = wert;          
	    	   if (wert < beta)             
	    		   beta = wert;       
	    	}
	     }
	    return localBeta;*/
	 }
	
	public Move calculateNextMove() {
		return calculateNextMove(new Move(gameobject.getRole(), 3));
	}
	
	public Move calculateNextMove(Move oppMove) {

		self = (gameobject.getRole() == Constants.xRole);
		opp = !self;
		Boolean[][] aktspielfeld = gameobject.getLatestSet().getField();
		if(oppMove.getColumn() != -1)
			Bewertung(aktspielfeld, (byte) oppMove.getColumn());
		
		Random r = new Random();
		byte spalte = (byte) r.nextInt(Constants.gamefieldcolcount);
		//byte spalte = 6;
		Move generierterZug = new Move(gameobject.getRole(), spalte);
		
		aktspielfeld = setzestein(aktspielfeld, spalte);
		Log.getInstance().write("KI hat Stein in Spalte " + String.valueOf(spalte)
				+ " gesetzt!");
		Bewertung(aktspielfeld, (byte) generierterZug.getColumn());

		/*if(gameobject.getLatestSet().getField().getBoolField()[6][0]==self)
			Log.getInstance().write("Ich hab in 6-0 gesetzt!");
		if([6][0]==opp)
			Log.getInstance().write("Der andere hat in 6-0 gesetzt!");*/
		
		return generierterZug;
	}

	private Boolean[][] setzestein(Boolean[][] gamefield, byte col) {
		// TODO Auto-generated method stub
		for(int i=0; i<Constants.gamefieldrowcount; i++)
			if (gamefield[col][i] == null)
				{
				gamefield[col][i]= self;
				break;
				}
		return gamefield;
	}


}
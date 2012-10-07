package utilities;
import java.util.Random;

import core.Constants;

import model.*;

/**
 * @author Johannes Riedel
 *
 */

public class KI{
	
	private Game gameobject;

	public KI(Game currentgame){
		gameobject = currentgame;
	}
	
	 private static void Bewertung(byte[][] spielfeld, byte neuerzug){
		 // ordne Spielfeld + neuem Zug Bewertungszahl zu
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

	
	public Move calculateNextMove(Move oppMove) {
		// TODO Auto-generated method stub
		Random r = new Random();
		byte spalte = (byte) r.nextInt(Constants.gamefieldcolcount);
		Move generierterZug = new Move(gameobject.getRole().get(), spalte);
		
		return generierterZug;
	}


}
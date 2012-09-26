/**
 * @author Johannes Riedel
 *
 */

public class KI {
	 final int NONE = 0; 
	 final int OPP = 1;
	 final int ME = 2;
	 private 
	 
	 private static void Bewertung(byte[][] spielfeld, byte neuerzug){
		 // ordne Spielfeld + neuem Zug Bewertungszahl zu
	 }
	 
	 private static void alphabeta(){
		 
	 }
	 


	private int Max(int tiefe, int alpha, int beta) {
	    if (tiefe == 0)
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
	    return localBeta;
	 }


}

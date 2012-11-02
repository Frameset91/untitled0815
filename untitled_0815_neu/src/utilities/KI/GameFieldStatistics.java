/**
 * kapselt Hilfs- & Statistikmethoden zum Gamefield der KI
 * 
 * @author Johannes Riedel
 * 
 * 
 */
package utilities.KI;

import java.util.ArrayList;

import core.Constants;

public class GameFieldStatistics {
	
	public static void removechipandupdate(Byte[] watermark, Boolean[][] spielfeld,  ArrayList<Byte> moeglichezuege, 
			byte col){
		spielfeld[col][watermark[col]] = null;
		watermark[col]--;
		if(watermark[col]+2 == Constants.gamefieldrowcount)
			moeglichezuege.add(col);
		}
	
	public static void updateafterinsert(Byte[] watermark, Boolean[][] spielfeld,  ArrayList<Byte> moeglichezuege, 
			byte col) {
		watermark[col]++;
		if(watermark[col]+1 >= Constants.gamefieldrowcount)
			moeglichezuege.remove(moeglichezuege.lastIndexOf(col));
		}
	
	public static void insertchipandupdate(Byte[] watermark, Boolean[][] spielfeld,  ArrayList<Byte> moeglichezuege, 
			Boolean role, byte col){
		spielfeld[col][watermark[col]+1] = role;
		updateafterinsert(watermark, spielfeld, moeglichezuege, col);
		
		
	}

}

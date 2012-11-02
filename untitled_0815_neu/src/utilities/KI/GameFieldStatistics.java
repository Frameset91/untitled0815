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
	
	public static void removechipandupdate(Byte[] watermark, Boolean[][] gamefield,  ArrayList<Byte> moeglichezuege, 
			byte col){
		gamefield[col][watermark[col]] = null;
		watermark[col]--;
		if(watermark[col]+2 == Constants.gamefieldrowcount)
			moeglichezuege.add(col);
		}
	
	public static void updateafterinsert(Byte[] watermark, Boolean[][] gamefield,  ArrayList<Byte> possiblemoves, 
			byte col) {
		watermark[col]++;
		if(watermark[col]+1 >= Constants.gamefieldrowcount)
			possiblemoves.remove(possiblemoves.lastIndexOf(col));
		}
	
	public static void insertchipandupdate(Byte[] watermark, Boolean[][] spielfeld,  ArrayList<Byte> possiblemoves, 
			Boolean role, byte col){
		spielfeld[col][watermark[col]+1] = role;
		updateafterinsert(watermark, spielfeld, possiblemoves, col);
		
		
	}

}

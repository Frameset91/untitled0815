package utilities.KI;

import java.util.ArrayList;

import utilities.Log;

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
		//if(col==3)
			//Log.getInstance().write("Neue Watermark von Spalte " + col + ": " + watermark[col]);
		if(watermark[col]+1 >= Constants.gamefieldrowcount)
			moeglichezuege.remove(moeglichezuege.lastIndexOf(col));
		}
	
	public static void insertchipandupdate(Byte[] watermark, Boolean[][] spielfeld,  ArrayList<Byte> moeglichezuege, 
			Boolean role, byte col){
		//Log.getInstance().write("Füge Stein zu Spalte " + col + " hinzu. alte Watermark: " + watermark[col]);
		spielfeld[col][watermark[col]+1] = role;
		updateafterinsert(watermark, spielfeld, moeglichezuege, col);
		
		
	}
	
	/*public static void printgamefield(Boolean[][] gamefield){
		for (int j = 0; j < gamefield.length; j++) {
			String text = "Spalte " + j + " ";
			for (int k = 0; k < gamefield[0].length; k++)
				text += gamefield[j][k] + ",";
			Log.getInstance().write(text);
			}
	
	}*/

}

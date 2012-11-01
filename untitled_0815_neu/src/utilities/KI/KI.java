package utilities.KI;
import java.util.ArrayList;
import java.util.Random;

import utilities.KI.*;

import core.Constants;

import model.*;

import java.lang.Math;

/**
 * Klasse enthält alle Methoden für die KI des Spiels
 * 
 * @author Johannes Riedel
 */

public class KI{	
	private Game gameobject;
	private byte KINextMove;	

	private Boolean[][] spielfeld;
	private Byte[] watermark = new Byte[Constants.gamefieldcolcount];
	private ArrayList<Byte> moeglichezuege;

	public KI(Game currentgame, int timeout){
		//TODO timeout
		gameobject = currentgame;

		moeglichezuege = new ArrayList<Byte>(0);
		for (Byte i=0; i < Constants.gamefieldcolcount; i++)
			{
			watermark[i]=-1;
			moeglichezuege.add(i);
			}
		
	}

	/**
	 * ermittelt nächstes Spielzug
	 * @param oppMove Spalte in die der Gegner gesetzt hat
	 * @return Spalte in die die KI setzt
	 */
	public byte calculateNextMove(byte oppMove) {	
		
		spielfeld = gameobject.getLatestSet().getField();
		
		if(oppMove!=-1)
			GameFieldStatistics.updateafterinsert(watermark, spielfeld, moeglichezuege, oppMove);
		
		
		KIThread kithread = new KIThread(this,watermark,spielfeld,moeglichezuege,
				(gameobject.getRole()==(Constants.xRole)), oppMove);
		kithread.start();
		
		// solange schlafen bis Timeout
		try 
			{
			Thread.sleep(gameobject.getTimeoutDraw()-1100);
			}
		catch (InterruptedException e) 
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		
		// KI-Thread abbrechen
		kithread.interrupt();
		
		// aktuelles Ergebnis des KI-Threads zurückgeben
		return getNextMove();
		
		
	}
	
	protected synchronized void setNextMove(byte KINextMove){
		this.KINextMove = KINextMove;
	} 
	
	private synchronized byte getNextMove(){
		return KINextMove;
	}

}
package utilities.KI;
import java.util.ArrayList;
import java.util.Random;

import utilities.Log;
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
	private int timeout;
	private byte KINextMove;	

	private Boolean[][] spielfeld;
	private Byte[] watermark = new Byte[Constants.gamefieldcolcount];
	private Byte[] orgwatermark = new Byte[Constants.gamefieldcolcount];
	private ArrayList<Byte> moeglichezuege,orgmoeglichezuege;

	public KI(Game currentgame, int timeout){
		//TODO timeout
		gameobject = currentgame;
		this.timeout = timeout;
		orgmoeglichezuege = new ArrayList<Byte>(0);
		for (Byte i=0; i < Constants.gamefieldcolcount; i++)
			{
			orgwatermark[i]=-1;
			orgmoeglichezuege.add(i);
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
			GameFieldStatistics.updateafterinsert(orgwatermark, spielfeld, orgmoeglichezuege, oppMove);

		for (int j = 0; j < watermark.length; j++) 
			watermark[j] = orgwatermark[j];
		moeglichezuege = new ArrayList<Byte>(0);
		for (int j = 0; j < orgmoeglichezuege.size(); j++)
			moeglichezuege.add(orgmoeglichezuege.get(j));
		
		
		KIThread kithread = new KIThread(this,watermark,spielfeld,moeglichezuege,
				(gameobject.getRole()==(Constants.xRole)), oppMove);
		kithread.start();
		
		// solange schlafen bis Timeout
		try 
			{
			Thread.sleep(timeout-100);
			}
		catch (InterruptedException e) 
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		
		// KI-Thread abbrechen
		kithread.interrupt();
		// aktuelles Ergebnis des KI-Threads zurückgeben
		byte nextMoveLocal = getNextMove();
		
		setNextMove((byte) -1);
		GameFieldStatistics.insertchipandupdate(orgwatermark, spielfeld, orgmoeglichezuege, 
				(gameobject.getRole()==(Constants.xRole)), nextMoveLocal);
		
		Log.getInstance().write("Ki-Thread abgebrochen. Finales Ergebnis: " + nextMoveLocal);
		
		return nextMoveLocal;
		
		
	}
	
	protected synchronized void setNextMove(byte KINextMove){
		this.KINextMove = KINextMove;
	} 
	
	protected synchronized byte getNextMove(){
		return KINextMove;
	}

}
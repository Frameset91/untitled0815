package utilities.KI;
/**
 * @author Johannes Riedel
 * speichert eine mögliche 4er-Kette zu gewinnen
 */

import java.util.ArrayList;

public class Trap {
	private byte size;
	private ArrayList<Position> stillnecessarychips; //gebrauchte Steine um Falle auf 4er-Kette auszuweiten
	
	public Trap(byte groesse) {
		this.size = groesse;
		stillnecessarychips = new ArrayList<Position>();
	}
	
	public Trap(byte groesse, ArrayList<Position> freiechips) {
		// TODO Auto-generated constructor stub
		this.size = groesse;
		this.stillnecessarychips= new ArrayList<Position>(freiechips.size());
	    for (Position freierchip : freiechips) 
	        this.stillnecessarychips.add(new Position(freierchip.getX(),freierchip.getY()));
		  
	}
	
	public byte getSize() {
		return size;
	}

	public void setSize(byte size) {
		this.size = size;
	}

	public ArrayList<Position> getStillnecessarychips() {
		return stillnecessarychips;
	}

	public void setStillnecessarychips(ArrayList<Position> stillnecessarychips) {
		this.stillnecessarychips = stillnecessarychips;
	}

	@Override public String toString() {
	    String result = "Größe: " + String.valueOf(size) + " Freie Chips an + ";
	    for(Position freierchip : stillnecessarychips)
	    	result+= "(" + String.valueOf(freierchip.getX()) + "," + 
	          String.valueOf(freierchip.getY()) + ") ";
	    return result;
	  }

}

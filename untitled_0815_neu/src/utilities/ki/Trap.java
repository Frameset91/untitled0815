package utilities.ki;
/**
 * @author Johannes
 * speichert eine mögliche 4er-Kette, zu gewinnen
 */

import java.util.ArrayList;

public class Trap {
	private byte groesse;
	private ArrayList<Position> freiechips;
	
	public Trap(byte groesse) {
		this.groesse = groesse;
		freiechips = new ArrayList<Position>();
	}
	
	public Trap(byte groesse, ArrayList<Position> freiechips) {
		// TODO Auto-generated constructor stub
		this.groesse = groesse;
		this.freiechips= new ArrayList<Position>(freiechips.size());
	    for (Position freierchip : freiechips) 
	        this.freiechips.add(new Position(freierchip.getX(),freierchip.getY()));
		  
	}

	public int getGroesse() {
		return groesse;
	}
	public void setGroesse(byte groesse) {
		this.groesse = groesse;
	}
	public ArrayList<Position> getChipsbenoetigt() {
		return freiechips;
	}
	public void setChipsbenoetigt(ArrayList<Position> chipsbenoetigt) {
		this.freiechips = chipsbenoetigt;
	}
	
	@Override public String toString() {
	    String result = "Größe: " + String.valueOf(groesse) + " Freie Chips an + ";
	    for(Position freierchip : freiechips)
	    	result+= "(" + String.valueOf(freierchip.getX()) + "," + 
	          String.valueOf(freierchip.getY()) + ") ";
	    return result;
	  }

}

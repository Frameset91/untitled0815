/*
 * Diese Klasse beschreibt eine neue Nachricht des Servers an die Agenten
 * 
 * @author Bjoern List 
 * 
 */
public class ServerMessage {
private String freigabe;
private String satzstatus;
private int gegnerzug;
private String sieger;

public ServerMessage() {
	
}



public String getFreigabe() {
	return freigabe;
}
public void setFreigabe(String string) {
	this.freigabe = string;
}
public String getSatzstatus() {
	return satzstatus;
}
public void setSatzstatus(String satzstatus) {
	this.satzstatus = satzstatus;
}
public int getGegnerzug() {
	return gegnerzug;
}
public void setGegnerzug(String string) {
	this.gegnerzug = Integer.parseInt(string);
}
public String getSieger() {
	return sieger;
}
public void setSieger(String sieger) {
	this.sieger = sieger;
}




}

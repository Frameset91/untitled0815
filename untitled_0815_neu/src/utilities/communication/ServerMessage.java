package utilities.communication;
/**
 * Diese Klasse beschreibt eine neue Nachricht des Servers an die Agenten
 * 
 * @author Bjoern List 
 * 
 */
public class ServerMessage {
private String release;
private String setstatus;
private int oppmove;
private String winner;

/**
 * Konstruktor
 */
public ServerMessage() {
	
}

/**
 *  Beginn Getter und Setter
 */


public String getRelease() {
	return release;
}
public void setRelease(String string) {
	this.release = string;
}
public String getSetstatus() {
	return setstatus;
}
public void setSetstatus(String setstatus) {
	this.setstatus = setstatus;
}
public int getOppmove() {
	return oppmove;
}
public void setOppmove(String string) {
	this.oppmove = Integer.parseInt(string);
}
public String getWinner() {
	return winner;
}
public void setWinner(String winner) {
	this.winner = winner;
}

/**
 * Ende Getter und Setter
 */



}

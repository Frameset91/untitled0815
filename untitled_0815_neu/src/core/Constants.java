package core;
/**
 * Hier werden Settings und Konstanten gesammelt
 * @author Johannes Riedel
 */

public class Constants {

	public static final int gamefieldcolcount = 7;
	public static final int gamefieldrowcount = 6;
	
	public static final char xRole = 'X';
	public static final char oRole = 'O';
	public static final char noRole = (char)177;
	public static final char defaultRole = xRole;
	public static final char winMarker = '!';
	
	public static final String defaultOwnName = "untitled0815";
	public static final String textTie = "Unentschieden";
	
	
	public static final int defaultTimeoutServer = 300;
	public static final int defaultTimeoutDraw = 2000;
	
	public static final int pointsWin = 2;
	public static final int pointsLoose = 0;
	public static final int pointsTie = 1;
	
	public static final String STATE_APP_RUNNING = "Programm l�uft, warte auf Spielbeginn";
	public static final String STATE_GAME_RUNNING = "Spiel l�uft, warte auf Satzbeginn";
	public static final String STATE_SET_RUNNING = "Satz l�uft, warte auf Satzende oder Satzabbruch";
	public static final String STATE_SET_ENDED = "Satz beendet, warte auf Eingabe des Gewinners";
	
	//Styles
	public static final String xToken = "xToken";
	public static final String oToken = "oToken";
	public static final String emptyToken = "eToken";
//	public static final String xToken = "-fx-fill: yellow; -fx-effect: innershadow(two-pass-box, grey, 10, 0.5, 0, 0);";
//	public static final String oToken = "-fx-fill: red; -fx-effect: innershadow(two-pass-box, grey, 10, 0.5, 0, 0);";
//	public static final String emptyToken = "-fx-fill: white; -fx-effect: innershadow(two-pass-box, grey, 10, 0.5, 0, 0);";
//	
	public static final int KImaxbewertung = 100000;

}

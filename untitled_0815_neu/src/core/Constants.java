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
	public static final char noRole = ' ';
	public static final char defaultRole = xRole;
	
	public static final int defaultTimeoutServer = 300;
	public static final int defaultTimeoutDraw = 2000;
	
	public static final String STATE_APP_RUNNING = "Programm läuft";
	public static final String STATE_GAME_RUNNING = "Spiel läuft";
	public static final String STATE_SET_RUNNING = "Satz läuft";
	public static final String STATE_SET_ENDED = "Satz beendet";
	
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

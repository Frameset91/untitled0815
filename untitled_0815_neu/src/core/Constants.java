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
	public static final char defaultRole = xRole;
	
	public static final int defaultTimeoutServer = 300;
	public static final int defaultTimeoutDraw = 2000;
	
	public static final String STATE_APP_RUNNING = "Programm l�uft";
	public static final String STATE_GAME_RUNNING = "Spiel l�uft";
	public static final String STATE_SET_RUNNING = "Satz l�uft";
	public static final String STATE_SET_ENDED = "Satz beendet";
	
	//Styles
//	static final String xToken = "token-yellow";
//	static final String oToken = "token-red";
//	static final String emptyToken = "token";
	public static final String xToken = "-fx-fill: yellow; -fx-effect: innershadow(two-pass-box, grey, 10, 0.5, 0, 0);";
	public static final String oToken = "-fx-fill: red; -fx-effect: innershadow(two-pass-box, grey, 10, 0.5, 0, 0);";
	public static final String emptyToken = "-fx-fill: white; -fx-effect: innershadow(two-pass-box, grey, 10, 0.5, 0, 0);";
	
	public static final int KImaxbewertung = 100000;

}

package core;
/**
 * Hier werden Settings und Konstanten gesammelt
 * @author Johannes Riedel
 */

public class Constants {

	public static final int gamefieldcolcount = 7;
	public static final int gamefieldrowcount = 6;
	
	public static final String xRole = "X";
	public static final String oRole = "O";
	public static final String defaultRole = xRole;
	
	
	//Styles
//	static final String xToken = "token-yellow";
//	static final String oToken = "token-red";
//	static final String emptyToken = "token";
	public static final String xToken = "-fx-fill: yellow; -fx-effect: innershadow(two-pass-box, grey, 10, 0.5, 0, 0);";
	public static final String oToken = "-fx-fill: red; -fx-effect: innershadow(two-pass-box, grey, 10, 0.5, 0, 0);";
	public static final String emptyToken = "-fx-fill: white; -fx-effect: innershadow(two-pass-box, grey, 10, 0.5, 0, 0);";

}

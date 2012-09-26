/**
 * Hier werden Settings und Konstanten gesammelt
 * @author Johannes Riedel
 */

public class Constants {
	static final Boolean self = true;
	static final Boolean opp = false;
	static final Boolean empty = null;
	
	static final int gamefieldcolcount = 7;
	static final int gamefieldrowcount = 6;
	
	static final String xRole = "X";
	static final String oRole = "O";
	
	//Styles
//	static final String xToken = "token-yellow";
//	static final String oToken = "token-red";
//	static final String emptyToken = "token";
	static final String xToken = "-fx-fill: yellow; -fx-effect: innershadow(two-pass-box, grey, 10, 0.5, 0, 0);";
	static final String oToken = "-fx-fill: red; -fx-effect: innershadow(two-pass-box, grey, 10, 0.5, 0, 0);";
	static final String emptyToken = "-fx-fill: white; -fx-effect: innershadow(two-pass-box, grey, 10, 0.5, 0, 0);";

}

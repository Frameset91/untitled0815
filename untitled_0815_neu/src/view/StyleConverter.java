package view;

import core.Constants;
import javafx.util.StringConverter;

public class StyleConverter extends StringConverter<String> {	

	@Override
	public String fromString(String arg0) {
		//nicht nötig
		return null;
	}

	@Override
	public String toString(String arg0) {
		//Property hat sich verändert -> UI anpassen
		String style ="";
		if(arg0 != null){
			switch (arg0) {
			case Constants.xToken:
				style="-fx-background-color: red; -fx-background-radius: 15; -fx-font-size: 15; -fx-effect: innershadow(two-pass-box, grey, 10, 0.5, 0, 0); -fx-alignment: center;";
				break;
			case Constants.oToken:
				style="-fx-background-color: yellow; -fx-background-radius: 15; -fx-font-size: 15; -fx-effect: innershadow(two-pass-box, grey, 10, 0.5, 0, 0); -fx-alignment: center;";
				break;
			case Constants.emptyToken:
				style="-fx-background-color: white; -fx-background-radius: 15; -fx-font-size: 15; -fx-effect: innershadow(two-pass-box, grey, 10, 0.5, 0, 0); -fx-alignment: center;";
				break;
			default:
				break;
			}		
		}
		return style;
	}

}

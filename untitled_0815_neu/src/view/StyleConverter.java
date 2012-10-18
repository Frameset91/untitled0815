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
			switch (arg0.charAt(0)) {
			case Constants.xRole:
				style="-fx-background-color: red; " +
						"-fx-background-radius: 20; " +
						"-fx-font-size: 15; " +
						"-fx-effect: innershadow(two-pass-box, grey, 10, 0.5, 0, 0); " +
						"-fx-alignment: center;" + 
						"-fx-font-weight: bold;" +
						"-fx-text-fill: RGB(100,100,100,0.5);";
				break;
			case Constants.oRole:
				style="-fx-background-color: yellow; " +
						"-fx-background-radius: 20; " +
						"-fx-font-size: 15; " +
						"-fx-effect: innershadow(two-pass-box, grey, 10, 0.5, 0, 0); " +
						"-fx-alignment: center;" +
						"-fx-font-weight: bold;" +
						"-fx-text-fill: RGB(100,100,100,0.5);";
				break;
			case Constants.noRole:
				style="-fx-background-color: white; " +
						"-fx-background-radius: 20; " +
						"-fx-font-size: 15; " +
						"-fx-effect: innershadow(two-pass-box, grey, 10, 0.5, 0, 0); " +
						"-fx-alignment: center;" + 
						"-fx-font-weight: bold;" +
						"-fx-text-fill: RGB(100,100,100,0.5);";
				break;
			default:
				break;
			}		
		}
		return style;
	}

}

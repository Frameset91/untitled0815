package view;

import core.Constants;
import javafx.util.StringConverter;

public class TokenTextConverter extends StringConverter<String> {	

	@Override
	public String fromString(String arg0) {
		//nicht n�tig
		return null;
	}

	@Override
	public String toString(String arg0) {
		//Property hat sich ver�ndert -> UI anpassen
		String text ="";
		if(arg0 != null){
			switch (arg0) {
			case Constants.xToken:
				text="X";
				break;
			case Constants.oToken:
				text="O";
				break;
			case Constants.emptyToken:
				text=" ";
				break;
			default:
				break;
			}		
		}
		return text;
	}

}

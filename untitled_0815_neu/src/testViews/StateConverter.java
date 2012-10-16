package testViews;

import core.Constants;
import javafx.util.StringConverter;

public class StateConverter extends StringConverter<Boolean[]> {	
	public final byte DISABLE_LEFT = 0;
	public final byte DISABLE_CENTER = 1;
	public final byte DISABLE_RIGHT = 2;
	public final byte DISABLE_SET_ABORT = 3;
	public final byte DISABLE_GAME_ABORT = 4;
	public final byte SHOW_SETEND_POPUP = 5;
	
	
	@Override
	public Boolean[] fromString(String arg0) {
			//Property hat sich verändert -> UI anpassen
		Boolean[] array = new Boolean[6];		
		
		
		switch (arg0) {
		case Constants.STATE_APP_RUNNING:
			array[DISABLE_LEFT] = false;
			array[DISABLE_CENTER] = true;
			array[DISABLE_RIGHT] = true;
			array[DISABLE_SET_ABORT] = true;
			array[DISABLE_GAME_ABORT] = true;
			array[SHOW_SETEND_POPUP] = false;
			break;
		case Constants.STATE_GAME_RUNNING:
			array[DISABLE_LEFT] = true;
			array[DISABLE_CENTER] = true;
			array[DISABLE_RIGHT] = false;	
			array[DISABLE_SET_ABORT] = true;
			array[DISABLE_GAME_ABORT] = false;
			array[SHOW_SETEND_POPUP] = false;
			break;
		case Constants.STATE_SET_RUNNING:
			array[DISABLE_LEFT] = true;
			array[DISABLE_CENTER] = false;
			array[DISABLE_RIGHT] = true;	
			array[DISABLE_SET_ABORT] = false;
			array[DISABLE_GAME_ABORT] = true;
			array[SHOW_SETEND_POPUP] = false;
			break;
		case Constants.STATE_SET_ENDED:	
			array[DISABLE_LEFT] = true;
			array[DISABLE_CENTER] = false;
			array[DISABLE_RIGHT] = true;	
			array[DISABLE_SET_ABORT] = true;
			array[DISABLE_GAME_ABORT] = true;
			array[SHOW_SETEND_POPUP] = true;
			break;
		default:
			array = null;
			break;
		}
		return array;
	}

	@Override
	public String toString(Boolean[] arg0) {
		// nicht benötigt
		return null;
	}

}

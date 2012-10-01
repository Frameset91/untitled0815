import java.util.EventObject;

import utilities.GameEvent;
import utilities.GameEventListener;

/**
 * Diese Methode beschreibt beispielhaft, wie ein die Behandlung von einem Event aussehen kann.
 * Das Interface GameEventListener wurde implementiert.
 * 
 * Die Methode handleEvent beinhaltet die Event Verarbeitung.
 * 
 * Dieser Listener muss gesondert in der Event Source registriert werden.
 * 
 * @author Bjoern
 *
 */

public class ExampleListener implements GameEventListener {

	@Override
	public void handleEvent(GameEvent e) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Event erhalten");
		
	}

	
	
		
	}
		


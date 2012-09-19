import java.util.EventObject;

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
	public void handleEvent(GameEvent event) {
		// TODO Auto-generated method stub
			System.out.println("EventListener---------");
			System.out.println("Event erhalten");
//			System.out.println("Typ: " + Integer.toString(event.getType());

		
	}
		
	}



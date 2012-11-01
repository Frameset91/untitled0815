package utilities.events;

import test_utilities.*;
/**
 * Dispatcher für die Events
 * 
 * Speichert alle Listener und triggert diese falls ein EVenmt auftritt
 * 
 * @author Bjoern List
 * 
 */
public class EventDispatcher {

	private EventListenerCollection listeners = new EventListenerCollection();
	private static EventDispatcher singleton = null;

	/**
	 * privater Konstruktor
	 */
	private EventDispatcher() {
	}

	/**
	 * Singleton Uebergabe
	 * 
	 * @return EventDispatcher object
	 */
	synchronized public static EventDispatcher getInstance() {
		if (singleton == null) {
			singleton = new EventDispatcher();
		}
		return singleton;
	}

	/**
	 * Listener hinzufügen
	 * 
	 * @param listener
	 *            EventListener Instanz
	 * @throws Exception
	 */
	public void addListener(GameEventListener listener) throws Exception {
		listeners.addListener(listener);
	}

	/**
	 * EventListener loeschen
	 * 
	 * @param listener
	 *            EventListener Instanz
	 * 
	 */
	public void removeEventListener(GameEventListener listener) {
		listeners.removeListener(listener);

	}

	/**
	 * Event an die Listener weiterreichen
	 * 
	 * @param e
	 *            Event
	 * @throws Exception
	 */
	public void triggerEvent(GameEvent e) throws Exception {
		listeners.propagate(e);

	}

}
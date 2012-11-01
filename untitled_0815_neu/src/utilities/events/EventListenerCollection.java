package utilities.events;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Sicherung aller Event Listener
 * 
 * @author Bjoern List
 * 
 * 
 */
public class EventListenerCollection {

	private ArrayList<GameEventListener> listeners = new ArrayList<GameEventListener>();

	/**
	 * Listener hinzufuegen
	 * 
	 * @param listener
	 *            Event Listener
	 * 
	 */
	public void addListener(GameEventListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Event an Listener senden
	 * 
	 * @param e
	 *            event
	 * 
	 */
	public void propagate(final GameEvent e) {
		for (int i = 0; i < this.listeners.size(); i++) {
			Event_Handler_Thread EventHandler = new Event_Handler_Thread(this.listeners.get(i),e);
			Thread t = new Thread(EventHandler);
			t.setName("Event "+ e.getType() +  " abarbeiten");
			t.start();
		}
	}

	/**
	 * Listener aus Liste entfernen
	 * 
	 * 
	 * @param listener
	 *            listener to remove
	 * 
	 */
	public void removeListener(GameEventListener listener) {
		for (Iterator<GameEventListener> iter = this.listeners.iterator(); iter
				.hasNext();) {
			GameEventListener next = (GameEventListener) iter.next();

			if (iter.next().equals(listener)) {
				this.listeners.remove(next);

			}
		}

	}

	/**
	 * Iterator für die Verarbeitung der Listener
	 * 
	 * @return Iterator
	 */
	public Iterator<GameEventListener> iterator() {
		return this.listeners.iterator();
	}
}
package utilities;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.concurrent.Task;

/**
 * Sicherung aller Event Listener
 * 
 * @author Bjoern
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
	public void propagate(final GameEvent e) throws Exception {
		for (int i = 0; i < this.listeners.size(); i++) {

			Task<Void> aufgabe = new Task<Void>() {
				protected Void call() throws Exception {
					for (int i = 0; i < listeners.size(); i++) {
						listeners.get(i).handleEvent(e);
					}
					return null;
				}
			};
			new Thread(aufgabe).start();

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
	 * @return
	 */
	public Iterator<GameEventListener> iterator() {
		return this.listeners.iterator();
	}
}
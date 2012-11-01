package utilities.events;



/**
 * Interface für die EventListener
 * 
 * @author Bjoern List
 */
public interface GameEventListener {
	public void handleEvent(GameEvent e) throws Exception;
}

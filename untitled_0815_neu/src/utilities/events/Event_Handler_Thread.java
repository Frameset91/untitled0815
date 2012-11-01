package utilities.events;


public class Event_Handler_Thread implements Runnable {
private GameEventListener listener;
private GameEvent event;
	
	/**
	 * Konstruktor
	 * 
	 * @param listener Listener für die Events
	 * @param e GameEvent Instanz
	 */
	public Event_Handler_Thread(GameEventListener listener, GameEvent e){
		this.listener = listener;
		this.event = e;
	}

	/**
	 * Im neuen Thread zur Event Behandlung wird die handleEvent() Methode der Listener aufgerufen
	 */
	@Override
	public void run() {
		try {
			listener.handleEvent(event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

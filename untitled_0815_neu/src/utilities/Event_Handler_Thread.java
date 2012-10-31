package utilities;

public class Event_Handler_Thread implements Runnable {
private GameEventListener listener;
private GameEvent event;
	
	public Event_Handler_Thread(GameEventListener listener, GameEvent e){
		this.listener = listener;
		this.event = e;
	}

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

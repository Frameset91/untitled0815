package test_utilities;

import utilities.Events.EventDispatcher;
import utilities.Events.GameEvent;
import utilities.Events.GameEventListener;

public class test_EventHandler implements GameEventListener{
private EventDispatcher disp;
	
	public test_EventHandler(){
		try {
			//registrieren
			disp = EventDispatcher.getInstance();
			disp.addListener(this);
			
			//Events werfen
			System.out.println("Start Event Test");
			System.out.println("###################################################");
			
			System.out.println("Test1: Load Game senden");
			GameEvent e1 = new GameEvent(GameEvent.Type.LoadGame, "Load game");
			disp.triggerEvent(e1);
			Thread.sleep(300);
			
			System.out.println("---------");
			
			System.out.println("Test2: EndSet Game senden");
			GameEvent e2 = new GameEvent(GameEvent.Type.EndSet, "EndSet");
			disp.triggerEvent(e2);
			Thread.sleep(300);
			System.out.println("---------");
						
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		test_EventHandler te = new test_EventHandler();

	}
	@Override
	public void handleEvent(GameEvent e) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Event " + e.getType() + " erhalten!" );
		System.out.println("ARGS: " + e.getArg());
		
	}

}

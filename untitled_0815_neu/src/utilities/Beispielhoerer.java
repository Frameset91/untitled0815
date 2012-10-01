package utilities;

public class Beispielhoerer implements GameEventListener{

	@Override
	public void handleEvent(GameEvent e) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(e.getName());
		System.out.println("Ich habe eine Event");
		
	}

}

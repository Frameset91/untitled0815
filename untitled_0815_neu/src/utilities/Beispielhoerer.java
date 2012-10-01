package utilities;

public class Beispielhoerer implements EventListener{

	@Override
	public void handleEvent(Event e) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(e.getName());
		System.out.println("Ich habe eine Event");
		
	}

}

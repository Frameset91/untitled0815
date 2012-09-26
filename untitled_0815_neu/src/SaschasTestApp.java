import javafx.application.Application;
import javafx.stage.Stage;


public class SaschasTestApp extends Application {
	private IGameView view;
	private GameController controller;
	

	@Override public void start (Stage mainstage) throws Exception{
			view = new SaschasTestGUI();
//		controller = new GameController(view, model);
		
		
		view.init(mainstage);
		mainstage.setHeight(550);
		mainstage.setWidth(820);
		mainstage.setTitle("4 Gewinnt - untitled0815");
		mainstage.show();
		//view.play();
		
		controller = new GameController(view);

	   
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

}

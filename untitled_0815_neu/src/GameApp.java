import javafx.application.Application;
import javafx.stage.Stage;


public class GameApp extends Application {

	private GUI03 view;
	private GameController controller;
	

	@Override public void start (Stage mainstage) throws Exception{
		
		view = new GUI03();
		
		
		
		view.init(mainstage);
		mainstage.setHeight(550);
		mainstage.setWidth(820);
		mainstage.setTitle("4 Gewinnt - untitled0815");
		mainstage.show();
		
		controller = new GameController(view);
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

}

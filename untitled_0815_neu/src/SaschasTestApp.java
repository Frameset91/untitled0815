import javafx.application.Application;
import javafx.stage.Stage;


public class SaschasTestApp extends Application {
	private SaschasTestGUI view;
	private GameController controller;
	private Game model;
	

	@Override public void start (Stage stage) throws Exception{
		model = new Game(Constants.gamefieldcolcount,Constants.gamefieldrowcount,Game.GameRole.x, "looserOpp");
		view = new SaschasTestGUI();
//		controller = new GameController(view, model);
		
		
		view.init(stage, model);
		stage.setHeight(500);
		stage.setWidth(500);
		stage.setTitle("4 Gewinnt - untitled0815");
		stage.show();
		
		
		//Tests
	    model.addMove(new Move(Game.GameRole.o, 1));
	    model.addMove(new Move(Game.GameRole.x, 1));
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

}

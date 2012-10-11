package view;
import core.GameController;
import model.Game;
import model.GameField;
import javafx.stage.Stage;

public interface IGameView {

	public abstract void init(Stage mainstage, GameController viewModel);

	public abstract void bindField(GameField field);
	
	public abstract void unbindField(GameField field);

	public abstract void bindGame(Game model);
	
	public abstract void unbindGame(Game model);
	
	

}
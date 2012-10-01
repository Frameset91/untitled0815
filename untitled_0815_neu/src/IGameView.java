import javafx.stage.Stage;

public interface IGameView {

	public abstract void init(Stage mainstage);

	public abstract void bindField(GameField field);
	
	public abstract void unbindField(GameField field);

	public abstract void bindGame(Game model);
	
	public abstract void unbindGame(Game model);
	
	

}
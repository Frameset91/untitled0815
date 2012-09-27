import javafx.stage.Stage;

public interface IGameView {

	public abstract void init(Stage mainstage);

	public abstract void bindField(GameField field);

	public abstract void bindGame(Game model);
	
	public abstract void addEventListener(IUIEventListener listener);
	
	public abstract void removeEventListener(IUIEventListener listener);

}
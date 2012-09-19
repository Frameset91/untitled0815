
public class ProgramController {

	private GameView gameView;
	private GameModel gameModel;
	
	public enum gameRole{
		x,o
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private void newGame(int cols, int rows, gameRole role, int sets){
		gameModel = new GameModel(); 
		
	}

}

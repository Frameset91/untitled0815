import javafx.beans.property.SimpleBooleanProperty;

/**
 * kapselt das Spielfeld mit allen Chips/Steinen
 * true = eigene Rolle
 * false = Gegner
 * null = kein Stein gesetzt
 * @author Sascha
 *
 */
public class GameField {
	private SimpleBooleanProperty[][] field;
	
	public GameField(int cols, int rows){
		field = new SimpleBooleanProperty[cols][rows];
		
	}
	
	public void addMove(Move move, Game.GameRole ownRole){
		int col = move.getColumn();
		for(int i=0; i<field[col].length; i++){
			if (field[col][i] == null){
				if(move.getRole() == ownRole)
					field[col][i].setValue(true);
				else
					field[col][i].setValue(false);
				break;
			}
		}		
	}
	
	public SimpleBooleanProperty[][] getField(){
		return field;
	}
}

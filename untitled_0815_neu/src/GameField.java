import javafx.beans.property.SimpleStringProperty;

/**
 * kapselt das Spielfeld mit allen Chips/Steinen
 * Als Boolean:
 * true = x
 * false = o
 * null = kein Stein gesetzt
 * @author Sascha
 *
 */
public class GameField {
	private SimpleStringProperty[][] field;
	
	public GameField(int cols, int rows){
		field = new SimpleStringProperty[cols][rows];
		
	}
	
	public void addMove(Move move){
		int col = move.getColumn();
		for(int i=0; i<field[col].length; i++){
			if (field[col][i] == null){
				if(move.getRole() == Game.GameRole.x)
					field[col][i].setValue(Constants.xToken);
				else
					field[col][i].setValue(Constants.oToken);
				break;
			}
		}		
	}
	
	public SimpleStringProperty[][] getField(){
		return field;
	}
	
	public Boolean[][] getBoolField(Game.GameRole ownRole){
		Boolean[][] array = new Boolean[field.length][field[0].length];
		for(int i = 0; i < field.length; i++){
			for(int j = 0; j< field[0].length; j++){
				if(field[i][j] != null){
					if(field[i][j].getValue() == Constants.xToken)
						array[i][j] = true;
					else
						array[i][j] = false;
				}
				else
					break;
			}
		}
		return array;
	}
}

package model;
import core.Constants;
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
		for(int i = 0; i < field.length; i++){
			for(int j = 0; j< field[0].length; j++){
				field[i][j] = new SimpleStringProperty(Constants.emptyToken);				
			}
		}
		
	}
	
	public void addMove(Move move){
		int col = move.getColumn();
		for(int i=0; i<field[col].length; i++){
			if (field[col][i].getValue() == Constants.emptyToken){
				if(move.getRole() == Constants.xRole)
					field[col][i].setValue(Constants.xToken);
				else
					field[col][i].setValue(Constants.oToken);
				break;
			}
		}		
	}
	
	public SimpleStringProperty[][] getPropertyField(){
		return field;
	}
	
	public Boolean[][] getBoolField(){
		Boolean[][] array = new Boolean[field.length][field[0].length];
		for(int i = 0; i < field.length; i++){
			for(int j = 0; j< field[0].length; j++){
				if(field[i][j].getValue() != Constants.emptyToken){
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

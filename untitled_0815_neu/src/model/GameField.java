package model;

/**
 * Das Spielfeld als Bestandteil des Satzes. GameField bildet das zweidimensionale Spielfeld in Form eines Arrays ab und platziert neue Züge.
 * 
 * Als Boolean:
 * true = x
 * false = o
 * null = kein Stein gesetzt
 * @author Sascha Ulbrich
 *
 */

import java.util.Observable;

import core.Constants;
import javafx.beans.property.SimpleStringProperty;

public class GameField{
//	private SimpleStringProperty[][] field;
	private Boolean[][] field;
	
	/**
	 * Konstruktor von GameField 
	 *  
	 * @param Spaltenanzahl, Zeilenanzahl des Spielfelds
	 */
	public GameField(int cols, int rows){
		field = new Boolean[cols][rows];
//		for(int i = 0; i < field.length; i++){
//			for(int j = 0; j< field[0].length; j++){
//				field[i][j] = new SimpleStringProperty(Constants.emptyToken);				
//			}
//		}
		
	}
	
	/**
	 * Methode zum Hinzufügen eines Zuges  
	 *  
	 * @param der neue Zug :Move
	 */
	public void addMove(Move move){
//		int col = move.getColumn();
//		for(int i=0; i<field[col].length; i++){
//			if (field[col][i].getValue() == Constants.emptyToken){
//				if(move.getRole() == Constants.xRole)
//					field[col][i].setValue(Constants.xToken);
//				else
//					field[col][i].setValue(Constants.oToken);
//				break;
//			}
//		}	
		int col = move.getColumn();
		for(int i=0; i<field[col].length; i++){
			if (field[col][i] == null){
				if(move.getRole() == Constants.xRole)
					field[col][i] = true;
				else
					field[col][i] = false;
				break;
			}
		}
		
	}
	
	/**
 	 * @return Spielfeld mit Style für jedes Feld/ jeden Stein :StringProperty[][]
	 */
//	public SimpleStringProperty[][] getPropertyField(){
//		return field;
//	}
	
	/**
	 * @return Spielfeld mit Booleans für jedes Feld :Boolean[][] (true = X; false = O; null wenn leer)
	 */
	public Boolean[][] getField(){
//		Boolean[][] array = new Boolean[field.length][field[0].length];
//		for(int i = 0; i < field.length; i++){
//			for(int j = 0; j< field[0].length; j++){
//				if(field[i][j].getValue() != Constants.emptyToken){
//					if(field[i][j].getValue() == Constants.xToken)
//						array[i][j] = true;
//					else
//						array[i][j] = false;
//				}
//				else
//					break;
//			}
//		}
//		return 
		Boolean[][] array = new Boolean[field.length][field[0].length];
		for(int i = 0; i < field.length; i++){
			for(int j = 0; j< field[0].length; j++){
				array[i] = field[i].clone();
			}				
		}
		return array;
	}
}

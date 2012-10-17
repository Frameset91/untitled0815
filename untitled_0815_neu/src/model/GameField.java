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


import core.Constants;

public class GameField{
	private Boolean[][] field;
	
	/**
	 * Konstruktor von GameField 
	 *  
	 * @param Spaltenanzahl :Integer, Zeilenanzahl des Spielfelds :Integer
	 */
	public GameField(int cols, int rows){
		field = new Boolean[cols][rows];
	}
	
	/**
	 * Methode zum Hinzufügen eines Zuges  
	 *  
	 * @param der neue Zug :Move
	 */
	public void addMove(Move move){
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
	 * @return Spielfeld mit Booleans für jedes Feld :Boolean[][] (true = X; false = O; null wenn leer)
	 */
	public Boolean[][] getField(){
		Boolean[][] array = new Boolean[field.length][field[0].length];
		for(int i = 0; i < field.length; i++){
			for(int j = 0; j< field[0].length; j++){
				array[i] = field[i].clone();
			}				
		}
		return array;
	}
}

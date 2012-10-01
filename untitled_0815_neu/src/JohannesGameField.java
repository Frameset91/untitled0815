/**
 * kapselt das Spielfeld mit allen Chips/Steinen
 * true = eigene Rolle
 * false = Gegner
 * null = kein Stein gesetzt
 * @author Johannes
 *
 */
public class JohannesGameField {
	private Boolean[][] field;
	
	public JohannesGameField(int cols, int rows){
		field = new Boolean[cols][rows];
		
	}
	
	public void addMove(Move move){
		int col = move.getColumn();
		for(int i=0; i<field[col].length; i++){
			if (field[col][i] == null){
				if(move.getRole() == /*todo*/ "x")
					field[col][i]= true;
				else
					field[col][i]= false;
				break;
			}
		}		
	}
	
	public Boolean[][] getField(){
		return field;
	}
}

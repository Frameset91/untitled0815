
public class GameField {
	private Boolean[][] field;
	
	public GameField(int cols, int rows){
		field = new Boolean[cols][rows];
		
	}
	
	public void addMove(Move move, Game.GameRole ownRole){
		int col = move.getColumn();
		for(int i=0; i<field[col].length; i++){
			if (field[col][i] == null){
				if(move.getRole() == ownRole)
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

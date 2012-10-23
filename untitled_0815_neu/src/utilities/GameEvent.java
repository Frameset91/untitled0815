package utilities;

/**
 * Event klasse
 * 
 * @author Bjoern LIst
 */
public class GameEvent {
    public enum Type{
		//Was kann durch das Event übermittlet werden?
		StartGame, LoadGame, EndGame, StartSet, EndSet, OppMove, WinnerSet
	}
	

	private Type type;
	private String arg;

   /**
    * Neues Event erzeugen
    * 
    * @param name       Event Type und weitere Argumente
    */
    public GameEvent(Type t, String arg) {
      
        type = t;
		this.arg = arg;
    }
    
    /**
     * Liefert den Type des Events
     * 
     * @return Type
     */
    public Type getType() {
		return type;
	}

    
    /**
     * Liefert die zusaetzlichen Argumente des Events
     * 
     * @return String arg
     */
	public String getArg() {
		return arg;
	}
    
}
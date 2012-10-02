package utilities;
import java.util.EventObject;

/**
 * Die Klasse beschreibt die Events, die im Verlauf des Spiels auftreten können
 * 
 * Dabei werden folgende Typen von Ereignissen über die Variabale type definiert
 * 0 - Unser Programm muss Spielzug machen
 * 1 - Gewinner steht fest
 * 2 - satz beendet/abgebrochen --> ohne Gewinner
 * 
 * 
 * 
 * @author Bjoern List
 *
 */
public class GameEvent_old extends EventObject {
	private int type;

	public int getType() {
		return type;
	}

	public GameEvent_old(Object source,int type) {
		super(source);
		this.type = type;
		// TODO Auto-generated constructor stub
	}

}



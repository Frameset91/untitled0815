package utilities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Dispatcher für die Events
 * 
 * Speichert alle Listener und triggert diese falls ein EVenmt auftritt
 * @author Bjoern lIst
 * 
 */
public class EventDispatcher {
    
	
	private EventListenerCollection listeners = new EventListenerCollection();
    private static EventDispatcher singleton = null;
    
    /**
     * privater Konstruktor
     */
    private EventDispatcher() {
    }
    
   /**
    * Singleton Uebergabe
    * 
    * @return   EventDispatcher object
    */
    synchronized public static EventDispatcher getInstance() {
    	if (singleton == null){
    		singleton = new EventDispatcher();
    	}
        return singleton;
    }
    
  
    
    
    /**
     * Listener hinzufügen
     * 
     * @param listener       EventListener Instanz
     * @throws Exception 
     */
     public void addListener(GameEventListener listener) throws Exception {
       listeners.addListener(listener);
     }


    /**
     * EventListener loeschen
     * 
     * @param listener       EventListener Instanz
     * @return
     */
     public void removeEventListener( GameEventListener listener) {
//         if (!this.listeners.containsKey(eventName)) {
//             return null;
//         }
         
    	 listeners.removeListener(listener);
              
     }
    
 
    /**
     *	Event an die Listener weiterreichen
     * 
     * @param e  Event 
     * @return   Event
     * @throws Exception 
     */
     public GameEvent triggerEvent(GameEvent e) throws Exception {
    	 listeners.propagate(e);
    	 return e;
     }
    
   

}
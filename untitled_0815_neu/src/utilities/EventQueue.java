package utilities;


import java.util.ArrayList;
import java.util.Iterator;

/**
 * Storage class for queued events
 * 
 * 
 */
class EventQueue {
    
   /**
    * All queued events
    */
    ArrayList<GameEvent> events = new ArrayList<GameEvent>();
    
   /**
    * Add a new event to the queue.
    * 
    * The inQueue property of the event will automatically
    * be set to 'true'.
    * 
    * @param e      Event that will be added
    */
    public void addEvent(GameEvent e) {
        e.queueEvent();
        this.events.add(e);
    }

   /**
    * Get all queued events
    * 
    * @return   ArrayList with all events
    */
    public ArrayList getQueuedEvents() {
        return this.events;
    }
    
   /**
    * Get queued events of a specific event name
    * 
    * @param    eventName       name of the event
    * @return   queued events
    */
    public ArrayList getQueuedEvents(String eventName) {
        ArrayList<GameEvent> qEvents = new ArrayList<GameEvent>();
        
        for (Iterator iter = this.events.iterator(); iter.hasNext();) {
            GameEvent e = (GameEvent)iter.next();
            if (e.getName().equals(eventName)) {
                qEvents.add(e);
            }
        }
        return qEvents;
    }

    /**
     * Clear the event queue
     */
    public void clearQueue() {
    	this.events.clear();
    }
}
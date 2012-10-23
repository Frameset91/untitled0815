package utilities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Dispatcher for the events
 * 
 * Stores the listener objects and notifies them.
 * 
 * 
 */
public class EventDispatcher {
    
   /**
    * Stores all listeners
    */
    private HashMap<String,EventListenerCollection> listeners = new HashMap<String,EventListenerCollection>();

   /**
    * Stores global listeners, that handle all events
    */
    private EventListenerCollection globalListeners = new EventListenerCollection();
    
   /**
    * Stores all instances that previously have been created
    */
    private static HashMap<String,EventDispatcher> instances = new HashMap<String,EventDispatcher>();
    
   /**
    * Queue that stores the triggered events so they
    * still can be propagated to event listeners that are registered
    * at a later time
    */
    private EventQueue queue = new EventQueue();
    
    /**
     * Constructor is private.
     *
     * Use getInstance() or getDetachedInstance() instead.
     */
    private EventDispatcher() {
    }
    
   /**
    * Get the default dispatcher
    * 
    * @return   EventDispatcher object
    */
    synchronized public static EventDispatcher getInstance() {
        return EventDispatcher.getInstance("__default");
    }
    
   /**
    * Create a new dispatcher or return an existing one,
    * if it has been created
    * 
    * @param name   unique name of the dispatcher instance
    * @return       EventDispatcher object
    */
    synchronized public static EventDispatcher getInstance(String name) {
        if (!EventDispatcher.instances.containsKey(name)) {
            EventDispatcher.instances.put(name, new EventDispatcher());
        }
        return (EventDispatcher)EventDispatcher.instances.get(name);
    }

    /**
     * Get an instance and do not remember it. 
     * 
     * @return
     */
    synchronized public static EventDispatcher getDetachedInstance() {
        return new EventDispatcher();
    }
    
    /**
     * Detach a dispatcher
     * 
     * When detaching a dispatcher getInstance() will return a fresh
     * dispatcher instead of the old one.
     * 
     * @param name
     * @return
     */
    synchronized public static boolean detachDispatcher(String name) {
        if (!EventDispatcher.instances.containsKey(name)) {
            return false;
        }
    	EventDispatcher.instances.remove(name);
    	return true;
    }
    
    /**
     * Check, whether a specified EventDispatcher instance already has been created
     * 
     * @param name
     * @return
     */
    synchronized public static boolean dispatcherExists(String name) {
    	return EventDispatcher.instances.containsKey(name);
    }
    
    /**
     * Add an event listener object
     * 
     * @param eventName      name of the event to listen on
     * @param listener       instance of the event listener
     * @throws Exception 
     */
     public void addListener(String eventName, GameEventListener listener) throws Exception {
         this.addListener(eventName, listener, false);
     }

   /**
    * Add an event listener object
    * 
    * @param eventName      name of the event to listen on
    * @param listener       instance of the event listener
    * @param autoRemove     whether to remove the listener after the first event it has handled
    * @throws Exception 
    */
    public void addListener(String eventName, GameEventListener listener, boolean autoRemove) throws Exception {
        if (!this.listeners.containsKey(eventName)) {
            this.listeners.put(eventName, new EventListenerCollection());
        }
        EventListenerCollection col = (EventListenerCollection)this.listeners.get(eventName);
        col.addListener(listener, autoRemove);
        
        // check the event queue
        ArrayList events = this.queue.getQueuedEvents(eventName);
        
        for (Iterator iter = events.iterator(); iter.hasNext();) {
            GameEvent e = (GameEvent)iter.next();
            this.propagate(e, false);
        }
    }

   /**
    * Remove an event listener
    * 
    * @param eventName      name of the event
    * @param className      the class name of the listener
    * @return
    */
    public GameEventListener removeEventListener(String eventName, String className) {
        if (!this.listeners.containsKey(eventName)) {
            return null;
        }
        EventListenerCollection collection = (EventListenerCollection)this.listeners.get(eventName);
        EventListenerContainer container = (EventListenerContainer)collection.removeListener(className);
        if (container != null) {
            return container.getListener();
        }
        return null;
    }

    /**
     * Remove an event listener
     * 
     * @param eventName      name of the event
     * @param listener       the event listener object
     * @return
     */
     public GameEventListener removeEventListener(String eventName, GameEventListener listener) {
         if (!this.listeners.containsKey(eventName)) {
             return null;
         }
         EventListenerCollection collection = (EventListenerCollection)this.listeners.get(eventName);
         EventListenerContainer container = (EventListenerContainer)collection.removeListener(listener);
         if (container != null) {
             return container.getListener();
         }
         return null;
     }
    
    /**
     * Add an event listener object
     * 
     * @param eventName      name of the event to listen on
     * @param listener       instance of the event listener
     * @throws Exception 
     */
     public void addGlobalListener(GameEventListener listener) throws Exception {
         this.addGlobalListener(listener, false);
     }

    /**
     * Add an event listener object
     * 
     * @param eventName      name of the event to listen on
     * @param listener       instance of the event listener
     * @param autoRemove     whether to remove the listener after the first event it has handled
     * @throws Exception 
     */
     public void addGlobalListener(GameEventListener listener, boolean autoRemove) throws Exception {
         this.globalListeners.addListener(listener, autoRemove);
         
         // check the event queue
         ArrayList events = this.queue.getQueuedEvents();
         
         for (Iterator iter = events.iterator(); iter.hasNext();) {
             GameEvent e = (GameEvent)iter.next();
             this.propagate(e, false);
         }
     }

   /**
    * Remove an event listener, that has been globally added
    * 
    * @param listener       the event listener object
    * @return               
    */
     public GameEventListener removeGlobalEventListener(GameEventListener listener) {
         EventListenerContainer container = (EventListenerContainer)this.globalListeners.removeListener(listener);
         if (container != null) {
             return container.getListener();
         }
         return null;
     }
     
     /**
      * Remove an event listener, that has been globally added
      * 
      * @param className       the classname of the event listener
      * @return
      */
       public GameEventListener removeGlobalEventListener(String className) {
           EventListenerContainer container = (EventListenerContainer)this.globalListeners.removeListener(className);
           if (container != null) {
               return container.getListener();
           }
           return null;
       }
       
    /**
     * Trigger an event, if you already created an event object
     * 
     * The Event object will not be queued.
     * 
     * @param e  Event that will be triggered
     * @return   The event object
     * @throws Exception 
     */
     public GameEvent triggerEvent(GameEvent e) throws Exception {
         return this.propagate(e, false);
     }

     
   /**
    * Trigger an event, if you already created an event object
    * 
    * @param e      Event that will be triggered
    * @param queue  Whether to queue the event
    * @return       The event object
    * @throws Exception 
    */
    public GameEvent triggerEvent(GameEvent e, boolean queue) throws Exception {
        return this.propagate(e, queue);
    }
    
   /**
    * Trigger an event that has no context information
    * 
    * The Event will not be queued.
    * 
    * @param name   name of the event
    * @return       The Event object
    * @throws Exception 
    */
    public GameEvent triggerEvent(String name) throws Exception {
        GameEvent e = new GameEvent(name);
        return this.propagate(e, false);
    }

   /**
    * Trigger an event that has no context information
    * 
    * @param name   name of the event
    * @param queue  Whether to queue the event
    * @return       The Event object
    * @throws Exception 
    */
    public GameEvent triggerEvent(String name, boolean queue) throws Exception {
        GameEvent e = new GameEvent(name);
        return this.propagate(e, queue);
    }

    /**
     * Trigger an event with context information
     * 
     * @param name      Name of the event
     * @param queue     Whether to queue the event
     * @param context   Context of the event
     * @return          The Event object
     * @throws Exception 
     */
    public GameEvent triggerEvent(String name, boolean queue, Object context) throws Exception {
        GameEvent e = new GameEvent(name, context);
        return this.propagate(e, queue);
    }

    /**
     * Trigger an event with context and user information
     * 
     * @param name      Name of the event
     * @param queue     Whether to queue the event
     * @param context   Context of the event
     * @param userInfo  Any additional information for the event 
     * @return          The Event object
     * @throws Exception 
     */
    public GameEvent triggerEvent(String name, boolean queue, Object context, Object userInfo) throws Exception {
        GameEvent e = new GameEvent(name, context, userInfo);
        return this.propagate(e, queue);
    }

   /**
    * Propagate an event to all listeners that have been registered
    * 
    * @param e      The event
    * @param queue  Whether you want the event to be queued or not
    * @return       The modified event
    */
    private GameEvent propagate(GameEvent e, boolean queue) throws Exception {
        if (this.listeners.containsKey(e.getName())) {
            EventListenerCollection col = (EventListenerCollection)this.listeners.get(e.getName());
            col.propagate(e);
        }

        if (e.isCancelled()) {
            return e;
        }
        
        this.globalListeners.propagate(e);

        if (e.isCancelled() || queue == false) {
            return e;
        }       
        // add this event to the queue
        this.queue.addEvent(e);
        return e;
    }

    /**
     * Get the names of all events for which any listeners
     * have been added.
     * 
     * @return	Set containing all event names
     */
    public String[] getRegisteredEventNames() {
    	String[] a = {};
    	String[] names = this.listeners.keySet().toArray(a);
    	return names;
    }
    
    /**
     * Get all event listeners of the speficied event
     * 
     * @param eventName
     * @return
     */
    public EventListenerCollection getEventListeners(String eventName) {
        if (this.listeners.containsKey(eventName)) {
            return (EventListenerCollection)this.listeners.get(eventName);
        }
        return new EventListenerCollection();
    }
    
    /**
     * Get the global event listeners
     * 
     * @return
     */
    public EventListenerCollection getGlobalEventListeners() {
        return this.globalListeners;
    }

    /**
     * Remove all event listeners from this dispatcher and clear the queue
     */
    public void reset() {
    	for (Iterator iter = this.listeners.values().iterator(); iter.hasNext();) {
			EventListenerCollection listeners = (EventListenerCollection) iter.next();
			listeners.removeAllListeners();
		}
    	this.globalListeners.removeAllListeners();
    	this.queue.clearQueue();
    }
}
package utilities;


/**
 * @author Stephan Schmidt <stephan.schmidt@schlund.de>
 */
public interface GameEventListener {
    public void handleEvent(GameEvent e) throws Exception;
}

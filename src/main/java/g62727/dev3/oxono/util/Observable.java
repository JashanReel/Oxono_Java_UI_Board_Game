package g62727.dev3.oxono.util;

/**
 * Interface writing a contract in order to make a class Observable by observers
 */
public interface Observable {
    /**
     * Method to register a new observer to an observable object's list
     * @param o - the observer to register
     */
    void register(Observer o);

    /**
     * Method to unregister an already observing observer of the observers list of the observable object
     * @param o - the observer to remove
     */
    void unregister(Observer o);
}

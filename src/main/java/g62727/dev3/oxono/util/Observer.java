package g62727.dev3.oxono.util;

/**
 * Interface writing the contract in order to create an observing class which will update automatically according to the
 * observables items changes
 */
public interface Observer {
    /**
     * Method called by the notifyObservers method of an observable object
     * Makes all the needed updates of the view to adapt the visual accordingly
     */
    void updateObs();
}

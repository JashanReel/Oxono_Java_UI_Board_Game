package g62727.dev3.oxono.model;

/**
 * Represents a cell in the board
 * It can be null if there's nothing on it
 * Or a token/totem if it's actually occupied
 */
public abstract class Pawn {
    /**
     * Attribute of the Pawn class
     */
    private Symbol symbol;

    /**
     * Constructor to instantiate the class inheritors in particular
     *
     * @param symbol - the symbol of the Pawn
     */
    public Pawn(Symbol symbol) {
        this.symbol = symbol;
    }

    /**
     * Getter giving access to the symbol of a pawn
     *
     * @return the symbol of the pawn
     */
    public Symbol getSymbol() {
        return this.symbol;
    }
}

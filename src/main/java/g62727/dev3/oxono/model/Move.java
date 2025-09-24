package g62727.dev3.oxono.model;

/**
 * Represents a move in the Oxono game.
 * A move consists of a totem, its new position, and the position where a token will be inserted.
 */
public class Move {
    /**
     * Attributes of the Move class
     */
    Totem totem;
    Position totemPos;
    Position tokenPos;

    /**
     * Constructor to instantiate a new Move with the specified totem and positions
     *
     * @param totem    - the totem being moved
     * @param totemPos - the new position of the totem
     * @param tokenPos - the position where a token will be inserted
     */
    Move(Totem totem, Position totemPos, Position tokenPos) {
        this.totem = totem;
        this.totemPos = totemPos;
        this.tokenPos = tokenPos;
    }

    /**
     * Gets the totem being moved
     *
     * @return the totem in question
     */
    Totem getTotem() {
        return totem;
    }

    /**
     * Gets the new position of the totem
     *
     * @return the totem's new position
     */
    Position getTotemPos() {
        return totemPos;
    }

    /**
     * Gets the position where a token will be inserted
     *
     * @return the token's insertion position
     */
    Position getTokenPos() {
        return tokenPos;
    }

    /**
     * Customized toString method for a better display of a Move object
     * @return the customized toString display
     */
    @Override
    public String toString() {
        return "Move{" +
                "totem=" + totem.getSymbol() +
                ", totemPos=" + totemPos.getRow() + " | " + totemPos.getCol() +
                ", tokenPos=" + tokenPos.getRow() + " | " + tokenPos.getCol() +
                '}';
    }
}

package g62727.dev3.oxono.model;

/**
 * Class representing a token in the game
 * Characterized by a color and a symbol
 */
public class Token extends Pawn {
    /**
     * Attribute of the Token class
     * Also has "symbol" thanks to its parent class
     */
    private Color color;
    private boolean winning;

    /**
     * Constructor to instantiate a new token thanks to the parent class Pawn
     *
     * @param symbol - the symbol of the Token
     * @param color  - the color of the Token
     */
    Token(Symbol symbol, Color color) {
        super(symbol);
        this.color = color;
    }

    /**
     * Getter giving access to the color of a token
     *
     * @return the color of the token
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Getter returning whether the token is a part of a 4 winning alignment
     * @return true if it's the case, false otherwise
     */
    public boolean isWinning() {
        return winning;
    }

    /**
     * Setter modifying the winning value of a token
     * Usually only the 4 winning tokens are set to true in the checkForWinner Rules method
     * @param winning - the new winning value of the token
     */
    void setWinning(boolean winning) {
        this.winning = winning;
    }
}

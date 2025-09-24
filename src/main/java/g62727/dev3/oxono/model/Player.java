package g62727.dev3.oxono.model;

import g62727.dev3.oxono.util.Strategy;

/**
 * Represents a player, human or not
 * Has a color and a starting number of X and O tokens
 */
public class Player {
    /**
     * Attributes of the Player class
     */
    private Color color;
    private int tokensO;
    private int tokensX;
    private Strategy strategy;

    /**
     * Constructor to instantiate a new player
     *
     * @param color      - the player's color
     * @param tokensO    - the player's number of O tokens
     * @param tokensX    - the player's number of X tokens
     * @param strategy - the player's strategy, null if human, a defined one if CPU
     */
    Player(Color color, int tokensO, int tokensX, Strategy strategy) {
        this.color = color;
        this.tokensO = tokensO;
        this.tokensX = tokensX;
        this.strategy = strategy;
    }

    /**
     * Getter giving access to the player's color
     *
     * @return the player's color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Getter giving access to a player's number of O tokens
     *
     * @return the player's current number of O tokens
     */
    public int getTokensO() {
        return this.tokensO;
    }

    /**
     * Getter giving access to a player's number of X tokens
     *
     * @return the player's current number of X tokens
     */
    public int getTokensX() {
        return this.tokensX;
    }

    /**
     * Getter giving access to the player's strategy
     *
     * @return null if the player's a human, a defined one otherwise
     */
    Strategy getStrategy() {
        return this.strategy;
    }

    /**
     * Setter modifying the strategy to adopt by the CPU to play along with
     *
     * @param strategy - the strategy to adopt
     */
    void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Method choosing one of the two totem to play from the game board
     *
     * @param oxono - the board where the totems are
     * @return the chosen totem
     */
    Totem chooseTotem(Oxono oxono) {
        return this.strategy.chooseTotem(oxono);
    }

    /**
     * Method choosing a valid move for the totem
     *
     * @param oxono - the board where the action happens
     * @param totem - the totem to move
     * @return the chosen valid position for the totem to move to
     */
    Position chooseTotemMove(Oxono oxono, Totem totem) {
        return this.strategy.chooseTotemMove(oxono, totem);
    }

    /**
     * Method choosing a valid position to insert a token
     *
     * @param oxono - the board where the action happens
     * @param totem - the totem where the token has to be inserted adjacently
     * @return the chosen valid position to insert the token
     */
    Position chooseTokenInsert(Oxono oxono, Totem totem) {
        return this.strategy.chooseTokenInsert(oxono, totem);
    }

    /**
     * Method to decrease the player's number of X tokens
     *
     * @return true if the player drew an X token, false otherwise
     */
    boolean drawX() {
        if (this.tokensX > 0) {
            --this.tokensX;
            return true;
        }
        return false;
    }

    /**
     * Method to decrease the player's number of O tokens
     *
     * @return true if the player drew an O token, false otherwise
     */
    boolean drawO() {
        if (this.tokensO > 0) {
            --this.tokensO;
            return true;
        }
        return false;
    }

    /**
     * Method to increase the player's number of X tokens
     * Used only when the game is undone
     */
    void addX() {
        ++this.tokensX;
    }

    /**
     * Method to increase the player's number of O tokens
     * Used only when the game is undone
     */
    void addO() {
        ++this.tokensO;
    }

    /**
     * Customized display of a Player object
     *
     * @return the player's color
     */
    @Override
    public String toString() {
        return "Player{" +
                "color=" + color +
                '}';
    }
}

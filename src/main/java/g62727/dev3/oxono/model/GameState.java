package g62727.dev3.oxono.model;

/**
 * Represents the context of the game, if the current turn is in a moving totem phase or an inserting token phase
 */
public enum GameState {
    /**
     * Represents the state of the game when a player has to insert a token
     */
    INSERT,
    /**
     * Represents the state of the game when a player has to move a totem
     */
    MOVE,
    /**
     * Represents the state of the game when a player has won the game
     */
    WIN,
    /**
     * Represents the state of the game when there's a draw and no winner
     */
    DRAW,
    /**
     * Represents the state of the game when a player has surrendered
     */
    SURRENDER,
    /**
     * Represents the state of the game when a refresh is demanded to rematch the opponent
     */
    REFRESH
}

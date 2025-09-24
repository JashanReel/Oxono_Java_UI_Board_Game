package g62727.dev3.oxono.model;

import g62727.dev3.oxono.util.Strategy;

/**
 * Enum representing different AI strategies for the Oxono game
 */
public enum Strategies {
    /**
     * Represents the Random play strategy
     */
    RANDOM,
    /**
     * Represents the minimax strategy at depth 3
     */
    MINIMAX_DEPTH_3,
    /**
     * Represents the minimax strategy at depth 6
     */
    MINIMAX_DEPTH_6,
    /**
     * Represents the win possible strategy
     */
    WIN_POSSIBLE;

    /**
     * Creates and returns a Strategy object based on the enum value
     * @param game - The current Game instance
     * @param oxono - The Rules instance containing game rules
     * @return A Strategy object corresponding to the enum value
     */
    Strategy createStrategy(Game game, Oxono oxono) {
        return switch (this) {
            case RANDOM -> new RandomStrategy();
            case WIN_POSSIBLE -> new WinPossibleStrategy(oxono);
            case MINIMAX_DEPTH_3 -> new MiniMaxStrategy(3, game, oxono);
            case MINIMAX_DEPTH_6 -> new MiniMaxStrategy(6, game, oxono);
        };
    }
}

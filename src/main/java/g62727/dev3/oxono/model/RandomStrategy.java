package g62727.dev3.oxono.model;

import g62727.dev3.oxono.util.Strategy;

import java.util.List;
import java.util.Random;

/**
 * Class representing a strategy by the CPU
 * This strategy is based on random choices played by the CPU
 */
public class RandomStrategy implements Strategy {
    /**
     * Random number generator used for the strategy's random decisions
     */
    private Random random = new Random();
    private int row;
    private int col;

    /**
     * Method randomly choosing one of the two totem to play from the game board
     *
     * @param oxono - the board where the totems are
     * @return the randomly chosen totem
     */
    @Override
    public Totem chooseTotem(Oxono oxono) {
        return random.nextBoolean() ? oxono.findTotem(Symbol.O) : oxono.findTotem(Symbol.X);
    }

    /**
     * Method randomly choosing a valid move for the totem
     *
     * @param oxono - the board where the action happens
     * @param totem - the totem to move
     * @return the randomly chosen valid position for the totem to move to
     */
    @Override
    public Position chooseTotemMove(Oxono oxono, Totem totem) {
        List<Position> validMoves = oxono.getValidMoves(totem);
        Position pos = validMoves.get(random.nextInt(validMoves.size()));
        this.row = pos.getRow();
        this.col = pos.getCol();
        return pos;
    }

    /**
     * Method randomly choosing a valid position to insert a token
     *
     * @param oxono - the board where the action happens
     * @param totem - the totem where the token has to be inserted adjacently
     * @return the randomly chosen valid position to insert the token
     */
    @Override
    public Position chooseTokenInsert(Oxono oxono, Totem totem) {
        List<Position> validInsert = oxono.getValidInsert(row, col);
        return validInsert.get(random.nextInt(validInsert.size()));
    }
}

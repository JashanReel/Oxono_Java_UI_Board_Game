package g62727.dev3.oxono.model;

import g62727.dev3.oxono.util.Command;

/**
 * Represents a command executed when an insert is valid when asking to insert a token
 */
public class InsertTokenCommand implements Command {
    /**
     * Attributes of the InsertTokenCommand class
     */
    private Game game;
    private Oxono oxono;
    private Token token;
    private Position position;
    private Player toPlay;

    /**
     * Constructor to instantiate a new command to insert a token
     *
     * @param oxono    - the board on which to insert the token
     * @param token    - the token to be inserted
     * @param position - the position where the token will be inserted
     * @param toPlay   - the player making playing the turn
     */
    InsertTokenCommand(Game game, Oxono oxono, Token token, Position position, Player toPlay) {
        this.game = game;
        this.oxono = oxono;
        this.token = token;
        this.position = position;
        this.toPlay = toPlay;
    }

    /**
     * Method executing the command to insert a token on the board
     * Updates the player's tokens count
     */
    @Override
    public void execute() {
        this.oxono.insert(token, position);
        this.game.setGameState(GameState.MOVE);
        this.game.switchPlayer();
        if (this.token.getSymbol() == Symbol.O) {
            this.toPlay.drawO();
        } else if (this.token.getSymbol() == Symbol.X) {
            this.toPlay.drawX();
        }
    }

    /**
     * Method undoing a previously executed command
     * Removes the token from the board and updates the player's tokens count
     */
    @Override
    public void unexecute() {
        this.oxono.removePawn(this.position);
        this.game.setGameState(GameState.INSERT);
        if (this.token.getSymbol() == Symbol.O) {
            this.toPlay.addO();
        } else if (this.token.getSymbol() == Symbol.X) {
            this.toPlay.addX();
        }
    }
}

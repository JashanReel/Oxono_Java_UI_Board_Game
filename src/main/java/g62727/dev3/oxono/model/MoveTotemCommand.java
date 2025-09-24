package g62727.dev3.oxono.model;

import g62727.dev3.oxono.util.Command;

/**
 * Represents a command executed when a move is valid when having to move a totem
 */
public class MoveTotemCommand implements Command {
    /**
     * Attributes of the MoveTotemCommand class
     */
    private Game game;
    private Oxono oxono;
    private Totem totem;
    private Position newPosition;
    private Position oldPosition;

    /**
     * Constructor to instantiate a new command to move a totem
     *
     * @param oxono    - the game board where the totem will be moved
     * @param totem    - the totem to move
     * @param position - the position which the totem will move to
     * @param row      - the current row of the totem
     * @param col      - the current col of the totem
     */
    MoveTotemCommand(Game game, Oxono oxono, Totem totem, Position position, int row, int col) {
        this.game = game;
        this.oxono = oxono;
        this.totem = totem;
        this.newPosition = position;
        this.oldPosition = new Position(row, col);
    }

    /**
     * Method executing the command to move the totem to its new position
     */
    @Override
    public void execute() {
        this.oxono.move(totem, newPosition);
        this.game.setGameState(GameState.INSERT);
        this.game.setLastMovedTotemSymbol(totem.getSymbol());
    }

    /**
     * Method undoing a previously executed command and moving the totem back to its old position
     */
    @Override
    public void unexecute() {
        this.oxono.move(totem, oldPosition);
        this.game.setGameState(GameState.MOVE);
        this.game.setLastMovedTotemSymbol(totem.getSymbol());
    }
}

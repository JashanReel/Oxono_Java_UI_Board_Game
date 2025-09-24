package g62727.dev3.oxono.model;

import g62727.dev3.oxono.util.Strategy;

import java.util.List;
import java.util.Random;

/**
 * Implements a strategy for the Oxono game that prioritizes winning moves.
 * This strategy checks if a winning move is possible before making random moves.
 */
public class WinPossibleStrategy implements Strategy {
    /**
     * Attributes of the WinPossibleStrategy class
     */
    private Random random;
    private Oxono oxono;
    private Totem chosenTotem;
    private Position posTotem;
    private Position posToken;
    private boolean won;
    private int row;
    private int col;

    /**
     * Constructs a new WinPossibleStrategy.
     * @param oxono The game rules
     */
    public WinPossibleStrategy(Oxono oxono) {
        this.random = new Random();
        this.oxono = oxono;
        this.won = false;
        this.chosenTotem = null;
        this.posTotem = null;
        this.posToken = null;
    }

    /**
     * Chooses a totem to move, prioritizing a winning move if possible.
     * @param oxono The game rules
     * @return The chosen Totem
     */
    @Override
    public Totem chooseTotem(Oxono oxono) {
        Totem totemO = (Totem) this.oxono.getPawnAt(this.oxono.totem_O_Pos());
        Totem totemX = (Totem) this.oxono.getPawnAt(this.oxono.totem_X_Pos());
        for (Totem totem : new Totem[]{totemO, totemX}) {
            if (!won) {
                canWinWithTotem(totem);
            }
        }

        if (won) {
            return this.chosenTotem;
        }

        return random.nextBoolean() ? oxono.findTotem(Symbol.O) : oxono.findTotem(Symbol.X);
    }

    /**
     * Chooses a position to move the selected totem.
     * @param oxono The game rules
     * @param totem The totem to move
     * @return The chosen Position for the totem
     */
    @Override
    public Position chooseTotemMove(Oxono oxono, Totem totem) {
        if (won) {
            return this.posTotem;
        }
        List<Position> validMoves = oxono.getValidMoves(totem);
        Position pos = validMoves.get(random.nextInt(validMoves.size()));
        this.row = pos.getRow();
        this.col = pos.getCol();
        return pos;
    }

    /**
     * Chooses a position to insert a token after moving a totem.
     * @param oxono The game rules
     * @param totem The totem that was moved
     * @return The chosen Position for token insertion
     */
    @Override
    public Position chooseTokenInsert(Oxono oxono, Totem totem) {
        if (won) {
            return this.posToken;
        }
        List<Position> validInserts = oxono.getValidInsert(this.row, this.col);
        return validInserts.get(random.nextInt(validInserts.size()));
    }

    /**
     * Checks if moving a specific totem can lead to a win.
     * @param totem The totem to check
     */
    private void canWinWithTotem(Totem totem) {
        List<Position> validMoves = oxono.getValidMoves(totem);
        if (!won) {
            int oldPosRow = (totem.getSymbol() == Symbol.O) ? this.oxono.totem_O_Pos().getRow() : this.oxono.totem_X_Pos().getRow();
            int oldPosCol = (totem.getSymbol() == Symbol.O) ? this.oxono.totem_O_Pos().getCol() : this.oxono.totem_X_Pos().getCol();
            Position oldPos = new Position(oldPosRow, oldPosCol);
            for (Position move : validMoves) {
                leadsToWin(this.oxono, totem, move, oldPos);
            }
        }
    }

    /**
     * Simulates moves to check if they lead to a win.
     * @param oxono The game rules
     * @param totem The totem being moved
     * @param move The potential move position
     * @param oldPos The original position of the totem
     */
    private void leadsToWin(Oxono oxono, Totem totem, Position move, Position oldPos) {
        simulateMove(move, totem);
        List<Position> validInserts = oxono.getValidInsert(move.getRow(), move.getCol());
        for (Position insert : validInserts) {
            simulateInsert(insert, totem.getSymbol());
            if (this.oxono.checkForWinner()) {
                this.chosenTotem = totem;
                this.posTotem = move;
                this.posToken = insert;
                this.won = true;
            }
            undoInsert(insert);
        }
        undoMove(totem, oldPos);
    }

    /**
     * Simulates moving a totem to a new position.
     * @param position The new position
     * @param totem The totem to move
     */
    private void simulateMove(Position position, Totem totem) {
        oxono.move(totem, position);
    }

    /**
     * Simulates inserting a token at a given position.
     * @param position The position to insert the token
     * @param symbol The symbol of the token
     */
    private void simulateInsert(Position position, Symbol symbol) {
        oxono.insert(new Token(symbol, Color.BLACK), position);
    }

    /**
     * Undoes a simulated totem move.
     * @param totem The totem to move back
     * @param posTotem The original position of the totem
     */
    private void undoMove(Totem totem, Position posTotem) {
        oxono.move(totem, posTotem);
    }

    /**
     * Undoes a simulated token insertion.
     * @param posToken The position of the inserted token
     */
    private void undoInsert(Position posToken) {
        oxono.removePawn(posToken);
    }
}

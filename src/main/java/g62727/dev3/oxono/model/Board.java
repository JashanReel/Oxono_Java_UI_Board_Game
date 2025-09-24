package g62727.dev3.oxono.model;

/**
 * Represents the board of the game which the action takes place in
 * Knows the current position of each totem and can return it
 * Knows basic operations like moving or inserting a pawn
 * Knows other basic operations : is the board full, is a cell empty, return the pawn of a precise cell
 */
public class Board {
    /**
     * Attributes of the Board class
     */
    private Pawn[][] board;
    private Position totemX;
    private Position totemO;
    static int BOARD_SIZE;

    /**
     * Constructor to instantiate a new board of empty cases
     */
    Board(int size) {
        BOARD_SIZE = size;
        this.board = new Pawn[BOARD_SIZE][BOARD_SIZE];
        this.board[(BOARD_SIZE - 1) / 2][(BOARD_SIZE - 1) / 2] = new Totem(Symbol.O);
        this.board[((BOARD_SIZE - 1) / 2) + 1][((BOARD_SIZE - 1) / 2) + 1] = new Totem(Symbol.X);
        this.setTotemO(new Position((BOARD_SIZE - 1) / 2, (BOARD_SIZE - 1) / 2));
        this.setTotemX(new Position(((BOARD_SIZE - 1) / 2) + 1, ((BOARD_SIZE - 1) / 2) + 1));
    }

    /**
     * Getter returning the value of the length and width of the board
     * @return the value of the length/width of the board
     */
    int getBoardSize() {
        return BOARD_SIZE;
    }

    /**
     * Setter modifying the size of the board
     * ONLY used by the Game facade when choosing the size of the board
     * @param boardSize - the new size of the board
     */
    void setBoardSize(int boardSize) {
        BOARD_SIZE = boardSize;
    }

    /**
     * Getter giving access to the X totem's coordinates
     *
     * @return the X totem's coordinates
     */
    Position getTotemX() {
        return this.totemX;
    }

    /**
     * Setter modifying the position of the X totem
     *
     * @param totemX - the new position of the X totem
     */
    private void setTotemX(Position totemX) {
        this.totemX = totemX;
    }

    /**
     * Getter giving access to the O totem's coordinates
     *
     * @return the O totem's coordinates
     */
    Position getTotemO() {
        return this.totemO;
    }

    /**
     * Setter modifying the position of the O totem
     *
     * @param totemO - the new position of the O totem
     */
    private void setTotemO(Position totemO) {
        this.totemO = totemO;
    }

    /**
     * Helper method checking if the given row and column are within the board boundaries
     *
     * @param row - the row to check
     * @param col - the col to check
     * @throws IllegalArgumentException if the row or/and the column is/are out of bounds
     */
    private void checkRowCol(int row, int col) {
        if (row < 0 || row >= BOARD_SIZE) {
            throw new IllegalArgumentException("Invalid row : " + row);
        }
        if (col < 0 || col >= BOARD_SIZE) {
            throw new IllegalArgumentException("Invalid col : " + col);
        }
    }

    /**
     * Helper method checking if the given position is valid, meaning within the board boundaries
     *
     * @param row - the row to check
     * @param col - the col to check
     * @return true if the position is valid, false otherwise
     */
    boolean isValidPosition(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }

    /**
     * Method returning the pawn at a given position
     *
     * @param position - the position to retrieve a pawn from
     * @return the pawn at the given position
     */
    Pawn getPawnAt(Position position) {
        int row = position.getRow();
        int col = position.getCol();
        checkRowCol(row, col);
        if (this.board[row][col] != null) {
            return this.board[row][col];
        }
        return null;
    }

    /**
     * Method finding a totem on the board according to the string representation of its symbol
     *
     * @param symbol - the symbol of the totem
     * @return the found Totem
     */
    Totem findTotem(Symbol symbol) {
        for (int row = 0; row < Board.BOARD_SIZE; row++) {
            for (int col = 0; col < Board.BOARD_SIZE; col++) {
                Pawn pawn = this.getPawnAt(new Position(row, col));
                if (pawn instanceof Totem && pawn.getSymbol() == symbol) {
                    return (Totem) pawn;
                }
            }
        }
        throw new IllegalStateException("Totem not found on the board");
    }

    /**
     * Method removing a pawn from the given position
     *
     * @param position - the position from which to remove the pawn
     */
    void removePawn(Position position) {
        this.board[position.getRow()][position.getCol()] = null;
    }

    /**
     * Helper method checking if a given position is empty, meaning a null Pawn
     *
     * @param pos - the position to check
     * @return true if the position is empty, false otherwise
     */
    boolean isEmpty(Position pos) {
        int row = pos.getRow();
        int col = pos.getCol();
        checkRowCol(row, col);
        return this.board[row][col] == null;
    }

    /**
     * Helper method checking if the board is completely full
     *
     * @return true if the board is full, false otherwise
     */
    boolean isFull() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (this.isEmpty(new Position(row, col))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Method returning the number of free cases left
     *
     * @return the number of free cases left on the board
     */
    int freeCasesLeft() {
        int cpt = 0;
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (this.isEmpty(new Position(row, col))) {
                    cpt++;
                }
            }
        }
        return cpt;
    }

    /**
     * Method trying to move a totem to a new position
     *
     * @param totem    - the totem to move
     * @param position - the possible new position of the totem
     * @return true if the move was successful, false otherwise
     */
    boolean move(Totem totem, Position position) {
        checkRowCol(position.getRow(), position.getCol());
        Symbol symbol = totem.getSymbol();
        return moveTotemO(position, symbol, totem) || moveTotemX(position, symbol, totem);
    }

    /**
     * Helper method moving the totem if its symbol is O
     * @param position - the position to move to
     * @param symbol - the symbol to verify
     * @param totem - the totem to move
     * @return true if the move was successful, false otherwise
     */
    private boolean moveTotemO(Position position, Symbol symbol, Totem totem) {
        if (symbol == Symbol.O) {
            this.board[this.totemO.getRow()][this.totemO.getCol()] = null;
            this.totemO.setRow(position.getRow());
            this.totemO.setCol(position.getCol());
            this.board[this.totemO.getRow()][this.totemO.getCol()] = totem;
            return true;
        }
        return false;
    }

    /**
     * Helper method moving the totem if its symbol is X
     * @param position - the position to move to
     * @param symbol - the symbol to verify
     * @param totem - the totem to move
     * @return true if the move was successful, false otherwise
     */
    private boolean moveTotemX(Position position, Symbol symbol, Totem totem) {
        if (symbol == Symbol.X) {
            this.board[this.totemX.getRow()][this.totemX.getCol()] = null;
            this.totemX.setRow(position.getRow());
            this.totemX.setCol(position.getCol());
            this.board[this.totemX.getRow()][this.totemX.getCol()] = totem;
            return true;
        }
        return false;
    }

    /**
     * Private helper method determining the current position of the totem according to its symbol
     * @param totem - the totem to play with
     * @return the correct current position of the totem according to its symbol
     */
    Position getTotemPosition(Totem totem) {
        return (totem.getSymbol() == Symbol.O) ? new Position(totemO.getRow(), totemO.getCol())
                                               : new Position(totemX.getRow(), totemX.getCol());
    }

    /**
     * Method inserting a token at a given position
     *
     * @param token    - the token to insert
     * @param position - the position to insert the token
     */
    void insert(Token token, Position position) {
        checkRowCol(position.getRow(), position.getCol());
        this.board[position.getRow()][position.getCol()] = token;
    }
}

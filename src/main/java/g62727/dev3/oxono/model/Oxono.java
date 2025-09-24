package g62727.dev3.oxono.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static g62727.dev3.oxono.model.Board.BOARD_SIZE;

/**
 * Class representing the rules of OXONO
 * How and when a totem can move or jump
 * How and when a token should be inserted
 * When does the game finish by a win or a draw
 */
public class Oxono {
    /**
     * Attributes of the Rules class
     */
    private Board board;
    private Player pink;
    private Player black;
    private GameState gameState;
    private static final int WIN_CONDITION = 4;
    private static int INITIAL_TOKEN_COUNT = 8;
    private int playerScore;
    private int computerScore;
    private Symbol lastMovedTotemSymbol;
    private Symbol toInsert;
    private Player toPlay;
    private Strategies lastStrat;

    /**
     * Constructor to instantiate a new set of game Rules
     * @param size - the size of the board to play on
     */
    Oxono(int size) {
        this.board = new Board(size);
        this.board.setBoardSize(size);
        this.black = new Player(Color.BLACK, INITIAL_TOKEN_COUNT, INITIAL_TOKEN_COUNT, null);
        this.pink = new Player(Color.PINK, INITIAL_TOKEN_COUNT, INITIAL_TOKEN_COUNT, null);
        this.gameState = GameState.MOVE;
        this.toPlay = this.pink;
        playerScore = computerScore = 0;
    }

    /**
     * Gets the size of the game board.
     * @return The size of the game board
     */
    int getBoardSize() {
        return this.board.getBoardSize();
    }

    /**
     * Checks if the board is full.
     * @return true if the board is full, false otherwise
     */
    boolean isFull() {
        return this.board.isFull();
    }

    /**
     * Finds a totem on the board with the specified symbol.
     * @param symbol The symbol of the totem to find
     * @return The found Totem object
     */
    Totem findTotem(Symbol symbol) {
        return this.board.findTotem(symbol);
    }

    /**
     * Gets the number of free spaces left on the board.
     * @return The number of free spaces
     */
    int freeCasesLeft() {
        return this.board.freeCasesLeft();
    }

    /**
     * Gets the pawn at the specified position.
     * @param position The position to check
     * @return The Pawn at the specified position
     */
    Pawn getPawnAt(Position position) {
        return this.board.getPawnAt(position);
    }

    /**
     * Moves a totem to a new position.
     * @param totem The totem to move
     * @param position The new position for the totem
     */
    void move(Totem totem, Position position) {
        this.board.move(totem, position);
    }

    /**
     * Inserts a token at the specified position.
     * @param token The token to insert
     * @param position The position to insert the token
     */
    void insert(Token token, Position position) {
        this.board.insert(token ,position);
    }

    /**
     * Removes a pawn from the specified position.
     * @param position The position of the pawn to remove
     */
    void removePawn(Position position) {
        this.board.removePawn(position);
    }

    /**
     * Gets the position of the O totem.
     * @return The position of the O totem
     */
    Position totem_O_Pos() {
        return this.board.getTotemO();
    }

    /**
     * Gets the position of the X totem.
     * @return The position of the X totem
     */
    Position totem_X_Pos() {
        return this.board.getTotemX();
    }

    /**
     * Gets the position of a specified totem.
     * @param totem The totem to find
     * @return The position of the specified totem
     */
    Position getTotemPosition(Totem totem) {
        return this.board.getTotemPosition(totem);
    }

    /**
     * Gets the pink player.
     * @return The pink player
     */
    Player getPink() {
        return pink;
    }

    /**
     * Gets the black player.
     * @return The black player
     */
    Player getBlack() {
        return black;
    }

    /**
     * Gets the current game state.
     * @return The current game state
     */
    GameState getGameState() {
        return gameState;
    }

    /**
     * Sets the current game state.
     * @param gameState The game state to set
     */
    void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Gets the last strategy used.
     * @return The last strategy used
     */
    Strategies getLastStrat() {
        return lastStrat;
    }

    /**
     * Sets the last strategy used.
     * @param lastStrat The strategy to set as last used
     */
    void setLastStrat(Strategies lastStrat) {
        this.lastStrat = lastStrat;
    }

    /**
     * Gets the current player to play.
     * @return The current player
     */
    Player getToPlay() {
        return toPlay;
    }

    /**
     * Sets the current player to play.
     * @param toPlay The player to set as current
     */
    void setToPlay(Player toPlay) {
        this.toPlay = toPlay;
    }

    /**
     * Gets the symbol to insert.
     * @return The symbol to insert
     */
    Symbol getToInsert() {
        return toInsert;
    }

    /**
     * Sets the symbol to insert.
     * @param toInsert The symbol to set for insertion
     */
    void setToInsert(Symbol toInsert) {
        this.toInsert = toInsert;
    }

    /**
     * Gets the symbol of the last moved totem.
     * @return The symbol of the last moved totem, or null if no totem has been moved
     */
    Symbol getLastMovedSymbol() {
        if (lastMovedTotemSymbol != null && this.lastMovedTotemSymbol == Symbol.O) {
            return Symbol.O;
        } else if (lastMovedTotemSymbol != null && this.lastMovedTotemSymbol == Symbol.X) {
            return Symbol.X;
        }
        return null;
    }

    /**
     * Sets the symbol of the last moved totem.
     * @param lastMovedTotemSymbol The symbol of the last moved totem
     */
    void setLastMovedTotemSymbol(Symbol lastMovedTotemSymbol) {
        this.lastMovedTotemSymbol = lastMovedTotemSymbol;
    }

    /**
     * Sets the score for the human player.
     * @param playerScore The score to set for the human player
     */
    void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    /**
     * Sets the score for the computer player.
     * @param computerScore The score to set for the computer player
     */
    void setComputerScore(int computerScore) {
        this.computerScore = computerScore;
    }

    /**
     * Increments the score for the specified player.
     * @param player The player whose score should be incremented
     */
    void incrementScore(Player player) {
        if (player == this.pink) {
            ++playerScore;
        } else if (player == this.black) {
            ++computerScore;
        }
    }

    /**
     * Getter allowing access to the score of the human player
     * @return the human player's score
     */
    int getScore(Color color) {
        if (color == Color.PINK) {
            return playerScore;
        } else {
            return computerScore;
        }
    }

    /**
     * Method checking whether a move is valid for a given totem or not
     *
     * @param totem    - the totem to move
     * @param position - the possible new position for the totem
     * @return true if the move is valid, false otherwise
     */
    boolean isValidMove(Totem totem, Position position) {
        Position currentPos = this.board.getTotemPosition(totem);
        List<Position> validMoves = this.getValidMoves(currentPos);
        return validMoves.contains(position);
    }

    /**
     * Private helper method determining the possible moves for the totem
     * @param currentPos - the current position of the totem
     * @return every single move valid for the totem to play
     */
    List<Position> getValidMoves(Position currentPos) {
        List<Position> moves = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        normalMoves(directions, currentPos.getRow(), currentPos.getCol(), moves);
        if (moves.isEmpty()) jumpMoves(directions, currentPos.getRow(), currentPos.getCol(), moves);
        if (moves.isEmpty()) anywhereTotem(moves, currentPos.getRow(), currentPos.getCol());
        return Collections.unmodifiableList(moves);
    }

    /**
     * Method returning every valid moves for a given totem
     * Only used for the strategies
     * @param totem - the totem to check moves for
     * @return the list of valid positions the totem can move to
     */
    List<Position> getValidMoves(Totem totem) {
        List<Position> validMoves = new ArrayList<>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Position position = new Position(row, col);
                if (isValidMove(totem, position)) {
                    validMoves.add(position);
                }
            }
        }
        return Collections.unmodifiableList(validMoves);
    }

    /**
     * Private helper method determining valid normal moves for the totem
     * @param directions - the directions to explore
     * @param row - the current row of the totem
     * @param col - the current column of the totem
     * @param listOfCoord - the list of every single valid move
     */
    private void normalMoves(int [][] directions, int row, int col, List<Position> listOfCoord) {
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (this.board.isValidPosition(newRow, newCol)) {
                if (this.board.isEmpty(new Position(newRow, newCol))) {
                    while (this.board.isValidPosition(newRow, newCol)
                            && this.board.isEmpty(new Position(newRow, newCol))) {
                        listOfCoord.add(new Position(newRow, newCol));
                        newRow += dir[0];
                        newCol += dir[1];
                    }
                }
            }
        }
    }

    /**
     * Private helper method determining valid jumps when the totem is stuck in every adjacent case
     * @param directions - the directions to explore
     * @param row - the current row of the totem
     * @param col - the current column of the totem
     * @param listOfCoord - the list of every single valid move
     */
    private void jumpMoves(int [][] directions, int row, int col, List<Position> listOfCoord) {
        for (int[] dir : directions) {
            helperJumpMoves(dir, row, col, listOfCoord);
        }
    }

    /**
     * Private helper method initializing the first jump and calling the validating method
     * @param dir - the direction to explore
     * @param row - the current row of the totem
     * @param col - the current column of the totem
     * @param listOfCoord - the list of every single valid move
     */
    private void helperJumpMoves(int[] dir, int row, int col, List<Position> listOfCoord) {
        int newRow = row + dir[0];
        int newCol = col + dir[1];
        helperJumpStartValid(dir, newRow, newCol, listOfCoord);
    }

    /**
     * Private helper method determining whether a totem can jump or not at all
     * @param dir - the direction to explore
     * @param newRow - the new row with the jump of the totem
     * @param newCol - the new column with the jump of the totem
     * @param listOfCoord - the list of every single valid move
     */
    private void helperJumpStartValid(int[] dir, int newRow, int newCol, List<Position> listOfCoord) {
        if (this.board.isValidPosition(newRow, newCol)) {
            if (!this.board.isEmpty(new Position(newRow, newCol))) {
                while (this.board.isValidPosition(newRow, newCol)
                        && !this.board.isEmpty(new Position(newRow, newCol))) {
                    newRow += dir[0];
                    newCol += dir[1];
                }
                helperJumpMovesAddCase(newRow, newCol, listOfCoord);
            }
        }
    }

    /**
     * Private helper method adding a move to the list of possible jumping moves
     * @param newRow - the row of the valid jump
     * @param newCol - the column of the valid jump
     * @param listOfCoord - the list of every single valid move
     */
    private void helperJumpMovesAddCase(int newRow, int newCol, List<Position> listOfCoord) {
        if (this.board.isValidPosition(newRow, newCol)
                && this.board.isEmpty(new Position(newRow, newCol))) {
            listOfCoord.add(new Position(newRow, newCol));
        }
    }

    /**
     * Private helper method determing valid jump everywhere on the board since the totem is completely stuck
     * @param listOfCoord - the list of every single valid move
     * @param row - the current row of the totem
     * @param col - the current column of the totem
     */
    private void anywhereTotem(List<Position> listOfCoord, int row, int col) {
        for (int rowLoop = 0; rowLoop < BOARD_SIZE; rowLoop++) {
            for (int colLoop = 0; colLoop < BOARD_SIZE; colLoop++) {
                if (this.board.isEmpty(new Position(rowLoop, colLoop)) && (rowLoop != row || colLoop != col)) {
                    listOfCoord.add(new Position(rowLoop, colLoop));
                }
            }
        }
    }

    /**
     * Method checking if the insertion of a token at the given position is valid
     *
     * @param token    - the token to insert
     * @param position - the position to insert the token
     * @return true if the insertion is possible, false otherwise
     */
    boolean isValidInsert(Token token, Position position) {
        int rowTotem = (token.getSymbol() == Symbol.O) ? this.board.getTotemO().getRow() : this.board.getTotemX().getRow();
        int colTotem = (token.getSymbol() == Symbol.O) ? this.board.getTotemO().getCol() : this.board.getTotemX().getCol();
        List<Position> listOfCoord = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        normalInserts(directions, rowTotem, colTotem, listOfCoord);
        if (listOfCoord.isEmpty()) {
            anywhereToken(listOfCoord);
        }
        return listOfCoord.contains(position);
    }

    /**
     * Private helper method determining the valid insertions in a normal case (the 4 adjacent cases are free)
     * @param directions - the directions to look the insertions close to
     * @param rowTotem - the current row of the totem to look the insertions for
     * @param colTotem - the current col of the totem to look the insertions for
     * @param listOfCoord - the list of all possibles insertions
     */
    private void normalInserts(int [][] directions, int rowTotem, int colTotem, List<Position> listOfCoord) {
        for (int[] dir : directions) {
            int newRow = rowTotem + dir[0];
            int newCol = colTotem + dir[1];
            if (this.board.isValidPosition(newRow, newCol) && this.board.isEmpty(new Position(newRow, newCol))) {
                listOfCoord.add(new Position(newRow, newCol));
            }
        }
    }

    /**
     * Private helper method returning every single free cell on the board for an insertion
     * Used when no normal insertion is possible
     * @param listOfCoord - the list of all possible insertions
     */
    private void anywhereToken(List<Position> listOfCoord) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (this.board.isEmpty(new Position(i, j))) {
                    listOfCoord.add(new Position(i, j));
                }
            }
        }
    }

    /**
     * Method returning every valid positions to insert the token
     *
     * @param row - the row to analyse the inserts for
     * @param col - the column to analyse the inserts for
     * @return the list of valid positions to insert the token
     */
    List<Position> getValidInsert(int row, int col) {
        List<Position> listOfCoord = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        normalInserts(directions, row, col, listOfCoord);
        if (listOfCoord.isEmpty()) {
            anywhereToken(listOfCoord);
        }
        return listOfCoord;
    }

    /**
     * Helper method checking whether there's a winner or not on a specific row or column
     *
     * @param startRow     - the starting row of the line to check
     * @param startCol     - the starting column of the line to check
     * @param rowIncrement - the increment for the row in each step (0 for horizontal, 1 for vertical)
     * @param colIncrement - The increment for the column in each step (1 for horizontal, 0 for vertical)
     * @return true if a winning condition is met on a line, false otherwise
     */
    private boolean checkLine(int startRow, int startCol, int rowIncrement, int colIncrement) {
        int[] counts = {0, 0}; // [colorCount, symbolCount]
        List<Token> listTokensColor =  new ArrayList<>();
        List<Token> listTokenSymbol = new ArrayList<>();
        Pawn previousPawn = null;
        for (int i = 0; i < BOARD_SIZE; i++) {
            Pawn currentPawn = this.board.getPawnAt(getPositionInLine(startRow, startCol, rowIncrement, colIncrement, i));
            if (isResetCondition(currentPawn)) {
                resetCounts(counts);
                listTokensColor.clear();
                listTokenSymbol.clear();
            } else if (updateAndCheckWin(previousPawn, currentPawn, counts, listTokensColor, listTokenSymbol)) {
                if (counts[0] == WIN_CONDITION) {
                    for (Token t : listTokensColor) {
                        t.setWinning(true);
                    }
                } else if (counts[1] == WIN_CONDITION) {
                    for (Token t : listTokenSymbol) {
                        t.setWinning(true);
                    }
                }
                return true;
            }
            previousPawn = currentPawn;
        }
        return false;
    }

    /**
     * Resets the counts to zero (for the symbol and the color)
     * @param counts - an array of 2 integers containing the counts to reset
     */
    private void resetCounts(int[] counts) {
        counts[0] = counts[1] = 0;
    }

    /**
     * Updates the counts and checks for a win condition
     *
     * @param previousPawn - the previous pawn in the sequence
     * @param currentPawn - the current pawn in the sequence
     * @param counts - an array of integers containing the counts to update
     * @return true if either win condition is met, false otherwise
     */
    private boolean updateAndCheckWin(Pawn previousPawn, Pawn currentPawn, int[] counts,
                                    List<Token> tokensColor, List<Token> tokensSymbol) {
        updateCounts(previousPawn, currentPawn, counts, tokensColor, tokensSymbol);
        return counts[0] == WIN_CONDITION || counts[1] == WIN_CONDITION;
    }

    /**
     * Updates the counts based on the previous and current pawns
     *
     * @param previousPawn - the previous pawn in the sequence
     * @param currentPawn - the current pawn in the sequence
     * @param counts - an array of integers containing the counts to update
     */
    private void updateCounts(Pawn previousPawn, Pawn currentPawn, int[] counts,
                              List<Token> tokensColor, List<Token> tokensSymbol) {
        counts[0] = isPreviousPawnNullColor(previousPawn, currentPawn, counts[0], tokensColor);
        counts[1] = isPreviousPawnNullSymbol(previousPawn, currentPawn, counts[1], tokensSymbol);
    }

    /**
     * Checks if the previous pawn is null and updates the color count
     *
     * @param previousPawn - the previous pawn in the sequence
     * @param currentPawn - the current pawn in the sequence
     * @param colorCount - the current color count
     * @return the updated color count
     */
    private int isPreviousPawnNullColor(Pawn previousPawn, Pawn currentPawn, int colorCount, List<Token> tokensColor) {
        if (previousPawn instanceof Token && currentPawn instanceof Token) {
            return updateColorCount((Token) currentPawn, (Token) previousPawn, colorCount, tokensColor);
        } else {
            return 1;
        }
    }

    /**
     * Checks if the previous pawn is null and updates the symbol count
     *
     * @param previousPawn - the previous pawn in the sequence
     * @param currentPawn - the current pawn in the sequence
     * @param symbolCount - the current symbol count
     * @return the updated symbol count
     */
    private int isPreviousPawnNullSymbol(Pawn previousPawn, Pawn currentPawn, int symbolCount, List<Token> tokensSymbol) {
        if (previousPawn instanceof Token && currentPawn instanceof Token) {
            return updateSymbolCount((Token) currentPawn, (Token) previousPawn, symbolCount, tokensSymbol);
        } else {
            return 1;
        }
    }

    /**
     * Updates the color count by comparing the colors of the pawns
     *
     * @param currentPawn - the current pawn
     * @param previousPawn - the previous pawn
     * @param colorCount - the current color count
     * @return the updated color count
     */
    private int updateColorCount(Token currentPawn, Token previousPawn, int colorCount, List<Token> tokensColor) {
        if (currentPawn.getColor() == previousPawn.getColor()) {
            if (!tokensColor.contains(previousPawn)) {
                tokensColor.add(previousPawn);
            }
            if (!tokensColor.contains(currentPawn)) {
                tokensColor.add(currentPawn);
            }
            return ++colorCount;
        } else {
            tokensColor.clear();
            tokensColor.add(currentPawn);
            return 1;
        }
    }

    /**
     * Updates the symbol count by comparing the symbols of the pawns
     *
     * @param currentPawn - the current pawn
     * @param previousPawn - the previous pawn
     * @param symbolCount - the current symbol count
     * @return the updated symbol count
     */
    private int updateSymbolCount(Token currentPawn, Token previousPawn, int symbolCount, List<Token> tokensSymbol) {
        if (currentPawn.getSymbol() == previousPawn.getSymbol()) {
            if (!tokensSymbol.contains(previousPawn)) {
                tokensSymbol.add(previousPawn);
            }
            if (!tokensSymbol.contains(currentPawn)) {
                tokensSymbol.add(currentPawn);
            }
            return ++symbolCount;
        } else {
            tokensSymbol.clear();
            tokensSymbol.add(currentPawn);
            return 1;
        }
    }

    /**
     * Calculates the position in a line based on given increments
     *
     * @param startRow - the starting row
     * @param startCol - the starting column
     * @param rowIncrement - the increment for rows
     * @param colIncrement - the increment for columns
     * @param i - the multiplier for increments
     * @return the calculated position as a Position object
     */
    private Position getPositionInLine(int startRow, int startCol, int rowIncrement, int colIncrement, int i) {
        int row = startRow + i * rowIncrement;
        int col = startCol + i * colIncrement;
        return new Position(row, col);
    }

    /**
     * Checks if the reset condition is met for a given pawn
     *
     * @param pawn - the pawn to check
     * @return true if the pawn is null or is a Totem; false otherwise
     */
    private boolean isResetCondition(Pawn pawn) {
        return pawn == null || pawn instanceof Totem;
    }

    /**
     * Helper method checking if there's a winner on the columns (vertically)
     *
     * @return true if it's the case, false otherwise
     */
    private boolean checkColumns() {
        for (int col = 0; col < BOARD_SIZE; col++) {
            if (checkLine(0, col, 1, 0)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method checking if there's a winner on the rows (horizontally)
     *
     * @return true if it's the case, false otherwise
     */
    private boolean checkRows() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            if (checkLine(row, 0, 0, 1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method checking whether there's a winner or not
     *
     * @return true if there is indeed a winner, false otherwise
     */
    boolean checkForWinner() {
        return checkRows() || checkColumns();
    }
}

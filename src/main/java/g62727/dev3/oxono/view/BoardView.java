package g62727.dev3.oxono.view;

import g62727.dev3.oxono.model.Game;
import g62727.dev3.oxono.model.Position;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

/**
 * Represents the board where the game is played in the graphical version
 * Contains every single cell of each row and column
 */
public class BoardView extends GridPane {
    /**
     * Attributes of the BoardView class
     */
    private CellViewFx[][] cells;
    private int boardSize;

    /**
     * Constructs a new BoardView with the specified size.
     * @param size The size of the board (number of rows and columns)
     */
    BoardView(int size) {
        this.boardSize = size;
        cells = new CellViewFx[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                cells[row][col] = new CellViewFx();
                add(cells[row][col], col, row);
            }
        }
        this.setAlignment(Pos.CENTER);
    }

    /**
     * Updates the board view based on the current game state.
     * @param game The current Game instance
     */
    void update(Game game) {
        int size = game.getBoardSize();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                cells[row][col].update(game.getPawnAt(new Position(row, col)));
            }
        }
    }

    /**
     * Applies or removes a gray overlay on the board.
     * @param disabled True to apply the gray overlay, false to remove it
     */
    void grayedBoard(boolean disabled) {
        if (disabled) {
            this.setStyle("-fx-opacity: 0.5; -fx-background-color: rgba(128, 128, 128, 0.5);");
        } else {
            this.setStyle("");
            for (int row = 0; row < this.boardSize; row++) {
                for (int col = 0; col < this.boardSize; col++) {
                    cells[row][col].resetStyle();
                }
            }
        }
    }

    /**
     * Removes all highlights from the board cells.
     */
    void removeAllHighlights() {
        for (int row = 0; row < this.boardSize; row++) {
            for (int col = 0; col < this.boardSize; col++) {
                cells[row][col].removeHighlight();
            }
        }
    }

    /**
     * Gets the size of the board.
     * @return The board size
     */
    int getBoardSize() {
        return this.boardSize;
    }

    /**
     * Gets the CellViewFx at the specified row and column.
     * @param row The row of the cell
     * @param col The column of the cell
     * @return The CellViewFx at the specified position
     */
    CellViewFx getCellView(int row, int col) {
        return cells[row][col];
    }
}

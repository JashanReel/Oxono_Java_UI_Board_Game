package g62727.dev3.oxono.model;

import java.util.Objects;

/**
 * Represents a position in the board, like a point would in an orthonormal reference
 * Characterized by a row and a col
 */
public class Position {
    /**
     * Attributes of the Position class
     */
    private int row;
    private int col;

    /**
     * Constructor to instantiate a new position
     *
     * @param row - the position's row/x coordinate
     * @param col - the position's col/y coordinate
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Getter giving access to a position's row
     *
     * @return the position's row
     */
    int getRow() {
        return this.row;
    }

    /**
     * Setter modifying the row value of a given position
     *
     * @param row - the row value
     */
    void setRow(int row) {
        this.row = row;
    }

    /**
     * Getter giving access to a position's col
     *
     * @return the position's col
     */
    int getCol() {
        return this.col;
    }

    /**
     * Setter modifying the col value of a given position
     *
     * @param col - the col value
     */
    void setCol(int col) {
        this.col = col;
    }

    /**
     * Customized display of a Position object
     *
     * @return the row and col of a position
     */
    @Override
    public String toString() {
        return "Position{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }

    /**
     * Rewritten equals to method to compare the values of the row and col of 2 positions instead of their references
     * @param o - the object to compare one position to
     * @return true if the 2 positions are equals, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}

package g62727.dev3.oxono.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(6);
    }

    @Test
    void testInitialBoardSetup() {
        assertEquals(new Position(2, 2), board.getTotemO());
        assertEquals(new Position(3, 3), board.getTotemX());
        assertNotNull(board.getPawnAt(new Position(2, 2)));
        assertNotNull(board.getPawnAt(new Position(3, 3)));
        assertEquals(Symbol.O, board.getPawnAt(new Position(2, 2)).getSymbol());
        assertEquals(Symbol.X, board.getPawnAt(new Position(3, 3)).getSymbol());
    }

    @Test
    void testGetBoardSize() {
        assertEquals(6, board.getBoardSize());
    }

    @Test
    void testSetBoardSize() {
        board.setBoardSize(8);
        assertEquals(8, Board.BOARD_SIZE);
    }

    @Test
    void testGetTotemX() {
        assertEquals(new Position(3, 3), board.getTotemX());
    }

    @Test
    void testGetTotemO() {
        assertEquals(new Position(2, 2), board.getTotemO());
    }

    @Test
    void testCheckRowCol() {
        assertThrows(IllegalArgumentException.class, () -> board.getPawnAt(new Position(-1, 0)));
        assertThrows(IllegalArgumentException.class, () -> board.getPawnAt(new Position(0, 6)));
    }

    @Test
    void testIsValidPositionTrue() {
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                assertTrue(board.isValidPosition(i, j));
            }
        }
    }

    @Test
    void testIsValidPositionFalse() {
        assertFalse(board.isValidPosition(7, 0));
        assertFalse(board.isValidPosition(0, 7));
        assertFalse(board.isValidPosition(7, 7));
    }

    @Test
    void testGetPawnAt() {
        assertNotNull(board.getPawnAt(new Position(2, 2)));
        assertNull(board.getPawnAt(new Position(0, 0)));
    }

    @Test
    void testRemovePawn() {
        Token token = new Token(Symbol.X, Color.BLACK);
        Position pos = new Position(4, 4);
        board.insert(token, pos);
        assertNotNull(board.getPawnAt(pos));
        board.removePawn(pos);
        assertNull(board.getPawnAt(pos));
    }

    @Test
    void testFindTotemThrowsException() {
        assertThrows(IllegalStateException.class, () -> board.findTotem(null));
    }

    @Test
    void testFindTotem() {
        Totem totemO = board.findTotem(Symbol.O);
        assertEquals(Symbol.O, totemO.getSymbol());
        assertEquals(new Position(2, 2), board.getTotemO());

        Totem totemX = board.findTotem(Symbol.X);
        assertEquals(Symbol.X, totemX.getSymbol());
        assertEquals(new Position(3, 3), board.getTotemX());
    }

    @Test
    void testIsEmpty() {
        assertTrue(board.isEmpty(new Position(0, 0)));
        assertFalse(board.isEmpty(new Position(2, 2)));
    }

    @Test
    void testIsFull() {
        assertFalse(board.isFull());

        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                if (board.isEmpty(new Position(i, j))) {
                    board.insert(new Token(Symbol.O, Color.PINK), new Position(i, j));
                }
            }
        }

        assertTrue(board.isFull());
    }

    @Test
    void testFreeCasesLeft() {
        assertEquals(34, board.freeCasesLeft());
        Position pos = new Position(0, 0);
        Token token = new Token(Symbol.O, Color.PINK);
        board.insert(token, pos);
        assertEquals(33, board.freeCasesLeft());
    }

    @Test
    void testMoveTotemO() {
        Totem totemO = board.findTotem(Symbol.O);
        Position newPos = new Position(0, 0);
        assertTrue(board.move(totemO, newPos));
        assertEquals(newPos, board.getTotemO());
        assertNotNull(board.getPawnAt(newPos));
        assertNull(board.getPawnAt(new Position(2, 2)));
    }

    @Test
    void testMoveTotemX() {
        Totem totemX = board.findTotem(Symbol.X);
        Position newPos = new Position(5, 5);
        assertTrue(board.move(totemX, newPos));
        assertEquals(newPos, board.getTotemX());
        assertNotNull(board.getPawnAt(newPos));
        assertNull(board.getPawnAt(new Position(3, 3)));
    }

    @Test
    void testGetTotemPosition() {
        Totem totemO = board.findTotem(Symbol.O);
        assertEquals(new Position(2, 2), board.getTotemPosition(totemO));

        Totem totemX = board.findTotem(Symbol.X);
        assertEquals(new Position(3, 3), board.getTotemPosition(totemX));
    }

    @Test
    void testInsertThrowsException() {
        Token token = new Token(Symbol.O, Color.PINK);
        assertThrows(IllegalArgumentException.class, () -> board.insert(token, new Position(6, 6)));
    }

    @Test
    void testInsert() {
        Position pos = new Position(0, 0);
        Token token = new Token(Symbol.O, Color.PINK);
        board.insert(token, pos);
        assertEquals(token, board.getPawnAt(pos));
    }

    @Test
    void testFreeCasesLeftAfterMultipleInsertions() {
        int initialFreeCases = board.freeCasesLeft();
        board.insert(new Token(Symbol.O, Color.PINK), new Position(0, 0));
        board.insert(new Token(Symbol.X, Color.BLACK), new Position(0, 1));
        board.insert(new Token(Symbol.O, Color.PINK), new Position(0, 2));
        assertEquals(initialFreeCases - 3, board.freeCasesLeft());
    }

    @Test
    void testIsFullWithAlmostFullBoard() {
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                if (board.isEmpty(new Position(i, j))) {
                    board.insert(new Token(Symbol.O, Color.PINK), new Position(i, j));
                }
            }
        }
        board.removePawn(new Position(0, 0));
        assertFalse(board.isFull());
    }

    @Test
    void testMoveToInvalidPosition() {
        Totem totemO = board.findTotem(Symbol.O);
        assertThrows(IllegalArgumentException.class, () -> board.move(totemO, new Position(6, 6)));
    }
}
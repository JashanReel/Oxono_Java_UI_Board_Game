package g62727.dev3.oxono.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OxonoTest {
    private Oxono oxono;
    private Board board;

    @BeforeEach
    void setUp() {
        this.board = new Board(6);
        this.oxono = new Oxono(6);
    }

    @Test
    void testInitialState() {
        assertEquals(GameState.MOVE, oxono.getGameState());
        assertEquals(Color.PINK, oxono.getToPlay().getColor());
        assertNull(oxono.getToInsert());
        assertEquals(0, oxono.getScore(Color.PINK));
        assertEquals(0, oxono.getScore(Color.BLACK));
    }

    @Test
    void testSetAndGetLastMovedTotemSymbol() {
        oxono.setLastMovedTotemSymbol(Symbol.O);
        assertEquals(Symbol.O, oxono.getLastMovedSymbol());
        oxono.setLastMovedTotemSymbol(Symbol.X);
        assertEquals(Symbol.X, oxono.getLastMovedSymbol());
    }

    @Test
    void testIncrementScore() {
        oxono.incrementScore(oxono.getPink());
        assertEquals(1, oxono.getScore(Color.PINK));
        oxono.incrementScore(oxono.getBlack());
        assertEquals(1, oxono.getScore(Color.BLACK));
        oxono.incrementScore(oxono.getBlack());
        assertEquals(1, oxono.getScore(Color.PINK));
        assertEquals(2, oxono.getScore(Color.BLACK));
    }

    @Test
    void testIsFull() {
        assertFalse(oxono.isFull());
        for (int i = 0; i < oxono.getBoardSize(); i++) {
            for (int j = 0; j < oxono.getBoardSize(); j++) {
                if (oxono.getPawnAt(new Position(i, j)) == null) {
                    oxono.insert(new Token(Symbol.O, Color.PINK), new Position(i, j));
                }
            }
        }
        assertTrue(oxono.isFull());
    }

    @Test
    void testFreeCasesLeft() {
        int initialFreeCases = oxono.freeCasesLeft();
        oxono.insert(new Token(Symbol.O, Color.PINK), new Position(0, 0));
        assertEquals(initialFreeCases - 1, oxono.freeCasesLeft());
    }

    @Test
    void testMove() {
        Totem totemO = oxono.findTotem(Symbol.O);
        Position initialPos = oxono.getTotemPosition(totemO);
        Position newPos = new Position(initialPos.getRow() + 1, initialPos.getCol());
        oxono.move(totemO, newPos);
        assertEquals(newPos, oxono.getTotemPosition(totemO));
    }

    @Test
    void testInsert() {
        Position pos = new Position(0, 0);
        assertNull(this.oxono.getPawnAt(pos));
        this.oxono.insert(new Token(Symbol.X, Color.BLACK), pos);
        assertNotNull(this.oxono.getPawnAt(pos));
    }

    @Test
    void testIsValidMove() {
        Totem totemO = oxono.findTotem(Symbol.O);
        assertTrue(oxono.isValidMove(totemO, new Position(2, 0)));
        assertTrue(oxono.isValidMove(totemO, new Position(2, 1)));
        assertTrue(oxono.isValidMove(totemO, new Position(2, 3)));
        assertTrue(oxono.isValidMove(totemO, new Position(2, 4)));
        assertTrue(oxono.isValidMove(totemO, new Position(2, 5)));

        assertTrue(oxono.isValidMove(totemO, new Position(0, 2)));
        assertTrue(oxono.isValidMove(totemO, new Position(1, 2)));
        assertTrue(oxono.isValidMove(totemO, new Position(3, 2)));
        assertTrue(oxono.isValidMove(totemO, new Position(4, 2)));
        assertTrue(oxono.isValidMove(totemO, new Position(5, 2)));

        assertFalse(oxono.isValidMove(totemO, new Position(3, 3)));
        assertFalse(oxono.isValidMove(totemO, new Position(3, 1)));
        assertFalse(oxono.isValidMove(totemO, new Position(1, 1)));
        assertFalse(oxono.isValidMove(totemO, new Position(1, 3)));
    }

    @Test
    void testIsValidInsert() {
        Token token = new Token(Symbol.O, Color.PINK);
        assertTrue(oxono.isValidInsert(token, new Position(2, 1)));
        assertTrue(oxono.isValidInsert(token, new Position(2, 3)));
        assertTrue(oxono.isValidInsert(token, new Position(1, 2)));
        assertTrue(oxono.isValidInsert(token, new Position(3, 2)));
        assertFalse(oxono.isValidInsert(token, new Position(1, 1)));
        assertFalse(oxono.isValidInsert(token, new Position(1, 3)));
        assertFalse(oxono.isValidInsert(token, new Position(3, 1)));
        assertFalse(oxono.isValidInsert(token, new Position(3, 3)));
    }

    @Test
    void testGetValidMoves() {
        Totem totemO = oxono.findTotem(Symbol.O);
        List<Position> validMoves = oxono.getValidMoves(totemO);
        assertFalse(validMoves.isEmpty());
        assertTrue(validMoves.contains(new Position(2, 0)));
        assertTrue(validMoves.contains(new Position(2, 1)));
        assertTrue(validMoves.contains(new Position(2, 3)));
        assertTrue(validMoves.contains(new Position(2, 4)));
        assertTrue(validMoves.contains(new Position(2, 5)));

        assertTrue(validMoves.contains(new Position(0, 2)));
        assertTrue(validMoves.contains(new Position(1, 2)));
        assertTrue(validMoves.contains(new Position(3, 2)));
        assertTrue(validMoves.contains(new Position(4, 2)));
        assertTrue(validMoves.contains(new Position(5, 2)));

        assertFalse(validMoves.contains(new Position(3, 3)));
        assertFalse(validMoves.contains(new Position(3, 1)));
        assertFalse(validMoves.contains(new Position(1, 1)));
        assertFalse(validMoves.contains(new Position(1, 3)));
    }

    @Test
    void testGetValidInsert() {
        List<Position> validInserts = oxono.getValidInsert(board.getTotemO().getRow(), board.getTotemO().getCol());
        assertEquals(4, validInserts.size());
        assertTrue(validInserts.contains(new Position(2, 1)));
        assertTrue(validInserts.contains(new Position(1, 2)));
        assertFalse(validInserts.isEmpty());
    }

    @Test
    void testCheckForWinner_Symbol_Row() {
        assertFalse(oxono.checkForWinner());

        oxono.insert(new Token(Symbol.O, Color.PINK), new Position(0, 0));
        oxono.insert(new Token(Symbol.O, Color.BLACK), new Position(0, 1));
        oxono.insert(new Token(Symbol.O, Color.PINK), new Position(0, 2));
        oxono.insert(new Token(Symbol.O, Color.BLACK), new Position(0, 3));

        assertTrue(oxono.checkForWinner());
    }

    @Test
    void testCheckForWinner_Color_Row() {
        assertFalse(oxono.checkForWinner());

        oxono.insert(new Token(Symbol.O, Color.PINK), new Position(0, 0));
        oxono.insert(new Token(Symbol.X, Color.PINK), new Position(0, 1));
        oxono.insert(new Token(Symbol.O, Color.PINK), new Position(0, 2));
        oxono.insert(new Token(Symbol.X, Color.PINK), new Position(0, 3));

        assertTrue(oxono.checkForWinner());
    }

    @Test
    void testCheckForWinner_Symbol_Col() {
        assertFalse(oxono.checkForWinner());

        oxono.insert(new Token(Symbol.O, Color.PINK), new Position(0, 0));
        oxono.insert(new Token(Symbol.O, Color.BLACK), new Position(1, 0));
        oxono.insert(new Token(Symbol.O, Color.PINK), new Position(2, 0));
        oxono.insert(new Token(Symbol.O, Color.BLACK), new Position(3, 0));

        assertTrue(oxono.checkForWinner());
    }

    @Test
    void testCheckForWinner_Color_Col() {
        assertFalse(oxono.checkForWinner());

        oxono.insert(new Token(Symbol.O, Color.PINK), new Position(0, 0));
        oxono.insert(new Token(Symbol.X, Color.PINK), new Position(1, 0));
        oxono.insert(new Token(Symbol.O, Color.PINK), new Position(2, 0));
        oxono.insert(new Token(Symbol.X, Color.PINK), new Position(3, 0));

        assertTrue(oxono.checkForWinner());
    }

    @Test
    void testCheckForWinner_Color_Diagonal_Fail() {
        assertFalse(oxono.checkForWinner());

        oxono.insert(new Token(Symbol.O, Color.PINK), new Position(0, 0));
        oxono.insert(new Token(Symbol.X, Color.PINK), new Position(1, 1));
        oxono.insert(new Token(Symbol.O, Color.PINK), new Position(2, 2));
        oxono.insert(new Token(Symbol.X, Color.PINK), new Position(3, 3));

        assertFalse(oxono.checkForWinner());
    }

    @Test
    void testCheckForWinner_Symbol_Diagonal_Fail() {
        assertFalse(oxono.checkForWinner());

        oxono.insert(new Token(Symbol.O, Color.PINK), new Position(0, 0));
        oxono.insert(new Token(Symbol.O, Color.BLACK), new Position(1, 1));
        oxono.insert(new Token(Symbol.O, Color.PINK), new Position(2, 2));
        oxono.insert(new Token(Symbol.O, Color.BLACK), new Position(3, 3));

        assertFalse(oxono.checkForWinner());
    }

    @Test
    void testCheckForWinner_Symbol_Row_Totem_Fail() {
        assertFalse(oxono.checkForWinner());

        oxono.insert(new Token(Symbol.O, Color.PINK), new Position(2, 0));
        oxono.insert(new Token(Symbol.O, Color.PINK), new Position(2, 1));
        oxono.insert(new Token(Symbol.O, Color.PINK), new Position(2, 3));

        assertFalse(oxono.checkForWinner());
    }
}
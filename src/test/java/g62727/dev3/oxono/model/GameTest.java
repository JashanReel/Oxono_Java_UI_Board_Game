package g62727.dev3.oxono.model;

import g62727.dev3.oxono.util.Strategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game(6);
    }

    @Test
    void testInitialGameState() {
        assertEquals(Color.PINK, game.getToPlay().getColor());
        assertNotNull(game.getBlack());
        assertNotNull(game.getPink());
        assertEquals(8, game.getBlack().getTokensO());
        assertEquals(8, game.getBlack().getTokensX());
        assertEquals(8, game.getPink().getTokensO());
        assertEquals(8, game.getPink().getTokensX());
    }

    @Test
    void testGetBoardSize() {
        assertEquals(6, game.getBoardSize());
    }

    @Test
    void testGetScore() {
        assertEquals(0, game.getScore(Color.PINK));
        assertEquals(0, game.getScore(Color.BLACK));
    }

    @Test
    void testGetSetLastMovedSymbol() {
        assertNull(game.getLastMovedSymbol());
        game.setLastMovedTotemSymbol(Symbol.X);
        assertEquals(Symbol.X, game.getLastMovedSymbol());
        game.moveTotem(game.findTotem(Symbol.O), 0, 2);
        assertEquals(Symbol.O, game.getLastMovedSymbol());
    }

    @Test
    void testIsValidInsert() {
        assertTrue(game.isValidInsert(new Position(2, 3)));
        assertFalse(game.isValidInsert(new Position(0, 0)));
    }

    @Test
    void testIsValidMove() {
        Totem totem = game.findTotem(Symbol.O);
        assertTrue(game.isValidMove(totem, new Position(2, 3)));
        assertFalse(game.isValidMove(totem, new Position(0, 0)));
    }

    @Test
    void testSurrenderFX() {
        Player winner = game.surrenderFX();
        assertEquals(Color.BLACK, winner.getColor());
        assertEquals(1, game.getScore(Color.BLACK));
        assertFalse(game.isRunning());
    }

    @Test
    void testMoveTotem() {
        Totem totem = game.findTotem(Symbol.O);
        assertTrue(game.moveTotem(totem, 2, 3));
        assertEquals(GameState.INSERT, game.getGameState());
        assertNull(game.getPawnAt(new Position(2, 2)));
        assertNotNull(game.getPawnAt(new Position(2, 3)));
    }

    @Test
    void testInsertToken() {
        game.setGameState(GameState.INSERT);
        game.enoughToMoveTotem(Symbol.O);
        assertNull(game.getPawnAt(new Position(2, 3)));
        assertTrue(game.insertToken(2, 3));
        assertEquals(GameState.MOVE, game.getGameState());
        assertNotNull(game.getPawnAt(new Position(2, 3)));
    }

    @Test
    void testWin() {
        assertFalse(game.win());

        Totem totemO = game.findTotem(Symbol.O);
        if (game.enoughToMoveTotem(Symbol.O)) {
            game.moveTotem(totemO, 3, 2);
            game.insertToken(3, 1);

            game.moveTotem(totemO, 4, 2);
            game.insertToken(4, 1);

            game.moveTotem(totemO, 5, 2);
            game.insertToken(5, 1);

            game.moveTotem(totemO, 2, 2);
            game.insertToken(2, 1);

            assertTrue(game.win());
        }
    }

    @Test
    void testDraw_Pink_Empty() {
        assertFalse(game.isDraw());

        while (game.getPink().getTokensX() > 0) {
            game.getPink().drawX();
        }

        while (game.getPink().getTokensO() > 0) {
            game.getPink().drawO();
        }

        while (game.getBlack().getTokensX() > 0) {
            game.getBlack().drawX();
        }

        while (game.getBlack().getTokensO() > 0) {
            game.getBlack().drawO();
        }

        assertTrue(game.isDraw());
    }

    @Test
    void testDraw_Black_Empty() {
        assertFalse(game.isDraw());

        while (game.getBlack().getTokensX() > 0) {
            game.getBlack().drawX();
        }

        while (game.getBlack().getTokensO() > 0) {
            game.getBlack().drawO();
        }

        while (game.getPink().getTokensX() > 0) {
            game.getPink().drawX();
        }

        while (game.getPink().getTokensO() > 0) {
            game.getPink().drawO();
        }

        assertTrue(game.isDraw());
    }

    @Test
    void test_UndoToken_RedoTotem_UndoTotem() {
        Totem totem = game.findTotem(Symbol.O);
        game.enoughToMoveTotem(totem.getSymbol());
        Position old = new Position(2, 2);
        Position newPos = new Position(2, 3);
        Position insert = new Position(2, 4);
        assertEquals(totem, game.getPawnAt(old));
        assertNull(game.getPawnAt(newPos));

        game.moveTotem(totem, newPos.getRow(), newPos.getCol());

        assertEquals(totem, game.getPawnAt(newPos));
        assertNull(game.getPawnAt(old));
        assertEquals(GameState.INSERT, game.getGameState());

        assertTrue(game.undoToken());

        assertEquals(totem, game.getPawnAt(old));
        assertNull(game.getPawnAt(newPos));
        assertEquals(GameState.MOVE, game.getGameState());

        assertTrue(game.redoTotem());

        assertEquals(totem, game.getPawnAt(newPos));
        assertNull(game.getPawnAt(old));
        assertEquals(GameState.INSERT, game.getGameState());

        game.insertToken(insert.getRow(), insert.getCol());

        assertTrue(game.undoTotem());

        assertEquals(GameState.MOVE, game.getGameState());
        assertEquals(totem, game.getPawnAt(old));
        assertNull(game.getPawnAt(newPos));
        assertNull(game.getPawnAt(insert));

        assertTrue(game.redoTotem());
        assertTrue(game.redoToken());

        assertEquals(totem, game.getPawnAt(newPos));
        assertNull(game.getPawnAt(old));
        assertEquals(GameState.MOVE, game.getGameState());
        assertNotNull(game.getPawnAt(insert));
    }

    @Test
    void testComputerTurn() {
        Totem totem = game.findTotem(Symbol.O);
        game.enoughToMoveTotem(totem.getSymbol());
        Position newPos = new Position(2, 3);
        Position insert = new Position(2, 4);
        game.setComputerStrategy(game.createRandomStrategy());
        game.moveTotem(totem, newPos.getRow(), newPos.getCol());
        game.insertToken(insert.getRow(), insert.getCol());
        int initialFreeCases = game.freeCasesLeft();
        game.computerTurn();
        assertTrue(game.freeCasesLeft() < initialFreeCases);
    }

    @Test
    void testSetComputerStrategy() {
        Strategy strategy = new Strategy() {
            @Override
            public Totem chooseTotem(Oxono oxono) {
                return oxono.findTotem(Symbol.O);
            }

            @Override
            public Position chooseTotemMove(Oxono oxono, Totem totem) {
                return new Position(0, 2);
            }

            @Override
            public Position chooseTokenInsert(Oxono oxono, Totem totem) {
                return new Position(0, 1);
            }
        };
        game.setComputerStrategy(strategy);
        Totem totem = game.findTotem(Symbol.X);
        game.enoughToMoveTotem(totem.getSymbol());
        Position newPos = new Position(4, 3);
        Position insert = new Position(4, 4);
        game.moveTotem(totem, newPos.getRow(), newPos.getCol());
        game.insertToken(insert.getRow(), insert.getCol());
        game.computerTurn();
        assertNotNull(game.getPawnAt(new Position(0, 1)));
    }

    /*@Test
    void testRegisterObserver() {
        TestObserver observer = new TestObserver();
        game.register(observer);
        game.moveTotem(game.findTotem(Symbol.O), 2, 3);
        assertTrue(observer.updated);
    }*/

    /*private static class TestObserver implements Observer {
        boolean updated = false;

        @Override
        public void updateObs(Game game) {
            updated = true;
        }
    }*/
}
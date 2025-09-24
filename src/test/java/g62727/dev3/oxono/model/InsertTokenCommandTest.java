package g62727.dev3.oxono.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InsertTokenCommandTest {
    private Game game;
    private Oxono oxono;
    private Token tokenO;
    private Token tokenX;
    private Position position;
    private Player player;
    private InsertTokenCommand command;

    @BeforeEach
    void setUp() {
        this.game = new Game(6);
        this.game.setGameState(GameState.INSERT);
        this.oxono = new Oxono(6);
        this.player = new Player(Color.PINK, 3, 3, null);
        this.position = new Position(0, 0);
        this.tokenO = new Token(Symbol.O, Color.PINK);
        this.tokenX = new Token(Symbol.X, Color.PINK);
    }

    @Test
    void testExecuteWithOToken() {
        assertEquals(this.game.getPink(), this.game.getToPlay());
        this.command = new InsertTokenCommand(game, oxono, tokenO, position, player);
        assertEquals(GameState.INSERT, this.game.getGameState());
        this.command.execute();
        assertEquals(GameState.MOVE, this.game.getGameState());
        assertEquals(this.game.getBlack(), this.game.getToPlay());
        assertNotNull(this.oxono.getPawnAt(this.position));
        assertEquals(this.tokenO, this.oxono.getPawnAt(this.position));
        assertEquals(2, this.player.getTokensO());
    }

    @Test
    void testExecuteWithXToken() {
        assertEquals(this.game.getPink(), this.game.getToPlay());
        this.command = new InsertTokenCommand(game, oxono, tokenX, position, player);
        assertEquals(GameState.INSERT, this.game.getGameState());
        this.command.execute();
        assertEquals(GameState.MOVE, this.game.getGameState());
        assertEquals(this.game.getBlack(), this.game.getToPlay());
        assertNotNull(this.oxono.getPawnAt(this.position));
        assertEquals(this.tokenX, this.oxono.getPawnAt(this.position));
        assertEquals(2, this.player.getTokensX());
    }

    @Test
    void testUnexecuteWithOToken() {
        this.command = new InsertTokenCommand(game, oxono, tokenO, position, player);
        assertEquals(GameState.INSERT, this.game.getGameState());
        this.command.execute();
        assertEquals(GameState.MOVE, this.game.getGameState());
        this.command.unexecute();
        assertEquals(GameState.INSERT, this.game.getGameState());
        assertNull(this.oxono.getPawnAt(this.position));
        assertEquals(3, this.player.getTokensO());
    }

    @Test
    void testUnexecuteWithXToken() {
        this.command = new InsertTokenCommand(game, oxono, tokenX, position, player);
        assertEquals(GameState.INSERT, this.game.getGameState());
        this.command.execute();
        assertEquals(GameState.MOVE, this.game.getGameState());
        this.command.unexecute();
        assertEquals(GameState.INSERT, this.game.getGameState());
        assertNull(this.oxono.getPawnAt(this.position));
        assertEquals(3, this.player.getTokensX());
    }
}
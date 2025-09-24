package g62727.dev3.oxono.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveTotemCommandTest {
    private Game game;
    private Oxono oxono;
    private Totem totem;
    private Position oldPosition;
    private Position newPosition;
    private MoveTotemCommand command;

    @BeforeEach
    void setUp() {
        this.game = new Game(6);
        this.oxono = new Oxono(6);
        totem = this.oxono.findTotem(Symbol.O);
        oldPosition = new Position(2, 2);
        newPosition = new Position(2, 3);
        command = new MoveTotemCommand(game, oxono, totem, newPosition, oldPosition.getRow(), oldPosition.getCol());
    }

    @Test
    void testConstructor() {
        assertNotNull(command);
    }

    @Test
    void testExecute() {
        command.execute();
        assertEquals(GameState.INSERT, this.game.getGameState());
        assertEquals(newPosition, oxono.totem_O_Pos());
    }

    @Test
    void testUnexecute() {
        assertEquals(GameState.MOVE, this.game.getGameState());
        command.execute();
        assertEquals(GameState.INSERT, this.game.getGameState());
        command.unexecute();
        assertEquals(GameState.MOVE, this.game.getGameState());
        assertEquals(oldPosition, oxono.totem_O_Pos());
    }

    @Test
    void testExecuteFollowedByUnexecute() {
        command.execute();
        assertEquals(newPosition, oxono.totem_O_Pos());

        command.unexecute();
        assertEquals(oldPosition, oxono.totem_O_Pos());
    }

    @Test
    void testMultipleExecutions() {
        command.execute();
        command.execute();
        assertEquals(newPosition, oxono.totem_O_Pos());
    }

    @Test
    void testMultipleUnexecutions() {
        command.execute();
        command.unexecute();
        command.unexecute();
        assertEquals(oldPosition, oxono.totem_O_Pos());
    }
}
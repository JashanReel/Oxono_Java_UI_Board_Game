package g62727.dev3.oxono.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player human;
    private Player computer;

    @BeforeEach
    void setUp() {
        this.human = new Player(Color.PINK, 8, 8, null);
        this.computer = new Player(Color.BLACK, 8, 8, new RandomStrategy());
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(Color.PINK, this.human.getColor());
        assertEquals(8, this.human.getTokensO());
        assertEquals(8, this.human.getTokensX());
        assertNull(this.human.getStrategy());

        assertEquals(Color.BLACK, this.computer.getColor());
        assertEquals(8, this.computer.getTokensO());
        assertEquals(8, this.computer.getTokensX());
        assertNotNull(this.computer.getStrategy());
    }

    @Test
    void testDrawX() {
        assertTrue(this.human.drawX());
        assertEquals(7, this.human.getTokensX());

        for (int i = 0; i < 7; i++) {
            assertTrue(this.human.drawX());
        }

        assertFalse(this.human.drawX());
        assertEquals(0, this.human.getTokensX());
    }

    @Test
    void testDrawO() {
        assertTrue(this.human.drawO());
        assertEquals(7, this.human.getTokensO());

        for (int i = 0; i < 7; i++) {
            assertTrue(this.human.drawO());
        }

        assertFalse(this.human.drawO());
        assertEquals(0, this.human.getTokensO());
    }

    @Test
    void testAddX() {
        this.human.drawX();
        this.human.addX();
        assertEquals(8, this.human.getTokensX());
    }

    @Test
    void testAddO() {
        this.human.drawO();
        this.human.addO();
        assertEquals(8, this.human.getTokensO());
    }

    @Test
    void testToString() {
        assertEquals("Player{color=PINK}", this.human.toString());
        assertEquals("Player{color=BLACK}", this.computer.toString());
    }
}
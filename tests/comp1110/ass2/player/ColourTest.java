package comp1110.ass2.player;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColourTest {

    @Test
    void charToColour() {
        assertEquals(Colour.YELLOW, Colour.charToColour('y'));
        assertEquals(Colour.RED, Colour.charToColour('r'));
        assertEquals(Colour.CYAN, Colour.charToColour('c'));
        assertEquals(Colour.PURPLE, Colour.charToColour('p'));
    }

    @Test
    void isColourValid() {
        assertTrue(Colour.isColourValid('y'));
        assertTrue(Colour.isColourValid('r'));
        assertTrue(Colour.isColourValid('c'));
        assertTrue(Colour.isColourValid('p'));
        for (int i = 0; i < 26; i++) {
            char colour = (char) (i + 97);
            if (colour != 'y' && colour != 'r' && colour != 'c' && colour != 'p') {
                Assertions.assertFalse(Colour.isColourValid(colour));
            }
        }
    }

    @Test
    void testToString() {
        assertEquals("YELLOW", Colour.YELLOW.toString());
        assertEquals("RED", Colour.RED.toString());
        assertEquals("CYAN", Colour.CYAN.toString());
        assertEquals("PURPLE", Colour.PURPLE.toString());
    }
}
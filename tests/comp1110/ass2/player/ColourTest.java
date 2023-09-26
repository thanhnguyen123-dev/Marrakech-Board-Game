package comp1110.ass2.player;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColourTest {

    @Test
    void charToColour() {
    }

    @Test
    void isColourValid() {
        Assertions.assertTrue(Colour.isColourValid('y'));
        Assertions.assertTrue(Colour.isColourValid('r'));
        Assertions.assertTrue(Colour.isColourValid('c'));
        Assertions.assertTrue(Colour.isColourValid('p'));
        for (int i = 0; i < 26; i++) {
            char colour = (char) (i + 97);
            if (colour != 'y' && colour != 'r' && colour != 'c' && colour != 'p') {
                Assertions.assertFalse(Colour.isColourValid(colour));
            }
        }
    }

    @Test
    void colourToString() {
    }

    @Test
    void getFrontEndColor() {
    }
}
package comp1110.ass2.player;

import javafx.scene.paint.Color;

/**
 * Colour enum represents the colour of each player's rug
 * @author Le Thanh Nguyen u7594144
 */
public enum Colour {
    YELLOW('y'),
    RED('r'),
    CYAN('c'),
    PURPLE('p');

    public final char colourChar;

    /**
     * Constructor: creates an instance of the Colour enum
     * @param colourChar character for the colour
     * @author Le Thanh Nguyen u7594144
     */
    Colour(char colourChar) {
        this.colourChar = colourChar;
    }


    /**
     * Converts the character to its corresponding colour
     * @param colourChar character for the colour
     * @return the corresponding colour
     * @author Le Thanh Nguyen u7594144
     */
    public static Colour charToColour(char colourChar) {
        for (Colour colour : Colour.values()) {
            if (colour.colourChar == colourChar) {
                return colour;
            }
        }
        return null;
    }

    /**
     * Determines whether the colour of a rug is valid or not
     * @param colourChar character for the rug colour
     * @return true if rug colour is valid, or false otherwise
     * @author u7582846 Yaolin Li
     */
    public static boolean isColourValid(char colourChar) {
        return charToColour(colourChar) != null;
    }

    /**
     * Converts a Colour enum to a string of its full name
     * @param colour a Colour enum
     * @return full name of the colour
     * @author Le Thanh Nguyen u7594144
     */
    public static String colourToString(Colour colour) {
        return switch (colour) {
            case YELLOW -> "Yellow";
            case RED -> "Red";
            case CYAN -> "Cyan";
            case PURPLE -> "Purple";
        };
    }

    /**
     * @param colour
     * @return
     * @author Le Thanh Nguyen u7594144
     */
    public static Color getFrontEndColor(Colour colour) {
        return switch (colour) {
            case YELLOW -> Color.YELLOW;
            case RED -> Color.RED;
            case CYAN -> Color.CYAN;
            case PURPLE -> Color.PURPLE;
        };
    }

    /**
     * getter method for colourChar
     * @return character for the colour
     * @author Le Thanh Nguyen u7594144
     */
    public char getColourChar() {
        return colourChar;
    }
}

package comp1110.ass2.player;

import javafx.scene.paint.Color;

/**
 * Colour enum represents each player's representative
 */
public enum Colour {
    YELLOW('y'),
    RED('r'),
    CYAN('c'),
    PURPLE('p');

    public final char colourChar;

    /**
     * Constructor: creates an instance of the Colour enum
     * @param colourChar character for the Colour
     */
    Colour(char colourChar) {
        this.colourChar = colourChar;
    }


    /**
     * Convert the character to its corresponding colour
     * @param colourChar character for the Colour
     * @return the corresponding Colour
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
     * Determine whether the colour of a rug is valid
     * @param colourChar: char of rug colour
     * @return true if rug colour is valid, or false otherwise
     */
    public static boolean isColourValid(char colourChar){
        if (charToColour(colourChar)!=null){
            return true;
        }else return false;
    }

    /**
     * Converts Colour enum to a string of its full name
     * @param colour Colour enum
     * @return full name of the colour
     */
    public static String colourToString(Colour colour) {
        switch (colour) {
            case YELLOW: return "Yellow";
            case RED: return "Red";
            case CYAN: return "Cyan";
            case PURPLE: return "Purple";
            default: return null;
        }
    }

    public static Color getFrontEndColor(Colour colour) {
        switch (colour) {
            case YELLOW: return Color.YELLOW;
            case RED: return Color.RED;
            case CYAN: return Color.CYAN;
            case PURPLE: return Color.PURPLE;
            default: return null;
        }
    }
}

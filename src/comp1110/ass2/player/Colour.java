package comp1110.ass2.player;

import javafx.scene.paint.Color;

/**
 * Colour enum represents the colour of each player's rug
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
     */
    Colour(char colourChar) {
        this.colourChar = colourChar;
    }


    /**
     * Converts the character to its corresponding colour
     * @param colourChar character for the colour
     * @return the corresponding colour
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
     * @author u7582846 Yaolin Li
     * @param colourChar character for the rug colour
     * @return true if rug colour is valid, or false otherwise
     */
    public static boolean isColourValid(char colourChar){
        if (charToColour(colourChar)!=null){
            return true;
        }else return false;
    }

    /**
     * Converts a Colour enum to a string of its full name
     * @param colour a Colour enum
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

package comp1110.ass2.player;

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
     * @param colourChar
     */
    Colour(char colourChar) {
        this.colourChar = colourChar;
    }


    /**
     * convert the character to its corresponding colour
     * @param colourChar
     * @return Colour
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
}

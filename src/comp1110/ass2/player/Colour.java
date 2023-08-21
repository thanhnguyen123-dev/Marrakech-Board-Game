package comp1110.ass2.player;

/**
 * Colour enum represents each player's representative
 */
public enum Colour {
    YELLOW('y'),
    RED('r'),
    CYAN('c'),
    PURPLE('p');

    public final char colorChar;

    /**
     * Constructor: creates an instance of the Colour enum
     * @param color
     */
    Colour(char color) {
        this.colorChar = color;
    }


    /**
     * convert the character to its corresponding colour
     * @param colourChar
     * @return Colour
     */
    public Colour charToColor(char colourChar) {
        switch (colourChar) {
            case 'y': return Colour.YELLOW;
            case 'r': return Colour.RED;
            case 'c': return Colour.CYAN;
            case 'p': return Colour.PURPLE;
            default: return null;
        }
    }

}

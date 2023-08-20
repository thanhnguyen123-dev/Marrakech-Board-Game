package comp1110.ass2.player;

/**
 * Colour of players' representative
 */
public enum Colour {
    YELLOW('y'),
    RED('r'),
    CYAN('c'),
    PURPLE('p');

    public final char colorChar;

    Colour(char color) {
        this.colorChar = color;
    }


    public Colour charToColor(char colorChar) {
        switch (colorChar) {
            case 'y': return Colour.YELLOW;
            case 'r': return Colour.RED;
            case 'c': return Colour.CYAN;
            case 'p': return Colour.PURPLE;
            default: return null;
        }
    }

}

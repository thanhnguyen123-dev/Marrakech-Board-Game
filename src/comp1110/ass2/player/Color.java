package comp1110.ass2.player;

public enum Color {
    YELLOW('y'),
    RED('r'),
    CYAN('c'),
    PURPLE('p');

    public final char colorChar;

    Color(char color) {
        this.colorChar = color;
    }


    public Color charToColor(char colorChar) {
        switch (colorChar) {
            case 'y': return Color.YELLOW;
            case 'r': return Color.RED;
            case 'c': return Color.CYAN;
            case 'p': return Color.PURPLE;
            default: return null;
        }
    }

}

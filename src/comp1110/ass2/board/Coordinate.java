package comp1110.ass2.board;

public class Coordinate {
    // x value
    private final int x;

    // y value
    private final int y;

    /**
     * Constructor to get the coordinate value
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

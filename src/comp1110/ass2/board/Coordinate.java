package comp1110.ass2.board;

/**
 * Coordinate class represents an object's position
 */
public class Coordinate {
    private final int x;

    private final int y;

    /**
     * Constructor: creates a new instance of the Coordinate class
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * getter method for x
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * getter method for y
     * @return y
     */
    public int getY() {
        return y;
    }
}

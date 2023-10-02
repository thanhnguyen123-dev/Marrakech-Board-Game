package comp1110.ass2.board;

/**
 * Coordinate class represents an object's position
 * (unused for now)
 * @author Le Thanh Nguyen u7594144
 */
public class Coordinate {
    private final int x;

    private final int y;

    /**
     * Constructor: creates a new instance of the Coordinate class
     * @author Le Thanh Nguyen u7594144
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * getter method for x
     * @return x
     * @author Le Thanh Nguyen u7594144
     */
    public int getX() {
        return x;
    }

    /**
     * getter method for y
     * @return y
     * @author Le Thanh Nguyen u7594144
     */
    public int getY() {
        return y;
    }
}

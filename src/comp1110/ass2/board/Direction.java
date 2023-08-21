package comp1110.ass2.board;

/**
 * Possible rotation direction for Assam
 */
public enum Direction {
    NORTH(0, 'N'),
    EAST(90, 'E'),
    SOUTH(180, 'S'),
    WEST(270, 'W');

    public int angleValue;
    public final char direction;

    /**
     * Constructor: creates a new instance for the Direction enum
     * @param angle
     * @param direction
     */
    Direction(int angle, char direction) {
        this.angleValue = angle;
        this.direction = direction;
    }

    /**
     * Apply the desired rotation to Assam
     * @param rotation
     */
    public void applyRotation(int rotation) {
        angleValue = (angleValue + rotation) % 360;
    }


}

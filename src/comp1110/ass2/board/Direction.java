package comp1110.ass2.board;

/**
 * Possible rotation direction for Assam
 */
public enum Direction {
    EAST(0, 'E'),
    SOUTH(90, 'S'),
    WEST(180, 'W'),
    NORTH(270, 'N');

    private int angle;
    private final char directionChar;

    /**
     * Constructor: creates a new instance for the Direction enum
     * @param angle angle of the direction in degrees
     * @param directionChar character for the direction
     */
    Direction(int angle, char directionChar) {
        this.angle = angle;
        this.directionChar = directionChar;
    }

    /**
     * Applies the desired rotation to the current angle
     * @param rotation rotation in degrees
     */
    public Direction rotate(int rotation) {
        return angleToDirection(this.angle + rotation);
    }

    /**
     * Converts angle to the corresponding direction
     * @param angle angle in degrees
     * @return the corresponding direction
     */
    public static Direction angleToDirection(int angle) {
        angle = (angle + 360) % 360;
        for (Direction direction : Direction.values()) {
            if (angle == direction.angle) {
                return direction;
            }
        }
        return null;
    }

    /**
     * Converts character to the corresponding direction
     * @param directionChar character for the direction
     * @return the corresponding direction
     */
    public static Direction charToDirection(char directionChar) {
        for (Direction direction : Direction.values()) {
            if (direction.directionChar == directionChar) {
                return direction;
            }
        }
        return null;
    }

    /**
     * getter method for directionChar
     * @return character for the direction
     */
    public char getDirectionChar() {
        return directionChar;
    }

    /**
     * getter method for angle
     * @return angle of the direction in degrees
     */
    public int getAngle() {
        return angle;
    }
}
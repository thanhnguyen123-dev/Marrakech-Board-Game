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
     * @param angle angle of the Direction in degrees
     * @param directionChar character for the Direction
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
     * Converts angle to the corresponding Direction
     * @param angle angle in degrees
     * @return the corresponding Direction
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
     * Converts character to the corresponding Direction
     * @param directionChar character for the Direction
     * @return the corresponding Direction
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
     * @return character for the Direction
     */
    public char getDirectionChar() {
        return directionChar;
    }

    /**
     * @return angle of the Direction in degrees
     */
    public int getAngle() {
        return angle;
    }
}
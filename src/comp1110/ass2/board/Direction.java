package comp1110.ass2.board;

/**
 * Possible rotation direction for Assam
 */
public enum Direction {
    EAST(0, 'E'),
    SOUTH(90, 'S'),
    WEST(180, 'W'),
    NORTH(270, 'N');

    private int angleValue;
    private final char directionChar;

    /**
     * Constructor: creates a new instance for the Direction enum
     * @param angle
     * @param directionChar
     */
    Direction(int angle, char directionChar) {
        this.angleValue = angle;
        this.directionChar = directionChar;
    }

    /**
     * Apply the desired rotation to Assam
     * @param rotation
     */
    public void applyRotation(int rotation) {
        angleValue = (angleValue + rotation) % 360;
    }

    public Direction rotate(int rotation) {
        return angleToDirection(this.angleValue + rotation);
    }

    public static Direction angleToDirection(int angleValue) {
        angleValue = (angleValue + 360) % 360;
        for (Direction direction : Direction.values()) {
            if (angleValue == direction.angleValue) {
                return direction;
            }
        }
        return null;
    }

    /**
     * Convert character to corresponding Direction
     * @param directionChar
     * @return
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
     * @return directionChar
     */
    public char getDirectionChar() {
        return directionChar;
    }

    /**
     * getter method for angleValue
     * @return angleValue
     */
    public int getAngleValue() {
        return angleValue;
    }
}
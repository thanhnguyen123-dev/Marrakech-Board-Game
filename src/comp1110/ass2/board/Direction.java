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
    public final char directionChar;

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

    public static Direction charToDirection(char directionChar) {
        for (Direction direction : Direction.values()) {
            if (direction.directionChar == directionChar) {
                return direction;
            }
        }
        return null;
    }
}
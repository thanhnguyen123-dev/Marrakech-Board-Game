package comp1110.ass2.board;

/**
 * Possible rotation direction for Assam
 * @author u7594144 Le Thanh Nguyen, u7620014 Haobo Zou
 */
public enum Direction {
    NORTH(0, 'N'),
    EAST(90, 'E'),
    SOUTH(180, 'S'),
    WEST(270, 'W');

    private final int angle;
    private final char directionChar;

    /**
     * Constructor: creates a Direction enum
     * @param angle angle of the direction in degrees
     * @param directionChar character for the direction
     * @author Le Thanh Nguyen u7594144
     */
    Direction(int angle, char directionChar) {
        this.angle = angle;
        this.directionChar = directionChar;
    }

    /**
     * Applies the desired rotation to the current angle
     * @param rotation rotation in degrees
     * @author u7620014 Haobo Zou
     */
    public Direction rotate(int rotation) {
        return angleToDirection(this.angle + rotation);
    }

    /**
     * Converts angle to the corresponding direction
     * @param angle angle in degrees
     * @return the corresponding direction
     * @author u7620014 Haobo Zou
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
     * @author Le Thanh Nguyen u7594144
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
     * @author Le Thanh Nguyen u7594144
     */
    public char getDirectionChar() {
        return directionChar;
    }

    /**
     * getter method for angle
     * @return angle of the direction in degrees
     * @author Le Thanh Nguyen u7594144
     */
    public int getAngle() {
        return angle;
    }

    /**
     * Converts a Direction enum to a string of its full name
     * @return full name of the direction
     * @author u7620014 Haobo Zou
     */
    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "NORTH";
            case EAST -> "EAST";
            case SOUTH -> "SOUTH";
            case WEST -> "WEST";
        };
    }
}
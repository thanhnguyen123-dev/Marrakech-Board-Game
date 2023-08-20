package comp1110.ass2.player;

public enum Direction {
    EAST(90, 'E'),
    WEST(270,'W'),
    SOUTH(180, 'S'),
    NORTH(0, 'N');

    public final int angleValue;
    public final char direction;

    Direction(int angle, char direction) {
        this.angleValue = angle;
        this.direction = direction;
    }


}

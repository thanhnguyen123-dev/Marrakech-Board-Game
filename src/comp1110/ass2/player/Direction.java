package comp1110.ass2.player;

public enum Direction {
    NORTH(0, 'N'),
    EAST(90, 'E'),
    SOUTH(180, 'S'),
    WEST(270,'W');

    public int angleValue;
    public final char direction;

    Direction(int angle, char direction) {
        this.angleValue = angle;
        this.direction = direction;
    }

    public void applyRotation(int rotation) {
        angleValue = (angleValue + rotation) % 360;
    }


}

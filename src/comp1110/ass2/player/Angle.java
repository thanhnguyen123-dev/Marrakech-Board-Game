package comp1110.ass2.player;

public enum Angle {
    DEGREE_0(0),
    DEGREE_90(90),
    DEGREE_180(180),
    DEGREE_270(270);

    public final int angleValue;
    Angle(int angle) {
        this.angleValue = angle;
    }
}

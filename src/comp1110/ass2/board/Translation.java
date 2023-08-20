package comp1110.ass2.board;

public class Translation {
    Coordinate translation;

    public Translation(Coordinate translation) {
        this.translation = translation;
    }

    public Coordinate applyTranslation(Coordinate location) {
        int newX = location.getX() + translation.getX();
        int newY = location.getY() + translation.getY();
        return new Coordinate(newX, newY);
    }
}

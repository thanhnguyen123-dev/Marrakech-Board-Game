package comp1110.ass2.board;

/**
 * Translation class calculates the new position of Assam after translation
 * (unused for now)
 * @author Le Thanh Nguyen u7594144
 */
public class Translation {
    Coordinate translation;

    /**
     * Constructor: creates an instance of the Translation class
     * @param translation
     */
    public Translation(Coordinate translation) {
        this.translation = translation;
    }

    /**
     * calculate the new coordinate after translation
     * @param location
     * @return
     */
    public Coordinate applyTranslation(Coordinate location) {
        int newX = location.getX() + translation.getX();
        int newY = location.getY() + translation.getY();
        return new Coordinate(newX, newY);
    }
}

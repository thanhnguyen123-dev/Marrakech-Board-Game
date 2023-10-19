package comp1110.ass2.gui;

import comp1110.ass2.gui.Game.Orientation;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * a custom rectangle class used to represent all the possible rug placements on the board inside Game class
 * @author u7620014 Haobo Zou
 */
class GameInvisibleRug extends Rectangle {
    private final GameTile[] gameTiles = new GameTile[2];
    private final Orientation orientation;

    /**
     * Constructor: creates an instance of the GameInvisibleRug class
     * @param x the x-axis relocation of its centre
     * @param y the y-axis relocation of its centre
     * @param gameTiles an array consisting of the two game tiles occupied by the invisible rug
     * @param orientation the orientation of the invisible rug
     * @author u7620014 Haobo Zou
     */
    public GameInvisibleRug(double x, double y, GameTile[] gameTiles, Orientation orientation) {
        super(0, 0, Game.TILE_SIDE, 2 * Game.TILE_SIDE);
        this.gameTiles[0] = gameTiles[0];
        this.gameTiles[1] = gameTiles[1];
        this.orientation = orientation;

        this.relocate(x - Game.TILE_SIDE / 2, y - Game.TILE_SIDE);
        if (orientation == Orientation.HORIZONTAL) {
            this.setRotate(90);
        }
        this.setFill(Color.WHITE);
        this.setStroke(Color.WHITE.darker());
        this.setStrokeWidth(Game.RUG_BORDER_WIDTH);
        this.setOpacity(0);
    }

    /**
     * getter method for orientation
     * @return the orientation of the invisible rug
     * @author u7620014 Haobo Zou
     */
    public Orientation getOrientation() {
        return this.orientation;
    }

    /**
     * getter method for gameTiles
     * @return an array consisting of the two game tiles occupied by the invisible rug
     * @author u7620014 Haobo Zou
     */
    public GameTile[] getGameTiles() {
        return this.gameTiles;
    }

    /**
     * Calculates the distance between the centre of the invisible rug and a given position
     * @param x the x coordinate of the given position
     * @param y the y coordinate of the given position
     * @return the distance between the centre of the invisible rug and the position
     * @author u7620014 Haobo Zou
     */
    public double distance(double x, double y) {
        return Math.sqrt((this.getLayoutX() - x) * (this.getLayoutX() - x) + (this.getLayoutY() - y) * (this.getLayoutY() - y));
    }
}

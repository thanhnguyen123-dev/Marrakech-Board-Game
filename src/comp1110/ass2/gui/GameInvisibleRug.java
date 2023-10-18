package comp1110.ass2.gui;

import comp1110.ass2.gui.Game.Orientation;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class GameInvisibleRug extends Rectangle {
    private final GameTile[] gameTiles = new GameTile[2];
    private final Orientation orientation;

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

    public Orientation getOrientation() {
        return this.orientation;
    }

    public GameTile[] getGameTiles() {
        return this.gameTiles;
    }

    public double distance(double x, double y) {
        return Math.sqrt((this.getLayoutX() - x) * (this.getLayoutX() - x) + (this.getLayoutY() - y) * (this.getLayoutY() - y));
    }
}

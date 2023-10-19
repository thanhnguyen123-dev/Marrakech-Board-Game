package comp1110.ass2.gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Represent the Mosaic Track Semi-Circle of the game
 * @author Le Thanh Nguyen
 */
public class GameMosaicTrack extends Circle {
    private final double CIRCLE_RADIUS = 43.5;
    public GameMosaicTrack(double x, double y) {
        this.setCenterX(x);
        this.setCenterY(y);
        this.setRadius(CIRCLE_RADIUS);

        this.setFill(Color.LIGHTBLUE);
        this.setStroke(Color.LIGHTBLUE.darker());
        this.setStrokeWidth(Game.TILE_BORDER_WIDTH - 0.5);
    }

}

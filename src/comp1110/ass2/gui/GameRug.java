package comp1110.ass2.gui;

import comp1110.ass2.player.Colour;
import comp1110.ass2.gui.Game.Orientation;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * a custom rectangle class used to represent placed rugs on the board inside Game class
 * @author u7620014 Haobo Zou
 */
class GameRug extends Rectangle {
    /**
     * Constructor: creates an instance of the GameRug class
     * @param x the x-axis relocation
     * @param y the y-axis relocation
     * @param orientation the orientation of the rug
     * @param colour the colour of the rug
     * @author u7620014 Haobo Zou
     */
    public GameRug(double x, double y, Orientation orientation, Colour colour) {
        super(0, 0, Game.TILE_SIDE, 2 * Game.TILE_SIDE);

        this.relocate(x, y);
        if (orientation == Orientation.HORIZONTAL) {
            this.setRotate(90);
        }
        Color color = Colour.getFrontEndColor(colour);
        if (color != null) {
            this.setFill(Colour.getFrontEndColor(colour));
            this.setStroke(color.darker());
            this.setStrokeWidth(Game.RUG_BORDER_WIDTH);
        }
    }
}

package comp1110.ass2.gui;

import comp1110.ass2.player.Colour;
import comp1110.ass2.gui.Game.Orientation;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class GameRug extends Rectangle {
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

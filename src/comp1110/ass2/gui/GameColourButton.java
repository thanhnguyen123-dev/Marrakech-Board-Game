package comp1110.ass2.gui;

import comp1110.ass2.player.Colour;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

class GameColourButton extends Button {
    private final Border border;

    public GameColourButton(Colour colour) {
        super();
        this.setShape(new Circle(Game.COLOUR_BUTTON_RADIUS));
        this.setMinSize(2 * Game.COLOUR_BUTTON_RADIUS, 2 * Game.COLOUR_BUTTON_RADIUS);
        this.setMaxSize(2 * Game.COLOUR_BUTTON_RADIUS, 2 * Game.COLOUR_BUTTON_RADIUS);
        this.setBackground(new Background(new BackgroundFill(Colour.getFrontEndColor(colour), CornerRadii.EMPTY, Insets.EMPTY)));
        this.border = new Border(new BorderStroke(Colour.getFrontEndColor(colour).darker(), Game.COLOUR_BUTTON_BORDER_STROKE_STYLE, Game.COLOUR_BUTTON_BORDER_RADII, Game.COLOUR_BUTTON_BORDER_WIDTH));
    }

    public void addBorder() {
        this.setBorder(this.border);
    }
}

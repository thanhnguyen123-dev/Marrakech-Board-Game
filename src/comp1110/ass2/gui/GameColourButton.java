package comp1110.ass2.gui;

import comp1110.ass2.player.Colour;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

/**
 * a custom coloured button class used for player colour selection inside Game class
 * @author u7620014 Haobo Zou
 */
class GameColourButton extends Button {
    private final Border border;

    /**
     * Constructor: creates an instance of the GameColourButton class
     * @param colour the colour of the corresponding player
     * @author u7620014 Haobo Zou
     */
    public GameColourButton(Colour colour) {
        super();
        this.setShape(new Circle(Game.COLOUR_BUTTON_RADIUS));
        this.setMinSize(2 * Game.COLOUR_BUTTON_RADIUS, 2 * Game.COLOUR_BUTTON_RADIUS);
        this.setMaxSize(2 * Game.COLOUR_BUTTON_RADIUS, 2 * Game.COLOUR_BUTTON_RADIUS);
        this.setBackground(new Background(new BackgroundFill(Colour.getFrontEndColor(colour), CornerRadii.EMPTY, Insets.EMPTY)));
        this.border = new Border(new BorderStroke(Colour.getFrontEndColor(colour).darker(), Game.COLOUR_BUTTON_BORDER_STROKE_STYLE, Game.COLOUR_BUTTON_BORDER_RADII, Game.COLOUR_BUTTON_BORDER_WIDTH));
    }

    /**
     * Adds a border to the coloured button
     * @author u7620014 Haobo Zou
     */
    public void addBorder() {
        this.setBorder(this.border);
    }
}

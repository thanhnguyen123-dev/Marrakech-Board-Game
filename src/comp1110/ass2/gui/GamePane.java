package comp1110.ass2.gui;

import javafx.scene.layout.Pane;

/**
 * a custom pane class used for splitting displayed content inside Game class
 * @author u7620014 Haobo Zou
 */
class GamePane extends Pane {
    /**
     * Constructor: creates an instance of the GamePane class
     * @param width a fixed width
     * @param height a fixed height
     * @author u7620014 Haobo Zou
     */
    public GamePane(double width, double height) {
        super();
        this.setMinSize(width, height);
        this.setMaxSize(width, height);
    }
}
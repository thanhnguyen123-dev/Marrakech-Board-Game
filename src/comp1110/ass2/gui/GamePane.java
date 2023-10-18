package comp1110.ass2.gui;

import javafx.scene.layout.Pane;

class GamePane extends Pane {
    public GamePane(double width, double height) {
        super();
        this.setMinSize(width, height);
        this.setMaxSize(width, height);
    }

}

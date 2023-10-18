package comp1110.ass2.gui;

import javafx.scene.control.Button;

class GameButton extends Button {
    public GameButton(String string, double width, double height) {
        super(string);
        this.setMinSize(width, height);
        this.setMaxSize(width, height);
    }
}

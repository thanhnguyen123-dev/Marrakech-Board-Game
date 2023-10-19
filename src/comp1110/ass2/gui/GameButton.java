package comp1110.ass2.gui;

import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * Customized rectangle buttons
 * @author u7620014 Haobo Zou, u7582846 Yaolin Li
 */
class GameButton extends Button {
    public GameButton(String string, double width, double height) {
        super(string);
        this.setStyle("-fx-text-fill: white;-fx-background-color: #672F09;");
        Font BUTTON_FONT = Font.font("Verdana", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 18);
        this.setFont(BUTTON_FONT);
        this.setMinSize(width, height);
        this.setMaxSize(width, height);
    }
}

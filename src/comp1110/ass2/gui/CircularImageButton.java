package comp1110.ass2.gui;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Customized circular image buttons
 *
 * @author u7582846 Yaolin Li
 */
public class CircularImageButton {
    public static Button createCircularImageButton(String imagePath) {
        // create button
        Button circularButton = new Button();
        circularButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");

        // load picture
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);

        // set picture as the button's background
        circularButton.setGraphic(imageView);

        return circularButton;
    }
}

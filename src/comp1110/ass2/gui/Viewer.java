package comp1110.ass2.gui;

import comp1110.ass2.GameState;
import comp1110.ass2.board.Board;
import comp1110.ass2.board.Direction;
import comp1110.ass2.board.Tile;
import comp1110.ass2.player.Colour;
import comp1110.ass2.player.Player;
import comp1110.ass2.player.Rug;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Viewer extends Application {

    private static final int VIEWER_WIDTH = 1200;
    private static final int VIEWER_HEIGHT = 700;
    private final Group root = new Group();
    private final Group controls = new Group();
    private final Group display = new Group();
    private final Group images = new Group();
    private final Group mosaicTrack = new Group();
    private TextField boardTextField;
    private FrontEndTile[][] frontEndTiles = new FrontEndTile[Board.NUM_OF_ROWS][Board.NUM_OF_COLS];
    private final double CIRCLE_RADIUS = 50;
    private final double SQUARE_SIDE = 75;

    /**
     * Subclass FrontEndTile that inherits properties from Superclass Polygon, containing BackEnd information
     */
    public class FrontEndTile extends Polygon {
        private Tile backEndTile;
        private double side;

        /**
         * Constructor: creates an instance of FrontEndTile
         * FrontEnd representation of a Tile
         */
        public FrontEndTile(double x, double y, double side, Tile backEndTile) {
            double halfSide = side / 2;
            this.backEndTile = backEndTile;
            this.side = side;

            // plot the points for the Square tile
            this.getPoints().setAll(
                    x - halfSide, y + halfSide, x + halfSide, y + halfSide,
                    x + halfSide, y - halfSide, x - halfSide, y - halfSide
            );
            this.setStroke(Color.BLACK);
            this.setStrokeWidth(2);
        }
    }

    public class MosaicTrackCircle extends Circle {
        public MosaicTrackCircle(double x, double y) {
            this.setCenterX(x);
            this.setCenterY(y);
            this.setRadius(CIRCLE_RADIUS);
            this.setFill(Color.LIGHTBLUE);
            this.setStroke(Color.BLACK);
            this.setStrokeWidth(1.5);
        }
    }

    /**
     * Display the Players information at the current game state
     */
    public void displayPlayerInfo(String state) {
        GameState gameState = new GameState(state);
        Player[] players = gameState.getPlayers();
        double xPixelValue = 750;
        double yPixelValue = 125;
        for (Player player : players) {
            // BackEnd data of Player
            int remainingDirhams = player.getDirham();
            int numOfRemainingRugs = player.getNumOfUnplacedRugs();
            Colour playerColour = player.getColour();

            // FrontEnd elements to represent the Player information
            String playerColourString = Colour.colourToString(playerColour);
            Color textColor = Colour.getFrontEndColor(playerColour);
            Boolean playerState = gameState.isPlayerAvailable(player);
            String playerStateString = "IN";
            if (!playerState) {
                playerStateString = "OUT";
            }

            Text playerText = new Text("Player " + playerColourString);
            playerText.setFont(Font.font("Verdana", 20));
            playerText.setFill(textColor);

            Text dirhamText = new Text("Remaining Dirhams: " + remainingDirhams);
            dirhamText.setScaleX(1.25);
            dirhamText.setScaleY(1.25);

            Text rugText = new Text("Remaining Rugs: " + numOfRemainingRugs);
            rugText.setScaleX(1.25);
            rugText.setScaleY(1.25);

            Text playerStateText = new Text("Player State: " + playerStateString);
            playerStateText.setScaleX(1.25);
            playerStateText.setScaleY(1.25);

            VBox vbox = new VBox(playerText, dirhamText, rugText, playerStateText);
            vbox.setLayoutX(xPixelValue);
            vbox.setLayoutY(yPixelValue);
            display.getChildren().addAll(vbox);
            yPixelValue += 75;
        }

    }

    /**
     * Draw the board at the current game state
     * @param state
     */
    public void drawBoard(String state) {
        GameState gameState = new GameState(state);
        Board board = gameState.getBoard();
        double rowPixelValue = 100;
        double colPixelValue = 100;
        double side = SQUARE_SIDE;
        for (int row = 0; row < Board.NUM_OF_ROWS; row++) {
            for (int col = 0; col < Board.NUM_OF_COLS; col++) {

                // Get the Tiles matrix from Board
                Tile[][] backEndTiles = board.getTiles();
                Tile backEndTile = backEndTiles[row][col];

                // Instantiate the FrontEndTile class
                FrontEndTile frontEndTile = new FrontEndTile(rowPixelValue, colPixelValue, side, backEndTile);

                // Check the condition when the Tile is EMPTY
                if (backEndTile.isEmpty()) {
                    frontEndTile.setFill(Color.ORANGE);

                }

                // Check the condition when the Tile is OCCUPIED by Assam
                else if (backEndTile.isAssam(board)) {
                    frontEndTile.setFill(Colour.getFrontEndColor(backEndTile.getTopRug().getColour()));
                    Direction assamDirection = board.getAssamDirection();
                    int assamAngle = assamDirection.getAngle();

                    // Assam FrontEnd representation
                    Image assamImage = new Image("Assam.png");
                    ImageView assamImageView = new ImageView(assamImage);
                    assamImageView.setLayoutX(rowPixelValue - 15);
                    assamImageView.setLayoutY(colPixelValue - 15);
                    assamImageView.setRotate(assamAngle);

                    Text assamIndicator = new Text("Assam");

                    images.getChildren().addAll(assamImageView, assamIndicator);
                }

                // Check the condition when the Tile is OCCUPIED by a Rug piece
                else {
                    Rug rug = backEndTile.getTopRug();
                    Colour rugColour = rug.getColour();
                    Color frontEndColor = Colour.getFrontEndColor(rugColour);
                    frontEndTile.setFill(frontEndColor);
                }

                frontEndTiles[row][col] = frontEndTile;
                rowPixelValue += side;
            }
            colPixelValue += side;
            rowPixelValue = 100;
        }

        // Initialize the ArrayList containing FrontEndTile
        ArrayList<FrontEndTile> frontEndTileArrayList = new ArrayList<>();
        for (int row = 0; row < Board.NUM_OF_ROWS; row++) {
            for (int col = 0; col < Board.NUM_OF_COLS; col++) {
                FrontEndTile frontEndTile = frontEndTiles[row][col];
                frontEndTileArrayList.add(frontEndTile);
            }
        }
        display.getChildren().addAll(frontEndTileArrayList);
    }

    /**
     * Draw mosaic tracks on the edge of the board
     */
    public void drawMosaicTrack() {
        double xTop = 137.5;
        double yTop = 62.5;
        for (int i = 0; i < 4; i++) {
            MosaicTrackCircle circle = new MosaicTrackCircle(xTop, yTop);
            mosaicTrack.getChildren().add(circle);
            xTop += 150;
        }

        double xBot = 62.5;
        double yBot = xBot + Board.NUM_OF_ROWS * SQUARE_SIDE;
        for (int i = 0; i < 4; i++) {
            MosaicTrackCircle circle = new MosaicTrackCircle(xBot, yBot);
            mosaicTrack.getChildren().add(circle);
            xBot += 150;
        }

        double xLeft = 62.5;
        double yLeft = 137.5;
        for (int i = 0; i < 3; i++) {
            MosaicTrackCircle circle = new MosaicTrackCircle(xLeft, yLeft);
            mosaicTrack.getChildren().add(circle);
            yLeft += 150;
        }

        double xRight = xLeft + Board.NUM_OF_COLS * SQUARE_SIDE;
        double yRight = 212.5;
        for (int i = 0; i < 3; i++) {
            MosaicTrackCircle circle = new MosaicTrackCircle(xRight, yRight);
            mosaicTrack.getChildren().add(circle);
            yRight += 150;
        }
    }


    /**
     * Draw a placement in the window, removing any previously drawn placements
     *
     * @param state an array of two strings, representing the current game state
     */
    void displayState(String state) {
        if (!GameState.isGameStringValid(state)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Invalid Game State");
            alert.show();
            return;
        }
        images.getChildren().clear();
        mosaicTrack.getChildren().clear();
        display.getChildren().clear();
        drawBoard(state);
        images.toFront();
        drawMosaicTrack();
        mosaicTrack.toBack();
        displayPlayerInfo(state);
    }

    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label boardLabel = new Label("Game State:");
        boardTextField = new TextField();
        boardTextField.setPrefWidth(800);
        Button button = new Button("Refresh");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                displayState(boardTextField.getText());
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(boardLabel,
                boardTextField, button);
        hb.setSpacing(10);
        hb.setLayoutX(50);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Marrakech Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(controls);
        root.getChildren().addAll(display, images, mosaicTrack);

        makeControls();

        scene.setFill(Color.rgb(200, 200, 200));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

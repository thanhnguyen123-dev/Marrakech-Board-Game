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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private final Group mosaicTrack =  new Group();
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
            this.setStrokeWidth(0.5);
        }

        /**
         * Fill the colour for FrontEndTile based on colour of BackEnd Tile
         * @param backEndColour
         */
        public void fillRugColour(Colour backEndColour) {
            switch (backEndColour) {
                case YELLOW -> this.setFill(Color.YELLOW);
                case RED -> this.setFill(Color.RED);
                case CYAN -> this.setFill(Color.CYAN);
                case PURPLE -> this.setFill(Color.PURPLE);
                default -> this.setFill(Color.ORANGE);
            }
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
            Text playerText = new Text("Player " + playerColourString);
            playerText.setFont(Font.font("Inter", 20));
            Text dirhamText = new Text("Remaining Dirhams: " + remainingDirhams);
            Text rugText = new Text("Remaining Rugs: " + numOfRemainingRugs);
            VBox vbox = new VBox(playerText, dirhamText, rugText);
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
        for (int row = 0; row < Board.NUM_OF_ROWS; row ++) {
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
                    frontEndTile.setFill(Color.ORANGE);
                    Direction assamDirection = board.getAssamDirection();
                    int assamAngleValue = assamDirection.getAngleValue();

                    // Assam FrontEnd representation
                    Image assamImage = new Image("Assam.png");
                    ImageView assamImageView = new ImageView(assamImage);
                    assamImageView.setLayoutX(rowPixelValue - 15);
                    assamImageView.setLayoutY(colPixelValue - 15);
                    assamImageView.setRotate(assamAngleValue);
                    images.getChildren().addAll(assamImageView);
                }

                // Check the condition when the Tile is OCCUPIED by a Rug piece
                else {
                    Rug rug = backEndTile.getTopRug();
                    Colour rugColour = rug.getColour();
                    frontEndTile.fillRugColour(rugColour);
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
            Circle circle = new Circle(CIRCLE_RADIUS);
            circle.setCenterX(xTop);
            circle.setCenterY(yTop);
            circle.setFill(Color.LIGHTBLUE);
            mosaicTrack.getChildren().add(circle);
            xTop += 150;
        }

        double xBot = 62.5;
        double yBot = xBot + Board.NUM_OF_ROWS * SQUARE_SIDE;
        for (int i = 0; i < 4; i++) {
            Circle circle = new Circle(CIRCLE_RADIUS);
            circle.setCenterX(xBot);
            circle.setCenterY(yBot);
            circle.setFill(Color.LIGHTBLUE);
            mosaicTrack.getChildren().add(circle);
            xBot += 150;
        }

        double xLeft = 62.5;
        double yLeft = 137.5;
        for (int i = 0; i < 3; i++) {
            Circle circle = new Circle(CIRCLE_RADIUS);
            circle.setCenterX(xLeft);
            circle.setCenterY(yLeft);
            circle.setFill(Color.LIGHTBLUE);
            mosaicTrack.getChildren().add(circle);
            yLeft += 150;
        }

        double xRight = xLeft + Board.NUM_OF_COLS * SQUARE_SIDE;
        double yRight = 212.5;
        for (int i = 0; i < 3; i++) {
            Circle circle = new Circle(CIRCLE_RADIUS);
            circle.setCenterX(xRight);
            circle.setCenterY(yRight);
            circle.setFill(Color.LIGHTBLUE);
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

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

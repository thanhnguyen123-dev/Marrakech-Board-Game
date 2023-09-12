package comp1110.ass2.gui;

import comp1110.ass2.GameState;
import comp1110.ass2.board.Board;
import comp1110.ass2.board.Tile;
import comp1110.ass2.player.Colour;
import comp1110.ass2.player.Rug;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Viewer extends Application {

    private static final int VIEWER_WIDTH = 1200;
    private static final int VIEWER_HEIGHT = 700;

    private final Group root = new Group();
    private final Group controls = new Group();
    private final Group display = new Group();
    private TextField boardTextField;

    // FRONT END fields
    private FrontEndTile[][] frontEndTiles = new FrontEndTile[Board.NUM_OF_ROWS][Board.NUM_OF_COLS];

    /**
     * Constructor: creates an instance of FrontEndTile
     * - Front-End representation of a Tile
     */
    public class FrontEndTile extends Polygon {
        Tile backEndTile;
        double side;

        // frontEndTile stores the data of backEndTile
        public FrontEndTile(double x, double y, double side, Tile backEndTile) {
            double halfSide = side / 2;
            this.backEndTile = backEndTile;
            this.side = side;
            this.getPoints().setAll(
                    x - halfSide, y + halfSide, x + halfSide, y + halfSide,
                    x + halfSide, y - halfSide, x - halfSide, y - halfSide
            );
            this.setStroke(Color.BLACK);
            this.setStrokeWidth(5);

        }

        /**
         * fill the colour for front-end elements
         * @param backEndColour
         */
        public void fillFrontEndColour(Colour backEndColour) {
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
     * Draw the board at the current game state
     * @param state
     */
    public void drawBoard(String state) {
        GameState gameState = new GameState(state);
        Board board = gameState.getBoard();
        double x = 100;
        double y = 100;
        double side = 30;
        for(int row = 0; row < Board.NUM_OF_ROWS; row ++) {
            for (int col = 0; col < Board.NUM_OF_COLS; col++) {

                Tile[][] backEndTiles = board.getTiles();
                Tile backEndTile = backEndTiles[row][col];
                FrontEndTile frontEndTile = new FrontEndTile(x, y, side, backEndTile);

                if (backEndTile.isEmpty()) {
                    frontEndTile.setFill(Color.ORANGE);
                }
                else {
                    // Temporary: Assam as white
                    if (backEndTile.isAssam(board)) {
                        frontEndTile.setFill(Color.WHITE);
                    }
                    else {
                        Rug rug = backEndTile.getTopRug();
                        Colour rugColour = rug.getColour();
                        frontEndTile.fillFrontEndColour(rugColour);

                    }
                }
                frontEndTiles[row][col] = frontEndTile;
                x += side;
            }
            y += side;
            x = 100;
        }

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
     * Draw a placement in the window, removing any previously drawn placements
     *
     * @param state an array of two strings, representing the current game state
     */
    void displayState(String state) {
        // FIXME Task 5: implement the simple state viewer
        drawBoard(state);

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
        root.getChildren().add(display);



        makeControls();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

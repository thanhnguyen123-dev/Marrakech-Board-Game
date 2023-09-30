package comp1110.ass2.gui;

import comp1110.ass2.GameState;
import comp1110.ass2.board.Tile;
import comp1110.ass2.player.Colour;
import comp1110.ass2.player.Player;
import comp1110.ass2.player.Rug;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Game extends Application {
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 700;

    private static final int NUM_OF_ROWS = 7;
    private static final int NUM_OF_COLS = 7;
    private static final double TILE_SIDE = 80;

    private final Group root = new Group();

    private final Group allTiles = new Group();
    private final GameTile[][] gameTiles = new GameTile[NUM_OF_ROWS][NUM_OF_COLS];

    private final Group placedRugs = new Group();
    private final ArrayList<GameRug> gameRugs = new ArrayList<GameRug>();

    private final Group invisibleRugs = new Group();
    private final ArrayList<InvisibleRug> vInvisibleRugs = new ArrayList<InvisibleRug>();
    private final ArrayList<InvisibleRug> hInvisibleRugs = new ArrayList<InvisibleRug>();

    private InvisibleRug highlighted;
    private DraggableGameRug draggableGameRug;

    private Player player1 = new Player(Colour.YELLOW);
    private Player player2 = new Player(Colour.RED);
    private Player player3 = new Player(Colour.CYAN);
    private Player player4 = new Player(Colour.PURPLE);
    private Player[] players = new Player[]{player1, player2, player3, player4};

    private final GameState gameState = new GameState(this.players);
    private int rugID = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.root.setLayoutX(120);
        this.root.setLayoutY(60);

        this.root.getChildren().add(this.allTiles);
        this.root.getChildren().add(this.placedRugs);
        this.root.getChildren().add(this.invisibleRugs);
        initAllTiles();
        initInvisibleRugs();

        this.draggableGameRug = new DraggableGameRug(700, 300, this, Colour.RED);
        this.root.getChildren().add(this.draggableGameRug);

        RugRotationButton rugRotationButton = new RugRotationButton();
        this.root.getChildren().add(rugRotationButton);
        rugRotationButton.setLayoutX(800);
        rugRotationButton.setLayoutY(20);

        RugPlacementButton rugPlacementButton = new RugPlacementButton();
        this.root.getChildren().add(rugPlacementButton);
        rugPlacementButton.setLayoutX(800);
        rugPlacementButton.setLayoutY(60);

        Scene scene = new Scene(this.root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initAllTiles() {
        for (int i = 0; i < NUM_OF_ROWS; i++) {
            for (int j = 0; j < NUM_OF_COLS; j++) {
                this.gameTiles[i][j] = new GameTile(i * TILE_SIDE, j * TILE_SIDE, i, j);
                this.allTiles.getChildren().add(this.gameTiles[i][j]);
            }
        }
    }

    private void initInvisibleRugs() {
        for (int i = 0; i < NUM_OF_ROWS - 1; i++) {
            for (int j = 0; j < NUM_OF_COLS; j++) {
                GameTile[] gameTiles = new GameTile[]{
                        this.gameTiles[i][j], this.gameTiles[i + 1][j]
                };
                InvisibleRug rug = new InvisibleRug(j * TILE_SIDE + TILE_SIDE / 2, i * TILE_SIDE + TILE_SIDE, gameTiles, Orientation.VERTICAL);
                this.vInvisibleRugs.add(rug);
                this.invisibleRugs.getChildren().add(rug);
            }
        }

        for (int i = 0; i < NUM_OF_ROWS; i++) {
            for (int j = 0; j < NUM_OF_COLS - 1; j++) {
                GameTile[] gameTiles = new GameTile[]{
                        this.gameTiles[i][j], this.gameTiles[i][j + 1]
                };
                InvisibleRug rug = new InvisibleRug(j * TILE_SIDE + TILE_SIDE, i * TILE_SIDE + TILE_SIDE / 2, gameTiles, Orientation.HORIZONTAL);
                this.hInvisibleRugs.add(rug);
                this.invisibleRugs.getChildren().add(rug);
            }
        }
    }

    private InvisibleRug findNearestInvisibleRug(double x, double y, Orientation orientation) {
        InvisibleRug nearestRug = null;
        double min = Double.POSITIVE_INFINITY;
        for (InvisibleRug invisibleRug : (orientation == Orientation.VERTICAL ? this.vInvisibleRugs : this.hInvisibleRugs)) {
            double currentDistance = invisibleRug.distance(x, y);
            if (min > currentDistance) {
                nearestRug = invisibleRug;
                min = currentDistance;
            }
        }
        return nearestRug;
    }

    private void highlightNearestInvisibleRug(double x, double y, Orientation orientation) {
        if (this.highlighted != null) {
            this.highlighted.setOpacity(0);
        }
        this.highlighted = findNearestInvisibleRug(x, y, orientation);
        this.highlighted.setOpacity(0.5);
    }

    private static class InvisibleRug extends Rectangle {
        private final GameTile[] gameTiles = new GameTile[2];
        private final Orientation orientation;

        public InvisibleRug(double x, double y, GameTile[] gameTiles, Orientation orientation) {
            super(0, 0, TILE_SIDE, 2 * TILE_SIDE);
            this.gameTiles[0] = gameTiles[0];
            this.gameTiles[1] = gameTiles[1];
            this.orientation = orientation;

            this.setLayoutX(x - TILE_SIDE / 2);
            this.setLayoutY(y - TILE_SIDE);
            if (this.orientation == Orientation.HORIZONTAL) {
                this.setRotate(90);
            }
            this.setFill(Color.WHITE);
            this.setOpacity(0);
        }

        public double distance(double x, double y) {
            return Math.sqrt((this.getLayoutX() - x) * (this.getLayoutX() - x) + (this.getLayoutY() - y) * (this.getLayoutY() - y));
        }
    }

    private static class GameRug extends Rectangle {
        public GameRug(double x, double y, Orientation orientation, Colour colour) {
            super(0, 0, TILE_SIDE, 2 * TILE_SIDE);

            this.setLayoutX(x);
            this.setLayoutY(y);
            if (orientation == Orientation.HORIZONTAL) {
                this.setRotate(90);
            }
            Color color = Colour.getFrontEndColor(colour);
            if (color != null) {
                this.setFill(Colour.getFrontEndColor(colour));
                this.setStroke(color.darker());
                this.setStrokeWidth(4);
            }
        }
    }


    private class DraggableGameRug extends GameRug {
        private final Game game;
        private Orientation orientation = Orientation.VERTICAL;
        private Colour colour;
        private double mouseX, mouseY;

        public DraggableGameRug(double x, double y, Game game, Colour colour) {
            super(x, y, Orientation.VERTICAL, colour);
            this.game = game;
            this.colour = colour;

            this.setOnMousePressed(event -> {
                this.toFront();
                this.mouseX = event.getSceneX();
                this.mouseY = event.getSceneY();
            });

            this.setOnMouseDragged(event -> {
                double movementX = event.getSceneX() - this.mouseX;
                double movementY = event.getSceneY() - this.mouseY;
                this.setLayoutX(this.getLayoutX() + movementX);
                this.setLayoutY(this.getLayoutY() + movementY);
                this.mouseX = event.getSceneX();
                this.mouseY = event.getSceneY();
                this.game.highlightNearestInvisibleRug(this.getLayoutX(), this.getLayoutY(), this.orientation);
            });

            this.setOnMouseReleased(event -> {
                InvisibleRug nearestInvisibleRug = this.game.findNearestInvisibleRug(this.getLayoutX(), this.getLayoutY(), this.orientation);
                this.setRotate(nearestInvisibleRug.getRotate());
                this.setLayoutX(nearestInvisibleRug.getLayoutX());
                this.setLayoutY(nearestInvisibleRug.getLayoutY());
            });
        }

        public void toggleOrientation() {
            this.setRotate(this.getRotate() + 90);
            this.orientation = this.orientation == Orientation.VERTICAL ? Orientation.HORIZONTAL : Orientation.VERTICAL;
            if (highlighted != null) {
                highlighted.setOpacity(0);
            }
        }
    }

    private enum Orientation {
        VERTICAL,
        HORIZONTAL;
    }

    private static class GameTile extends Rectangle {
        private final int row;
        private final int col;

        public GameTile(double x, double y, int row, int col) {
            super(0, 0, TILE_SIDE, TILE_SIDE);
            this.row = row;
            this.col = col;

            this.setLayoutX(x);
            this.setLayoutY(y);
            this.setFill(Color.LIGHTGRAY);
            this.setStroke(Color.LIGHTGRAY.darker());
            this.setStrokeWidth(2);
        }
    }

    private class RugRotationButton extends Button {
        public RugRotationButton() {
            super("Rotate Rug");

            this.setOnMouseClicked(event -> {
                draggableGameRug.toggleOrientation();
                highlighted = null;
            });
        }
    }

    private class RugPlacementButton extends Button {
        public RugPlacementButton() {
            super("Place Rug");

            this.setOnMouseClicked(event -> {
                if (highlighted != null) {
                    rugID++;
                    Rug rug = new Rug(draggableGameRug.colour, rugID, getTilesFromHighlighted());
                    gameState.makePlacement(rug);
                    GameRug gameRug = new GameRug(highlighted.getLayoutX(), highlighted.getLayoutY(), draggableGameRug.orientation, draggableGameRug.colour);
                    placedRugs.getChildren().add(gameRug);
                    gameRugs.add(gameRug);
                }
            });
        }
    }

    private Tile[] getTilesFromHighlighted() {
        if (highlighted != null) {
            return new Tile[]{getTileFromGameTile(highlighted.gameTiles[0]), getTileFromGameTile(highlighted.gameTiles[1])};
        }
        return null;
    }

    private Tile getTileFromGameTile(GameTile gameTile) {
        return gameState.getBoard().getTiles()[gameTile.row][gameTile.col];
    }
}

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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Game extends Application {
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 700;

    private static final double MARGIN = 30;
    private static final double BOARD_AREA_SIDE = WINDOW_HEIGHT - 2 * MARGIN;
    private static final double PLAYER_AREA_WIDTH = WINDOW_WIDTH - BOARD_AREA_SIDE - 3 * MARGIN;
    private static final double PLAYER_AREA_HEIGHT = BOARD_AREA_SIDE;
    private static final double STATS_AREA_WIDTH = PLAYER_AREA_WIDTH;
    private static final double STATS_AREA_HEIGHT = 400;
    private static final double CONTROL_AREA_WIDTH = PLAYER_AREA_WIDTH;
    private static final double CONTROL_AREA_HEIGHT = PLAYER_AREA_HEIGHT - STATS_AREA_HEIGHT - MARGIN;
    private static final Color GAME_PANE_BORDER_COLOR = Color.SANDYBROWN.darker();
    private static final BorderStrokeStyle GAME_PANE_BORDER_STROKE_STYLE = BorderStrokeStyle.SOLID;
    private static final CornerRadii GAME_PANE_BORDER_RADII = new CornerRadii(24);
    private static final BorderWidths GAME_PANE_BORDER_WIDTH = new BorderWidths(4);

    private static final int NUM_OF_ROWS = 7;
    private static final int NUM_OF_COLS = 7;
    private static final double TILE_SIDE = 64;
    private static final double TILE_RELOCATION_X = (BOARD_AREA_SIDE - NUM_OF_COLS * TILE_SIDE) / 2;
    private static final double TILE_RELOCATION_Y = (BOARD_AREA_SIDE - NUM_OF_ROWS * TILE_SIDE) / 2;
    private static final Color TILE_COLOR = Color.SANDYBROWN;
    private static final int TILE_BORDER_WIDTH = 4;
    private static final int RUG_BORDER_WIDTH = 4;

    private final Group root = new Group();
    private final Pane allTiles = new Pane();
    private final GameTile[][] gameTiles = new GameTile[NUM_OF_ROWS][NUM_OF_COLS];
    private final Pane placedRugs = new Pane();
    private final ArrayList<GameRug> gameRugs = new ArrayList<GameRug>();
    private final Pane invisibleRugs = new Pane();
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
    private Phase currentPhase = Phase.ROTATION;
    private int dieResult;
    private int rugID = 0;

    private Text phaseText = new Text();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final Pane gameArea = new Pane();
        this.root.getChildren().add(gameArea);
        Border gamePaneBorder = new Border(new BorderStroke(GAME_PANE_BORDER_COLOR, GAME_PANE_BORDER_STROKE_STYLE, GAME_PANE_BORDER_RADII, GAME_PANE_BORDER_WIDTH));

        final Pane boardArea = new GamePane(BOARD_AREA_SIDE, BOARD_AREA_SIDE);
        boardArea.setBorder(gamePaneBorder);
        boardArea.relocate(MARGIN, MARGIN);
        gameArea.getChildren().add(boardArea);

        initAllTiles();
        initInvisibleRugs();
        this.allTiles.relocate(TILE_RELOCATION_X, TILE_RELOCATION_Y);
        boardArea.getChildren().add(this.allTiles);
        this.placedRugs.relocate(TILE_RELOCATION_X, TILE_RELOCATION_Y);
        boardArea.getChildren().add(this.placedRugs);
        this.invisibleRugs.relocate(TILE_RELOCATION_X, TILE_RELOCATION_Y);
        boardArea.getChildren().add(this.invisibleRugs);

        final GamePane playerArea = new GamePane(PLAYER_AREA_WIDTH, PLAYER_AREA_HEIGHT);
        playerArea.relocate(WINDOW_HEIGHT, MARGIN);
        gameArea.getChildren().add(playerArea);

        final GamePane statsArea = new GamePane(STATS_AREA_WIDTH, STATS_AREA_HEIGHT);
        statsArea.setBorder(gamePaneBorder);
        playerArea.getChildren().add(statsArea);

        phaseText.setText(getCurrentPhaseText());
        statsArea.getChildren().add(phaseText);

        final GamePane controlArea = new GamePane(CONTROL_AREA_WIDTH, CONTROL_AREA_HEIGHT);
        controlArea.setBorder(gamePaneBorder);
        controlArea.relocate(0, STATS_AREA_HEIGHT + MARGIN);
        playerArea.getChildren().add(controlArea);

        Button rotateAssamBtn = makeRotateAssamBtn();
        rotateAssamBtn.relocate(240, 30);
        controlArea.getChildren().add(rotateAssamBtn);

        Button rollDieBtn = makeRollDieBtn();
        rollDieBtn.relocate(240, 60);
        controlArea.getChildren().add(rollDieBtn);

        Button moveAssamBtn = makeMoveAssamBtn();
        moveAssamBtn.relocate(240, 90);
        controlArea.getChildren().add(moveAssamBtn);

        Button rotateRugBtn = makeRotateRugBtn();
        rotateRugBtn.relocate(240, 120);
        controlArea.getChildren().add(rotateRugBtn);

        Button placeRugBtn = makePlaceRugBtn();
        placeRugBtn.relocate(240, 150);
        controlArea.getChildren().add(placeRugBtn);

        Button nextPhaseBtn = makeNextPhaseBtn();
        nextPhaseBtn.relocate(240, 180);
        controlArea.getChildren().add(nextPhaseBtn);

        this.draggableGameRug = new DraggableGameRug(750, 500, this, Colour.RED);
        this.root.getChildren().add(this.draggableGameRug);

        Scene scene = makeScene();

        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Scene makeScene() {
        Scene scene = new Scene(this.root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.Q || event.getCode() == KeyCode.E) {
                if (this.currentPhase == Phase.PLACEMENT) {
                    toggleRugOrientation();
                }
            }
            if (event.getCode() == KeyCode.ENTER) {
                if (this.currentPhase == Phase.PLACEMENT) {
                    placeRug();
                }
            }
        });
        return scene;
    }

    private Button makeRotateAssamBtn() {
        Button rotateAssamBtn = new Button("Rotate Assam");
        rotateAssamBtn.setOnMouseClicked(event -> {
            //FIXME
            return;
        });
        return rotateAssamBtn;
    }

    private Button makeRollDieBtn() {
        Button rollDieBtn = new Button("Roll Die");
        rollDieBtn.setOnMouseClicked(event -> {
            //FIXME
            return;
        });
        return rollDieBtn;
    }

    private Button makeMoveAssamBtn() {
        Button moveAssamBtn = new Button("Move Assam");
        moveAssamBtn.setOnMouseClicked(event -> {
            //FIXME
            return;
        });
        return moveAssamBtn;
    }

    private Button makeRotateRugBtn() {
        Button rotateRugBtn = new Button("Rotate Rug");
        rotateRugBtn.setOnMouseClicked(event -> {
            if (this.currentPhase == Phase.PLACEMENT) {
                toggleRugOrientation();
            }
        });
        return rotateRugBtn;
    }

    private void toggleRugOrientation() {
        if (this.draggableGameRug != null) {
            this.draggableGameRug.setRotate(this.draggableGameRug.getRotate() + 90);
            this.draggableGameRug.orientation = this.draggableGameRug.orientation == Orientation.VERTICAL ? Orientation.HORIZONTAL : Orientation.VERTICAL;
            if (this.highlighted != null) {
                this.highlighted.setOpacity(0);
                this.highlighted = null;
            }
        }
    }

    private Button makePlaceRugBtn() {
        Button placeRugBtn = new Button("Place Rug");
        placeRugBtn.setOnMouseClicked(event -> {
            if (this.currentPhase == Phase.PLACEMENT) {
                placeRug();
            }
        });
        return placeRugBtn;
    }

    private void placeRug() {
        //FIXME
        if (this.highlighted != null) {
            this.rugID++;
            Rug rug = new Rug(this.draggableGameRug.colour, this.rugID, getTilesFromHighlighted());
            this.gameState.makePlacement(rug);
            GameRug gameRug = new GameRug(this.highlighted.getLayoutX(), this.highlighted.getLayoutY(), this.draggableGameRug.orientation, this.draggableGameRug.colour);
            this.placedRugs.getChildren().add(gameRug);
            this.gameRugs.add(gameRug);
        }
    }

    private Button makeNextPhaseBtn() {
        Button nextPhaseBtn = new Button("Next Phase (testing only)");
        nextPhaseBtn.setOnMouseClicked(event -> {
            nextPhase();
            phaseText.setText(getCurrentPhaseText());
        });
        return nextPhaseBtn;
    }

    private class GamePane extends Pane {
        public GamePane(double width, double height) {
            super();
            this.setMinWidth(width);
            this.setMaxWidth(width);
            this.setMinHeight(height);
            this.setMaxHeight(height);
        }
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
        this.highlighted.setOpacity(0.33);
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
            this.setStroke(Color.WHITE.darker());
            this.setStrokeWidth(RUG_BORDER_WIDTH);
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
                this.setStrokeWidth(RUG_BORDER_WIDTH);
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
                this.game.highlightNearestInvisibleRug(this.getLayoutX() - TILE_RELOCATION_X - MARGIN, this.getLayoutY() - TILE_RELOCATION_Y - MARGIN, this.orientation);
            });

            this.setOnMouseReleased(event -> {
                InvisibleRug nearestInvisibleRug = this.game.findNearestInvisibleRug(this.getLayoutX() - TILE_RELOCATION_X - MARGIN, this.getLayoutY() - TILE_RELOCATION_Y - MARGIN, this.orientation);
                this.setLayoutX(nearestInvisibleRug.getLayoutX() + TILE_RELOCATION_X + MARGIN);
                this.setLayoutY(nearestInvisibleRug.getLayoutY() + TILE_RELOCATION_Y + MARGIN);
            });
        }
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
            this.setFill(TILE_COLOR);
            this.setStroke(TILE_COLOR.darker());
            this.setStrokeWidth(TILE_BORDER_WIDTH);
        }
    }

    private enum Phase {
        ROTATION,
        MOVEMENT,
        PLACEMENT;
    }

    private void nextPhase() {
        switch (this.currentPhase) {
            case ROTATION -> this.currentPhase = Phase.MOVEMENT;
            case MOVEMENT -> this.currentPhase = Phase.PLACEMENT;
            case PLACEMENT -> this.currentPhase = Phase.ROTATION;
        }
    }

    private String getCurrentPhaseText() {
        switch (this.currentPhase) {
            case ROTATION -> {
                return "Current Phase: Rotation Phase";
            }
            case MOVEMENT -> {
                return "Current Phase: Movement Phase";
            }
            case PLACEMENT -> {
                return "Current Phase: Placement Phase";
            }
        }
        return null;
    }

    private enum Orientation {
        VERTICAL,
        HORIZONTAL;
    }

    private Tile[] getTilesFromHighlighted() {
        if (this.highlighted != null) {
            return new Tile[]{getTileFromGameTile(this.highlighted.gameTiles[0]), getTileFromGameTile(this.highlighted.gameTiles[1])};
        }
        return null;
    }

    private Tile getTileFromGameTile(GameTile gameTile) {
        return this.gameState.getBoard().getTiles()[gameTile.row][gameTile.col];
    }
}
package comp1110.ass2.gui;

import comp1110.ass2.GameState;
import comp1110.ass2.board.Tile;
import comp1110.ass2.player.Colour;
import comp1110.ass2.player.Player;
import comp1110.ass2.player.Rug;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

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
    private static final int BUTTON_WIDTH = 120;
    private static final int BUTTON_HEIGHT = 60;
    private static final int COLOUR_BUTTON_RADIUS = 60;
    private static final BorderStrokeStyle COLOUR_BUTTON_BORDER_STROKE_STYLE = GAME_PANE_BORDER_STROKE_STYLE;
    private static final CornerRadii COLOUR_BUTTON_BORDER_RADII = GAME_PANE_BORDER_RADII;
    private static final BorderWidths COLOUR_BUTTON_BORDER_WIDTH = new BorderWidths(8);

    private final Pane allTiles = new Pane();
    private final GameTile[][] gameTiles = new GameTile[NUM_OF_ROWS][NUM_OF_COLS];
    private final Pane placedRugs = new Pane();
    private final ArrayList<GameRug> gameRugs = new ArrayList<GameRug>();
    private final Pane invisibleRugs = new Pane();
    private final ArrayList<InvisibleRug> vInvisibleRugs = new ArrayList<InvisibleRug>();
    private final ArrayList<InvisibleRug> hInvisibleRugs = new ArrayList<InvisibleRug>();

    private InvisibleRug highlighted;
    private DraggableGameRug draggableGameRug;

    private int numOfPlayers;
    private Player[] players;
    private GameState gameState;
    private Phase currentPhase = Phase.ROTATION;
    private int dieResult;
    private int rugID = 0;

    private Text phaseText = new Text();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //title screen
        Pane titlePane = new Pane();
        Scene titleScene = new Scene(titlePane, WINDOW_WIDTH, WINDOW_HEIGHT);
        Text title = new Text("Marrakech");
        title.relocate(WINDOW_WIDTH / 2 - 135, 240);
        title.setFont(new Font("", 60));
        GameButton btnStart = new GameButton("Start", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnStart.relocate(WINDOW_WIDTH / 2 - BUTTON_WIDTH / 2, 360);
        titlePane.getChildren().addAll(title, btnStart);

        //choose number of players
        Pane numberPane = new Pane();
        Scene numberScene = new Scene(numberPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        //choice box to choose the number of players
        ChoiceBox<Integer> choiceBox = new ChoiceBox<Integer>();
        choiceBox.setMinWidth(BUTTON_WIDTH);
        choiceBox.setMaxWidth(BUTTON_WIDTH);
        choiceBox.getItems().addAll(2, 3, 4);
        choiceBox.setValue(2);
        choiceBox.relocate(WINDOW_WIDTH / 2 - BUTTON_WIDTH / 2, 320);

        GameButton btnNumberBack = new GameButton("Back", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnNumberBack.relocate(BUTTON_HEIGHT / 2, BUTTON_HEIGHT / 2);
        GameButton btnNumberConfirm = new GameButton("Confirm", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnNumberConfirm.relocate(WINDOW_WIDTH / 2 - BUTTON_WIDTH / 2, 360);
        numberPane.getChildren().addAll(choiceBox, btnNumberBack, btnNumberConfirm);

        //players choose their colours
        Pane colourPane = new Pane();
        Scene colourScene = new Scene(colourPane, WINDOW_WIDTH, WINDOW_HEIGHT);

        ColourButton btnCyan = new ColourButton(Colour.CYAN);
        btnCyan.relocate(270, 180);
        ColourButton btnYellow = new ColourButton(Colour.YELLOW);
        btnYellow.relocate(270 + 3 * COLOUR_BUTTON_RADIUS, 180);
        ColourButton btnRed = new ColourButton(Colour.RED);
        btnRed.relocate(270 + 6 * COLOUR_BUTTON_RADIUS, 180);
        ColourButton btnPurple = new ColourButton(Colour.PURPLE);
        btnPurple.relocate(270 + 9 * COLOUR_BUTTON_RADIUS, 180);

        GameButton btnColourBack = new GameButton("Back", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnColourBack.relocate(BUTTON_HEIGHT / 2, BUTTON_HEIGHT / 2);
        GameButton btnColourReset = new GameButton("Reset", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnColourReset.relocate(WINDOW_WIDTH / 2 - BUTTON_WIDTH * 1.5, 420);
        GameButton btnColourConfirm = new GameButton("Confirm", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnColourConfirm.relocate(WINDOW_WIDTH / 2 + BUTTON_WIDTH * 0.5, 420);
        btnColourConfirm.setDisable(true);
        colourPane.getChildren().addAll(btnCyan, btnYellow, btnRed, btnPurple, btnColourBack, btnColourReset, btnColourConfirm);

        ArrayList<Player> tmp = new ArrayList<Player>();
        ArrayList<ColourButton> colourButtons = new ArrayList<ColourButton>(List.of(btnCyan, btnYellow, btnRed, btnPurple));
        for (ColourButton button : colourButtons) {
            button.setOnMouseClicked(event -> {
                tmp.add(new Player(button.colour));
                button.setBorder(new Border(new BorderStroke(Colour.getFrontEndColor(button.colour).darker(), COLOUR_BUTTON_BORDER_STROKE_STYLE, COLOUR_BUTTON_BORDER_RADII, COLOUR_BUTTON_BORDER_WIDTH)));
                button.setDisable(true);
                if (tmp.size() == this.numOfPlayers) {
                    colourButtons.forEach(b -> b.setDisable(true));
                    btnColourConfirm.setDisable(false);
                }
            });
        }

        Scene mainScene = makeMainScene(primaryStage);

        btnStart.setOnMouseClicked(event -> {
            primaryStage.setScene(numberScene);
        });

        btnNumberBack.setOnMouseClicked(event -> {
            this.numOfPlayers = 0;
            primaryStage.setScene(titleScene);
        });

        btnNumberConfirm.setOnMouseClicked(event -> {
            this.numOfPlayers = choiceBox.getValue();
            primaryStage.setScene(colourScene);
        });

        btnColourBack.setOnMouseClicked(event -> {
            tmp.clear();
            primaryStage.setScene(numberScene);
        });

        btnColourReset.setOnMouseClicked(event -> {
            tmp.clear();
            btnColourConfirm.setDisable(true);
            colourButtons.forEach(b -> {
                b.setDisable(false);
                b.setBorder(null);
            });
        });

        btnColourConfirm.setOnMouseClicked(event -> {
            this.players = tmp.toArray(new Player[0]);
            primaryStage.setScene(mainScene);
        });

        primaryStage.setResizable(false);
        primaryStage.setTitle("Game");
        primaryStage.setScene(titleScene);
        primaryStage.show();
    }

    private Scene makeMainScene(Stage primaryStage) {
        GamePane gamePane = new GamePane(WINDOW_WIDTH, WINDOW_HEIGHT);
        Border gamePaneBorder = new Border(new BorderStroke(GAME_PANE_BORDER_COLOR, GAME_PANE_BORDER_STROKE_STYLE, GAME_PANE_BORDER_RADII, GAME_PANE_BORDER_WIDTH));

        final Pane boardArea = new GamePane(BOARD_AREA_SIDE, BOARD_AREA_SIDE);
        boardArea.setBorder(gamePaneBorder);
        boardArea.relocate(MARGIN, MARGIN);
        gamePane.getChildren().add(boardArea);

        initAllTiles();
        initInvisibleRugs();
        this.allTiles.relocate(TILE_RELOCATION_X, TILE_RELOCATION_Y);
        this.placedRugs.relocate(TILE_RELOCATION_X, TILE_RELOCATION_Y);
        this.invisibleRugs.relocate(TILE_RELOCATION_X, TILE_RELOCATION_Y);
        boardArea.getChildren().addAll(this.allTiles, this.placedRugs, this.invisibleRugs);

        final GamePane playerArea = new GamePane(PLAYER_AREA_WIDTH, PLAYER_AREA_HEIGHT);
        playerArea.relocate(WINDOW_HEIGHT, MARGIN);
        gamePane.getChildren().add(playerArea);

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


        Scene scene = new Scene(gamePane, WINDOW_WIDTH, WINDOW_HEIGHT);
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
            this.setMinSize(width, height);
            this.setMaxSize(width, height);
        }
    }

    private class GameButton extends Button {
        public GameButton(String string, double width, double height) {
            super(string);
            this.setMinSize(width, height);
            this.setMaxSize(width, height);
        }
    }

    private class ColourButton extends Button {
        private final Colour colour;

        public ColourButton(Colour colour) {
            super();
            this.colour = colour;
            this.setShape(new Circle(COLOUR_BUTTON_RADIUS));
            this.setMinSize(2 * COLOUR_BUTTON_RADIUS, 2 * COLOUR_BUTTON_RADIUS);
            this.setMaxSize(2 * COLOUR_BUTTON_RADIUS, 2 * COLOUR_BUTTON_RADIUS);
            this.setBackground(new Background(new BackgroundFill(Colour.getFrontEndColor(colour), CornerRadii.EMPTY, Insets.EMPTY)));
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
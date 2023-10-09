package comp1110.ass2.gui;

import comp1110.ass2.GameState;
import comp1110.ass2.board.Direction;
import comp1110.ass2.board.Tile;
import comp1110.ass2.player.Colour;
import comp1110.ass2.player.Die;
import comp1110.ass2.player.Player;
import comp1110.ass2.player.Rug;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;
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
    private static final double HIGHLIGHTED_OPACITY = 1 - (Math.sqrt(5) - 1) / 2;
    //https://fonts.google.com/icons?selected=Material%20Symbols%20Rounded%3Anavigation%3AFILL%401%3Bwght%40400%3BGRAD%400%3Bopsz%4024
    private static final String ASSAM_SVG = "M480-240 222-130q-13 5-24.5 2.5T178-138q-8-8-10.5-20t2.5-25l273-615q5-12 15.5-18t21.5-6q11 0 21.5 6t15.5 18l273 615q5 13 2.5 25T782-138q-8 8-19.5 10.5T738-130L480-240Z";

    private final Pane allTiles = new Pane();
    private final Pane placedRugs = new Pane();
    private final Pane invisibleRugs = new Pane();
    private final GameTile[][] gameTiles = new GameTile[NUM_OF_ROWS][NUM_OF_COLS];
    private final ArrayList<InvisibleRug> vInvisibleRugs = new ArrayList<>();
    private final ArrayList<InvisibleRug> hInvisibleRugs = new ArrayList<>();

    private int numOfPlayers;
    private Phase currentPhase;
    private Text phaseText;
    private Region assam;
    private int dieResult;
    private InvisibleRug highlighted;
    private DraggableRug draggableRug;
    private int rugID;

    private Player[] players;
    private GameState gameState;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //title screen
        Pane titlePane = new Pane();
        Scene titleScene = new Scene(titlePane, WINDOW_WIDTH, WINDOW_HEIGHT);
        Text title = new Text("Marrakech");
        title.relocate(WINDOW_WIDTH / 2.0 - 135, 240);
        title.setFont(new Font("", 60));
        GameButton btnStart = new GameButton("Start", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnStart.relocate(WINDOW_WIDTH / 2.0 - BUTTON_WIDTH / 2.0, 360);

        titlePane.getChildren().addAll(title, btnStart);

        //choose number of players
        Pane numberPane = new Pane();
        Scene numberScene = new Scene(numberPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        //choice box to choose the number of players
        ChoiceBox<Integer> choiceBox = new ChoiceBox<>();
        choiceBox.setMinWidth(BUTTON_WIDTH);
        choiceBox.setMaxWidth(BUTTON_WIDTH);
        choiceBox.getItems().addAll(2, 3, 4);
        choiceBox.setValue(2);
        choiceBox.relocate(WINDOW_WIDTH / 2.0 - BUTTON_WIDTH / 2.0, 320);
        //Back and Confirm buttons
        GameButton btnNumberBack = new GameButton("Back", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnNumberBack.relocate(BUTTON_HEIGHT / 2.0, BUTTON_HEIGHT / 2.0);
        GameButton btnNumberConfirm = new GameButton("Confirm", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnNumberConfirm.relocate(WINDOW_WIDTH / 2.0 - BUTTON_WIDTH / 2.0, 360);
        btnNumberConfirm.requestFocus();

        numberPane.getChildren().addAll(choiceBox, btnNumberBack, btnNumberConfirm);

        //players choose their colours
        Pane colourPane = new Pane();
        Scene colourScene = new Scene(colourPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        //buttons for different colours
        ColourButton btnCyan = new ColourButton(Colour.CYAN);
        btnCyan.relocate(270, 180);
        ColourButton btnYellow = new ColourButton(Colour.YELLOW);
        btnYellow.relocate(270 + 3 * COLOUR_BUTTON_RADIUS, 180);
        ColourButton btnRed = new ColourButton(Colour.RED);
        btnRed.relocate(270 + 6 * COLOUR_BUTTON_RADIUS, 180);
        ColourButton btnPurple = new ColourButton(Colour.PURPLE);
        btnPurple.relocate(270 + 9 * COLOUR_BUTTON_RADIUS, 180);
        ArrayList<ColourButton> colourButtons = new ArrayList<>(List.of(btnCyan, btnYellow, btnRed, btnPurple));
        //Back, Reset and Confirm buttons
        GameButton btnColourBack = new GameButton("Back", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnColourBack.relocate(BUTTON_HEIGHT / 2.0, BUTTON_HEIGHT / 2.0);
        GameButton btnColourReset = new GameButton("Reset", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnColourReset.relocate(WINDOW_WIDTH / 2.0 - BUTTON_WIDTH * 1.5, 420);
        GameButton btnColourConfirm = new GameButton("Confirm", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnColourConfirm.relocate(WINDOW_WIDTH / 2.0 + BUTTON_WIDTH * 0.5, 420);
        btnColourConfirm.setDisable(true);

        colourPane.getChildren().addAll(btnCyan, btnYellow, btnRed, btnPurple, btnColourBack, btnColourReset, btnColourConfirm);

        //keep a temporary list of players
        ArrayList<Player> tmp = new ArrayList<>();
        for (ColourButton colourButton : colourButtons) {
            colourButton.setOnMouseClicked(event -> {
                tmp.add(new Player(colourButton.colour));
                colourButton.setBorder(colourButton.border);
                colourButton.setDisable(true);
                if (tmp.size() == this.numOfPlayers) {
                    colourButtons.forEach(b -> b.setDisable(true));
                    btnColourConfirm.setDisable(false);
                    btnColourConfirm.requestFocus();
                }
            });
        }

        btnStart.setOnMouseClicked(event -> {
            primaryStage.setScene(numberScene);
            btnNumberConfirm.requestFocus();
        });

        btnNumberBack.setOnMouseClicked(event -> {
            this.numOfPlayers = 0;
            choiceBox.setValue(2);
            primaryStage.setScene(titleScene);
        });

        btnNumberConfirm.setOnMouseClicked(event -> {
            this.numOfPlayers = choiceBox.getValue();
            primaryStage.setScene(colourScene);
            btnColourReset.requestFocus();
        });

        btnColourBack.setOnMouseClicked(event -> {
            tmp.clear();
            btnColourConfirm.setDisable(true);
            colourButtons.forEach(b -> {
                b.setDisable(false);
                b.setBorder(null);
            });
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
            this.gameState = new GameState(this.players);
            //main game
            Scene mainScene = makeMainScene();
            primaryStage.setScene(mainScene);
        });

        primaryStage.setResizable(false);
        primaryStage.setTitle("Game");
        primaryStage.setScene(titleScene);
        primaryStage.show();
    }

    private Scene makeMainScene() {
        this.currentPhase = Phase.ROTATION;
        GamePane gameArea = new GamePane(WINDOW_WIDTH, WINDOW_HEIGHT);
        Border gamePaneBorder = new Border(new BorderStroke(GAME_PANE_BORDER_COLOR, GAME_PANE_BORDER_STROKE_STYLE, GAME_PANE_BORDER_RADII, GAME_PANE_BORDER_WIDTH));

        //board area to display information about the board and assam
        final Pane boardArea = new GamePane(BOARD_AREA_SIDE, BOARD_AREA_SIDE);
        boardArea.setBorder(gamePaneBorder);
        boardArea.relocate(MARGIN, MARGIN);
        gameArea.getChildren().add(boardArea);

        final Pane tileArea = new GamePane(NUM_OF_COLS * TILE_SIDE, NUM_OF_ROWS * TILE_SIDE);
        tileArea.relocate(TILE_RELOCATION_X, TILE_RELOCATION_Y);

        initTiles();
        initInvisibleRugs();
        initAssam();
        tileArea.getChildren().addAll(this.allTiles, this.placedRugs, this.invisibleRugs, this.assam);
        boardArea.getChildren().add(tileArea);

        //player area to display stats and controls for the players, contains stats area and control area
        final GamePane playerArea = new GamePane(PLAYER_AREA_WIDTH, PLAYER_AREA_HEIGHT);
        playerArea.relocate(WINDOW_HEIGHT, MARGIN);
        gameArea.getChildren().add(playerArea);

        //stats area
        final GamePane statsArea = new GamePane(STATS_AREA_WIDTH, STATS_AREA_HEIGHT);
        statsArea.setBorder(gamePaneBorder);
        playerArea.getChildren().add(statsArea);

        this.phaseText = new Text(this.currentPhase.toString());
        this.phaseText.relocate(0, 60);
        statsArea.getChildren().add(this.phaseText);

        //control area
        GamePane controlArea = new GamePane(CONTROL_AREA_WIDTH, CONTROL_AREA_HEIGHT);
        controlArea.setBorder(gamePaneBorder);
        controlArea.relocate(0, STATS_AREA_HEIGHT + MARGIN);
        playerArea.getChildren().add(controlArea);

        GameButton btnRotate = new GameButton("Rotate", BUTTON_WIDTH, BUTTON_HEIGHT);

        //buttons inside control area
        GameButton btnRotateLeft = new GameButton("Rotate Left", BUTTON_WIDTH, BUTTON_HEIGHT);
        GameButton btnRotateRight = new GameButton("Rotate Right", BUTTON_WIDTH, BUTTON_HEIGHT);
        GameButton btnRotateZero = new GameButton("No Rotation", BUTTON_WIDTH, BUTTON_HEIGHT);
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(btnRotateLeft, btnRotateRight, btnRotateZero);

        GameButton btnRollDie = new GameButton("Roll Die", BUTTON_WIDTH, BUTTON_HEIGHT);

        Text dieResultText = new Text();

        GameButton btnMoveAssam = new GameButton("Move Assam", BUTTON_WIDTH, BUTTON_HEIGHT);

        GameButton btnMakePayment = new GameButton("Make Payment", BUTTON_WIDTH, BUTTON_HEIGHT);

        Button btnMakePlacement = new GameButton("Make Placement", BUTTON_WIDTH, BUTTON_HEIGHT);

        //allows player to set Assam's initial direction
        controlArea.getChildren().add(btnRotate);

        btnRotate.setOnMouseClicked(event -> {
            nextPhase();
            controlArea.getChildren().remove(btnRotate);
            controlArea.getChildren().add(btnRollDie);
        });

        for (Node button : buttonBar.getButtons()) {
            button.setOnMouseClicked(event -> {
                nextPhase();
                controlArea.getChildren().remove(buttonBar);
                controlArea.getChildren().add(btnRollDie);
            });
        }

        btnRollDie.setOnMouseClicked(event -> {
            this.dieResult = Die.getSide();
            dieResultText.setText("Die result is " + this.dieResult + ".");
            controlArea.getChildren().remove(btnRollDie);
            controlArea.getChildren().addAll(dieResultText, btnMoveAssam);
        });

        btnMoveAssam.setOnMouseClicked(event -> {
            controlArea.getChildren().removeAll(dieResultText, btnMoveAssam);
            controlArea.getChildren().add(btnMakePayment);
            this.gameState.moveAssam(this.dieResult);
            updateAssam();
        });

        btnMakePayment.setOnMouseClicked(event -> {
            nextPhase();
            controlArea.getChildren().remove(btnMakePayment);
            controlArea.getChildren().add(btnMakePlacement);
            this.draggableRug = new DraggableRug(750, 500, this.gameState.getCurrentPlayer().getColour());
            gameArea.getChildren().add(this.draggableRug);
        });

        btnMakePlacement.setOnMouseClicked(event -> {
            if (this.currentPhase == Phase.PLACEMENT && this.highlighted != null) {
                controlArea.getChildren().remove(btnMakePlacement);
                gameArea.getChildren().remove(this.draggableRug);
                makePlacement();
                if (!gameState.isGameOver()) {
                    controlArea.getChildren().add(buttonBar);
                }
            }
        });

        Scene scene = getScene(gameArea);
        return scene;
    }

    private Scene getScene(GamePane gameArea) {
        Scene scene = new Scene(gameArea, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.Q || event.getCode() == KeyCode.E) {
                if (this.currentPhase == Phase.PLACEMENT) {
                    toggleRugOrientation();
                }
            }
            if (event.getCode() == KeyCode.ENTER) {
                if (this.currentPhase == Phase.PLACEMENT) {
                    makePlacement();
                }
            }
        });
        return scene;
    }

    private void initTiles() {
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

    private void initAssam() {
        SVGPath svg = new SVGPath();
        svg.setContent(ASSAM_SVG);
        this.assam = new Region();
        this.assam.setShape(svg);
        this.assam.setMinSize(TILE_SIDE / 2, TILE_SIDE / 2);
        this.assam.setMaxSize(TILE_SIDE / 2, TILE_SIDE / 2);
        this.assam.setBackground(new Background(new BackgroundFill(Color.LIMEGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        this.assam.setBorder(new Border(new BorderStroke(Color.LIMEGREEN.darker(), GAME_PANE_BORDER_STROKE_STYLE, GAME_PANE_BORDER_RADII, GAME_PANE_BORDER_WIDTH)));
        updateAssam();
    }

    private void updateAssam() {
        Tile assamTile = this.gameState.getBoard().getAssamTile();
        int row = assamTile.getRow();
        int col = assamTile.getCol();
        this.assam.relocate(TILE_SIDE / 4 + col * TILE_SIDE, TILE_SIDE / 4 + row * TILE_SIDE);
        this.assam.setRotate(getAssamDirection().getAngle());
    }

    private Direction getAssamDirection() {
        return this.gameState.getBoard().getAssamDirection();
    }

    private void toggleRugOrientation() {
        if (this.draggableRug != null) {
            this.draggableRug.setRotate(this.draggableRug.getRotate() + 90);
            this.draggableRug.orientation = this.draggableRug.orientation == Orientation.VERTICAL ? Orientation.HORIZONTAL : Orientation.VERTICAL;
            if (this.highlighted != null) {
                this.highlighted.setOpacity(0);
                this.highlighted = null;
            }
        }
    }

    private void makePlacement() {
        if (this.highlighted != null) {
            Rug rug = new Rug(this.draggableRug.colour, this.rugID++, getTilesFromHighlighted());
            this.gameState.makePlacement(rug);
            GameRug gameRug = new GameRug(this.highlighted.getLayoutX(), this.highlighted.getLayoutY(), this.draggableRug.orientation, this.draggableRug.colour);

            this.highlighted.setOpacity(0);
            this.highlighted = null;
            this.placedRugs.getChildren().add(gameRug);
            this.draggableRug = null;

            if (!gameState.isGameOver()) {
                nextPhase();
                gameState.nextPlayer();
            }
        }
    }

    private static class GamePane extends Pane {
        public GamePane(double width, double height) {
            super();
            this.setMinSize(width, height);
            this.setMaxSize(width, height);
        }

    }

    private static class GameButton extends Button {
        public GameButton(String string, double width, double height) {
            super(string);
            this.setMinSize(width, height);
            this.setMaxSize(width, height);
        }

    }

    private static class ColourButton extends Button {
        private final Colour colour;

        private final Border border;

        public ColourButton(Colour colour) {
            super();
            this.colour = colour;
            this.setShape(new Circle(COLOUR_BUTTON_RADIUS));
            this.setMinSize(2 * COLOUR_BUTTON_RADIUS, 2 * COLOUR_BUTTON_RADIUS);
            this.setMaxSize(2 * COLOUR_BUTTON_RADIUS, 2 * COLOUR_BUTTON_RADIUS);
            this.setBackground(new Background(new BackgroundFill(Colour.getFrontEndColor(colour), CornerRadii.EMPTY, Insets.EMPTY)));
            this.border = new Border(new BorderStroke(Colour.getFrontEndColor(colour).darker(), COLOUR_BUTTON_BORDER_STROKE_STYLE, COLOUR_BUTTON_BORDER_RADII, COLOUR_BUTTON_BORDER_WIDTH));
        }
    }


    private static class InvisibleRug extends Rectangle {
        private final GameTile[] gameTiles = new GameTile[2];

        public InvisibleRug(double x, double y, GameTile[] gameTiles, Orientation orientation) {
            super(0, 0, TILE_SIDE, 2 * TILE_SIDE);
            this.gameTiles[0] = gameTiles[0];
            this.gameTiles[1] = gameTiles[1];

            this.relocate(x - TILE_SIDE / 2, y - TILE_SIDE);
            if (orientation == Orientation.HORIZONTAL) {
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

            this.relocate(x, y);
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

    private class DraggableRug extends GameRug {
        private Orientation orientation = Orientation.VERTICAL;
        private final Colour colour;
        private double mouseX, mouseY;

        public DraggableRug(double x, double y, Colour colour) {
            super(x, y, Orientation.VERTICAL, colour);
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
                highlightNearestInvisibleRug();
            });

            this.setOnMouseReleased(event -> {
                InvisibleRug nearestInvisibleRug = findNearestInvisibleRug();
                if (nearestInvisibleRug != null) {
                    this.setLayoutX(nearestInvisibleRug.getLayoutX() + TILE_RELOCATION_X + MARGIN);
                    this.setLayoutY(nearestInvisibleRug.getLayoutY() + TILE_RELOCATION_Y + MARGIN);
                }
            });
        }

        private InvisibleRug findNearestInvisibleRug() {
            if (this.orientation == Orientation.VERTICAL) {
                vInvisibleRugs.sort(compareByDistance());
                for (InvisibleRug vInvisibleRug : vInvisibleRugs) {
                    Rug rug = new Rug(draggableRug.colour, rugID, getTilesFromInvisibleRug(vInvisibleRug));
                    if (gameState.getBoard().isPlacementValid(rug)) {
                        return vInvisibleRug;
                    }
                }
            }
            hInvisibleRugs.sort(compareByDistance());
            for (InvisibleRug hInvisibleRug : hInvisibleRugs) {
                Rug rug = new Rug(draggableRug.colour, rugID, getTilesFromInvisibleRug(hInvisibleRug));
                if (gameState.getBoard().isPlacementValid(rug)) {
                    return hInvisibleRug;
                }
            }
            return null;
        }

        private void highlightNearestInvisibleRug() {
            if (highlighted != null) {
                highlighted.setOpacity(0);
            }
            highlighted = findNearestInvisibleRug();
            if (highlighted != null) {
                highlighted.setOpacity(HIGHLIGHTED_OPACITY);
            }
        }

        public Comparator<InvisibleRug> compareByDistance() {
            return new Comparator<>() {
                @Override
                public int compare(InvisibleRug rug1, InvisibleRug rug2) {
                    return Double.compare(
                            rug1.distance(DraggableRug.this.getLayoutX() - TILE_RELOCATION_X - MARGIN, DraggableRug.this.getLayoutY() - TILE_RELOCATION_Y - MARGIN),
                            rug2.distance(DraggableRug.this.getLayoutX() - TILE_RELOCATION_X - MARGIN, DraggableRug.this.getLayoutY() - TILE_RELOCATION_Y - MARGIN));
                }
            };
        }
    }

    private static class GameTile extends Rectangle {
        private final int row;
        private final int col;

        public GameTile(double x, double y, int row, int col) {
            super(0, 0, TILE_SIDE, TILE_SIDE);
            this.row = row;
            this.col = col;

            this.relocate(x, y);
            this.setFill(TILE_COLOR);
            this.setStroke(TILE_COLOR.darker());
            this.setStrokeWidth(TILE_BORDER_WIDTH);
        }
    }

    private enum Phase {
        ROTATION,
        MOVEMENT,
        PLACEMENT;

        public String toString() {
            switch (this) {
                case ROTATION -> {
                    return "ROTATION";
                }
                case MOVEMENT -> {
                    return "MOVEMENT";
                }
                case PLACEMENT -> {
                    return "PLACEMENT";
                }
                default -> {
                    return "";
                }
            }
        }
    }

    private void nextPhase() {
        switch (this.currentPhase) {
            case ROTATION -> this.currentPhase = Phase.MOVEMENT;
            case MOVEMENT -> this.currentPhase = Phase.PLACEMENT;
            case PLACEMENT -> this.currentPhase = Phase.ROTATION;
        }
        this.phaseText.setText(this.currentPhase.toString());
    }

    private enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    private Tile[] getTilesFromHighlighted() {
        if (this.highlighted != null) {
            return new Tile[]{getTileFromGameTile(this.highlighted.gameTiles[0]), getTileFromGameTile(this.highlighted.gameTiles[1])};
        }
        return null;
    }

    private Tile[] getTilesFromInvisibleRug(InvisibleRug invisibleRug) {
        return new Tile[]{getTileFromGameTile(invisibleRug.gameTiles[0]), getTileFromGameTile(invisibleRug.gameTiles[1])};
    }

    private Tile getTileFromGameTile(GameTile gameTile) {
        return this.gameState.getBoard().getTiles()[gameTile.row][gameTile.col];
    }
}
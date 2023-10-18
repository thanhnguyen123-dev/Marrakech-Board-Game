package comp1110.ass2.gui;

import comp1110.ass2.GameState;
import comp1110.ass2.board.Tile;
import comp1110.ass2.player.Colour;
import comp1110.ass2.player.Die;
import comp1110.ass2.player.Player;
import comp1110.ass2.player.Player.Strategy;
import comp1110.ass2.player.Rug;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Game extends Application {
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 700;
    static final double MARGIN_LEFT = 120;
    static final double MARGIN_TOP = 30;
    private static final double BOARD_AREA_SIDE = WINDOW_HEIGHT - 2 * MARGIN_TOP;
    private static final double PLAYER_AREA_WIDTH = WINDOW_WIDTH - BOARD_AREA_SIDE - MARGIN_LEFT - 2 * MARGIN_TOP;
    private static final double PLAYER_AREA_HEIGHT = BOARD_AREA_SIDE;
    private static final double STATS_AREA_WIDTH = PLAYER_AREA_WIDTH;
    private static final double STATS_AREA_HEIGHT = 400;
    private static final double CONTROL_AREA_WIDTH = PLAYER_AREA_WIDTH;
    private static final double CONTROL_AREA_HEIGHT = PLAYER_AREA_HEIGHT - STATS_AREA_HEIGHT - MARGIN_TOP;
    static final Color TILE_COLOR = Color.TAN;
    private static final Color GAME_PANE_BORDER_COLOR = TILE_COLOR.darker();
    private static final BorderStrokeStyle GAME_PANE_BORDER_STROKE_STYLE = BorderStrokeStyle.SOLID;
    private static final CornerRadii GAME_PANE_BORDER_RADII = new CornerRadii(24);
    private static final BorderWidths GAME_PANE_BORDER_WIDTH = new BorderWidths(4);
    private static final int NUM_OF_ROWS = 7;
    private static final int NUM_OF_COLS = 7;
    static final double TILE_SIDE = 64;
    static final double TILE_RELOCATION_X = (BOARD_AREA_SIDE - NUM_OF_COLS * TILE_SIDE) / 2;
    static final double TILE_RELOCATION_Y = (BOARD_AREA_SIDE - NUM_OF_ROWS * TILE_SIDE) / 2;
    static final int TILE_BORDER_WIDTH = 4;
    static final int RUG_BORDER_WIDTH = 4;
    private static final int BUTTON_WIDTH = 120;
    private static final int BUTTON_HEIGHT = 60;
    static final int COLOUR_BUTTON_RADIUS = 60;
    static final BorderStrokeStyle COLOUR_BUTTON_BORDER_STROKE_STYLE = GAME_PANE_BORDER_STROKE_STYLE;
    static final CornerRadii COLOUR_BUTTON_BORDER_RADII = GAME_PANE_BORDER_RADII;
    static final BorderWidths COLOUR_BUTTON_BORDER_WIDTH = new BorderWidths(8);
    static final double HIGHLIGHTED_OPACITY = 1 - (Math.sqrt(5) - 1) / 2;
    private static final double ASSAM_SIDE = (Math.sqrt(5) - 1) / 2 * TILE_SIDE;
    private static final double ASSAM_RELOCATION = (TILE_SIDE - ASSAM_SIDE) / 2;
    private static final Color ASSAM_COLOR = Color.SPRINGGREEN.darker();
    //https://fonts.google.com/icons?selected=Material%20Symbols%20Rounded%3Anavigation%3AFILL%401%3Bwght%40400%3BGRAD%400%3Bopsz%4024
    private static final String ASSAM_SVG = "M480-240 222-130q-13 5-24.5 2.5T178-138q-8-8-10.5-20t2.5-25l273-615q5-12 15.5-18t21.5-6q11 0 21.5 6t15.5 18l273 615q5 13 2.5 25T782-138q-8 8-19.5 10.5T738-130L480-240Z";

    private final Pane allTiles = new Pane();
    private final Pane placedRugs = new Pane();
    private final Pane invisibleRugs = new Pane();
    private final GameTile[][] gameTiles = new GameTile[NUM_OF_ROWS][NUM_OF_COLS];
    final ArrayList<GameInvisibleRug> vGameInvisibleRugs = new ArrayList<>();
    final ArrayList<GameInvisibleRug> hGameInvisibleRugs = new ArrayList<>();

    private GamePane gameArea;
    private GamePane controlArea;


    // number of human and computer players
    private int numOfPlayers;

    private Phase currentPhase;
    private Text phaseText;
    private Region assam;
    private int dieResult;
    GameInvisibleRug highlighted;
    GameDraggableRug gameDraggableRug;
    int rugID;
    //Keep a temporary list of players
    private final ArrayList<Player> tmp = new ArrayList<>();
    private ArrayList<PlayerSelector> playerSelectors;

    VBox eachPlayerStatArea = new VBox();
    HBox computerPlayerControl = new HBox();
    // simulate mouse click
    MouseEvent clickEvent = new MouseEvent(
            MouseEvent.MOUSE_CLICKED,
            0, 0, 0, 0,
            MouseButton.PRIMARY, 1,
            true, true, true, true,
            true, true, true, true,
            true, true, null
    );
    MouseEvent pressEvent = new MouseEvent(
            MouseEvent.MOUSE_PRESSED,
            0, 0, 0, 0,
            MouseButton.PRIMARY, 1,
            true, true, true, true,
            true, true, true, true,
            true, true, null
    );
    MouseEvent dragEvent = new MouseEvent(
            MouseEvent.MOUSE_DRAGGED,
            100, 100, 100, 100,
            MouseButton.PRIMARY, 1,
            true, true, true, true,
            true, true, true, true,
            true, true, null
    );
    MouseEvent releaseEvent = new MouseEvent(
            MouseEvent.MOUSE_RELEASED,
            100, 100, 100, 100,
            MouseButton.PRIMARY, 1,
            true, true, true, true,
            true, true, true, true,
            true, true, null
    );

    private final GameButton btnNumberConfirm = new GameButton("Confirm", BUTTON_WIDTH, BUTTON_HEIGHT);
    private final GameButton btnColourConfirm = new GameButton("Confirm", BUTTON_WIDTH, BUTTON_HEIGHT);

    private ArrayList<GameButton> btnRotations;
    private final GameButton btnConfirmRotation = new GameButton("Confirm Rotation", BUTTON_WIDTH * 1.2, BUTTON_HEIGHT);
    private final GameButton btnRollDie = new GameButton("Roll Die", BUTTON_WIDTH * 1.2, BUTTON_HEIGHT);
    private final GameButton btnMoveAssam = new GameButton("Confirm Movement", BUTTON_WIDTH * 1.2, BUTTON_HEIGHT);
    private final GameButton btnConfirmPayment = new GameButton("Proceed", BUTTON_WIDTH * 1.2, BUTTON_HEIGHT);
    private final GameButton btnRotateRug = new GameButton("Rotate Rug", BUTTON_WIDTH * 1.2, BUTTON_HEIGHT);
    private final GameButton btnConfirmPlacement = new GameButton("Confirm Placement", BUTTON_WIDTH * 1.2, BUTTON_HEIGHT);

    // all players, including human and computer players
    private Player[] players;
    GameState gameState;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Home page, title screen
        VBox titlePane = new VBox();
        Scene titleScene = new Scene(titlePane, WINDOW_WIDTH, WINDOW_HEIGHT);
        Text title = new Text("Play Marrakech!");
        title.setFont(new Font("", 60));
        GameButton btnStart = new GameButton("Start", BUTTON_WIDTH, BUTTON_HEIGHT);
        // Make two elements center vertical
        titlePane.setAlignment(Pos.CENTER);
        titlePane.setSpacing(20);
        // Add all children of titlePane
        titlePane.getChildren().addAll(title, btnStart);

        // Choose number of players
        Pane numberPane = new Pane();
        Scene numberScene = new Scene(numberPane, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Choice box to choose the number of human players
        Text numberText = new Text("Please choose the number of players");
        numberText.setFont(new Font(18));
        numberText.relocate(WINDOW_WIDTH / 2.0 - BUTTON_WIDTH / 2.0 - 300, 280);

        ChoiceBox<Integer> choiceBox = new ChoiceBox<>();
        choiceBox.setMinWidth(BUTTON_WIDTH);
        choiceBox.setMaxWidth(BUTTON_WIDTH);
        choiceBox.getItems().addAll(2, 3, 4);
        // The default number for human players is 2
        choiceBox.setValue(2);
        choiceBox.relocate(WINDOW_WIDTH / 2.0 - BUTTON_WIDTH / 2.0, 320);

        //Back and Confirm buttons
        GameButton btnNumberBack = new GameButton("Back", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnNumberBack.relocate(BUTTON_HEIGHT / 2.0, BUTTON_HEIGHT / 2.0);
        this.btnNumberConfirm.relocate(WINDOW_WIDTH / 2.0 - BUTTON_WIDTH / 2.0, 360);
        this.btnNumberConfirm.requestFocus();

        // Add all children of numberPane
        numberPane.getChildren().addAll(choiceBox, numberText, btnNumberBack, this.btnNumberConfirm);

        // Players choose their colours
        Pane colourPane = new Pane();
        Scene colourScene = new Scene(colourPane, WINDOW_WIDTH, WINDOW_HEIGHT);

        this.playerSelectors = makeNewPlayerSelectors();

        //Back, Reset and Confirm buttons
        GameButton btnColourBack = new GameButton("Back", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnColourBack.relocate(BUTTON_HEIGHT / 2.0, BUTTON_HEIGHT / 2.0);
        GameButton btnColourReset = new GameButton("Reset", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnColourReset.relocate(WINDOW_WIDTH / 2.0 - BUTTON_WIDTH * 1.5, 420);
        this.btnColourConfirm.relocate(WINDOW_WIDTH / 2.0 + BUTTON_WIDTH * 0.5, 420);
        this.btnColourConfirm.setDisable(true);

        colourPane.getChildren().addAll(this.playerSelectors);
        colourPane.getChildren().addAll(btnColourBack, btnColourReset, btnColourConfirm);

        // Start button on the homepage
        btnStart.setOnMouseClicked(event -> {
            primaryStage.setScene(numberScene);
            this.btnNumberConfirm.requestFocus();
        });

        // Back button on choose player number scene
        btnNumberBack.setOnMouseClicked(event -> {
            // Initialize human players and computer players
            this.numOfPlayers = 0;
            choiceBox.setValue(2);
            primaryStage.setScene(titleScene);
        });

        // Confirm button on choose player number scene
        this.btnNumberConfirm.setOnMouseClicked(event -> {
            this.numOfPlayers = choiceBox.getValue();
            primaryStage.setScene(colourScene);
            btnColourReset.requestFocus();
        });

        // Click back button on choose colour scene
        btnColourBack.setOnMouseClicked(event -> {
            // Initialize human players and computer players
            this.tmp.clear();
            btnColourConfirm.setDisable(true);
            colourPane.getChildren().removeAll(this.playerSelectors);
            this.playerSelectors = makeNewPlayerSelectors();
            colourPane.getChildren().addAll(this.playerSelectors);
            primaryStage.setScene(numberScene);
        });

        // Reset color on choose colour scene
        btnColourReset.setOnMouseClicked(event -> {
            this.tmp.clear();
            this.btnColourConfirm.setDisable(true);
            colourPane.getChildren().removeAll(this.playerSelectors);
            this.playerSelectors = makeNewPlayerSelectors();
            colourPane.getChildren().addAll(this.playerSelectors);
        });

        // Confirm selected colour on choose colour scene
        this.btnColourConfirm.setOnMouseClicked(event -> {
            this.players = this.tmp.toArray(new Player[0]);
            this.gameState = new GameState(this.players);
            //main game
            Scene mainScene = makeMainScene();
            primaryStage.setScene(mainScene);
        });

        primaryStage.setResizable(false);
        primaryStage.setTitle("Marrakech Game");
        primaryStage.setScene(titleScene);
        primaryStage.show();
    }

    private ArrayList<PlayerSelector> makeNewPlayerSelectors() {
        // Player Selectors for all the players
        PlayerSelector playerCyan = new PlayerSelector(COLOUR_BUTTON_RADIUS * 1.2, COLOUR_BUTTON_RADIUS * 4, Colour.CYAN);
        playerCyan.relocate(270, 180);
        PlayerSelector playerYellow = new PlayerSelector(COLOUR_BUTTON_RADIUS * 1.2, COLOUR_BUTTON_RADIUS * 4, Colour.YELLOW);
        playerYellow.relocate(270 + 3 * COLOUR_BUTTON_RADIUS, 180);
        PlayerSelector playerRed = new PlayerSelector(COLOUR_BUTTON_RADIUS * 1.2, COLOUR_BUTTON_RADIUS * 4, Colour.RED);
        playerRed.relocate(270 + 6 * COLOUR_BUTTON_RADIUS, 180);
        PlayerSelector playerPurple = new PlayerSelector(COLOUR_BUTTON_RADIUS * 1.2, COLOUR_BUTTON_RADIUS * 4, Colour.PURPLE);
        playerPurple.relocate(270 + 9 * COLOUR_BUTTON_RADIUS, 180);

        return new ArrayList<>(List.of(playerCyan, playerYellow, playerRed, playerPurple));
    }

    /**
     * @return
     */
    private Scene makeMainScene() {
        // Initialize game phase as rotation phase
        this.currentPhase = Phase.ROTATION;
        this.gameArea = new GamePane(WINDOW_WIDTH, WINDOW_HEIGHT);
        Border gamePaneBorder = new Border(new BorderStroke(GAME_PANE_BORDER_COLOR, GAME_PANE_BORDER_STROKE_STYLE, GAME_PANE_BORDER_RADII, GAME_PANE_BORDER_WIDTH));

        // Board area to display information about the board and assam
        final Pane boardArea = new GamePane(BOARD_AREA_SIDE, BOARD_AREA_SIDE);
        boardArea.setBorder(gamePaneBorder);
        boardArea.relocate(MARGIN_LEFT, MARGIN_TOP);
        this.gameArea.getChildren().add(boardArea);

        final Pane tileArea = new GamePane(NUM_OF_COLS * TILE_SIDE, NUM_OF_ROWS * TILE_SIDE);
        tileArea.relocate(TILE_RELOCATION_X, TILE_RELOCATION_Y);

        initTiles();
        initInvisibleRugs();
        initAssam();
        tileArea.getChildren().addAll(this.allTiles, this.placedRugs, this.invisibleRugs, this.assam);
        boardArea.getChildren().add(tileArea);

        // Display area to display statements and controls for the players, contains statements area and control area
        final GamePane playerArea = new GamePane(PLAYER_AREA_WIDTH, PLAYER_AREA_HEIGHT);
        playerArea.relocate(MARGIN_LEFT + BOARD_AREA_SIDE + MARGIN_TOP, MARGIN_TOP);
        this.gameArea.getChildren().add(playerArea);

        // Stats area, include game stats and player stats
        final GamePane statsArea = new GamePane(STATS_AREA_WIDTH, STATS_AREA_HEIGHT);
        statsArea.setBorder(gamePaneBorder);
        playerArea.getChildren().add(statsArea);
        // Display game stats
        this.phaseText = new Text("Current phase of game: " + this.currentPhase.toString());
        this.phaseText.setFont(new Font(20));
        this.phaseText.relocate(30, 10);
        statsArea.getChildren().add(this.phaseText);

        // Display player stats
        final VBox playerStatArea = new VBox();
        playerStatArea.setPrefSize(STATS_AREA_WIDTH, STATS_AREA_HEIGHT - 50);
        playerStatArea.relocate(30, 50);
        // load player stats
        updatePlayerStats();
        playerStatArea.getChildren().addAll(eachPlayerStatArea);
        statsArea.getChildren().add(playerStatArea);

        // control area
        this.controlArea = new GamePane(CONTROL_AREA_WIDTH, CONTROL_AREA_HEIGHT);
        this.controlArea.setBorder(gamePaneBorder);
        this.controlArea.relocate(0, STATS_AREA_HEIGHT + MARGIN_TOP);
        playerArea.getChildren().add(this.controlArea);

        //buttons inside control area
        GameButton btnInitialRotation = new GameButton("Rotate Right", BUTTON_WIDTH * 1.2, BUTTON_HEIGHT);
        btnInitialRotation.relocate(BUTTON_WIDTH, BUTTON_HEIGHT / 2.0);
        GameButton btnConfirmInitialRotation = new GameButton("Confirm Rotation", BUTTON_WIDTH * 1.2, BUTTON_HEIGHT);
        btnConfirmInitialRotation.relocate(BUTTON_WIDTH, BUTTON_HEIGHT * 2);

        GameButton btnRotateLeft = new GameButton("Rotate Left", BUTTON_WIDTH * 0.8, BUTTON_HEIGHT);
        btnRotateLeft.relocate(BUTTON_WIDTH * 0.6, BUTTON_HEIGHT / 2.0);
        GameButton btnRotateRight = new GameButton("Rotate Right", BUTTON_WIDTH * 0.8, BUTTON_HEIGHT);
        btnRotateRight.relocate(BUTTON_WIDTH * 1.8, BUTTON_HEIGHT / 2.0);
        this.btnRotations = new ArrayList<>(List.of(btnRotateLeft, btnRotateRight));
        this.btnConfirmRotation.relocate(BUTTON_WIDTH, BUTTON_HEIGHT * 2);

        this.btnRollDie.relocate(BUTTON_WIDTH, BUTTON_HEIGHT * 2);

        Text movementText = new Text();
        movementText.relocate(BUTTON_WIDTH, BUTTON_HEIGHT);
        this.btnMoveAssam.relocate(BUTTON_WIDTH, BUTTON_HEIGHT * 2);

        Text paymentText = new Text();
        paymentText.relocate(BUTTON_WIDTH, BUTTON_HEIGHT);
        paymentText.setWrappingWidth(300);
        this.btnConfirmPayment.relocate(BUTTON_WIDTH, BUTTON_HEIGHT * 2);

        this.btnRotateRug.relocate(BUTTON_WIDTH * 1.5, BUTTON_HEIGHT / 2.0);
        this.btnConfirmPlacement.relocate(BUTTON_WIDTH * 1.5, BUTTON_HEIGHT * 2);

        //allows player to set Assam's initial direction
        this.controlArea.getChildren().addAll(btnInitialRotation, btnConfirmInitialRotation);

        btnInitialRotation.setOnMouseClicked(event -> {
            this.assam.setRotate(this.assam.getRotate() + 90);
        });

        btnConfirmInitialRotation.setOnMouseClicked(event -> {
            if (this.currentPhase == Phase.ROTATION) {
                nextPhase();
                this.controlArea.getChildren().removeAll(btnInitialRotation, btnConfirmInitialRotation);
                this.gameState.rotateAssam((int) this.assam.getRotate() - getAssamAngle());
                this.controlArea.getChildren().add(this.btnRollDie);
            }
        });

        btnRotateLeft.setOnMouseClicked(event -> {
            if (this.gameState.getBoard().isRotationLegal((int) this.assam.getRotate() - getAssamAngle() - 90)) {
                this.assam.setRotate(this.assam.getRotate() - 90);
            }
        });

        btnRotateRight.setOnMouseClicked(event -> {
            if (this.gameState.getBoard().isRotationLegal((int) this.assam.getRotate() - getAssamAngle() + 90)) {
                this.assam.setRotate(this.assam.getRotate() + 90);
            }
        });

        // confirm rotation Assam
        this.btnConfirmRotation.setOnMouseClicked(event -> {
            if (this.currentPhase == Phase.ROTATION) {
                nextPhase();
                this.controlArea.getChildren().removeAll(this.btnRotations);
                this.controlArea.getChildren().remove(this.btnConfirmRotation);
                this.gameState.rotateAssam((int) this.assam.getRotate() - getAssamAngle());
                this.controlArea.getChildren().add(this.btnRollDie);
            }
        });

        // roll die
        this.btnRollDie.setOnMouseClicked(event -> {
            this.controlArea.getChildren().remove(this.btnRollDie);
            this.dieResult = Die.getSide();
            this.controlArea.getChildren().addAll(movementText, this.btnMoveAssam);
            movementText.setText("Die result is " + this.dieResult + ". Assam will now move " + this.dieResult + " steps.");
            movementText.setFont(new Font(16));
        });

        // make Assam move
        this.btnMoveAssam.setOnMouseClicked(event -> {
            this.controlArea.getChildren().removeAll(movementText, this.btnMoveAssam);
            this.gameState.moveAssam(this.dieResult);
            updateAssam();
            this.controlArea.getChildren().addAll(paymentText, btnConfirmPayment);
            if (!this.gameState.isPaymentRequired()) {
                paymentText.setText("No payment required.");
            } else if (this.gameState.isPaymentAffordable()) {
                paymentText.setText("You will need to pay Player " + this.gameState.findAssamRugOwner().getColour().toString() + " " + this.gameState.getPaymentAmount() + " dirhams.");
            } else {
                paymentText.setText("You cannot afford to pay. You will have to give all your dirhams to Player " + this.gameState.findAssamRugOwner().getColour().toString() + ". After that, You will be removed from the game");
            }
            paymentText.setFont(new Font(16));
        });

        // If need payment, confirm payment
        this.btnConfirmPayment.setOnMouseClicked(event -> {
            if (this.currentPhase == Phase.MOVEMENT) {
                nextPhase();
                this.controlArea.getChildren().removeAll(paymentText, this.btnConfirmPayment);
                if (!this.gameState.isPaymentRequired() || this.gameState.isPaymentAffordable()) {
                    this.gameState.makePayment();
                    this.gameDraggableRug = new GameDraggableRug(this, MARGIN_LEFT + BOARD_AREA_SIDE + MARGIN_TOP + BUTTON_WIDTH * 0.5, 500, this.gameState.getCurrentPlayer().getColour());
                    this.gameArea.getChildren().add(this.gameDraggableRug);
                    this.controlArea.getChildren().addAll(this.btnRotateRug, this.btnConfirmPlacement);
                } else {
                    this.gameState.makePayment();
                    if (!gameState.isGameOver()) {
                        this.gameState.removeCurrentPlayer();
                        this.gameState.nextPlayer();
                        nextPhase();
                        // computer player and human player act
                        simulatePlayerAct();
                    }
                }
                // update players statement
                updatePlayerStats();
            }
        });

        // players rotate rug
        this.btnRotateRug.setOnMouseClicked(event -> rotateRug());

        // confirm rugs placement
        this.btnConfirmPlacement.setOnMouseClicked(event -> makePlacement());

        Scene scene = new Scene(gameArea, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.Q || event.getCode() == KeyCode.E) {
                if (this.currentPhase == Phase.PLACEMENT) {
                    rotateRug();
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

    /**
     *
     */
    private void rotateRug() {
        if (this.gameDraggableRug != null) {
            this.gameDraggableRug.setRotate(this.gameDraggableRug.getRotate() + 90);
            if (this.gameDraggableRug.getOrientation() == Orientation.VERTICAL) {
                this.gameDraggableRug.setOrientation(Orientation.HORIZONTAL);
            } else {
                this.gameDraggableRug.setOrientation(Orientation.VERTICAL);
            }
            if (this.highlighted != null) {
                this.highlighted.setOpacity(0);
                this.highlighted = null;
            }
        }
    }

    private void makePlacement() {
        if (this.highlighted != null) {
            this.controlArea.getChildren().removeAll(this.btnRotateRug, this.btnConfirmPlacement);
            this.gameArea.getChildren().remove(this.gameDraggableRug);

            Rug rug = new Rug(this.gameDraggableRug.getColour(), this.rugID++, getTilesFromHighlighted());
            this.gameState.makePlacement(rug);
            GameRug gameRug = new GameRug(this.highlighted.getLayoutX(), this.highlighted.getLayoutY(), this.gameDraggableRug.getOrientation(), this.gameDraggableRug.getColour());
            this.placedRugs.getChildren().add(gameRug);

            this.highlighted.setOpacity(0);
            this.highlighted = null;
            this.gameDraggableRug = null;

            if (!this.gameState.isGameOver()) {
                nextPhase();
                this.gameState.nextPlayer();
                // computer player and human player act
                simulatePlayerAct();
                // update player stats
                updatePlayerStats();
            }
        }
    }

    private void simulatePlayerAct() {
        if (this.gameState.getCurrentPlayer().isComputer()) {
            // computer player
            this.controlArea.getChildren().addAll(this.btnRotations);
            this.controlArea.getChildren().add(this.btnConfirmRotation);
            this.btnRotations.forEach(e -> e.setDisable(true));
            this.btnConfirmRotation.setDisable(true);
            this.btnConfirmRotation.fireEvent(clickEvent);
            this.btnRollDie.fireEvent(clickEvent);
            this.btnMoveAssam.fireEvent(clickEvent);
            this.btnConfirmPayment.fireEvent(clickEvent);
            this.gameDraggableRug.fireEvent(pressEvent);
            this.gameDraggableRug.fireEvent(dragEvent);
            this.gameDraggableRug.fireEvent(releaseEvent);
            this.btnConfirmPlacement.fireEvent(clickEvent);
        } else {
            // human player
            this.controlArea.getChildren().addAll(this.btnRotations);
            this.controlArea.getChildren().add(this.btnConfirmRotation);
            this.btnRotations.forEach(e -> e.setDisable(false));
            this.btnConfirmRotation.setDisable(false);
        }
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
                GameInvisibleRug rug = new GameInvisibleRug(j * TILE_SIDE + TILE_SIDE / 2, i * TILE_SIDE + TILE_SIDE, gameTiles, Orientation.VERTICAL);
                this.vGameInvisibleRugs.add(rug);
                this.invisibleRugs.getChildren().add(rug);
            }
        }

        for (int i = 0; i < NUM_OF_ROWS; i++) {
            for (int j = 0; j < NUM_OF_COLS - 1; j++) {
                GameTile[] gameTiles = new GameTile[]{
                        this.gameTiles[i][j], this.gameTiles[i][j + 1]
                };
                GameInvisibleRug rug = new GameInvisibleRug(j * TILE_SIDE + TILE_SIDE, i * TILE_SIDE + TILE_SIDE / 2, gameTiles, Orientation.HORIZONTAL);
                this.hGameInvisibleRugs.add(rug);
                this.invisibleRugs.getChildren().add(rug);
            }
        }
    }

    private void initAssam() {
        SVGPath svg = new SVGPath();
        svg.setContent(ASSAM_SVG);
        this.assam = new Region();
        this.assam.setShape(svg);
        this.assam.setMinSize(ASSAM_SIDE, ASSAM_SIDE);
        this.assam.setMaxSize(ASSAM_SIDE, ASSAM_SIDE);
        this.assam.setBackground(new Background(new BackgroundFill(ASSAM_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        this.assam.setBorder(new Border(new BorderStroke(ASSAM_COLOR.darker(), GAME_PANE_BORDER_STROKE_STYLE, GAME_PANE_BORDER_RADII, GAME_PANE_BORDER_WIDTH)));
        updateAssam();
    }

    private void updateAssam() {
        Tile assamTile = this.gameState.getBoard().getAssamTile();
        int row = assamTile.getRow();
        int col = assamTile.getCol();
        this.assam.relocate(ASSAM_RELOCATION + col * TILE_SIDE, ASSAM_RELOCATION + row * TILE_SIDE);
        this.assam.setRotate(getAssamAngle());
    }

    private int getAssamAngle() {
        return this.gameState.getBoard().getAssamDirection().getAngle();
    }

    /**
     * Update player stats
     */
    private void updatePlayerStats() {
        eachPlayerStatArea.getChildren().clear();
        eachPlayerStatArea.setSpacing(5);
        for (Player player : this.tmp) {
            if (player.isComputer()) {
                Text colour = new Text("Computer Player: " + player.getColour());
                colour.setFont(new Font(16));
                eachPlayerStatArea.getChildren().add(colour);
            } else {
                Text colour = new Text("Player: " + player.getColour());
                colour.setFont(new Font(16));
                eachPlayerStatArea.getChildren().add(colour);
            }
            Text dirham = new Text("Dirham: " + player.getDirham());
            dirham.setFont(new Font(16));
            Text numOfUnplacedRugs = new Text("Number of unplaced rugs: " + player.getNumOfUnplacedRugs());
            numOfUnplacedRugs.setFont(new Font(16));
            eachPlayerStatArea.getChildren().addAll(dirham, numOfUnplacedRugs);
        }
    }

    private class PlayerSelector extends Pane {
        private Player player;
        private final CheckBox chbIsComputer;
        private final GameColourButton gameColourButton;
        private final Label lblStrategy;
        private final RadioButton rbRandom;
        private final RadioButton rbIntelligent;
        private final ToggleGroup toggleGroup;

        public PlayerSelector(double width, double height, Colour colour) {
            super();
            this.setMinSize(width, height);
            this.setMaxSize(width, height);

            this.gameColourButton = new GameColourButton(colour);
            this.getChildren().add(this.gameColourButton);

            this.chbIsComputer = new CheckBox();
            this.chbIsComputer.setText("Computer");
            this.chbIsComputer.relocate(0, COLOUR_BUTTON_RADIUS * 2.2);

            this.lblStrategy = new Label("Strategy:");
            this.lblStrategy.relocate(0, COLOUR_BUTTON_RADIUS * 2.6);

            this.toggleGroup = new ToggleGroup();
            this.rbRandom = new RadioButton("Random");
            this.rbRandom.relocate(0, COLOUR_BUTTON_RADIUS * 3.0);
            this.rbRandom.setToggleGroup(this.toggleGroup);
            this.rbIntelligent = new RadioButton("Intelligent");
            this.rbIntelligent.relocate(0, COLOUR_BUTTON_RADIUS * 3.4);
            this.rbIntelligent.setToggleGroup(this.toggleGroup);
            this.rbIntelligent.setDisable(true);

            this.gameColourButton.setOnMouseClicked(event -> {
                this.gameColourButton.setDisable(true);
                this.gameColourButton.addBorder();
                this.getChildren().add(this.chbIsComputer);

                this.player = new Player(colour);
                tmp.add(this.player);
                if (tmp.size() == numOfPlayers) {
                    // When the appropriate number of players has been selected, disable the other colors.
                    playerSelectors.forEach(p -> p.gameColourButton.setDisable(true));
                    // Once players have finished selecting, then they can click Confirm
                    btnColourConfirm.setDisable(false);
                    btnColourConfirm.requestFocus();
                }
            });

            this.chbIsComputer.setOnMouseClicked(event -> {
                if (this.chbIsComputer.isSelected()) {
                    this.player.setIsComputer(true);
                    this.player.setStrategy(Strategy.RANDOM);
                    this.rbRandom.setSelected(true);
                    this.getChildren().addAll(this.lblStrategy, this.rbRandom, this.rbIntelligent);
                }
                if (!this.chbIsComputer.isSelected()) {
                    this.player.setIsComputer(false);
                    this.player.setStrategy(null);
                    this.getChildren().removeAll(this.lblStrategy, this.rbRandom, this.rbIntelligent);
                }
            });

            this.rbRandom.setOnMouseClicked(event -> {
                this.player.setStrategy(Strategy.RANDOM);
            });

            this.rbIntelligent.setOnMouseClicked(event -> {
                this.player.setStrategy(Strategy.INTELLIGENT);
            });
        }
    }

    private enum Phase {
        ROTATION,
        MOVEMENT,
        PLACEMENT;

        @Override
        public String toString() {
            return switch (this) {
                case ROTATION -> "ROTATION";
                case MOVEMENT -> "MOVEMENT";
                case PLACEMENT -> "PLACEMENT";
            };
        }
    }

    private void nextPhase() {
        switch (this.currentPhase) {
            case ROTATION -> this.currentPhase = Phase.MOVEMENT;
            case MOVEMENT -> this.currentPhase = Phase.PLACEMENT;
            case PLACEMENT -> this.currentPhase = Phase.ROTATION;
        }
        this.phaseText.setText("Current phase of game: " + this.currentPhase);
    }

    enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    Tile[] getTilesFromHighlighted() {
        if (this.highlighted != null) {
            return new Tile[]{getTileFromGameTile(this.highlighted.getGameTiles()[0]), getTileFromGameTile(this.highlighted.getGameTiles()[1])};
        }
        return null;
    }

    Tile[] getTilesFromInvisibleRug(GameInvisibleRug gameInvisibleRug) {
        return new Tile[]{getTileFromGameTile(gameInvisibleRug.getGameTiles()[0]), getTileFromGameTile(gameInvisibleRug.getGameTiles()[1])};
    }

    Tile getTileFromGameTile(GameTile gameTile) {
        return this.gameState.getBoard().getTiles()[gameTile.getRow()][gameTile.getCol()];
    }
}
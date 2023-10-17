package comp1110.ass2.gui;

import comp1110.ass2.GameState;
import comp1110.ass2.board.Tile;
import comp1110.ass2.player.Colour;
import comp1110.ass2.player.Die;
import comp1110.ass2.player.Player;
import comp1110.ass2.player.Rug;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
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
    private static final double MARGIN_LEFT = 120;
    private static final double MARGIN_TOP = 30;
    private static final double BOARD_AREA_SIDE = WINDOW_HEIGHT - 2 * MARGIN_TOP;
    private static final double PLAYER_AREA_WIDTH = WINDOW_WIDTH - BOARD_AREA_SIDE - MARGIN_LEFT - 2 * MARGIN_TOP;
    private static final double PLAYER_AREA_HEIGHT = BOARD_AREA_SIDE;
    private static final double STATS_AREA_WIDTH = PLAYER_AREA_WIDTH;
    private static final double STATS_AREA_HEIGHT = 400;
    private static final double CONTROL_AREA_WIDTH = PLAYER_AREA_WIDTH;
    private static final double CONTROL_AREA_HEIGHT = PLAYER_AREA_HEIGHT - STATS_AREA_HEIGHT - MARGIN_TOP;
    private static final Color TILE_COLOR = Color.TAN;
    private static final Color GAME_PANE_BORDER_COLOR = TILE_COLOR.darker();
    private static final BorderStrokeStyle GAME_PANE_BORDER_STROKE_STYLE = BorderStrokeStyle.SOLID;
    private static final CornerRadii GAME_PANE_BORDER_RADII = new CornerRadii(24);
    private static final BorderWidths GAME_PANE_BORDER_WIDTH = new BorderWidths(4);
    private static final int NUM_OF_ROWS = 7;
    private static final int NUM_OF_COLS = 7;
    private static final double TILE_SIDE = 64;
    private static final double TILE_RELOCATION_X = (BOARD_AREA_SIDE - NUM_OF_COLS * TILE_SIDE) / 2;
    private static final double TILE_RELOCATION_Y = (BOARD_AREA_SIDE - NUM_OF_ROWS * TILE_SIDE) / 2;
    private static final int TILE_BORDER_WIDTH = 4;
    private static final int RUG_BORDER_WIDTH = 4;
    private static final int BUTTON_WIDTH = 120;
    private static final int BUTTON_HEIGHT = 60;
    private static final int COLOUR_BUTTON_RADIUS = 60;
    private static final BorderStrokeStyle COLOUR_BUTTON_BORDER_STROKE_STYLE = GAME_PANE_BORDER_STROKE_STYLE;
    private static final CornerRadii COLOUR_BUTTON_BORDER_RADII = GAME_PANE_BORDER_RADII;
    private static final BorderWidths COLOUR_BUTTON_BORDER_WIDTH = new BorderWidths(8);
    private static final double HIGHLIGHTED_OPACITY = 1 - (Math.sqrt(5) - 1) / 2;
    private static final double ASSAM_SIDE = (Math.sqrt(5) - 1) / 2 * TILE_SIDE;
    private static final double ASSAM_RELOCATION = (TILE_SIDE - ASSAM_SIDE) / 2;
    private static final Color ASSAM_COLOR = Color.SPRINGGREEN.darker();
    //https://fonts.google.com/icons?selected=Material%20Symbols%20Rounded%3Anavigation%3AFILL%401%3Bwght%40400%3BGRAD%400%3Bopsz%4024
    private static final String ASSAM_SVG = "M480-240 222-130q-13 5-24.5 2.5T178-138q-8-8-10.5-20t2.5-25l273-615q5-12 15.5-18t21.5-6q11 0 21.5 6t15.5 18l273 615q5 13 2.5 25T782-138q-8 8-19.5 10.5T738-130L480-240Z";

    private final Pane allTiles = new Pane();
    private final Pane placedRugs = new Pane();
    private final Pane invisibleRugs = new Pane();
    private final GameTile[][] gameTiles = new GameTile[NUM_OF_ROWS][NUM_OF_COLS];
    private final ArrayList<InvisibleRug> vInvisibleRugs = new ArrayList<>();
    private final ArrayList<InvisibleRug> hInvisibleRugs = new ArrayList<>();

    private GamePane gameArea;
    private GamePane controlArea;
    private ArrayList<GameButton> btnRotations;
    private GameButton btnConfirmRotation;
    private GameButton btnRotateRug;
    private GameButton btnConfirmPlacement;

    // number of human players
    private int numOfPlayers;
    // number of computer players
    private int numOfComputerPlayers;
    private Phase currentPhase;
    private Text phaseText;
    private Region assam;
    private int dieResult;
    private InvisibleRug highlighted;
    private DraggableRug draggableRug;
    private int rugID;
    //Keep a temporary list of players
    ArrayList<Player> tmp = new ArrayList<>();
    //Keep a temporary list of computer players
    ArrayList<Player> tmpComputer = new ArrayList<>();
    VBox eachPlayerStatArea = new VBox();

    // all players, including human and computer players
    private Player[] players;
    private GameState gameState;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @param primaryStage
     */
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
        Text humanPlayer = new Text("Choose number of human player");
        humanPlayer.setFont(new Font(18));
        humanPlayer.relocate(WINDOW_WIDTH / 2.0 - BUTTON_WIDTH / 2.0 - 300, 280);
        ChoiceBox<Integer> choiceBox = new ChoiceBox<>();
        choiceBox.setMinWidth(BUTTON_WIDTH);
        choiceBox.setMaxWidth(BUTTON_WIDTH);
        choiceBox.getItems().addAll(1, 2, 3, 4);
        // The default number for human players is 2
        choiceBox.setValue(2);
        choiceBox.relocate(WINDOW_WIDTH / 2.0 - BUTTON_WIDTH / 2.0 - 300, 320);
        // Choice box to choose the number of computer players
        Text computerPlayer = new Text("Choose number of computer player");
        computerPlayer.setFont(new Font(18));
        computerPlayer.relocate(WINDOW_WIDTH / 2.0 - BUTTON_WIDTH / 2.0 + 300, 280);
        ChoiceBox<Integer> choiceComputerBox = new ChoiceBox<>();
        choiceComputerBox.setMinWidth(BUTTON_WIDTH);
        choiceComputerBox.setMaxWidth(BUTTON_WIDTH);
        choiceComputerBox.getItems().addAll(0, 1, 2, 3, 4);
        // The default number for computer players is 0
        choiceComputerBox.setValue(0);
        choiceComputerBox.relocate(WINDOW_WIDTH / 2.0 - BUTTON_WIDTH / 2.0 + 300, 320);
        //Back and Confirm buttons
        GameButton btnNumberBack = new GameButton("Back", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnNumberBack.relocate(BUTTON_HEIGHT / 2.0, BUTTON_HEIGHT / 2.0);
        GameButton btnNumberConfirm = new GameButton("Confirm", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnNumberConfirm.relocate(WINDOW_WIDTH / 2.0 - BUTTON_WIDTH / 2.0, 400);
        btnNumberConfirm.requestFocus();
        // Add all children of numberPane
        numberPane.getChildren().addAll(choiceBox, choiceComputerBox, humanPlayer, computerPlayer, btnNumberBack, btnNumberConfirm);

        // Players choose their colours
        Pane colourPane = new Pane();
        Scene colourScene = new Scene(colourPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        // Buttons for different colours
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
        // Add all children of colourPane
        colourPane.getChildren().addAll(btnCyan, btnYellow, btnRed, btnPurple, btnColourBack, btnColourReset, btnColourConfirm);

        // Add colour to players
        ArrayList<ColourButton> humanColourButtons = new ArrayList<>();
        for (ColourButton colourButton : colourButtons) {
            colourButton.setOnMouseClicked(event -> {
                // Add selected colour to human player
                tmp.add(new Player(colourButton.colour));
                colourButton.setBorder(colourButton.border);
                colourButton.setDisable(true);
                humanColourButtons.add(colourButton);
                if (tmp.size() == this.numOfPlayers) {
                    // When the color for the number of players has been selected, disable the other colors.
                    colourButtons.forEach(b -> b.setDisable(true));
                    // Clone colourButtons
                    ArrayList<ColourButton> tmpColourButtons = new ArrayList<>(colourButtons);
                    tmpColourButtons.removeAll(humanColourButtons);
                    // Once the human player has finished selecting, then they can click Confirm
                    btnColourConfirm.setDisable(false);
                    btnColourConfirm.requestFocus();
                    // Existing computer player
                    if (this.numOfComputerPlayers > 0) {
                        // Assign the remaining colours to the computer players
                        for (int i = 0; i < numOfComputerPlayers; i++) {
                            tmpComputer.add(new Player(tmpColourButtons.get(i).colour));
                        }
                    }
                }
            });
        }

        // Start button on the homepage
        btnStart.setOnMouseClicked(event -> {
            primaryStage.setScene(numberScene);
            btnNumberConfirm.requestFocus();
        });

        // Back button on choose player number scene
        btnNumberBack.setOnMouseClicked(event -> {
            // Initialize human players and computer players
            this.numOfPlayers = 0;
            this.numOfComputerPlayers = 0;
            choiceBox.setValue(2);
            choiceComputerBox.setValue(0);
            primaryStage.setScene(titleScene);
        });

        // Confirm button on choose player number scene
        btnNumberConfirm.setOnMouseClicked(event -> {
            this.numOfPlayers = choiceBox.getValue();
            this.numOfComputerPlayers = choiceComputerBox.getValue();
            // Total number of human player + computer players cannot exceed 4
            if (this.numOfPlayers + numOfComputerPlayers > 4) {
                // Create a dialog box
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning!");
                alert.setHeaderText("Too many players!");
                alert.setContentText("The total number of players and computer players \ncannot exceed 4!");
                // Displays the dialog box and waits for the user to close it
                alert.showAndWait();
            }
            // If there is no computer player, human players should at least 2
            else if (this.numOfPlayers == 1 && this.numOfComputerPlayers == 0) {
                // Create a dialog box
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning!");
                alert.setHeaderText("Too few players!");
                alert.setContentText("You should choose at least two players");
                // Displays the dialog box and waits for the user to close it
                alert.showAndWait();
            } else {
                primaryStage.setScene(colourScene);
                btnColourReset.requestFocus();
            }

        });

        // Click back button on choose colour scene
        btnColourBack.setOnMouseClicked(event -> {
            // Initialize human players and computer players
            tmp.clear();
            tmpComputer.clear();
            btnColourConfirm.setDisable(true);
            colourButtons.forEach(b -> {
                b.setDisable(false);
                b.setBorder(null);
            });
            primaryStage.setScene(numberScene);
        });

        // Reset color on choose colour scene
        btnColourReset.setOnMouseClicked(event -> {
            tmp.clear();
            tmpComputer.clear();
            btnColourConfirm.setDisable(true);
            colourButtons.forEach(b -> {
                b.setDisable(false);
                b.setBorder(null);
            });
        });

        // Confirm selected colour on choose colour scene
        btnColourConfirm.setOnMouseClicked(event -> {
            // set computer players attribute
            if (this.numOfComputerPlayers > 0) {
                tmpComputer.forEach(t -> t.setIsComputer());
            }
            tmp.addAll(tmpComputer);
            this.players = tmp.toArray(new Player[0]);
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
        updatePlayerStatement();
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
        this.btnConfirmRotation = new GameButton("Confirm Rotation", BUTTON_WIDTH * 1.2, BUTTON_HEIGHT);
        btnConfirmRotation.relocate(BUTTON_WIDTH, BUTTON_HEIGHT * 2);

        GameButton btnRollDie = new GameButton("Roll Die", BUTTON_WIDTH * 1.2, BUTTON_HEIGHT);
        btnRollDie.relocate(BUTTON_WIDTH, BUTTON_HEIGHT * 2);

        Text movementText = new Text();
        movementText.relocate(BUTTON_WIDTH, BUTTON_HEIGHT);
        GameButton btnMoveAssam = new GameButton("Confirm Movement", BUTTON_WIDTH * 1.2, BUTTON_HEIGHT);
        btnMoveAssam.relocate(BUTTON_WIDTH, BUTTON_HEIGHT * 2);

        Text paymentText = new Text();
        paymentText.relocate(BUTTON_WIDTH, BUTTON_HEIGHT);
        paymentText.setWrappingWidth(300);
        GameButton btnConfirmPayment = new GameButton("Proceed", BUTTON_WIDTH * 1.2, BUTTON_HEIGHT);
        btnConfirmPayment.relocate(BUTTON_WIDTH, BUTTON_HEIGHT * 2);

        this.btnRotateRug = new GameButton("Rotate Rug", BUTTON_WIDTH * 1.2, BUTTON_HEIGHT);
        this.btnRotateRug.relocate(BUTTON_WIDTH * 1.5, BUTTON_HEIGHT / 2.0);
        this.btnConfirmPlacement = new GameButton("Confirm Placement", BUTTON_WIDTH * 1.2, BUTTON_HEIGHT);
        this.btnConfirmPlacement.relocate(BUTTON_WIDTH * 1.5, BUTTON_HEIGHT * 2);

        //allows player to set Assam's initial direction
        this.controlArea.getChildren().addAll(btnInitialRotation, btnConfirmInitialRotation);

        btnInitialRotation.setOnMouseClicked(event -> {
            this.assam.setRotate(this.assam.getRotate() + 90);
        });

        btnConfirmInitialRotation.setOnMouseClicked(event -> {
            if (currentPhase == Phase.ROTATION) {
                nextPhase();
                this.controlArea.getChildren().removeAll(btnInitialRotation, btnConfirmInitialRotation);
                this.gameState.rotateAssam((int) this.assam.getRotate() - getAssamAngle());
                this.controlArea.getChildren().add(btnRollDie);
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
                this.controlArea.getChildren().add(btnRollDie);
            }
        });

        // roll die
        btnRollDie.setOnMouseClicked(event -> {
            this.controlArea.getChildren().remove(btnRollDie);
            this.dieResult = Die.getSide();
            this.controlArea.getChildren().addAll(movementText, btnMoveAssam);
            movementText.setText("Die result is " + this.dieResult + ". Assam will now move " + this.dieResult + " steps.");
            movementText.setFont(new Font(16));
        });

        // make Assam move
        btnMoveAssam.setOnMouseClicked(event -> {
            this.controlArea.getChildren().removeAll(movementText, btnMoveAssam);
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
        btnConfirmPayment.setOnMouseClicked(event -> {
            if (this.currentPhase == Phase.MOVEMENT) {
                nextPhase();
                this.controlArea.getChildren().removeAll(paymentText, btnConfirmPayment);
                if (!this.gameState.isPaymentRequired() || this.gameState.isPaymentAffordable()) {
                    this.gameState.makePayment();
                    this.draggableRug = new DraggableRug(MARGIN_LEFT + BOARD_AREA_SIDE + MARGIN_TOP + BUTTON_WIDTH * 0.5, 500, this.gameState.getCurrentPlayer().getColour());
                    this.gameArea.getChildren().add(this.draggableRug);
                    this.controlArea.getChildren().addAll(this.btnRotateRug, this.btnConfirmPlacement);
                } else {
                    this.gameState.makePayment();
                    if (!gameState.isGameOver()) {
                        this.gameState.removeCurrentPlayer();
                        this.gameState.nextPlayer();
                        nextPhase();
                        this.controlArea.getChildren().addAll(this.btnRotations);
                        this.controlArea.getChildren().add(this.btnConfirmRotation);
                    }
                }
                // update players statement
                updatePlayerStatement();
            }
        });

        // players rotate rug
        btnRotateRug.setOnMouseClicked(event -> rotateRug());

        // confirm rugs placement
        btnConfirmPlacement.setOnMouseClicked(event -> makePlacement());

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
        System.out.println(this.gameState.getCurrentPlayer().isComputer());
        if (this.highlighted != null) {
            this.controlArea.getChildren().removeAll(this.btnRotateRug, this.btnConfirmPlacement);
            this.gameArea.getChildren().remove(this.draggableRug);

            Rug rug = new Rug(this.draggableRug.colour, this.rugID++, getTilesFromHighlighted());
            this.gameState.makePlacement(rug);
            GameRug gameRug = new GameRug(this.highlighted.getLayoutX(), this.highlighted.getLayoutY(), this.draggableRug.orientation, this.draggableRug.colour);
            this.placedRugs.getChildren().add(gameRug);

            this.highlighted.setOpacity(0);
            this.highlighted = null;
            this.draggableRug = null;

            if (!gameState.isGameOver()) {
                nextPhase();
                gameState.nextPlayer();
                this.controlArea.getChildren().addAll(this.btnRotations);
                this.controlArea.getChildren().add(this.btnConfirmRotation);
                // update players statement
                updatePlayerStatement();
            }
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
     * Update player statement
     */
    private void updatePlayerStatement() {
        eachPlayerStatArea.getChildren().clear();
        eachPlayerStatArea.setSpacing(5);
        for (int i = 0; i < this.tmp.size(); i++) {
            if (this.tmp.get(i).isComputer()) {
                Text colour = new Text("Computer Player: " + this.tmp.get(i).getColour());
                colour.setFont(new Font(16));
                eachPlayerStatArea.getChildren().add(colour);
            } else {
                Text colour = new Text("Player: " + this.tmp.get(i).getColour());
                colour.setFont(new Font(16));
                eachPlayerStatArea.getChildren().add(colour);
            }
            Text dirham = new Text("Dirham: " + Integer.toString(this.tmp.get(i).getDirham()));
            dirham.setFont(new Font(16));
            Text numOfUnplacedRugs = new Text("Number of unplaced rugs: " + Integer.toString(this.tmp.get(i).getNumOfUnplacedRugs()));
            numOfUnplacedRugs.setFont(new Font(16));
            eachPlayerStatArea.getChildren().addAll(dirham, numOfUnplacedRugs);
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
                    this.setLayoutX(nearestInvisibleRug.getLayoutX() + TILE_RELOCATION_X + MARGIN_LEFT);
                    this.setLayoutY(nearestInvisibleRug.getLayoutY() + TILE_RELOCATION_Y + MARGIN_TOP);
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
                            rug1.distance(DraggableRug.this.getLayoutX() - TILE_RELOCATION_X - MARGIN_LEFT, DraggableRug.this.getLayoutY() - TILE_RELOCATION_Y - MARGIN_TOP),
                            rug2.distance(DraggableRug.this.getLayoutX() - TILE_RELOCATION_X - MARGIN_LEFT, DraggableRug.this.getLayoutY() - TILE_RELOCATION_Y - MARGIN_TOP));
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
        this.phaseText.setText("Current phase of game: " + this.currentPhase.toString());
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
package comp1110.ass2.gui;

import comp1110.ass2.GameState;
import comp1110.ass2.board.Board;
import comp1110.ass2.board.Tile;
import comp1110.ass2.player.Colour;
import comp1110.ass2.board.Die;
import comp1110.ass2.player.Player;
import comp1110.ass2.player.Player.Strategy;
import comp1110.ass2.player.Rug;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private static final Color GAME_PANE_BORDER_COLOR = TILE_COLOR.darker().darker();
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
    private static final BorderWidths ASSAM_BORDER_WIDTH = new BorderWidths(4);
    private static final double ASSAM_SIDE = (Math.sqrt(5) - 1) / 2 * TILE_SIDE;
    private static final double ASSAM_RELOCATION = (TILE_SIDE - ASSAM_SIDE) / 2;
    private static final Color ASSAM_COLOR = Color.SPRINGGREEN.darker();
    //https://fonts.google.com/icons?selected=Material%20Symbols%20Rounded%3Anavigation%3AFILL%401%3Bwght%40400%3BGRAD%400%3Bopsz%4024
    private static final String ASSAM_SVG = "M480-240 222-130q-13 5-24.5 2.5T178-138q-8-8-10.5-20t2.5-25l273-615q5-12 15.5-18t21.5-6q11 0 21.5 6t15.5 18l273 615q5 13 2.5 25T782-138q-8 8-19.5 10.5T738-130L480-240Z";
    private static final Font GENERAL_TEXT_FONT_REGULAR = Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 14);
    private static final Font GENERAL_TEXT_FONT_ITALIC = Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 14);
    private static final Font PLAYER_COLOUR_TEXT_FONT_REGULAR = Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 18);
    private static final String TEXT_FILL = "#3F301D";

    private final Pane allTiles = new Pane();
    private Pane placedRugs = new Pane();
    private final Pane invisibleRugs = new Pane();
    private final GameTile[][] gameTiles = new GameTile[NUM_OF_ROWS][NUM_OF_COLS];
    // Vertical invisible rugs
    final ArrayList<GameInvisibleRug> vGameInvisibleRugs = new ArrayList<>();
    // Horizontal invisible rugs
    final ArrayList<GameInvisibleRug> hGameInvisibleRugs = new ArrayList<>();
    private Group mosaic = new Group();
    private GamePane gameArea;
    // Control area on game scene, with player operated button and information
    private GamePane controlArea;
    Button btnMainBack = CircularImageButton.createCircularImageButton("resources/back.png");
    Button hintButton = CircularImageButton.createCircularImageButton("resources/problem.png");
    Button restart = CircularImageButton.createCircularImageButton("resources/restart.png");
    // Number of human and computer players
    private int numOfPlayers;

    private Phase currentPhase;
    private final Text phaseText = new Text();
    private Region assam;
    private int dieResult;
    GameInvisibleRug highlighted;
    GameDraggableRug gameDraggableRug;
    int rugID;
    // Keep a temporary list of players
    private final ArrayList<Player> tmp = new ArrayList<>();
    private ArrayList<PlayerSelector> playerSelectors;

    private final VBox playerStatsVBox = new VBox();

    private final GameButton btnNumberConfirm = new GameButton("Confirm", BUTTON_WIDTH, BUTTON_HEIGHT);
    private final GameButton btnColourConfirm = new GameButton("Confirm", BUTTON_WIDTH, BUTTON_HEIGHT);

    private final GameButton btnInitialRotation = new GameButton("Rotate Right", BUTTON_WIDTH * 1.2, BUTTON_HEIGHT);
    private final GameButton btnConfirmInitialRotation = new GameButton("Confirm", BUTTON_WIDTH * 1.2, BUTTON_HEIGHT);
    private ArrayList<GameButton> btnRotations;
    private final GameButton btnConfirmRotation = new GameButton("Confirm", BUTTON_WIDTH * 1.2, BUTTON_HEIGHT);
    private final GameButton btnRollDie = new GameButton("Roll Die", BUTTON_WIDTH * 1.2, BUTTON_HEIGHT);
    private final GameButton btnMoveAssam = new GameButton("Proceed", BUTTON_WIDTH * 1.2, BUTTON_HEIGHT);
    private final GameButton btnConfirmPayment = new GameButton("Proceed", BUTTON_WIDTH * 1.2, BUTTON_HEIGHT);
    private final GameButton btnRotateRug = new GameButton("Rotate", BUTTON_WIDTH * 1.2, BUTTON_HEIGHT);
    private final GameButton btnConfirmPlacement = new GameButton("Confirm", BUTTON_WIDTH * 1.2, BUTTON_HEIGHT);

    private final Text movementText = new Text();
    private final Text paymentText = new Text();

    // All players, including human and computer players
    private Player[] players;
    GameState gameState;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes and displays the primary stage for the Marrakech game.
     * @param primaryStage The primary stage on which the game scenes are displayed.
     * @author u7620014 Haobo Zou, u7582846 Yaolin Li
     */
    @Override
    public void start(Stage primaryStage) {
        // Home page, titleText screen
        StackPane titleRootPane = new StackPane();
        VBox titleVBox = new VBox();
        BackgroundImage background = drawBackground();
        titleRootPane.setBackground(new Background(background));

        Text titleText = new Text("Marrakech");
        titleText.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 60));
        titleText.setStyle("-fx-fill:" + TEXT_FILL);
        GameButton btnStart = new GameButton("Start", BUTTON_WIDTH, BUTTON_HEIGHT);
        // Make two elements center vertical
        titleVBox.setAlignment(Pos.CENTER);
        titleVBox.setSpacing(20);
        // Add all children of titleVBox
        titleVBox.getChildren().addAll(titleText, btnStart);
        titleRootPane.getChildren().add(titleVBox);
        Scene titleScene = new Scene(titleRootPane, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Choose number of players
        StackPane numberRootPane = new StackPane();
        Pane numberPane = new Pane();
        numberRootPane.setBackground(new Background(background));
        Scene numberScene = new Scene(numberRootPane, WINDOW_WIDTH, WINDOW_HEIGHT);

        Text numberText = new Text("Please choose the number of players");
        numberText.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 28));
        numberText.setStyle("-fx-fill:" + TEXT_FILL);
        double numberTextWidth = numberText.getLayoutBounds().getWidth();
        numberText.relocate((WINDOW_WIDTH - numberTextWidth) / 2, 260);

        ChoiceBox<Integer> choiceBox = new ChoiceBox<>();
        choiceBox.setMinWidth(BUTTON_WIDTH);
        choiceBox.setMaxWidth(BUTTON_WIDTH);
        choiceBox.getItems().addAll(2, 3, 4);
        // The default number for human players is 2
        choiceBox.setValue(2);
        choiceBox.relocate(WINDOW_WIDTH / 2.0 - BUTTON_WIDTH / 2.0, 320);

        //Back and Confirm buttons
        Button btnNumberBack = CircularImageButton.createCircularImageButton("resources/back.png");
        btnNumberBack.relocate(BUTTON_HEIGHT / 3.0, BUTTON_HEIGHT / 2.0);
        this.btnNumberConfirm.relocate(WINDOW_WIDTH / 2.0 - BUTTON_WIDTH / 2.0, 360);
        this.btnNumberConfirm.requestFocus();

        // Add all children of numberPane
        numberRootPane.getChildren().add(numberPane);
        numberPane.getChildren().addAll(choiceBox, numberText, btnNumberBack, this.btnNumberConfirm);

        // Players choose their colours
        Pane colourPane = new Pane();
        StackPane colourRootPane = new StackPane();
        colourRootPane.setBackground(new Background(background));
        Scene colourScene = new Scene(colourRootPane, WINDOW_WIDTH, WINDOW_HEIGHT);

        this.playerSelectors = makeNewPlayerSelectors();

        //Back, Reset and Confirm buttons
        Button btnColourBack = CircularImageButton.createCircularImageButton("resources/back.png");
        btnColourBack.relocate(BUTTON_HEIGHT / 3.0, BUTTON_HEIGHT / 2.0);
        GameButton btnColourReset = new GameButton("Reset", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnColourReset.relocate(WINDOW_WIDTH / 2.0 - BUTTON_WIDTH * 1.5, 420);
        this.btnColourConfirm.relocate(WINDOW_WIDTH / 2.0 + BUTTON_WIDTH * 0.5, 420);
        this.btnColourConfirm.setDisable(true);

        colourPane.getChildren().addAll(this.playerSelectors);
        colourPane.getChildren().addAll(btnColourBack, btnColourReset, this.btnColourConfirm);
        colourRootPane.getChildren().add(colourPane);

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
            this.btnColourConfirm.setDisable(true);
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
            this.tmp.clear();
            this.rugID = 0;
            //main game
            Scene mainScene = makeMainScene();
            primaryStage.setScene(mainScene);
        });

        // back button on main game scene
        btnMainBack.setOnMouseClicked(event -> {
            this.placedRugs = new Pane();
            this.players = null;
            this.gameState = null;
            colourPane.getChildren().removeAll(this.playerSelectors);
            this.playerSelectors = makeNewPlayerSelectors();
            colourPane.getChildren().addAll(this.playerSelectors);
            this.btnColourConfirm.setDisable(true);
            primaryStage.setScene(colourScene);
        });

        // After game is over, click restart to back to homepage
        restart.setOnMouseClicked(event -> {
            this.placedRugs = new Pane();
            this.players = null;
            this.gameState = null;
            colourPane.getChildren().removeAll(this.playerSelectors);
            this.playerSelectors = makeNewPlayerSelectors();
            colourPane.getChildren().addAll(this.playerSelectors);
            this.btnColourConfirm.setDisable(true);
            primaryStage.setScene(titleScene);
        });

        primaryStage.setResizable(false);
        primaryStage.setTitle("Marrakech");
        primaryStage.setScene(titleScene);
        primaryStage.show();
    }

    /**
     * Creates a list of PlayerSelector objects, each representing a distinct player color.
     * Players can choose their own colour
     * @return ArrayList of PlayerSelector objects with specific colours
     * @author u7620014 Haobo Zou, u7582846 Yaolin Li
     */
    private ArrayList<PlayerSelector> makeNewPlayerSelectors() {
        // Player Selectors for all the players
        PlayerSelector playerCyan = new PlayerSelector(COLOUR_BUTTON_RADIUS * 1.2, COLOUR_BUTTON_RADIUS * 4, Colour.CYAN);
        playerCyan.relocate(4.5 * COLOUR_BUTTON_RADIUS, 180);
        PlayerSelector playerYellow = new PlayerSelector(COLOUR_BUTTON_RADIUS * 1.2, COLOUR_BUTTON_RADIUS * 4, Colour.YELLOW);
        playerYellow.relocate(7.5 * COLOUR_BUTTON_RADIUS, 180);
        PlayerSelector playerRed = new PlayerSelector(COLOUR_BUTTON_RADIUS * 1.2, COLOUR_BUTTON_RADIUS * 4, Colour.RED);
        playerRed.relocate(10.5 * COLOUR_BUTTON_RADIUS, 180);
        PlayerSelector playerPurple = new PlayerSelector(COLOUR_BUTTON_RADIUS * 1.2, COLOUR_BUTTON_RADIUS * 4, Colour.PURPLE);
        playerPurple.relocate(13.5 * COLOUR_BUTTON_RADIUS, 180);

        return new ArrayList<>(List.of(playerCyan, playerYellow, playerRed, playerPurple));
    }

    /**
     * Constructs and initializes the main game scene,
     * including game board, players' stats and control area
     * @return Scene representing the main game area with all its nodes and controls.
     * @author u7620014 Haobo Zou, u7582846 Yaolin Li
     */
    private Scene makeMainScene() {
        // Initialize game phase as rotation phase
        this.currentPhase = Phase.ROTATION;
        if (this.highlighted != null) {
            this.highlighted.setOpacity(0);
            this.highlighted = null;
        }
        this.gameDraggableRug = null;
        this.gameArea = new GamePane(WINDOW_WIDTH, WINDOW_HEIGHT);
        Border gamePaneBorder = new Border(new BorderStroke(GAME_PANE_BORDER_COLOR, GAME_PANE_BORDER_STROKE_STYLE, GAME_PANE_BORDER_RADII, GAME_PANE_BORDER_WIDTH));
        // back button
        btnMainBack.relocate(BUTTON_HEIGHT / 3.0, BUTTON_HEIGHT / 2.0);
        // hint information
        hintButton.relocate(BUTTON_HEIGHT / 3.0, WINDOW_HEIGHT - BUTTON_HEIGHT * 2);
        // Board area to display information bout the board and assam
        final GamePane boardArea = new GamePane(BOARD_AREA_SIDE, BOARD_AREA_SIDE);
        boardArea.setBorder(gamePaneBorder);
        // background image
        ImageView imageView = drawGameBoardBackground(BOARD_AREA_SIDE, BOARD_AREA_SIDE);
        imageView.relocate(MARGIN_LEFT, MARGIN_TOP);
        gameArea.getChildren().add(imageView);
        boardArea.relocate(MARGIN_LEFT, MARGIN_TOP);
        this.gameArea.getChildren().addAll(btnMainBack, hintButton, boardArea);

        final Pane tileArea = new GamePane(NUM_OF_COLS * TILE_SIDE, NUM_OF_ROWS * TILE_SIDE);
        tileArea.relocate(TILE_RELOCATION_X, TILE_RELOCATION_Y);

        initTiles();
        initInvisibleRugs();
        initAssam();
        tileArea.getChildren().addAll(this.allTiles, this.placedRugs, this.invisibleRugs, this.assam);
        boardArea.getChildren().add(tileArea);

        // Display area to display stats and controls for the players, contains stats area and control area
        final GamePane playerArea = new GamePane(PLAYER_AREA_WIDTH, PLAYER_AREA_HEIGHT);
        ImageView playerView = drawGameBoardBackground(STATS_AREA_WIDTH, STATS_AREA_HEIGHT);
        playerArea.relocate(MARGIN_LEFT + BOARD_AREA_SIDE + MARGIN_TOP, MARGIN_TOP);
        this.gameArea.getChildren().add(playerArea);
        playerArea.getChildren().add(playerView);

        // Stats area, includes current game phase and player stats
        final GamePane statsArea = new GamePane(STATS_AREA_WIDTH, STATS_AREA_HEIGHT);
        statsArea.setBorder(gamePaneBorder);
        playerArea.getChildren().add(statsArea);

        // Add styles for elements inside stats area
        this.phaseText.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 18));
        this.phaseText.setStyle("-fx-fill:" + TEXT_FILL);
        this.phaseText.relocate(20, 20);
        this.playerStatsVBox.setSpacing(4);
        this.playerStatsVBox.relocate(20, 80);

        // Update stats area
        updateStatsArea();
        statsArea.getChildren().addAll(phaseText, this.playerStatsVBox);

        // Control area on game scene, with player operated button and information
        this.controlArea = new GamePane(CONTROL_AREA_WIDTH, CONTROL_AREA_HEIGHT);
        ImageView controlView = drawGameBoardBackground(CONTROL_AREA_WIDTH, CONTROL_AREA_HEIGHT);
        controlView.relocate(0, STATS_AREA_HEIGHT + MARGIN_TOP);
        this.controlArea.setBorder(gamePaneBorder);
        this.controlArea.relocate(0, STATS_AREA_HEIGHT + MARGIN_TOP);
        playerArea.getChildren().addAll(controlView, controlArea);

        // Buttons inside control area
        this.btnInitialRotation.relocate(BUTTON_WIDTH, BUTTON_HEIGHT / 2.0);
        this.btnConfirmInitialRotation.relocate(BUTTON_WIDTH, BUTTON_HEIGHT * 2);

        GameButton btnRotateLeft = new GameButton("Rotate Left", BUTTON_WIDTH * 1.4, BUTTON_HEIGHT);
        btnRotateLeft.relocate(BUTTON_WIDTH * 0.1, BUTTON_HEIGHT / 2.0);
        GameButton btnRotateRight = new GameButton("Rotate Right", BUTTON_WIDTH * 1.4, BUTTON_HEIGHT);
        btnRotateRight.relocate(BUTTON_WIDTH * 1.67, BUTTON_HEIGHT / 2.0);
        this.btnRotations = new ArrayList<>(List.of(btnRotateLeft, btnRotateRight));
        this.btnConfirmRotation.relocate(BUTTON_WIDTH, BUTTON_HEIGHT * 2);

        this.btnRollDie.relocate(BUTTON_WIDTH, BUTTON_HEIGHT * 2);

        this.movementText.relocate(BUTTON_WIDTH * 0.4, BUTTON_HEIGHT * 0.6);
        this.movementText.setFont(GENERAL_TEXT_FONT_ITALIC);
        this.movementText.setStyle("-fx-fill:" + TEXT_FILL);
        this.movementText.setWrappingWidth(300);
        this.btnMoveAssam.relocate(BUTTON_WIDTH, BUTTON_HEIGHT * 2);

        this.paymentText.relocate(BUTTON_WIDTH * 0.4, BUTTON_HEIGHT * 0.6);
        this.paymentText.setFont(GENERAL_TEXT_FONT_ITALIC);
        this.paymentText.setStyle("-fx-fill:" + TEXT_FILL);
        this.paymentText.setWrappingWidth(300);
        this.btnConfirmPayment.relocate(BUTTON_WIDTH, BUTTON_HEIGHT * 2);

        this.btnRotateRug.relocate(BUTTON_WIDTH * 1.5, BUTTON_HEIGHT / 2.0);
        this.btnConfirmPlacement.relocate(BUTTON_WIDTH * 1.5, BUTTON_HEIGHT * 2);

        //allows player or computer to set Assam's initial direction
        if (this.gameState.getCurrentPlayer().isComputer()) {
            // For computer player's turn, disable button
            this.btnMainBack.setDisable(true);
            this.hintButton.setDisable(true);
            this.btnInitialRotation.setDisable(true);
            this.btnConfirmInitialRotation.setDisable(true);
            simulateInitialRotation();
        }
        this.controlArea.getChildren().addAll(this.btnInitialRotation, this.btnConfirmInitialRotation);

        // Click hint button to display game rules
        hintButton.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Hint");
            // Modify the default icon
            ImageView newIcon = new ImageView(new Image("resources/hint.png"));
            newIcon.setFitWidth(40);
            newIcon.setFitHeight(40);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("resources/hint.png"));
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setGraphic(newIcon);
            // Set background colour of dialog
            dialogPane.setStyle("-fx-background-color: #eecdab; -fx-padding: 15px;");
            dialogPane.setPrefSize(400, 250);
            // Different hints in different phases
            alert.setHeaderText("In " + currentPhase + " phase");
            if (currentPhase == Phase.ROTATION) {
                alert.setContentText("You can use 'Rotate Left' and 'Rotate Right' to rotate direction of Assam or current rug, " +
                        "then click 'Confirm.");
            } else if (currentPhase == Phase.MOVEMENT) {
                alert.setContentText("You can 'Roll Die' to make Assam move, " +
                        "if Assam lands on the rug of another player who is still in the game," +
                        " you need to pay that player a certain amount of dirhams.");
            } else if (currentPhase == Phase.PLACEMENT) {
                alert.setContentText("You can rotate and drag the rug onto the board, " +
                        "trying to make your rug occupy as much space as possible.");
            }
            // Set style of Alert dialogPane
            Platform.runLater(() -> {
                dialogPane.lookup(".label").setStyle("-fx-font-size: 18px; -fx-font-weight: bold;" + "-fx-text-fill:" + TEXT_FILL);
                dialogPane.lookup(".button").setStyle("-fx-text-fill: white;-fx-background-color: #672F09;");
                dialogPane.lookup(".header-panel").setStyle("-fx-background-color: #eecdab");
                dialogPane.lookup(".content").setStyle("-fx-font-size: 16px;" + "-fx-text-fill:" + TEXT_FILL);
            });
            alert.showAndWait();
        });

        this.btnInitialRotation.setOnMouseClicked(event -> this.assam.setRotate(this.assam.getRotate() + 90));

        this.btnConfirmInitialRotation.setOnMouseClicked(event -> {
            if (this.currentPhase == Phase.ROTATION) {
                this.controlArea.getChildren().removeAll(btnInitialRotation, btnConfirmInitialRotation);
                this.gameState.rotateAssam((int) this.assam.getRotate() - getAssamAngle());
                nextPhase();
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
                this.controlArea.getChildren().removeAll(this.btnRotations);
                this.controlArea.getChildren().remove(this.btnConfirmRotation);
                this.gameState.rotateAssam((int) this.assam.getRotate() - getAssamAngle());
                nextPhase();
                this.controlArea.getChildren().add(this.btnRollDie);
            }
        });

        // roll die
        this.btnRollDie.setOnMouseClicked(event -> {
            this.controlArea.getChildren().remove(this.btnRollDie);
            this.dieResult = Die.getSide();
            this.movementText.setText("Die result is " + this.dieResult + ". Assam will now move " + this.dieResult + " steps.");
            this.controlArea.getChildren().addAll(movementText, this.btnMoveAssam);
        });

        // make Assam move
        this.btnMoveAssam.setOnMouseClicked(event -> {
            this.controlArea.getChildren().removeAll(this.movementText, this.btnMoveAssam);
            this.gameState.moveAssam(this.dieResult);
            updateAssam();
            if (!this.gameState.isPaymentRequired()) {
                paymentText.setText("No payment required.");
            } else if (this.gameState.isPaymentAffordable()) {
                paymentText.setText("You will need to pay Player " + this.gameState.findAssamRugOwner().getColour().toString() + " " + this.gameState.getPaymentAmount() + " dirhams.");
            } else {
                paymentText.setText("You cannot afford to pay. You will have to give all your dirhams to Player " + this.gameState.findAssamRugOwner().getColour().toString() + ". After that, You will be removed from the game.");
            }
            this.controlArea.getChildren().addAll(paymentText, this.btnConfirmPayment);
        });

        // If need payment, confirm payment
        this.btnConfirmPayment.setOnMouseClicked(event -> {
            if (this.currentPhase == Phase.MOVEMENT) {
                this.controlArea.getChildren().removeAll(paymentText, this.btnConfirmPayment);
                if (!this.gameState.isPaymentRequired() || this.gameState.isPaymentAffordable()) {
                    this.gameState.makePayment();
                    nextPhase();

                    // update players stats
                    updateStatsArea();
                    if (this.highlighted != null) {
                        this.highlighted.setOpacity(0);
                        this.highlighted = null;
                    }
                    this.gameDraggableRug = new GameDraggableRug(this, MARGIN_LEFT + BOARD_AREA_SIDE + MARGIN_TOP + BUTTON_WIDTH * 0.5, 500, this.gameState.getCurrentPlayer().getColour());

                    this.gameArea.getChildren().add(this.gameDraggableRug);
                    this.controlArea.getChildren().addAll(this.btnRotateRug, this.btnConfirmPlacement);
                } else {
                    // If the current player cannot afford to pay the full amount
                    this.gameState.makePayment();
                    this.gameState.removeCurrentPlayer();
                    nextPhase();

                    updateStatsArea();

                    if (!this.gameState.isGameOver()) {
                        nextTurn();
                    } else {
                        this.btnMainBack.setDisable(false);
                        this.hintButton.setDisable(false);
                    }
                }
            }
        });

        // players rotate rug
        this.btnRotateRug.setOnMouseClicked(event -> rotateRug());

        // confirm rugs placement
        this.btnConfirmPlacement.setOnMouseClicked(event -> makePlacement());

        StackPane gameRootArea = new StackPane();
        BackgroundImage background = drawBackground();
        gameRootArea.setBackground(new Background(background));
        Scene scene = new Scene(gameRootArea, WINDOW_WIDTH, WINDOW_HEIGHT);
        gameRootArea.getChildren().add(gameArea);
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
     * Rotates the current rug
     * @author u7620014 Haobo Zou
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

    /**
     * Place the current rug on the game board
     * @author u7620014 Haobo Zou
     */
    private void makePlacement() {
        if (this.highlighted != null) {
            this.controlArea.getChildren().removeAll(this.btnRotateRug, this.btnConfirmPlacement);
            this.gameArea.getChildren().remove(this.gameDraggableRug);

            Rug rug = new Rug(this.gameDraggableRug.getColour(), this.rugID++, getTilesFromHighlighted());
            this.gameState.makePlacement(rug);

            updateStatsArea();
            GameRug gameRug = new GameRug(this.highlighted.getLayoutX(), this.highlighted.getLayoutY(), this.gameDraggableRug.getOrientation(), this.gameDraggableRug.getColour());
            this.placedRugs.getChildren().add(gameRug);
            if (this.highlighted != null) {
                this.highlighted.setOpacity(0);
                this.highlighted = null;
            }
            this.gameDraggableRug = null;

            if (!this.gameState.isGameOver()) {
                nextTurn();
            } else {
                this.btnMainBack.setDisable(false);
                this.hintButton.setDisable(false);
            }
        }
    }

    /**
     * Simulates the initial rotation of Assam and roll die for computer players
     * Set a delay so that players can see more clearly
     * @author u7620014 Haobo Zou
     */
    private void simulateInitialRotation() {
        delay(() -> {
            this.controlArea.getChildren().removeAll(this.btnInitialRotation, this.btnConfirmInitialRotation);
            this.btnInitialRotation.setDisable(false);
            this.btnConfirmInitialRotation.setDisable(false);

            int rotation = new Random().nextInt(4) * 90;
            this.gameState.rotateAssam(rotation);
            updateAssam();
            nextPhase();

            this.controlArea.getChildren().add(this.btnRollDie);
            this.btnRollDie.setDisable(true);
            simulateRollDie();
        });
    }

    /**
     * Simulates the following rotation of Assam for computer players
     * Set a delay so that players can see more clearly
     * @author u7620014 Haobo Zou
     */
    private void simulateRotation() {
        delay(() -> {
            this.controlArea.getChildren().removeAll(this.btnRotations);
            this.controlArea.getChildren().remove(this.btnConfirmRotation);
            this.btnRotations.forEach(b -> b.setDisable(false));
            this.btnConfirmRotation.setDisable(false);

            if (this.gameState.getCurrentPlayer().getStrategy() == Strategy.RANDOM) {
                int rotation = new Random().nextInt(3) * 90 - 90;
                this.gameState.rotateAssam(rotation);
                updateAssam();
                nextPhase();
            } else {
                //FIXME
            }

            this.controlArea.getChildren().add(this.btnRollDie);
            this.btnRollDie.setDisable(true);
            simulateRollDie();
        });
    }

    /**
     * Simulate roll die to make Assam move for computer players
     * Set a delay so that players can see more clearly
     * @author u7620014 Haobo Zou
     */
    private void simulateRollDie() {
        delay(() -> {
            this.controlArea.getChildren().remove(this.btnRollDie);
            this.btnRollDie.setDisable(false);

            this.dieResult = Die.getSide();

            this.movementText.setText("Die result is " + this.dieResult + ". Assam will now move " + this.dieResult + " steps.");
            this.controlArea.getChildren().addAll(this.movementText, this.btnMoveAssam);
            this.btnMoveAssam.setDisable(true);
            simulateMovement();
        });
    }

    /**
     * Simulate move Assam for computer players
     * Set a delay so that players can see more clearly
     * @author u7620014 Haobo Zou
     */
    private void simulateMovement() {
        delay(() -> {
            this.controlArea.getChildren().removeAll(this.movementText, this.btnMoveAssam);
            this.btnMoveAssam.setDisable(false);

            this.gameState.moveAssam(this.dieResult);
            updateAssam();

            if (!this.gameState.isPaymentRequired()) {
                this.paymentText.setText("No payment required.");
            } else if (this.gameState.isPaymentAffordable()) {
                this.paymentText.setText("You will need to pay Player " + this.gameState.findAssamRugOwner().getColour().toString() + " " + this.gameState.getPaymentAmount() + " dirhams.");
            } else {
                this.paymentText.setText("You cannot afford to pay. You will have to give all your dirhams to Player " + this.gameState.findAssamRugOwner().getColour().toString() + ". After that, You will be removed from the game.");
            }
            this.controlArea.getChildren().addAll(this.paymentText, this.btnConfirmPayment);
            this.btnConfirmPayment.setDisable(true);
            simulatePayment();
        });
    }

    /**
     * Simulate make payment for computer players
     * Set a delay so that players can see more clearly
     * @author u7620014 Haobo Zou
     */
    private void simulatePayment() {
        delay(() -> {
            this.controlArea.getChildren().removeAll(paymentText, this.btnConfirmPayment);
            this.btnConfirmPayment.setDisable(false);

            if (!this.gameState.isPaymentRequired() || this.gameState.isPaymentAffordable()) {
                this.gameState.makePayment();
                nextPhase();

                // update players stats
                updateStatsArea();
                this.controlArea.getChildren().addAll(this.btnRotateRug, this.btnConfirmPlacement);
                this.btnRotateRug.setDisable(true);
                this.btnConfirmPlacement.setDisable(true);
                simulatePlacement();
            } else {
                // If the current player cannot afford to pay the full amount
                this.gameState.makePayment();
                this.gameState.removeCurrentPlayer();
                nextPhase();

                updateStatsArea();

                if (!this.gameState.isGameOver()) {
                    nextTurn();
                } else {
                    this.btnMainBack.setDisable(false);
                    this.hintButton.setDisable(false);
                }
            }
        });
    }

    /**
     * Simulate place the rug on the game board for computer players
     * Set a delay so that players can see more clearly
     * @author u7620014 Haobo Zou
     */
    private void simulatePlacement() {
        delay(() -> {
            this.controlArea.getChildren().removeAll(this.btnRotateRug, this.btnConfirmPlacement);
            this.btnRotateRug.setDisable(false);
            this.btnConfirmPlacement.setDisable(false);

            ArrayList<GameInvisibleRug> validRugs = findAllValidPlacements();

            if (this.gameState.getCurrentPlayer().getStrategy() == Strategy.RANDOM) {
                int index = new Random().nextInt(validRugs.size());
                GameInvisibleRug randomValidRug = validRugs.get(index);
                Rug rug = new Rug(this.gameState.getCurrentPlayer().getColour(), this.rugID++, getTilesFromInvisibleRug(randomValidRug));
                this.gameState.makePlacement(rug);

                updateStatsArea();
                GameRug gameRug = new GameRug(randomValidRug.getLayoutX(), randomValidRug.getLayoutY(), randomValidRug.getOrientation(), this.gameState.getCurrentPlayer().getColour());
                this.placedRugs.getChildren().add(gameRug);
            } else {
                //FIXME
            }

            if (!this.gameState.isGameOver()) {
                nextTurn();
            } else {
                this.btnMainBack.setDisable(false);
                this.hintButton.setDisable(false);
            }
        });
    }

    /**
     * @return A list of all valid placement positions for the current player's rug.
     * @author u7620014 Haobo Zou
     */
    private ArrayList<GameInvisibleRug> findAllValidPlacements() {
        ArrayList<GameInvisibleRug> rugs = new ArrayList<>();
        // Iterate over vertical invisible rugs to find valid placement positions
        for (GameInvisibleRug vGameInvisibleRug : this.vGameInvisibleRugs) {
            Rug rug = new Rug(this.gameState.getCurrentPlayer().getColour(), this.rugID, getTilesFromInvisibleRug(vGameInvisibleRug));
            if (this.gameState.getBoard().isPlacementValid(rug)) {
                rugs.add(vGameInvisibleRug);
            }
        }
        // Iterate over horizontal invisible rugs to find valid placement positions
        for (GameInvisibleRug hGameInvisibleRug : this.hGameInvisibleRugs) {
            Rug rug = new Rug(this.gameState.getCurrentPlayer().getColour(), this.rugID, getTilesFromInvisibleRug(hGameInvisibleRug));
            if (this.gameState.getBoard().isPlacementValid(rug)) {
                rugs.add(hGameInvisibleRug);
            }
        }
        return rugs;
    }

    /**
     * Add delay so that the player can see the computer player's actions more clearly
     * @param func Delay the execution of the code
     * @author u7620014 Haobo Zou
     */
    private void delay(Runnable func) {
        // Create a Task to sleep for the specified duration
        Task<Void> sleeper = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Sleep for millis milliseconds
                final long millis = 100;
                Thread.sleep(millis);
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> func.run());
        new Thread(sleeper).start();
    }

    /**
     * Advances the game to the next player's turn and updates the UI accordingly.
     * @author u7620014 Haobo Zou
     */
    private void nextTurn() {
        this.gameState.nextPlayer();
        // Move to the next game phase
        nextPhase();
        // Update players stats area
        updateStatsArea();
        this.controlArea.getChildren().addAll(this.btnRotations);
        this.controlArea.getChildren().add(this.btnConfirmRotation);

        if (this.gameState.getCurrentPlayer().isComputer()) {
            // For computer player's turn, disable button
            this.btnMainBack.setDisable(true);
            this.hintButton.setDisable(true);
            this.btnRotations.forEach(b -> b.setDisable(true));
            this.btnConfirmRotation.setDisable(true);
            simulateRotation();
        } else {
            this.btnMainBack.setDisable(false);
            this.hintButton.setDisable(false);
        }
    }

    /**
     * Initializes the game tiles and adds them to the game board.
     * @author u7620014 Haobo Zou
     */
    private void initTiles() {
        for (int i = 0; i < NUM_OF_ROWS; i++) {
            for (int j = 0; j < NUM_OF_COLS; j++) {
                this.gameTiles[i][j] = new GameTile(i * TILE_SIDE, j * TILE_SIDE, i, j);
                this.allTiles.getChildren().add(this.gameTiles[i][j]);
            }
        }
        this.drawMosaicTrack();
        this.allTiles.getChildren().addAll(mosaic);
        mosaic.toBack();
    }

    /**
     * Draw the Mosaic Track of the game
     * @author Le Thanh Nguyen
     */
    public void drawMosaicTrack() {
        double xTop = TILE_SIDE;
        double yTop = 0;
        for (int i = 0; i < 4; i++) {
            GameMosaicTrack circle = new GameMosaicTrack(xTop, yTop);
            mosaic.getChildren().add(circle);
            xTop += TILE_SIDE * 2;
        }

        double xBot = 0;
        double yBot = yTop + 7 * TILE_SIDE;
        for (int i = 0; i < 4; i++) {
            GameMosaicTrack circle = new GameMosaicTrack(xBot, yBot);
            mosaic.getChildren().add(circle);
            xBot += TILE_SIDE * 2;
        }

        double xLeft = 0;
        double yLeft = TILE_SIDE;
        for (int i = 0; i < 3; i++) {
            GameMosaicTrack circle = new GameMosaicTrack(xLeft, yLeft);
            mosaic.getChildren().add(circle);
            yLeft += TILE_SIDE * 2;
        }

        double xRight = 7 * TILE_SIDE;
        double yRight = 0;
        for (int i = 0; i < 4; i++) {
            GameMosaicTrack circle = new GameMosaicTrack(xRight, yRight);
            mosaic.getChildren().add(circle);
            yRight += TILE_SIDE * 2;
        }
    }

    /**
     * Initializes the invisible rugs for the game board.
     * @author u7620014 Haobo Zou
     */
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

    /**
     * Initialize Assam's position on (3, 3), direction is north
     * @author u7620014 Haobo Zou
     */
    private void initAssam() {
        SVGPath svg = new SVGPath();
        svg.setContent(ASSAM_SVG);
        this.assam = new Region();
        this.assam.setShape(svg);
        this.assam.setMinSize(ASSAM_SIDE, ASSAM_SIDE);
        this.assam.setMaxSize(ASSAM_SIDE, ASSAM_SIDE);
        this.assam.setBackground(new Background(new BackgroundFill(ASSAM_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        this.assam.setBorder(new Border(new BorderStroke(ASSAM_COLOR.darker(), GAME_PANE_BORDER_STROKE_STYLE, GAME_PANE_BORDER_RADII, ASSAM_BORDER_WIDTH)));
        updateAssam();
    }

    /**
     * Update Assam's position and direction after moving Assam
     * @author u7620014 Haobo Zou
     */
    private void updateAssam() {
        Tile assamTile = this.gameState.getBoard().getAssamTile();
        int row = assamTile.getRow();
        int col = assamTile.getCol();
        this.assam.relocate(ASSAM_RELOCATION + col * TILE_SIDE, ASSAM_RELOCATION + row * TILE_SIDE);
        this.assam.setRotate(getAssamAngle());
    }

    /**
     * Get Assam rotation angle
     * @return Assam rotation angle
     * @author u7620014 Haobo Zou
     */
    private int getAssamAngle() {
        return this.gameState.getBoard().getAssamDirection().getAngle();
    }

    /**
     * Update player stats after each operation
     * @author u7620014 Haobo Zou, u7582846 Yaolin Li
     */
    private void updateStatsArea() {
        if (!this.gameState.isGameOver()) {
            this.phaseText.setText("Current Game Phase: " + this.currentPhase);
        } else {
            this.phaseText.setText("Game Over");
        }

        this.playerStatsVBox.getChildren().clear();
        for (Player player : this.gameState.getPlayers()) {
            String colourString = "PLAYER " + player.getColour();
            Text colourText = new Text();
            colourText.setFont(PLAYER_COLOUR_TEXT_FONT_REGULAR);

            String scoreString = "     Current Score: " + this.gameState.getScores().get(player);
            Text scoreText = new Text();
            scoreText.setFont(GENERAL_TEXT_FONT_ITALIC);

            Text statsText = new Text(player.getDirham() + " dirhams     " + player.getNumOfUnplacedRugs() + " rugs remaining");
            statsText.setFont(GENERAL_TEXT_FONT_ITALIC);

            if (this.gameState.isPlayerAvailable(player)) {
                scoreText.setStyle("-fx-fill:" + TEXT_FILL);
                statsText.setStyle("-fx-fill:" + TEXT_FILL);
                if (!this.gameState.isGameOver() && player.equals(this.gameState.getCurrentPlayer())) {
                    colourString += " - CURRENT";
                }
                if (this.gameState.isGameOver()) {
                    ArrayList<Player> winners = this.gameState.getWinners();
                    if (winners.contains(player)) {
                        // Show winners
                        colourString += " - WINNER!";
                        // Show alert dialog
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        // Modify the default icon
                        ImageView newIcon = new ImageView(new Image("resources/game-over.png"));
                        newIcon.setFitWidth(40);
                        newIcon.setFitHeight(40);
                        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                        stage.getIcons().add(new Image("resources/game-over.png"));
                        DialogPane dialogPane = alert.getDialogPane();
                        dialogPane.setGraphic(newIcon);
                        // Set background colour of dialog
                        dialogPane.setStyle("-fx-background-color: #eecdab; -fx-padding: 15px;");
                        dialogPane.setPrefSize(400, 200);
                        // Different hints in different phases
                        alert.setTitle("Game Over");
                        alert.setHeaderText("Game Over!");
                        alert.setContentText("Congratulations to " + colourString + " with highest score: " + this.gameState.getScores().get(player));
                        // Set style of Alert dialogPane
                        Platform.runLater(() -> {
                            dialogPane.lookup(".label").setStyle("-fx-font-size: 18px; -fx-font-weight: bold;" + "-fx-text-fill:" + TEXT_FILL);
                            dialogPane.lookup(".button").setStyle("-fx-text-fill: white;-fx-background-color: #672F09;");
                            dialogPane.lookup(".header-panel").setStyle("-fx-background-color: #eecdab");
                            dialogPane.lookup(".content").setStyle("-fx-font-size: 16px;" + "-fx-text-fill:" + TEXT_FILL);
                        });
                        alert.show();
                        this.controlArea.getChildren().clear();
                        StackPane controlAreaAfterGameOver = new StackPane();
                        controlAreaAfterGameOver.setPrefSize(CONTROL_AREA_WIDTH, CONTROL_AREA_HEIGHT);
                        Text playAgain = new Text("Play Again!");
                        playAgain.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
                        playAgain.setStyle("-fx-fill:" + TEXT_FILL);
                        playAgain.setWrappingWidth(300);
                        controlAreaAfterGameOver.getChildren().addAll(playAgain, restart);
                        this.controlArea.getChildren().add(controlAreaAfterGameOver);
                    }
                }
                colourText.setFill(Colour.getFrontEndColor(player.getColour()));
                colourText.setStroke(Colour.getFrontEndColor(player.getColour()).darker());
            } else {
                colourString += " - OUT";
                colourText.setFill(Color.GRAY);
                colourText.setOpacity(1 - HIGHLIGHTED_OPACITY);
                scoreText.setFill(Color.GRAY);
                statsText.setFill(Color.GRAY);
            }
            colourText.setText(colourString);

            if (player.isComputer()) {
                scoreText.setText("COMPUTER - " + player.getStrategy() + scoreString);
            } else {
                scoreText.setText("HUMAN" + scoreString);
            }
            playerStatsVBox.getChildren().addAll(colourText, scoreText, statsText);
        }
    }

    /**
     * Create and return a background image for whole game
     * @return The configured background image.
     * @author u7582846 Yaolin Li
     */
    private BackgroundImage drawBackground() {
        Image backgroundImage = new Image("resources/background.jpg");
        // set image size, make the picture adaptive
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true);
        return new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, backgroundSize);
    }

    /**
     * Creates and returns an ImageView representing the specific game board's background.
     * @param width  the specific game board's width
     * @param height the specific game board's height
     * @return An ImageView representing the specific game board's background with rounded corners.
     * @author u7582846 Yaolin Li
     */
    private ImageView drawGameBoardBackground(double width, double height) {
        // set shallow background
        ImageView imageView = new ImageView(new Image("resources/gameBackground.jpg"));
        // set ImageView size
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        // Use Rectangle to set the rounded corners for the ImageView
        Rectangle clip = new Rectangle(
                imageView.getFitWidth(), imageView.getFitHeight()
        );
        clip.setArcWidth(50);
        clip.setArcHeight(50);
        imageView.setClip(clip);
        return imageView;
    }

    /**
     * Provides a user interface element to allow players to select their color,
     * and choose to be a human player or computer player
     * @author u7620014 Haobo Zou
     */
    private class PlayerSelector extends Pane {
        private final Colour colour;
        private Player player;
        private final CheckBox chbIsComputer;
        private final GameColourButton gameColourButton;
        private final Label lblStrategy;
        private final RadioButton rbRandom;
        private final RadioButton rbIntelligent;

        public PlayerSelector(double width, double height, Colour colour) {
            super();
            this.setMinSize(width, height);
            this.setMaxSize(width, height);
            this.colour = colour;

            this.gameColourButton = new GameColourButton(colour);
            this.getChildren().add(this.gameColourButton);

            this.chbIsComputer = new CheckBox();
            this.chbIsComputer.setText("Computer");
            this.chbIsComputer.setFont(GENERAL_TEXT_FONT_ITALIC);
            this.chbIsComputer.setStyle("-fx-text-fill:" + TEXT_FILL);
            this.chbIsComputer.relocate(0, COLOUR_BUTTON_RADIUS * 2.2);

            this.lblStrategy = new Label("Strategy:");
            this.lblStrategy.setFont(GENERAL_TEXT_FONT_REGULAR);
            this.lblStrategy.setStyle("-fx-text-fill:" + TEXT_FILL);
            this.lblStrategy.relocate(0, COLOUR_BUTTON_RADIUS * 2.6);

            ToggleGroup toggleGroup = new ToggleGroup();
            this.rbRandom = new RadioButton("Random");
            this.rbRandom.setFont(GENERAL_TEXT_FONT_ITALIC);
            this.rbRandom.setStyle("-fx-text-fill:" + TEXT_FILL);
            this.rbRandom.relocate(0, COLOUR_BUTTON_RADIUS * 3.0);
            this.rbRandom.setToggleGroup(toggleGroup);
            this.rbIntelligent = new RadioButton("Intelligent");
            this.rbIntelligent.setFont(GENERAL_TEXT_FONT_ITALIC);
            this.rbIntelligent.setStyle("-fx-text-fill:" + TEXT_FILL);
            this.rbIntelligent.relocate(0, COLOUR_BUTTON_RADIUS * 3.4);
            this.rbIntelligent.setToggleGroup(toggleGroup);
            this.rbIntelligent.setDisable(true);

            this.gameColourButton.setOnMouseClicked(event -> {
                this.gameColourButton.setDisable(true);
                this.gameColourButton.setOpacity(1);
                this.gameColourButton.addBorder();
                this.getChildren().add(this.chbIsComputer);

                this.player = new Player(colour);
                tmp.add(this.player);
                if (tmp.size() == numOfPlayers) {
                    // When the appropriate number of players has been selected, disable the other colors,
                    // and make them transparent
                    playerSelectors.forEach(playerSelector -> {
                        playerSelector.gameColourButton.setDisable(true);
                        if (!tmp.stream().map(Player::getColour).toList().contains(playerSelector.colour)) {
                            playerSelector.gameColourButton.setOpacity(HIGHLIGHTED_OPACITY);
                        }
                    });
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

            this.rbRandom.setOnMouseClicked(event -> this.player.setStrategy(Strategy.RANDOM));

            this.rbIntelligent.setOnMouseClicked(event -> this.player.setStrategy(Strategy.INTELLIGENT));
        }
    }

    /**
     * Represents the three phases of the game, rotation, movement and placement
     * @author u7620014 Haobo Zou
     */
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

    /**
     * Transitions the game to the next phase
     * @author u7620014 Haobo Zou
     */
    private void nextPhase() {
        switch (this.currentPhase) {
            case ROTATION -> this.currentPhase = Phase.MOVEMENT;
            case MOVEMENT -> this.currentPhase = Phase.PLACEMENT;
            case PLACEMENT -> this.currentPhase = Phase.ROTATION;
        }
        // Display current phase in player stats area
        this.phaseText.setText("Current Game Phase: " + this.currentPhase);
    }

    /**
     * Represents the orientation of game objects.
     * @author u7620014 Haobo Zou
     */
    enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    /**
     * Retrieves the tiles associated with the highlighted game object
     * @return An array containing the tiles associated with the highlighted game object, or null if no object is highlighted.
     * @author u7620014 Haobo Zou
     */
    Tile[] getTilesFromHighlighted() {
        if (this.highlighted != null) {
            return new Tile[]{getTileFromGameTile(this.highlighted.getGameTiles()[0]), getTileFromGameTile(this.highlighted.getGameTiles()[1])};
        }
        return null;
    }

    /**
     * Converts GameInvisibleRug to corresponding Tile[][]
     * @param gameInvisibleRug The invisible rug whose GameInvisibleRug need to be converted.
     * @return An array of converted Tile[][].
     * @author u7620014 Haobo Zou
     */
    Tile[] getTilesFromInvisibleRug(GameInvisibleRug gameInvisibleRug) {
        return new Tile[]{getTileFromGameTile(gameInvisibleRug.getGameTiles()[0]), getTileFromGameTile(gameInvisibleRug.getGameTiles()[1])};
    }

    /**
     * Converts GameTile to corresponding Tile[][]
     * @param gameTile The rug whose GameTile need to be converted.
     * @return An array of converted Tile[][].
     * @author u7620014 Haobo Zou
     */
    Tile getTileFromGameTile(GameTile gameTile) {
        return this.gameState.getBoard().getTiles()[gameTile.getRow()][gameTile.getCol()];
    }
}
package comp1110.ass2;

import comp1110.ass2.board.Board;
import comp1110.ass2.board.Tile;
import comp1110.ass2.player.Colour;
import comp1110.ass2.player.Player;
import comp1110.ass2.player.Rug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * GameState class determines the state of each player
 * and makes sure the game is played according to the rules
 */
public class GameState {
    private static final int LENGTH_OF_PLAYER_STRING = 8;
    private static final int LENGTH_OF_ASSAM_STRING = 4;
    private static final String EMPTY_BOARD_STRING = "Bn00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00";

    private final Board board;
    // All players
    private final Player[] players;
    // Players are still in game
    private final List<Player> availablePlayers;
    private Player currentPlayer;

    /**
     * Constructor: creates an instance of the GameState class
     * @param players an array of all the players at the beginning of the game
     */
    public GameState(Player[] players) {
        this.board = new Board();
        this.players = players;
        this.availablePlayers = new ArrayList<Player>(Arrays.asList(this.players));
        this.currentPlayer = this.availablePlayers.get(0);
    }

    /**
     * getter method for board
     * @return the board
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * getter method for players
     * @return the array containing all the players
     */
    public Player[] getPlayers() {
        return this.players;
    }

    /**
     * Changes current player to the next available player in the game
     */
    public void nextPlayer() {
        int index = this.availablePlayers.indexOf(this.currentPlayer);
        this.currentPlayer = this.availablePlayers.get((index + 1) % this.availablePlayers.size());
    }

    /**
     * Removes current player from the list of available players in the game
     */
    private void removeCurrentPlayer() {
        this.availablePlayers.remove(this.currentPlayer);
    }

    public boolean isGameOver() {
        return this.currentPlayer.getNumOfUnplacedRugs() == 0;
    }

    /**
     * Rotates Assam
     */
    public void rotateAssam(int rotation) {
        this.board.rotateAssam(rotation);
    }

    /**
     * Moves Assam
     * @param steps number of steps to be taken by Assam
     */
    public void moveAssam(int steps) {
        this.board.moveAssam(steps);
    }

    /**
     * Finds the player who matches the given colour
     * @param colour a colour
     * @return the player who matches the given colour, null if such player doesn't exist
     */
    private Player findPlayer(Colour colour) {
        for (Player player : this.players) {
            if (player.getColour() == colour) {
                return player;
            }
        }
        return null;
    }

    /**
     * Finds the player whose rug Assam is currently standing on
     * @return the owner of the rug on which Assam is standing,
     * null if Assam is standing on an empty tile
     */
    private Player findAssamRugOwner() {
        Rug assamRug = this.board.getAssamTile().getTopRug();
        if (assamRug == null) {
            return null;
        }
        return findPlayer(assamRug.getColour());
    }

    /**
     * Checks if a payment to another player is required after moving Assam
     * @return true if required, false if not
     */
    private boolean isPaymentRequired() {
        Player otherPlayer = findAssamRugOwner();
        if (otherPlayer != null && this.availablePlayers.contains(otherPlayer) && this.currentPlayer != otherPlayer) {
            return true;
        }
        return false;
    }

    /**
     * Calculates the amount of payment required
     * @return the amount of payment
     */
    public int getPaymentAmount() {
        // FIXME
        return 0;
    }

    /**
     * Determines whether the payment is affordable or not
     * @return true if affordable, false if not
     */
    private boolean isPaymentAffordable() {
        if (isPaymentRequired()) {
            return currentPlayer.getDirham() >= getPaymentAmount();
        }
        return true;
    }

    /**
     * The current player makes their payment, and gets removed if they cannot afford to pay full amount
     */
    public void makePayment() {
        if (isPaymentAffordable()) {
            this.currentPlayer.makePayment(findAssamRugOwner(), getPaymentAmount());
        } else {
            this.currentPlayer.makePayment(findAssamRugOwner(), this.currentPlayer.getDirham());
            removeCurrentPlayer();
        }
    }

    /**
     * The current player places one of their remaining rugs on the board
     * @param rug the rug to be placed on the board
     */
    public void makePlacement(Rug rug) {
        this.currentPlayer.placeRug();
        this.board.placeRug(rug);
    }

    /**
     * Checks the validity of the given gameString
     * @param gameString string representation for a game state
     * @return true if the gameString is valid, false if not
     */
    public static boolean isGameStringValid(String gameString) {
        String playerStringsPattern = "(P[yrcp][0-9]{5}[io]){2,4}";
        String assamStringPattern = "A[0-6]{2}[NESW]";
        String boardStringPattern = "B([yrcp][0-9]{2}|n00){49}";
        String pattern = "^" + playerStringsPattern + assamStringPattern + boardStringPattern + "$";
        return gameString.matches(pattern);
    }

    /**
     * Constructor: creates an instance of the GameState class
     * Decodes the gameString to get corresponding values for instance fields
     * @param gameString string representation for the game state
     */
    public GameState(String gameString) {
        List<String> playerStrings = getPlayerStrings(gameString);
        String assamString = getAssamString(gameString);
        String boardString = getBoardString(gameString);
        this.board = new Board(assamString, boardString);
        this.players = new Player[playerStrings.size()];
        this.availablePlayers = new ArrayList<Player>();
        for (int i = 0; i < this.players.length; i++) {
            String playerString = playerStrings.get(i);
            this.players[i] = new Player(playerString);
            if (playerString.contains("i")) {
                this.availablePlayers.add(this.players[i]);
            }
        }
    }

    /**
     * Obtains playerStrings from a given gameString
     * @param gameString string representation for game state
     * @return a list of string representations for each player;
     */
    private static List<String> getPlayerStrings(String gameString) {
        List<String> playerStrings = new ArrayList<String>();
        int beginIndex = 0;
        while (gameString.contains("P")) {
            playerStrings.add(gameString.substring(beginIndex, beginIndex + LENGTH_OF_PLAYER_STRING));
            gameString = gameString.substring(beginIndex + LENGTH_OF_PLAYER_STRING);
        }
        return playerStrings;
    }

    /**
     * Obtains assamString from a given gameString
     * @param gameString string representation for game state
     * @return the string representation for Assam
     */
    private static String getAssamString(String gameString) {
        int beginIndex = gameString.indexOf("A");
        return gameString.substring(beginIndex, beginIndex + LENGTH_OF_ASSAM_STRING);
    }

    /**
     * Obtains boardString from a given gameString
     * @param gameString string representation for game state
     * @return the string representation for the board
     */
    private static String getBoardString(String gameString) {
        int beginIndex = gameString.indexOf("B");
        return gameString.substring(beginIndex);
    }

    /**
     * Generates gameString based on data of the game state
     * @return string representation for the game state
     */
    public static String generateGameString(GameState gameState) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Player player : gameState.players) {
            stringBuilder.append(Player.generatePlayerString(player));
            if (gameState.availablePlayers.contains(player)) {
                stringBuilder.append("i");
            } else {
                stringBuilder.append("o");
            }
        }
        stringBuilder.append(Board.generateAssamString(gameState.board)).append(Board.generateBoardString(gameState.board));
        return stringBuilder.toString();
    }

    /**
     * Determines whether a rug(string) is valid.
     * @param gameString string representation for the game state
     * @param rugString string representation for the rug
     * @return true if the rug is valid, or false otherwise
     */
    public static boolean isRugValid(String gameString, String rugString) {
        GameState gameState = new GameState(gameString);
        Board board = gameState.board;
        // The String is 7 characters long
        if (rugString.length() != 7) {
            return false;
        }
        Rug rug = new Rug(rugString);
        // The first character in the String corresponds to the colour character of a player present in the game
        if (!Colour.isColourValid(rugString.charAt(0))) {
            return false;
        }
        // The next 4 characters represent coordinates that are on the board
        for (Tile tile : rug.getRugTiles()) {
            if (!Board.isTileValid(tile)) {
                return false;
            }
        }
        // The combination of that ID number and colour is unique
        List<Rug> visibleRugs = board.getVisibleRugs();
        for (Rug placedRug : visibleRugs) {
            if (rug.getColour() == placedRug.getColour() && rug.getID() == placedRug.getID()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines whether a game is over or not based on the gameString
     * @param currentGame string representation for the game state
     * @return true or false
     */
    public static boolean isGameOver(String currentGame) {
        GameState gameState = new GameState(currentGame);
        for (Player player : gameState.availablePlayers) {
            if (player.getNumOfUnplacedRugs() != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculates Assam's position and direction after movement
     * @param currentAssam string representation for Assam before movement
     * @param dieResult number of steps to be taken by Assam
     * @return string representation for Assam after movement
     */
    public static String moveAssam(String currentAssam, int dieResult) {
        GameState gameState = new GameState(currentAssam + EMPTY_BOARD_STRING);
        gameState.moveAssam(dieResult);
        return Board.generateAssamString(gameState.board);
    }

    /**
     * Determines whether a player is currently in the game or not
     * @param player a given player
     * @return true or false
     */
    public boolean isPlayerAvailable(Player player) {
        return this.availablePlayers.contains(player);
    }

}



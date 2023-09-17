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
    private static final int LENGTH_OF_PLAYER_STRING = 7;
    private static final int LENGTH_OF_ASSAM_STRING = 3;
    private static final String EMPTY_BOARD_STRING = "Bn00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00";

    private final Board board;
    // All players
    private final Player[] players;
    // Players are still in game
    private final List<Player> availablePlayers;
    private Player currentPlayer;

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
    public void removeCurrentPlayer() {
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

    public void moveAssam(int steps) {
        this.board.moveAssam(steps);
    }

    private Player getAssamRugOwner() {
        Rug rug = this.board.getAssamTile().getTopRug();
        Colour rugColour = rug == null ? null : rug.getColour();
        for (Player player : this.players) {
            if (player.getColour() == rugColour) {
                return player;
            }
        }
        return null;
    }

    public boolean isPaymentRequired() {
        Player owner = getAssamRugOwner();
        if (this.availablePlayers.contains(owner) && this.currentPlayer != owner) {
            return true;
        }
        return false;
    }

    public int getPaymentAmount() {
        // FIXME
        return 0;
    }

    public boolean isPaymentAffordable(int amount) {
        return currentPlayer.getDirham() >= amount;
    }

    public void makePayment(int amount) {
        this.currentPlayer.makePayment(getAssamRugOwner(), amount);
    }

    public void makePlacement(Rug rug) {
        this.currentPlayer.placeRug();
        this.board.placeRug(rug);
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


    private static List<String> getPlayerStrings(String gameString) {
        List<String> playerStrings = new ArrayList<String>();
        int beginIndex = 1;
        while (gameString.contains("P")) {
            playerStrings.add(gameString.substring(beginIndex, beginIndex + LENGTH_OF_PLAYER_STRING));
            gameString = gameString.substring(beginIndex + LENGTH_OF_PLAYER_STRING);
        }
        return playerStrings;
    }

    private static String getAssamString(String gameString) {
        int beginIndex = gameString.indexOf("A") + 1;
        return gameString.substring(beginIndex, beginIndex + LENGTH_OF_ASSAM_STRING);
    }

    private static String getBoardString(String gameString) {
        int beginIndex = gameString.indexOf("B") + 1;
        return gameString.substring(beginIndex);
    }

    /**
     * Determines whether a rug(string) is valid.
     *
     * @param gameString string representation for the game state
     * @param rugString string representation for the rug
     * @return true if the rug is valid, or false otherwise
     */
    public static boolean isRugValid(String gameString, String rugString) {
        GameState gameState = new GameState(gameString);
        Board board = gameState.getBoard();
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

    public static boolean isGameOver(String currentGame) {
        GameState gameState = new GameState(currentGame);
        for (Player player : gameState.availablePlayers) {
            if (player.getNumOfUnplacedRugs() != 0) {
                return false;
            }
        }
        return true;
    }

    public static String moveAssam(String currentAssam, int dieResult) {
        GameState gameState = new GameState(currentAssam + EMPTY_BOARD_STRING);
        gameState.moveAssam(dieResult);
        return gameState.board.generateAssamString();
    }

}



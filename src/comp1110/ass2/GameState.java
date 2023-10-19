package comp1110.ass2;

import comp1110.ass2.board.Board;
import comp1110.ass2.board.Tile;
import comp1110.ass2.player.Colour;
import comp1110.ass2.player.Player;
import comp1110.ass2.player.Rug;

import java.util.*;
import java.util.stream.Collectors;

/**
 * GameState class determines the state of each player
 * and makes sure the game is played according to the rules
 * @author u7594144 Le Thanh Nguyen, u7582846 Yaolin Li, u7620014 Haobo Zou
 */
public class GameState {
    private static final int LENGTH_OF_PLAYER_STRING = 8;
    private static final int LENGTH_OF_ASSAM_STRING = 4;

    private final Board board;
    // All players
    private final Player[] players;
    // Players are still in game
    private final List<Player> availablePlayers;
    private Player currentPlayer;

    /**
     * Constructor: creates an instance of the GameState class
     * @param players an array of all the players
     * @author u7620014 Haobo Zou
     */
    public GameState(Player[] players) {
        this.board = new Board();
        this.players = players;
        this.availablePlayers = new ArrayList<>(Arrays.asList(this.players));
        this.currentPlayer = this.availablePlayers.get(0);
    }

    /**
     * getter method for board
     * @return the board
     * @author u7620014 Haobo Zou
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * getter method for players
     * @return the array containing all the players
     * @author u7620014 Haobo Zou
     */
    public Player[] getPlayers() {
        return this.players;
    }

    /**
     * getter method for currentPlayer
     * @return the current player
     * @author u7620014 Haobo Zou
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Changes current player to the next available player in the game
     * @author u7620014 Haobo Zou
     */
    public void nextPlayer() {
        int index = Arrays.asList(this.players).indexOf(this.currentPlayer);
        for (int i = index + 1; i < index + this.players.length; i++) {
            if (this.availablePlayers.contains(this.players[i % this.players.length])) {
                this.currentPlayer = this.players[i % this.players.length];
                break;
            }
        }
    }

    /**
     * Removes current player from the list of available players in the game
     * @author u7620014 Haobo Zou
     */
    public void removeCurrentPlayer() {
        this.availablePlayers.remove(this.currentPlayer);
    }

    /**
     * Determines whether a player is currently in the game or not
     * @param player a given player
     * @return true or false
     * @author u7620014 Haobo Zou
     */
    public boolean isPlayerAvailable(Player player) {
        return this.availablePlayers.contains(player);
    }

    public boolean isGameOver() {
        for (Player player : this.availablePlayers) {
            if (player.getNumOfUnplacedRugs() != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Rotates Assam with a given rotation
     * @param rotation rotation in degrees
     * @author u7620014 Haobo Zou
     */
    public void rotateAssam(int rotation) {
        this.board.rotateAssam(rotation);
    }

    /**
     * Moves Assam with given steps
     * @param steps number of steps
     * @author u7620014 Haobo Zou
     */
    public void moveAssam(int steps) {
        this.board.moveAssam(steps);
    }

    /**
     * Finds the player who matches the given colour
     * @param colour a colour
     * @return the player who matches the given colour, null if such player doesn't exist
     * @author u7620014 Haobo Zou
     */
    public Player findPlayer(Colour colour) {
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
     * @author u7620014 Haobo Zou
     */
    public Player findAssamRugOwner() {
        Rug assamRug = this.board.getAssamTile().getTopRug();
        if (assamRug == null) {
            return null;
        }
        return findPlayer(assamRug.getColour());
    }

    /**
     * Checks if a payment to another player is required after moving Assam
     * @return true if required, false if not
     * @author u7620014 Haobo Zou
     */
    public boolean isPaymentRequired() {
        Player otherPlayer = findAssamRugOwner();
        return otherPlayer != null && this.availablePlayers.contains(otherPlayer) && this.currentPlayer != otherPlayer;
    }


    /**
     * Determines whether the payment is affordable or not
     * @return true if affordable, false if not
     * @author u7620014 Haobo Zou
     */
    public boolean isPaymentAffordable() {
        if (isPaymentRequired()) {
            return currentPlayer.getDirham() >= getPaymentAmount();
        }
        return true;
    }

    /**
     * The current player makes their payment, and gets removed if they cannot afford to pay full amount
     * @author u7620014 Haobo Zou
     */
    public void makePayment() {
        if (isPaymentRequired()) {
            if (isPaymentAffordable()) {
                this.currentPlayer.pay(findAssamRugOwner(), getPaymentAmount());
            } else {
                this.currentPlayer.pay(findAssamRugOwner(), this.currentPlayer.getDirham());
            }
        }
    }

    /**
     * The current player places one of their remaining rugs on the board
     * @param rug the rug to be placed on the board
     * @author u7620014 Haobo Zou
     */
    public void makePlacement(Rug rug) {
        Player player = findPlayer(rug.getColour());
        if (player != null) {
            player.placeRug();
            this.board.placeRug(rug);
        }
    }

    /**
     * Checks the validity of a given gameString
     * @param gameString string representation for a game state
     * @return true if the gameString is valid, false if not
     * @author u7620014 Haobo Zou
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
     * @author u7620014 Haobo Zou
     */
    public GameState(String gameString) {
        List<String> playerStrings = getPlayerStrings(gameString);
        String assamString = getAssamString(gameString);
        String boardString = getBoardString(gameString);
        this.board = new Board(assamString, boardString);
        this.players = new Player[playerStrings.size()];
        this.availablePlayers = new ArrayList<>();
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
     * @author u7620014 Haobo Zou
     */
    public static List<String> getPlayerStrings(String gameString) {
        List<String> playerStrings = new ArrayList<>();
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
     * @author u7620014 Haobo Zou
     */
    public static String getAssamString(String gameString) {
        int beginIndex = gameString.indexOf("A");
        return gameString.substring(beginIndex, beginIndex + LENGTH_OF_ASSAM_STRING);
    }

    /**
     * Obtains boardString from a given gameString
     * @param gameString string representation for game state
     * @return the string representation for the board
     * @author u7620014 Haobo Zou
     */
    public static String getBoardString(String gameString) {
        int beginIndex = gameString.indexOf("B");
        return gameString.substring(beginIndex);
    }

    /**
     * Generates gameString based on data of the game state
     * @return string representation for the game state
     * @author u7620014 Haobo Zou
     */
    public String generateGameString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Player player : this.players) {
            stringBuilder.append(player.generatePlayerString());
            if (this.availablePlayers.contains(player)) {
                stringBuilder.append("i");
            } else {
                stringBuilder.append("o");
            }
        }
        stringBuilder.append(this.board.generateAssamString()).append(this.board.generateBoardString());
        return stringBuilder.toString();
    }

    /**
     * Determines whether a rug(string) is valid.
     * @param gameString string representation for the game state
     * @param rugString string representation for the rug
     * @return true if the rug is valid, or false otherwise
     * @author u7582846 Yaolin Li
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
        for (Rug visibleRug : visibleRugs) {
            if (rug.getColour() == visibleRug.getColour() && rug.getID() == visibleRug.getID()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get all the winners
     * @return an ArrayList of all the winners
     * @author Le Thanh Nguyen u7594144
     */
    public ArrayList<Player> getWinners() {
        HashMap<Player, Integer> scores = getScores();
        int maxScore = Collections.max(scores.values());
        ArrayList<Player> maxScorePlayers = new ArrayList<>(this.availablePlayers
                .stream()
                .filter(p -> scores.get(p) == maxScore)
                .collect(Collectors.toList()));
        if (maxScorePlayers.size() > 1) {
            HashMap<Player, Integer> dirhams = new HashMap<>();
            maxScorePlayers.forEach(p -> dirhams.put(p, p.getDirham()));
            int maxDirhams = Collections.max(dirhams.values());
            ArrayList<Player> winners = new ArrayList<>(maxScorePlayers
                    .stream()
                    .filter(p -> p.getDirham() == maxDirhams)
                    .collect(Collectors.toList()));
            return winners;
        }
        return maxScorePlayers;
    }

    /**
     * Calculates each player's score at the current state
     * @return a HashMap of all players' scores, including 0
     * @author Le Thanh Nguyen u7594144
     */
    public HashMap<Player, Integer> getScores() {
        HashMap<Player, Integer> nums = getNumsOfVisibleRugTiles();
        HashMap<Player, Integer> scores = new HashMap<>();
        nums.forEach((player, num) -> scores.put(player, player.getDirham() + nums.get(player)));
        return scores;
    }

    /**
     * Counts the number of visible rug tiles of each player at the current state
     * @return a HashMap of all players' respective numbers of visible rug tiles, including 0
     * @author Le Thanh Nguyen u7594144
     */
    public HashMap<Player, Integer> getNumsOfVisibleRugTiles() {
        Tile[][] tiles = this.board.getTiles();
        HashMap<Player, Integer> hashMap = new HashMap<>();
        for (int row = 0; row < Board.NUM_OF_ROWS; row++) {
            for (int col = 0; col < Board.NUM_OF_COLS; col++) {
                Tile tile = tiles[row][col];
                if (tile.hasRug()) {
                    Player rugOwner = findPlayer(tile.getTopRug().getColour());
                    if (!hashMap.containsKey(rugOwner)) {
                        hashMap.put(rugOwner, 1);
                    } else {
                        hashMap.put(rugOwner, hashMap.get(rugOwner) + 1);
                    }
                }
            }
        }
        for (Player player : this.players) {
            if (!hashMap.containsKey(player)) {
                hashMap.put(player, 0);
            }
        }
        return hashMap;
    }

    /**
     * Calculates the amount of payment required
     * @return the amount of payment
     * @author u7594144 Le Thanh Nguyen
     */
    public int getPaymentAmount() {
        Set<Tile> visited = new HashSet<>();
        return getPaymentAmount(this.board.getAssamTile(), visited);
    }

    /**
     * Calculates the amount of payment required using DFS
     * @param currentTile a tile to be checked
     * @param visitedTiles a set of already visited tiles
     * @return the amount of payment
     * @author u7594144 Le Thanh Nguyen
     */

    public int getPaymentAmount(Tile currentTile, Set<Tile> visitedTiles) {
        if (visitedTiles.contains(currentTile)) return 0;
        visitedTiles.add(currentTile);
        List<Tile> adjacentTiles = this.board.getAdjacentTiles(currentTile);
        Colour currentColour = currentTile.getTopRug().getColour();
        int payment = 1;
        for (Tile adjTile : adjacentTiles) {
            if (adjTile.getTopRug() != null && adjTile.getTopRug().getColour() == currentColour) {
                payment += getPaymentAmount(adjTile, visitedTiles);
            }
        }
        return payment;
    }
}
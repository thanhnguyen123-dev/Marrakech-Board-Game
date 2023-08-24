package comp1110.ass2;

import comp1110.ass2.board.Board;
import comp1110.ass2.player.Colour;
import comp1110.ass2.player.Player;
import comp1110.ass2.player.Rug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * GameState class determines the state of each player
 * Make sure the game is played according to the rules
 */
public class GameState {
    private static final int LENGTH_OF_PLAYER_STRING = 7;
    private static final int LENGTH_OF_ASSAM_STRING = 3;

    private Board board;
    // All players
    private Player[] players;
    // Players are still in game
    private List<Player> availablePlayers;
    private Player currentPlayer;

    public GameState(Player[] players) {
        this.board = new Board();
        this.players = players;
        this.availablePlayers = new ArrayList<Player>(Arrays.asList(this.players));
        this.currentPlayer = this.availablePlayers.get(0);
    }

    public void nextPlayer() {
        int index = this.availablePlayers.indexOf(this.currentPlayer);
        this.currentPlayer = this.availablePlayers.get((index + 1) % this.availablePlayers.size());
    }

    public void removeCurrentPlayer() {
        this.availablePlayers.remove(this.currentPlayer);
    }

    public boolean isGameOver() {
        return this.currentPlayer.getNumOfUnplacedRugs() == 0;
    }

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

    public boolean isPlacementValid(Rug rug) {
        return this.board.isPlacementValid(rug);
    }

    public void makePlacement(Rug rug) {
        this.currentPlayer.placeRug();
        this.board.placeRug(rug);
    }

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

    public static boolean isRugValid(String gameString, String rug) {
        // FIXME
        return false;
    }
}

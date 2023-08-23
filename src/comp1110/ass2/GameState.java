package comp1110.ass2;

import comp1110.ass2.board.Board;
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
        return rug == null ? null : rug.getOwner();
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
        List<String> playerStrings = new ArrayList<String>(Arrays.asList(gameString.split("P")));
        playerStrings.remove(0);
        String[] tmp = playerStrings.remove(playerStrings.size() - 1).split("A");
        playerStrings.add(tmp[0]);
        String[] assamBoardString = tmp[1].split("B");
        String assamString = assamBoardString[0];
        String boardString = assamBoardString[1];
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

    public static boolean isRugValid(String gameString, String rug) {
        // FIXME
        return false;
    }
}

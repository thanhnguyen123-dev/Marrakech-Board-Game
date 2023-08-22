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
    private Board board = new Board();
    // All players
    private Player[] players;
    // Players are still in game
    private List<Player> availablePlayers;
    private Player currentPlayer;

    public GameState(Player[] players) {
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
        if (currentPlayer.getDirham() >= amount) {
            return true;
        }
        return false;
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

    public static boolean isRugValid(String gameString, String rug) {
        // FIXME
        return false;
    }
}

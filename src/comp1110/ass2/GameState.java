package comp1110.ass2;

import comp1110.ass2.board.Board;
import comp1110.ass2.player.Player;
import comp1110.ass2.player.Rug;

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
    private List availablePlayers;

    public GameState(Player[] players) {
        this.players = players;
        this.availablePlayers = Arrays.asList(this.players);
    }

    public boolean isPlacementValid(Rug rug) {
        return board.isRugValid(rug);
    }

    public void makePlacement(Rug rug) {
        rug.getOwner().placeRug();
        board.placeRug(rug);
    }
}

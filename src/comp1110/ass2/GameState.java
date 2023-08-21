package comp1110.ass2;

import comp1110.ass2.board.Board;
import comp1110.ass2.player.Colour;
import comp1110.ass2.player.Player;

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

    public GameState(int numOfPlayers, Colour[] colours) {
        for (int i = 0; i < numOfPlayers; i++) {
            this.players[i] = new Player(colours[i]);
        }
        this.availablePlayers = Arrays.asList(this.players);
    }
}

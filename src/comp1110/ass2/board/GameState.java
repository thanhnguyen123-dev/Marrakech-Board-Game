package comp1110.ass2.board;

import comp1110.ass2.player.Colour;
import comp1110.ass2.player.Player;
import comp1110.ass2.player.Direction;

import java.util.Arrays;
import java.util.List;

/**
 * GameState class determines the state of each player
 * Make sure the game is played according to the rules
 */
public class GameState {
    private static final int NUM_OF_ROWS = 7;
    private static final int NUM_OF_COLS = 7;
    // All players
    private Player[] players;
    // Players are still in game
    private List availablePlayers;
    private Tile[][] tiles;
    private Coordinate assamPosition;
    private Direction assamDirection;
    private Scoreboard scoreboard;

    public GameState(int numOfPlayers, Colour[] colours) {
        for (int i = 0; i < numOfPlayers; i++) {
            this.players[i] = new Player(colours[i]);
        }
        this.availablePlayers = Arrays.asList(this.players);
        this.tiles = new Tile[NUM_OF_ROWS][NUM_OF_COLS];
        this.assamPosition = new Coordinate(3, 3);
        this.assamDirection = Direction.NORTH;
        this.scoreboard = new Scoreboard();
    }
}

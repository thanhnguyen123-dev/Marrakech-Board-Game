package comp1110.ass2.board;

import comp1110.ass2.player.Rug;

import java.util.ArrayList;
import java.util.List;

public class Board {
    static final int NUM_OF_ROWS = 7;
    static final int NUM_OF_COLS = 7;

    private Tile[][] tiles = new Tile[Board.NUM_OF_ROWS][Board.NUM_OF_COLS];
    private List placedRugs = new ArrayList<Rug>();
    private Coordinate assamPosition = new Coordinate(3, 3);
    private Direction assamDirection = Direction.NORTH;
}
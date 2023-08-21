package comp1110.ass2.board;

import comp1110.ass2.player.Rug;

/**
 * Tile class defines the state of a particular position on the board
 */
public class Tile {
    // Position of each tile on the board
    private Coordinate position;

    /**
     * Enum determines whether a Tile is empty or not
     * If there is a rug or Assam on the Tile, it's occupied
     */
    private enum TileState{
        EMPTY, OCCUPIED;
    }

    private TileState tileState;
    private Rug topRug;
    public Rug getTopRug() {
        return this.topRug;
    }
    public boolean isEmpty(){
        return tileState==TileState.EMPTY;
    }
}
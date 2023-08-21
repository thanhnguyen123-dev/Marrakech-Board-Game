package comp1110.ass2.board;

import comp1110.ass2.player.Rug;

/**
 * Tile class defines the state of a particular position on the board
 */
public class Tile {
    // Position of each tile on the board
    private Coordinate position;
    /**
     * Used to judge whether a tile is empty or not
     * If there is a rug, it's occupied
     */
    private enum TileState{
        EMPTY, OCCUPIED;
    }
    // Store the state of tile
    private TileState tileState;
    private Rug topRug;

    public Rug getTopRug() {
        return this.topRug;
    }

    public void setTopRug(Rug rug) {
        this.topRug = rug;
    }

    public boolean isEmpty(){
        return tileState==TileState.EMPTY;
    }
}
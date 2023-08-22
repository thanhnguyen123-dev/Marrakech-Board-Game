package comp1110.ass2.board;

import comp1110.ass2.player.Rug;

/**
 * Tile class defines the state of a particular position on the board
 */
public class Tile {
    // Position of each tile on the board
    private final int row, col;
    private Rug topRug;

    /**
     * Enum determines whether a Tile is empty or not
     * If there is a rug or Assam on the Tile, it's occupied
     */
    private enum TileState{
        EMPTY, OCCUPIED;
    }

    private TileState tileState;

    public Tile(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public Rug getTopRug() {
        return this.topRug;
    }

    public void setTopRug(Rug rug) {
        this.topRug = rug;
    }

    public boolean isAdjacent(Tile otherTile) {
        if (this.row == otherTile.row && Math.abs(this.col - otherTile.col) == 1) {
            return true;
        }
        if (this.col == otherTile.col && Math.abs(this.row - otherTile.row) == 1) {
            return true;
        }
        return false;
    }

    public boolean isEmpty(){
        return tileState==TileState.EMPTY;
    }
}
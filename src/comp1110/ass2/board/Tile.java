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
     * If a Rug or Assam is on the Tile, it's occupied
     */
    private enum TileState{
        EMPTY, OCCUPIED;
    }

    // Initialize tileState to EMPTY
    private TileState tileState = TileState.EMPTY;

    /**
     * Constructor: creates an instance of the Tile class
     * @param row row index of the Tile
     * @param col column index of the Tile
     */
    public Tile(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return the row index
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return the column index
     */
    public int getCol() {
        return this.col;
    }

    /**
     * @return the top Rug on the Tile
     */
    public Rug getTopRug() {
        return this.topRug;
    }

    /**
     * setter method to place a new top Rug on the Tile
     * @param rug the new top Rug on the Tile
     */
    public void setTopRug(Rug rug) {
        this.topRug = rug;
        this.tileState = TileState.OCCUPIED;
    }

    /**
     * Check if two Tile objects are adjacent to each other
     * @param otherTile the other Tile
     * @return true or false
     */
    public boolean isAdjacent(Tile otherTile) {
        if (this.row == otherTile.row && Math.abs(this.col - otherTile.col) == 1) {
            return true;
        }
        if (this.col == otherTile.col && Math.abs(this.row - otherTile.row) == 1) {
            return true;
        }
        return false;
    }

    /**
     * Check if the TileState is EMPTY
     * @return true or false
     */
    public boolean isEmpty(){
        return tileState==TileState.EMPTY;
    }

    /**
     * Check if the Tile contains a part of the rug
     */
    public boolean isRug() {
        if (this.getTopRug() != null) {
            return true;
        }
        return false;
    }

    /**
     * Check if the Tile contains Assam
     * @param board
     * @return true or false
     */
    public boolean isAssam(Board board) {
        Tile assamTile = board.getAssamTile();
        if (this.getRow() == assamTile.getRow() &&
                this.getCol() == assamTile.getCol()) {
            return true;
        }
        return false;
    }
}
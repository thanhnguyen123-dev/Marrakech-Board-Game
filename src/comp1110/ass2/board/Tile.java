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
     * TileState enum determines whether a tile is empty or not
     * If a rug or Assam is on the tile, it's occupied
     */
    private enum TileState{
        EMPTY, OCCUPIED;
    }

    // Initialize tileState to EMPTY
    private TileState tileState = TileState.EMPTY;

    /**
     * Constructor: creates an instance of the Tile class
     * @param row row index of the tile
     * @param col column index of the tile
     */
    public Tile(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * getter method for row
     * @return the row index
     */
    public int getRow() {
        return this.row;
    }

    /**
     * getter method for col
     * @return the column index
     */
    public int getCol() {
        return this.col;
    }

    /**
     * getter method for topRug
     * @return the top rug on the tile
     */
    public Rug getTopRug() {
        return this.topRug;
    }

    /**
     * setter method for topRug
     * @param rug the new top rug
     */
    public void setTopRug(Rug rug) {
        this.topRug = rug;
        this.tileState = TileState.OCCUPIED;
    }

    /**
     * Check if two tiles are adjacent to each other
     * @param otherTile the other tile
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
     * Check if the tile contains a part of the rug
     */
    public boolean isRug() {
        if (this.getTopRug() != null) {
            return true;
        }
        return false;
    }

    /**
     * Check if Assam is standing on the tile
     * @param board the board
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
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

    // Initialize tileState to EMPTY
    private TileState tileState = TileState.EMPTY;

    /**
     * Constructor: creates an instance of the Tile class
     * @param row
     * @param col
     */
    public Tile(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * getter method for the row position of the Tile
     * @return row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * getter method for the column position of the Tile
     * @return col
     */
    public int getCol() {
        return this.col;
    }

    /**
     * getter method for the Rug on the Tile
     * @return topRug
     */
    public Rug getTopRug() {
        return this.topRug;
    }

    /**
     * setter method for the Rug on the Tile
     * @param rug
     */
    public void setTopRug(Rug rug) {
        this.topRug = rug;
        this.tileState = TileState.OCCUPIED;
    }

    /**
     * Check if two Tile objects are adjacent to each other
     * @param otherTile
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
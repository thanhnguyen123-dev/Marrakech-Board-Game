package comp1110.ass2.board;

import comp1110.ass2.player.Rug;

/**
 * Tile class defines the state of a particular position on the board
 */
public class Tile {
    // Position of each tile on the board
    private final int row, col;
    private Rug topRug;
    private boolean hasAssam;

    /**
     * Constructor: creates an instance of the Tile class
     * @param row row index of the tile
     * @param col column index of the tile
     */
    public Tile(int row, int col) {
        this.row = row;
        this.col = col;
        this.hasAssam = false;
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
    }

    /**
     * getter method for hasAssam
     * @return true of false
     */
    public boolean hasAssam() {
        return this.hasAssam;
    }

    /**
     * setter method for hasAssam
     * @param hasAssam if Assam is standing on the tile or not
     */
    public void setHasAssam(boolean hasAssam) {
        this.hasAssam = hasAssam;
    }

    /**
     * Checks if two tiles are adjacent to each other
     * @param that the other tile
     * @return true or false
     */
    public boolean isAdjacent(Tile that) {
        if (this.row == that.row && Math.abs(this.col - that.col) == 1) {
            return true;
        }
        if (this.col == that.col && Math.abs(this.row - that.row) == 1) {
            return true;
        }
        return false;
    }

    /**
     * Checks if a tile has a rug on it
     * @return true or false
     */
    public boolean hasRug() {
        return this.topRug != null;
    }
}
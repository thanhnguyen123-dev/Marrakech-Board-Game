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
    }

    /**
     * Checks if two tiles are adjacent to each other
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
     * Checks if a tile has a rug on it
     * @return true or false
     */
    public boolean isEmpty() {
        return this.topRug == null;
    }

    /**
     * Checks if Assam is standing on the tile
     * @param board the board
     * @return true or false
     */
    public boolean isAssam(Board board) {
        Tile assamTile = board.getAssamTile();
        return this == assamTile;
    }
}
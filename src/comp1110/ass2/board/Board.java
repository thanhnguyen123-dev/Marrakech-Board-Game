package comp1110.ass2.board;

import comp1110.ass2.player.Rug;

import java.util.ArrayList;
import java.util.List;

/**
 * Board class represents the dimensions of the board and relevant objects on
 * the board
 */
public class Board {
    public static final int NUM_OF_ROWS = 7;
    public static final int NUM_OF_COLS = 7;
    private static final int LENGTH_OF_SHORT_RUG_STRING = 3;

    private final Tile[][] tiles = new Tile[NUM_OF_ROWS][NUM_OF_COLS];
    private final List<Rug> placedRugs = new ArrayList<Rug>();
    private final List<Rug> visibleRugs = new ArrayList<Rug>(); // All visible Rugs on the board
    private Tile assamTile;
    private Direction assamDirection;

    /**
     * Constructor: creates an instance of the Board class
     * Initialize the Board
     */
    public Board() {
        for (int row = 0; row < NUM_OF_ROWS; row++) {
            for (int col = 0; col < NUM_OF_COLS; col++) {
                this.tiles[row][col] = new Tile(row, col);
            }
        }
        this.assamTile = this.tiles[3][3];
        this.assamDirection = Direction.NORTH;
    }

    /**
     * @return all the Tiles of the Board
     */
    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * @return a list of all placed Rugs on the Board
     */
    public List<Rug> getPlacedRugs() {
        return this.placedRugs;
    }

    /**
     * @return a list of all visible Rugs on the Board
     */
    public List<Rug> getVisibleRugs() {
        return this.visibleRugs;
    }

    /**
     * @return the Tile on which Assam is standing
     */
    public Tile getAssamTile() {
        return this.assamTile;
    }

    /**
     * @return the Direction which Assam is facing
     */
    public Direction getAssamDirection() {
        return this.assamDirection;
    }

    /**
     * Moves Assam by a given number of steps from rolling the Die
     * @param steps number of steps to be taken by Assam
     */
    public void moveAssam(int steps) {
        int row = this.assamTile.getRow();
        int col = this.assamTile.getCol();
        switch (this.assamDirection) {
            case NORTH -> {
                if (row - steps < 0) {
                    this.assamTile = this.tiles[0][col];
                    moveAssamOutOfBounds();
                    moveAssam(steps - row - 1);
                } else {
                    this.assamTile = this.tiles[row - steps][col];
                }
            }
            case EAST -> {
                if (col + steps > NUM_OF_COLS - 1) {
                    this.assamTile = this.tiles[row][NUM_OF_COLS - 1];
                    moveAssamOutOfBounds();
                    moveAssam(col + steps - NUM_OF_COLS);
                } else {
                    this.assamTile = this.tiles[row][col + steps];
                }
            }
            case SOUTH -> {
                if (row + steps > NUM_OF_ROWS - 1) {
                    this.assamTile = this.tiles[NUM_OF_ROWS - 1][col];
                    moveAssamOutOfBounds();
                    moveAssam(row + steps - NUM_OF_ROWS);
                } else {
                    this.assamTile = this.tiles[row + steps][col];
                }
            }
            case WEST -> {
                if (col - steps < 0) {
                    this.assamTile = this.tiles[row][0];
                    moveAssamOutOfBounds();
                    moveAssam(steps - col - 1);
                } else {
                    this.assamTile = this.tiles[row][col - steps];
                }
            }
        }
    }

    /**
     * Moves Assam off and back onto the Board
     */
    private void moveAssamOutOfBounds() {
        int row = this.assamTile.getRow();
        int col = this.assamTile.getCol();
        if (row == 0 && col == NUM_OF_COLS - 1) {
            switch (this.assamDirection) {
                case NORTH -> this.assamDirection = Direction.WEST;
                case EAST -> this.assamDirection = Direction.SOUTH;
            }
            return;
        }
        if (row == NUM_OF_ROWS - 1 && col == 0) {
            switch (this.assamDirection) {
                case SOUTH -> this.assamDirection = Direction.EAST;
                case WEST -> this.assamDirection = Direction.NORTH;
            }
            return;
        }
        switch (this.assamDirection) {
            case NORTH -> this.assamTile = col % 2 == 0 ? this.tiles[0][col + 1] : this.tiles[0][col - 1];
            case EAST ->
                    this.assamTile = row % 2 == 0 ? this.tiles[row - 1][NUM_OF_COLS - 1] : this.tiles[row + 1][NUM_OF_COLS - 1];
            case SOUTH ->
                    this.assamTile = col % 2 == 0 ? this.tiles[NUM_OF_ROWS - 1][col - 1] : this.tiles[NUM_OF_ROWS - 1][col + 1];
            case WEST -> this.assamTile = row % 2 == 0 ? this.tiles[row + 1][0] : this.tiles[row - 1][0];
        }
        this.assamDirection = this.assamDirection.rotate(180);
    }

    /**
     * Rotates Assam by a given angle of rotation
     * @param rotation rotation in degrees
     */
    public void rotateAssam(int rotation) {
        this.assamDirection = this.assamDirection.rotate(rotation);
    }

    /**
     * Checks if a Rug placement is valid according to the game rules
     * @param rug the Rug to be validated
     * @return true or false
     */
    public boolean isPlacementValid(Rug rug) {
        Tile[] rugTiles = rug.getRugTiles();
        if (!rugTiles[0].isAdjacent(this.assamTile) && !rugTiles[1].isAdjacent(this.assamTile)) {
            return false;
        }
        if (rugTiles[0] == this.assamTile || rugTiles[1] == this.assamTile) {
            return false;
        }
        if (rugTiles[0].getTopRug() != null && rugTiles[1].getTopRug() != null && rugTiles[0].getTopRug().getColour() == rugTiles[1].getTopRug().getColour() && rugTiles[0].getTopRug().getID() == rugTiles[1].getTopRug().getID()) {
            return false;
        }
        return true;
    }

    /**
     * Places a Rug onto the Board's Tile
     * @param rug the Rug to be placed
     */
    public void placeRug(Rug rug) {
        placedRugs.add(rug);
        for (Tile tile : rug.getRugTiles()) {
            tile.setTopRug(rug);
        }
    }

    /**
     * Generate AssamString based on data of the Board
     * @return string representation for Assam
     */
    public String generateAssamString() {
        return "A" + assamTile.getCol() + assamTile.getRow() + assamDirection.getDirectionChar();
    }

    /**
     * Constructor: creates an instance of the Board class
     * Decode the assamString and the boardString to get corresponding values for instance fields
     * @param assamString string representation for Assam
     * @param boardString string representation for the Board
     */
    public Board(String assamString, String boardString) {
        for (int col = 0; col < NUM_OF_ROWS; col++) {
            for (int row = 0; row < NUM_OF_COLS; row++) {
                this.tiles[row][col] = new Tile(row, col);
                int beginIndex = (col * NUM_OF_ROWS + row) * LENGTH_OF_SHORT_RUG_STRING;
                String shortRugString = boardString.substring(beginIndex, beginIndex + LENGTH_OF_SHORT_RUG_STRING);
                if (!shortRugString.equals("n00")) {
                    Rug rug = new Rug(shortRugString, this.tiles[row][col]);
                    this.tiles[row][col].setTopRug(rug);
                    // Get all visible rugs according to board string
                    visibleRugs.add(rug);
                }
            }
        }
        int[] position = parse(assamString.substring(0, 2));
        this.assamTile = this.tiles[position[0]][position[1]];
        this.assamDirection = Direction.charToDirection(assamString.charAt(2));
    }

    private static int[] parse(String coordinates) {
        int row = Integer.parseInt(coordinates.charAt(1) + "", 10);
        int col = Integer.parseInt(coordinates.charAt(0) + "", 10);
        return new int[]{row, col};
    }


    /**
     * Determines whether the coordinate of a Tile on which can place a Rug is valid
     * @param tile the Tile to be validated
     * @return true if the coordinate of Tile is valid, or false otherwise
     */
    public static boolean isTileValid(Tile tile) {
        int row = tile.getRow();
        int col = tile.getCol();
        if (row >= 0 && row < NUM_OF_ROWS) {
            if (col >= 0 && col < NUM_OF_COLS) {
                return true;
            }
        }
        return false;
    }
}
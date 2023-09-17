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
     * getter method for tiles
     * @return the 2D array containing all the tiles of the board
     */
    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * getter method for placedRugs
     * @return the list containing all the placed rugs on the board
     */
    public List<Rug> getPlacedRugs() {
        return this.placedRugs;
    }

    /**
     * getter method for visibleRugs
     * @return the list containing all the visible rugs on the board
     */
    public List<Rug> getVisibleRugs() {
        return this.visibleRugs;
    }

    /**
     * getter method for assamTile
     * @return the tile on which Assam is standing
     */
    public Tile getAssamTile() {
        return this.assamTile;
    }

    /**
     * getter method for assamDirection
     * @return the direction which Assam is facing
     */
    public Direction getAssamDirection() {
        return this.assamDirection;
    }

    /**
     * Moves Assam by a given number of steps from rolling the die
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
     * Moves Assam off and back onto the board following the mosaic track
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
     * Checks if a rug placement is valid according to the game rules
     * @param rug the rug to be validated
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
     * Places a rug on the board
     * @param rug the rug to be placed
     */
    public void placeRug(Rug rug) {
        placedRugs.add(rug);
        for (Tile tile : rug.getRugTiles()) {
            tile.setTopRug(rug);
        }
    }

    /**
     * Constructor: creates an instance of the Board class
     * Decodes the assamString and the boardString to get corresponding values for instance fields
     * @param assamString string representation for Assam
     * @param boardString string representation for the board
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
     * Generates AssamString based on data of the board
     * @return string representation for Assam
     */
    public static String generateAssamString(Board board) {
        Tile assamTile = board.getAssamTile();
        Direction assamDirection = board.getAssamDirection();
        return "A" + assamTile.getCol() + assamTile.getRow() + assamDirection.getDirectionChar();
    }

    /**
     * Generates BoardString based on data of the board
     * @return string representation for the board
     */
    public static String generateBoardString(Board board) {
        StringBuilder stringBuilder = new StringBuilder();
        Tile[][] tiles = board.getTiles();
        for (int col = 0; col < NUM_OF_ROWS; col++) {
            for (int row = 0; row < NUM_OF_COLS; row++) {
                if (tiles[row][col].getTopRug() == null) {
                    stringBuilder.append("n00");
                } else {
                    Rug rug = tiles[row][col].getTopRug();
                    char colourChar = rug.getColour().colourChar;
                    int id = rug.getID();
                    stringBuilder.append(colourChar).append(id);
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Determines whether the coordinate of the tile on which a rug is placed is valid
     * @param tile the tile to be validated
     * @return true if the coordinate of tile is valid, or false otherwise
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
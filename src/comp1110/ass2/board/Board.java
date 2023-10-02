package comp1110.ass2.board;

import comp1110.ass2.player.Rug;
import comp1110.ass2.utils.StringToTile;

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
        this.assamTile.setHasAssam(true);
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
     * getter method for visibleRugs
     * @return the list containing all the visible rugs on the board
     * @author u7582846 Yaolin Li
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
     * @author u7620014 Haobo Zou
     */
    public void moveAssam(int steps) {
        int row = this.assamTile.getRow();
        int col = this.assamTile.getCol();
        this.assamTile.setHasAssam(false);
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
        this.assamTile.setHasAssam(true);
    }

    /**
     * Moves Assam off and back onto the board following the mosaic track
     * @author u7620014 Haobo Zou
     */
    public void moveAssamOutOfBounds() {
        int row = this.assamTile.getRow();
        int col = this.assamTile.getCol();
        this.assamTile.setHasAssam(false);
        if (row == 0 && col == NUM_OF_COLS - 1) {
            switch (this.assamDirection) {
                case NORTH -> this.assamDirection = Direction.WEST;
                case EAST -> this.assamDirection = Direction.SOUTH;
            }
        } else if (row == NUM_OF_ROWS - 1 && col == 0) {
            switch (this.assamDirection) {
                case SOUTH -> this.assamDirection = Direction.EAST;
                case WEST -> this.assamDirection = Direction.NORTH;
            }
        } else {
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
        this.assamTile.setHasAssam(true);
    }

    /**
     * Rotates Assam by a given angle of rotation
     * @param rotation rotation in degrees
     */
    public void rotateAssam(int rotation) {
        List<Integer> legalRotations = new ArrayList<>();
        legalRotations.add(-90);
        for (Direction direction : Direction.values()) {
            Integer angleDirection = direction.getAngle();
            if (angleDirection != 180) {
                legalRotations.add(angleDirection);
            }
        }
        if (legalRotations.contains(rotation)) {
            this.assamDirection = this.assamDirection.rotate(rotation);
        }
    }

    /**
     * Checks if a rug placement is valid according to the game rules
     * @param rug the rug to be validated
     * @return true or false
     * @author u7620014 Haobo Zou
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
     * @author u7620014 Haobo Zou
     */
    public void placeRug(Rug rug) {
        for (Tile tile : rug.getRugTiles()) {
            tile.setTopRug(rug);
        }
    }

    /**
     * Constructor: creates an instance of the Board class
     * Decodes the assamString and the boardString to get corresponding values for instance fields
     * @param assamString string representation for Assam
     * @param boardString string representation for the board
     * @author u7620014 Haobo Zou
     */
    public Board(String assamString, String boardString) {
        for (int col = 0; col < NUM_OF_ROWS; col++) {
            for (int row = 0; row < NUM_OF_COLS; row++) {
                this.tiles[row][col] = new Tile(row, col);
                int beginIndex = (col * NUM_OF_ROWS + row) * LENGTH_OF_SHORT_RUG_STRING + 1;
                String shortRugString = boardString.substring(beginIndex, beginIndex + LENGTH_OF_SHORT_RUG_STRING);
                if (!shortRugString.equals("n00")) {
                    Rug rug = new Rug(shortRugString, this.tiles[row][col]);
                    this.tiles[row][col].setTopRug(rug);
                    // Get all visible rugs according to board string
                    visibleRugs.add(rug);
                }
            }
        }
        this.assamTile = StringToTile.getTileFromString(this.tiles, assamString.substring(1, 3));
        this.assamTile.setHasAssam(true);
        this.assamDirection = Direction.charToDirection(assamString.charAt(3));
    }

    /**
     * Generates assamString based on data of the board
     * @return string representation for Assam
     * @author u7620014 Haobo Zou
     */
    public String generateAssamString() {
        return "A" + this.assamTile.getCol() + this.assamTile.getRow() + this.assamDirection.getDirectionChar();
    }

    /**
     * Generates boardString based on data of the board
     * @return string representation for the board
     * @author u7620014 Haobo Zou
     */
    public String generateBoardString() {
        StringBuilder stringBuilder = new StringBuilder("B");
        for (int col = 0; col < NUM_OF_ROWS; col++) {
            for (int row = 0; row < NUM_OF_COLS; row++) {
                if (this.tiles[row][col].getTopRug() == null) {
                    stringBuilder.append("n00");
                } else {
                    Rug rug = this.tiles[row][col].getTopRug();
                    char colourChar = rug.getColour().colourChar;
                    int id = rug.getID();
                    stringBuilder.append(colourChar).append(String.format("%02d", id));
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Determines whether the coordinate of the tile on which a rug is placed is valid
     * @param tile the tile to be validated
     * @return true if the coordinate of tile is valid, or false otherwise
     * @author u7582846 Yaolin Li
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
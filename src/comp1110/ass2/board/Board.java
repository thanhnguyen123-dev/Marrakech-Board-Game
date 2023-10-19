package comp1110.ass2.board;

import comp1110.ass2.player.Rug;
import comp1110.ass2.utils.StringToTile;

import java.util.ArrayList;
import java.util.List;

/**
 * Board class contains properties of each board
 * @author u7594144 Le Thanh Nguyen, u7582846 Yaolin Li, u7620014 Haobo Zou
 */
public class Board {
    public static final int NUM_OF_ROWS = 7;
    public static final int NUM_OF_COLS = 7;
    private static final int LENGTH_OF_SHORT_RUG_STRING = 3;

    private final Tile[][] tiles = new Tile[NUM_OF_ROWS][NUM_OF_COLS];
    private final List<Rug> visibleRugs = new ArrayList<>(); // All visible Rugs on the board
    private Tile assamTile;
    private Direction assamDirection;
    private final List<Tile> assamPath = new ArrayList<>();

    /**
     * Constructor: creates an instance of the Board class
     * @author u7620014 Haobo Zou
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
     * @author u7620014 Haobo Zou
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
     * @author u7620014 Haobo Zou
     */
    public Tile getAssamTile() {
        return this.assamTile;
    }

    /**
     * getter method for assamDirection
     * @return the direction which Assam is facing
     * @author u7620014 Haobo Zou
     */
    public Direction getAssamDirection() {
        return this.assamDirection;
    }

    /**
     * getter method for assamPath
     * @return a List of tiles representing the path taken by assam after his last movement
     * @author u7620014 Haobo Zou
     */
    public List<Tile> getAssamPath() {
        return this.assamPath;
    }

    /**
     * Calculates Assam's path if he moves 4 steps from his current position after a given rotation
     * @param rotation a given rotation
     * @return a List of tiles representing the path taken by Assam after a given rotation and movement by 4 steps
     * @author u7620014 Haobo Zou
     */
    public List<Tile> getAssamFullPath(int rotation) {
        Board board = new Board();
        board.assamTile.setHasAssam(false);
        board.assamTile = board.tiles[this.assamTile.getRow()][this.assamTile.getCol()];
        board.assamTile.setHasAssam(true);
        board.assamDirection = this.assamDirection;
        board.rotateAssam(rotation);
        board.moveAssam(4);
        return board.assamPath;
    }

    /**
     * Moves Assam with given steps
     * @param steps number of steps
     * @author u7620014 Haobo Zou
     */
    public void moveAssam(int steps) {
        this.assamPath.clear();
        moveAssamInBounds(steps);
    }

    /**
     * Moves Assam within the board with given steps
     * @param steps number of steps
     * @author u7620014 Haobo Zou
     */
    public void moveAssamInBounds(int steps) {
        int row = this.assamTile.getRow();
        int col = this.assamTile.getCol();
        this.assamTile.setHasAssam(false);
        switch (this.assamDirection) {
            case NORTH -> {
                if (row - steps < 0) {
                    for (int i = row - 1; i >= 0; i--) this.assamPath.add(this.tiles[i][col]);
                    this.assamTile = this.tiles[0][col];
                    moveAssamOutOfBounds();
                    moveAssamInBounds(steps - row - 1);
                } else {
                    for (int i = row - 1; i >= row - steps; i--) this.assamPath.add(this.tiles[i][col]);
                    this.assamTile = this.tiles[row - steps][col];
                }
            }
            case EAST -> {
                if (col + steps > NUM_OF_COLS - 1) {
                    for (int j = col + 1; j <= NUM_OF_COLS - 1; j++) this.assamPath.add(this.tiles[row][j]);
                    this.assamTile = this.tiles[row][NUM_OF_COLS - 1];
                    moveAssamOutOfBounds();
                    moveAssamInBounds(col + steps - NUM_OF_COLS);
                } else {
                    for (int j = col + 1; j <= col + steps; j++) this.assamPath.add(this.tiles[row][j]);
                    this.assamTile = this.tiles[row][col + steps];
                }
            }
            case SOUTH -> {
                if (row + steps > NUM_OF_ROWS - 1) {
                    for (int i = row + 1; i <= NUM_OF_ROWS - 1; i++) this.assamPath.add(this.tiles[i][col]);
                    this.assamTile = this.tiles[NUM_OF_ROWS - 1][col];
                    moveAssamOutOfBounds();
                    moveAssamInBounds(row + steps - NUM_OF_ROWS);
                } else {
                    for (int i = row + 1; i <= row + steps; i++) this.assamPath.add(this.tiles[i][col]);
                    this.assamTile = this.tiles[row + steps][col];
                }
            }
            case WEST -> {
                if (col - steps < 0) {
                    for (int j = col - 1; j >= 0; j--) this.assamPath.add(this.tiles[row][j]);
                    this.assamTile = this.tiles[row][0];
                    moveAssamOutOfBounds();
                    moveAssamInBounds(steps - col - 1);
                } else {
                    for (int j = col - 1; j >= col - steps; j--) this.assamPath.add(this.tiles[row][j]);
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
        this.assamPath.add(this.assamTile);
        this.assamTile.setHasAssam(true);
    }

    /**
     * Rotates Assam with a given angle of rotation
     * @param rotation rotation in degrees
     * @author Le Thanh Nguyen u7594144
     */
    public void rotateAssam(int rotation) {
        if (isRotationLegal(rotation)) {
            this.assamDirection = this.assamDirection.rotate(rotation);
        }
    }

    /**
     * Determines if a rotation for Assam is legal
     * @param rotation rotation in degrees
     * @author Le Thanh Nguyen u7594144
     */
    public boolean isRotationLegal(int rotation) {
        List<Integer> legalRotations = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            int angleDirection = direction.getAngle();
            if (angleDirection != 180) {
                legalRotations.add(angleDirection);
            }
        }
        return legalRotations.contains((rotation + 360) % 360);
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
                    // Sets the represented rug as the top rug, and adds it to the list of all visible rugs
                    Rug rug = new Rug(shortRugString, this.tiles[row][col]);
                    this.tiles[row][col].setTopRug(rug);
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
                    char colourChar = rug.getColour().getColourChar();
                    int id = rug.getID();
                    stringBuilder.append(colourChar).append(String.format("%02d", id));
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Determines whether the coordinates of the tile on which a rug is placed are valid
     * @param tile the tile to be validated
     * @return true if the coordinates of tile are valid, or false otherwise
     * @author u7582846 Yaolin Li
     */
    public static boolean isTileValid(Tile tile) {
        int row = tile.getRow();
        int col = tile.getCol();
        return row >= 0 && row < NUM_OF_ROWS && col >= 0 && col < NUM_OF_COLS;
    }

    /**
     * Get a List of all available adjacent tiles regarding the current tile
     * @param tile the current tile
     * @return a List of adjacent tiles
     * @author u7594144 Le Thanh Nguyen
     */
    public List<Tile> getAdjacentTiles(Tile tile) {
        List<Tile> adjacentTiles = new ArrayList<>();
        int currentTileX = tile.getRow();
        int currentTileY = tile.getCol();
        Tile tile1 = new Tile(currentTileX - 1, currentTileY);
        Tile tile2 = new Tile(currentTileX + 1, currentTileY);
        Tile tile3 = new Tile(currentTileX, currentTileY - 1);
        Tile tile4 = new Tile(currentTileX, currentTileY + 1);

        List<Tile> tmp = new ArrayList<>();
        tmp.add(tile1);
        tmp.add(tile2);
        tmp.add(tile3);
        tmp.add(tile4);

        for (Tile adjacentTile : tmp) {
            if (isTileValid(adjacentTile)) {
                int row = adjacentTile.getRow();
                int col = adjacentTile.getCol();
                adjacentTiles.add(this.tiles[row][col]);
            }
        }
        return adjacentTiles;
    }
}
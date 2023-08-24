package comp1110.ass2.board;

import comp1110.ass2.player.Rug;

import java.util.ArrayList;
import java.util.List;

/**
 * Board class represents the dimensions of the board and relevant objects on
 * the board
 */
public class Board {
    private static final int NUM_OF_ROWS = 7;
    private static final int NUM_OF_COLS = 7;
    private static final int LENGTH_OF_SHORT_RUG_STRING = 3;

    private Tile[][] tiles = new Tile[NUM_OF_ROWS][NUM_OF_COLS];
    private List<Rug> placedRugs = new ArrayList<Rug>();
    private Tile assamTile;
    private Direction assamDirection;

    public Board() {
        for (int row = 0; row < NUM_OF_ROWS; row++) {
            for (int col = 0; col < NUM_OF_COLS; col++) {
                this.tiles[row][col] = new Tile(row, col);
            }
        }
        this.assamTile = this.tiles[3][3];
        this.assamDirection = Direction.NORTH;
    }

    public Tile getAssamTile() {
        return this.assamTile;
    }

    public void moveAssam(int steps) {
        int row = this.assamTile.getRow();
        int col = this.assamTile.getCol();
        switch (this.assamDirection) {
            case NORTH:
                if (row - steps < 0) {
                    this.assamTile = this.tiles[0][col];
                    moveAssamOutOfBounds();
                    moveAssam(steps - row - 1);
                } else {
                    this.assamTile = this.tiles[row - steps][col];
                }
                break;
            case EAST:
                if (col + steps > NUM_OF_COLS - 1) {
                    this.assamTile = this.tiles[row][NUM_OF_COLS - 1];
                    moveAssamOutOfBounds();
                    moveAssam(col + steps - NUM_OF_COLS);
                } else {
                    this.assamTile = this.tiles[row][col + steps];
                }
                break;
            case SOUTH:
                if (row + steps > NUM_OF_ROWS - 1) {
                    this.assamTile = this.tiles[NUM_OF_ROWS - 1][col];
                    moveAssamOutOfBounds();
                    moveAssam(row + steps - NUM_OF_ROWS);
                } else {
                    this.assamTile = this.tiles[row + steps][col];
                }
                break;
            case WEST:
                if (col - steps < 0) {
                    this.assamTile = this.tiles[row][0];
                    moveAssamOutOfBounds();
                    moveAssam(steps - col - 1);
                } else {
                    this.assamTile = this.tiles[row][col - steps];
                }
                break;
        }
    }

    private void moveAssamOutOfBounds() {
        int row = this.assamTile.getRow();
        int col = this.assamTile.getCol();
        if (row == 0 && col == NUM_OF_COLS - 1) {
            switch (this.assamDirection) {
                case NORTH -> this.assamDirection = Direction.NORTH;
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

    public void rotateAssam(int rotation) {
        this.assamDirection = this.assamDirection.rotate(rotation);
    }

    public boolean isPlacementValid(Rug rug) {
        Tile[] rugTiles = rug.getRugTiles();
        if (!rugTiles[0].isAdjacent(this.assamTile) && !rugTiles[1].isAdjacent(this.assamTile)) {
            return false;
        }
        if (rugTiles[0] == this.assamTile || rugTiles[1] == this.assamTile) {
            return false;
        }
        if (rugTiles[0].getTopRug() != null && rugTiles[1].getTopRug() != null && rugTiles[0].getTopRug().getID() == rugTiles[1].getTopRug().getID()) {
            return false;
        }
        return true;
    }

    public void placeRug(Rug rug) {
        placedRugs.add(rug);
        for (Tile tile : rug.getRugTiles()) {
            tile.setTopRug(rug);
        }
    }

    public Board(String assamString, String boardString) {
        for (int col = 0; col < NUM_OF_ROWS; col++) {
            for (int row = 0; row < NUM_OF_COLS; row++) {
                this.tiles[row][col] = new Tile(row, col);
                int beginIndex = (col * NUM_OF_ROWS + row) * LENGTH_OF_SHORT_RUG_STRING;
                String shortRugString = boardString.substring(beginIndex, beginIndex + LENGTH_OF_SHORT_RUG_STRING);
                if (!shortRugString.equals("n00")) {
                    Rug rug = new Rug(shortRugString, this.tiles[row][col]);
                    this.tiles[row][col].setTopRug(rug);
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
}
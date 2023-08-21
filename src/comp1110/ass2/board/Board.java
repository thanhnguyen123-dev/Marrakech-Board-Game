package comp1110.ass2.board;

import comp1110.ass2.player.Rug;

import comp1110.ass2.Marrakech;

import java.util.ArrayList;
import java.util.List;

/**
 * Board class represents the dimensions of the board and relevant objects on
 * the board
 */
public class Board {
    private static final int NUM_OF_ROWS = 7;
    private static final int NUM_OF_COLS = 7;

    private Tile[][] tiles = new Tile[Board.NUM_OF_ROWS][Board.NUM_OF_COLS];
    private List<Rug> placedRugs = new ArrayList<Rug>();
    private Coordinate assamPosition = new Coordinate(3, 3);
    private Direction assamDirection = Direction.NORTH;

    public Coordinate getAssamPosition() {
        return this.assamPosition;
    }

    public void moveAssam(int steps) {
        switch (assamDirection) {
            case NORTH -> moveAssam(0, -steps);
            case EAST -> moveAssam(steps, 0);
            case SOUTH -> moveAssam(0, steps);
            case WEST -> moveAssam(-steps, 0);
        }
    }

    private void moveAssam(int x, int y) {
        // FIXME
    }

    public void rotateAssam(int rotation) {
        assamDirection.applyRotation(rotation);
    }

    public Rug getAssamRug() {
        return this.tiles[this.assamPosition.getX()][this.assamPosition.getY()].getTopRug();
    }

    public void placeRug(Rug rug) {
        placedRugs.add(rug);
        for (Coordinate position : rug.getRugTiles()) {
            tiles[position.getX()][position.getY()].setTopRug(rug);
        }
    }

    public boolean isRugValid(Rug rug) {
        // FIXME
        return false;
    }
}